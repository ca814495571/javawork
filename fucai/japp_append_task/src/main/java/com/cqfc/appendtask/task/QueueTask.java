package com.cqfc.appendtask.task;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;

import com.cqfc.appendtask.service.IAppendTaskService;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.util.AppendTaskConstant;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.TaskDbGenerator;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class QueueTask implements Runnable {
	private String dbName;
	private String lotteryId;
	private String issueNo;
	private CountDownLatch latch;

	public QueueTask(CountDownLatch latch, String dbName, String lotteryId, String issueNo) {
		this.latch = latch;
		this.dbName = dbName;
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
	}

	/**
	 * 创建追号明细订单数据
	 */
	@Override
	public void run() {
		try {
			ScanLog.scan.debug("jms scan,lotteryId=%s,issueNo=%s,dbName=%s(start)", lotteryId, issueNo, dbName);
			taskProcess(dbName, lotteryId, issueNo);
			ScanLog.scan.debug("jms scan,lotteryId=%s,issueNo=%s,dbName=%s(finish)", lotteryId, issueNo, dbName);
		} catch (Exception e) {
			ScanLog.scan.error("创建追号明细订单数据扫描发生异常,dbName=" + dbName + ",lotteryId=" + lotteryId + ",issueNo=" + issueNo,
					e);
		} finally {
			latch.countDown();
		}
	}

	private static void taskProcess(String dbName, String lotteryId, String issueNo) {
		TaskDbGenerator.setDynamicDataSource(dbName);

		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		IAppendTaskService appendTaskService = ctx.getBean("appendTaskServiceImpl", IAppendTaskService.class);
		for (int i = 0; i < AppendTaskConstant.PER_DATASOURCE_TABLE_NUM; i++) {
			int currentPage = 1;
			int pageSize = 2000;
			String tableName = TaskDbGenerator.getTableName(i);
			while (true) {
				List<AppendTaskDetail> detailList = appendTaskService.getAppendTaskDetailListByParam(lotteryId,
						issueNo, tableName, currentPage, pageSize);
				if (null != detailList && detailList.size() > 0) {
					for (AppendTaskDetail detail : detailList) {
						String appendTaskId = detail.getAppendTaskId();
						ReturnMessage appendOrderMsg = TransactionProcessor.dynamicInvoke(
								ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER, "createAppendOrder", detail);
//						ReturnMessage appendOrderMsg = TransactionProcessor.dynamicInvoke(
//								ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER, "createAppendOrder", appendTaskId,
//								ConstantsUtil.DEVICE_TYPE_TICKET_BUSINESS, detail);
						if (!appendOrderMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
							ScanLog.scan.error("创建追号订单发生异常,appendTaskId=%s,lotteryId=%s,issueNo=%s,errorMsg=%s",
									appendTaskId, lotteryId, issueNo, appendOrderMsg.getMsg());
						} else {
							int isSuccess = (Integer) appendOrderMsg.getObj();
							ScanLog.scan.info("扫描追号明细创建订单,appendTaskId=%s,lotteryId=%s,issueNo=%s,returnValue=%d",
									appendTaskId, lotteryId, issueNo, isSuccess);
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		}
	}

}

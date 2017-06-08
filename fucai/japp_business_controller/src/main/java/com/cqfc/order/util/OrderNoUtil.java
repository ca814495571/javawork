package com.cqfc.order.util;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.OrderConstant;
import com.jami.util.Log;

/**
 * orderNo规则：系统唯一ID + 库索引（2位） + 表索引（3位） 组成的长整型数值
 * 
 * @author liwh
 */
public class OrderNoUtil {

	public static long getOrderNo(String ticketId) {
		try {
			ReturnMessage ret = TransactionProcessor.dynamicInvoke("idGenerate", "idGen", "orderNo");
			if (!ret.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.error("获取orderNo发生异常,ticketId=%s,errorMsg=%s", ticketId, ret.getMsg());
			}
			long resp = (Long) ret.getObj();
			String index = getDbTableIndex(ticketId);
			if (index.length() != 5) {
				Log.run.error("获取订单索引发生异常,ticketId=%s,index=%s", ticketId, index);
				return 0;
			}
			return Long.valueOf(resp + index);
		} catch (Exception e) {
			Log.fucaibiz.error("获取orderNo发生异常,ticketId=" + ticketId, e);
			return 0;
		}
	}

	private static String getDbTableIndex(String ticketId) {
		String index = "";
		try {
			int value = ticketId.hashCode();
			String dbIndex = String.valueOf(Math.abs(value / OrderConstant.PER_DATASOURCE_TABLE_NUM
					% OrderConstant.DATASOURCE_NUM));
			dbIndex = dbIndex.length() == 1 ? "0" + dbIndex : dbIndex;
			String tableIndex = String.valueOf(Math.abs(value % OrderConstant.PER_DATASOURCE_TABLE_NUM));
			tableIndex = tableIndex.length() == 2 ? "0" + tableIndex : (tableIndex.length() == 1 ? "00" + tableIndex
					: tableIndex);
			index = dbIndex + tableIndex;
			Log.run.debug("订单索引,ticketId=%s,value=%s", ticketId, index);
		} catch (Exception e) {
			Log.run.error("获取订单索引发生异常,ticketId=" + ticketId, e);
		}
		return index;
	}
}

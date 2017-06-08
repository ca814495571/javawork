package com.cqfc.ticketwinning.testClient;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.jami.util.DataSourceContextHolder;

public class DataSourceTest {

	private static ApplicationContext applicationContext = null;

	
	public static void main(String[] args) throws Exception {

		applicationContext = new ClassPathXmlApplicationContext(
				"spring.xml");

//		org.apache.ibatis.datasource.pooled.PooledDataSource fucai_order_00 = (org.apache.ibatis.datasource.pooled.PooledDataSource) applicationContext
//				.getBean("fucai_partner_order_00");
//		org.apache.ibatis.datasource.pooled.PooledDataSource fucai_order_01 = (org.apache.ibatis.datasource.pooled.PooledDataSource) applicationContext
//				.getBean("fucai_partner_order_08");
//		org.apache.ibatis.datasource.pooled.PooledDataSource fucai = (org.apache.ibatis.datasource.pooled.PooledDataSource) applicationContext
//				.getBean("fucai");
//		applicationContext = new ClassPathXmlApplicationContext(
//				"spring.xml");
		
//		DynamicDataSource dynamicDataSource = (DynamicDataSource) applicationContext
//				.getBean("dynamicDataSource");
//		DataSourceContextHolder.setDataSourceType("fucai_partner_order_00");
//		dynamicDataSource.addDateSource("fucai_order_00", fucai_order_00);
//		System.out.println(dynamicDataSource.getConnection().getMetaData().getURL());
//		dynamicDataSource.addDateSource("fucai_order_01", fucai_order_01);
//		DataSourceContextHolder.setDataSourceType("fucai_partner_order_08");
//		DataSourceContextHolder.setDataSourceType("fucai");
//		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
//		WinningService winningService = ctx.getBean("winningService", WinningService.class);
//		WinningSum winningSum = new WinningSum();
//		
//		winningService.getSumGroupbyPartnerId(winningSum, 1, 100);
//		System.out.println(winningService.getWinningList(1, 2));
//		System.out.println(winningService.countTotalSize());
//		Winning winning = new Winning();
//		winning.setOrderId(454554);
//		winning.setPartnerId("safsad");
//		winning.setUserId(441);
//		winning.setDbName("test");
//		winning.setTableName("test");
//		winning.setLotteryId("sdafas");
//		winning.setPlayType("asdfs");
//		winning.setMultiple(2);
//		winning.setOrderContent("dsafsd");
//		winning.setOrderNo("sdfasdfsdfsa");
//		winning.setIssueNo("sdfasfsd");
//		DataSourceContextHolder.setDataSourceType("fucai");
//		System.out.println(dynamicDataSource.getConnection().getMetaData().getURL());
//		winningService.createWinning(winning);
//		System.out.println(dynamicDataSource.getConnection().getMetaData().getURL());
		
//		dynamicDataSource.addDateSource("fucai", fucai);
		
		
//		DataSourceContextHolder.setDataSourceType("fucai_order_00",fucai_order_00);
//		System.out.println("连接成功"+dynamicDataSource.getConnection().getMetaData().getURL());
//		
//		DataSourceContextHolder.setDataSourceType("fucai_order_01",fucai_order_01);
//		System.out.println("连接成功"+dynamicDataSource.getConnection().getMetaData().getURL());
//		
//		DataSourceContextHolder.setDataSourceType("fucai",fucai);
//		System.out.println("连接成功"+dynamicDataSource.getConnection().getMetaData().getURL());

	//	DataSourceUtil.useDataSource(null);
		
		insertAllDbOrderRecord();
	}
	
	
	public static void insertAllDbOrderRecord(){
//		DataSourceContextHolder.setDataSourceType("fucai");
		String dbName = "";
		Order partnerOrder = new Order();
		partnerOrder.setLotteryId("SSQ");
		partnerOrder.setPartnerId("00860001");
		partnerOrder.setOrderType(1);
		partnerOrder.setUserId(1);
		partnerOrder.setIssueNo("2014001");
		partnerOrder.setOrderContent("01,02,03,04,05,06:07");
		partnerOrder.setOrderStatus(4);
		partnerOrder.setTotalAmount(2);
		partnerOrder.setMultiple(1);
		partnerOrder.setPlayType("0");
		partnerOrder.setPaySerialNumber("11111");
		partnerOrder.setRealName("lsz");
		partnerOrder.setCardNo("131000198605172973");
		partnerOrder.setMobile("18988765634");
		partnerOrder.setExt("测试数据");
		partnerOrder.setCreateTime("2014-08-14 17:44:21");
		
		for(int i = 0; i < 1; i++){
			//dbName = TicketWinningTaskUtil.getDbName(i);
			//DataSourceContextHolder.setDataSourceType(dbName);
			//System.out.println("dbName --->" + dbName);
			for(int j = 0; j < TicketWinningConstantsUtil.ORDER_DB_TABLE_SIZE; j++){
				//更新订单状态
				System.out.println("i --> " + i +" , j --> " + j);
				partnerOrder.setTradeId("123"+i+j);
				partnerOrder.setOrderNo("456#789#7897609");
				System.out.println("before ---> ");
				TransactionProcessor.dynamicInvoke("partnerOrder",
						"addPartnerOrder", partnerOrder);
				System.out.println("after");
			}
		}
		
	}

}

package com.cqfc.statistics.service;

import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * start - end ：表示时间段的始末
 */

@Service
public class CqfcService {

    private static final Logger logger = LoggerFactory.getLogger(CqfcService.class);

    @Autowired
    private JdbcTemplate cqfcJdbcTemplate;

    @Autowired
    private JdbcTemplate cqfcfinanceJdbcTemplate;

    //获取所有的投注站
    private static List<String> stationList = null;
    private long lastOpTime = 0L;
    public List<String> getStationInfo() {
        long delta = System.currentTimeMillis() - lastOpTime;
        if (stationList!=null && delta<3600*1000)    return stationList;
        //String queryBetStation = "select distinct Fbet_station from t_lottery_deal_recent";
        //String queryBetStation = "select distinct station_code from t_lottery_betting_station";
        //stationList = cqfcJdbcTemplate.queryForList(queryBetStation, String.class);
        String queryBetStation = "select distinct station_code from t_lottery_betting_station order by station_code asc";
        stationList = cqfcfinanceJdbcTemplate.queryForList(queryBetStation, String.class);
        logger.info("total bet stations: " + stationList.size());
        return stationList;
    }

    //获取所有的彩种 Map<lotteryId, lotteryName>
    private static Map<String, String> lotteryItemMap = null;
    public Map<String, String> getLotteryItemInfo() {
        if (lotteryItemMap!=null)    return lotteryItemMap;
        lotteryItemMap = new HashMap<String, String>();
        String queryLottery = "select Flottery_id, Flottery_name from t_lottery_item";
        for (Map<String, Object> lotteryItem : cqfcJdbcTemplate.queryForList(queryLottery)) {
            lotteryItemMap.put((String)lotteryItem.get("Flottery_id"), (String)lotteryItem.get("Flottery_name"));
        }
        logger.info("total lottery:" + lotteryItemMap.size());
        return lotteryItemMap;
    }

    //获取某投注站在给定日期的统计信息
    public Stat getStat(String bet_station, Date givenDate) {
        String start = CommonUtil.getDateStart(givenDate);
        String end = CommonUtil.getDateEnd(givenDate);
        Object[] params = new Object[] {bet_station, start, end};
        String queryPay = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where Fbet_station=? and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time>=? and Fbuy_time<?";
        Map<String, Object> pay = cqfcJdbcTemplate.queryForMap(queryPay, params);
        BigDecimal pay_money = (BigDecimal)pay.get("totalAmount");
        BigDecimal pay_cnt = (BigDecimal)pay.get("totalCount");
        if (pay_money==null)    pay_money = new BigDecimal(0);  //如果没有投注记录，则设置为0
        if (pay_cnt==null)      pay_cnt = new BigDecimal(0);    //如果没有投注记录，则设置为0
        String queryNewUser = "select count(Fuser_id) from t_lottery_userinfo_all where Fbet_station=? and Fregist_time>=? and Fregist_time<?";
        Long new_user = cqfcfinanceJdbcTemplate.queryForObject(queryNewUser, params, Long.class);
        if (new_user==null)     new_user = 0L;
        long rdbTime = CommonUtil.getRdbTime(givenDate);
        return new Stat(rdbTime, pay_money, pay_cnt, new_user, bet_station);
    }

    //获取在某给定日期，某个彩种在某投注站的统计信息
    public StatLottery getStatLottery(String bet_station, String lotteryId, Date givenDate) {
        String start = CommonUtil.getDateStart(givenDate);
        String end = CommonUtil.getDateEnd(givenDate);
        Object[] params = new Object[] {bet_station, lotteryId, start, end};
        String queryPay = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where Fbet_station=? and  Flottery_id=? and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time>=? and Fbuy_time<?";
        Map<String, Object> pay = cqfcJdbcTemplate.queryForMap(queryPay, params);
        BigDecimal pay_money = (BigDecimal)pay.get("totalAmount");
        BigDecimal pay_cnt = (BigDecimal)pay.get("totalCount");
        if (pay_money==null)    pay_money = new BigDecimal(0);  //如果没有投注记录，则设置为0
        if (pay_cnt==null)      pay_cnt = new BigDecimal(0);    //如果没有投注记录，则设置为0
        long rdbTime = CommonUtil.getRdbTime(givenDate);
        return new StatLottery(rdbTime, lotteryItemMap.get(lotteryId), pay_money, pay_cnt, bet_station);
    }


    public User getUser(String bet_station, Date startDate, Date givenDate) {
        String content = getUserContent(bet_station, startDate, givenDate);
        return new User(content, bet_station);
    }

    //获取给定时间段的某投注站所有用户信息，按约定格式拼接成字符串
    private String getUserContent(String bet_station, Date startDate, Date givenDate) {
        List<User> ret = new ArrayList<User>();
        //Object[] params = new Object[] {bet_station,  CommonUtil.getDateStart(startDate), CommonUtil.getDateEnd(givenDate)};
        //String queryUser = "select Fnick, Fregist_time from t_lottery_userinfo_all where Fbet_station=? and Fregist_time>=? and Fregist_time<?";
        Object[] params = new Object[] {bet_station};
        String queryUser = "select Fnick, Fregist_time from t_lottery_userinfo_all where Fbet_station=? order by Flast_update_time desc";
        List<Map<String, Object>> userInfoList = cqfcfinanceJdbcTemplate.queryForList(queryUser, params);
        StringBuilder contentBuilder = new StringBuilder();
        Long registTime = 0L;
        for (Map<String, Object> userinfo : userInfoList) {
            registTime = 0L;
            registTime = ((Timestamp)userinfo.get("Fregist_time")).getTime();
//            try {
//                //Date date = CommonUtil.timestampFormat.parse((String) userinfo.get("Fregist_time"));
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            contentBuilder.append(registTime).append("##").append(userinfo.get("Fnick")).append("||");
        }
        if (contentBuilder.length()>2)    contentBuilder.setLength(contentBuilder.length() - 2);  //remove the trailing "||"
        return contentBuilder.toString();
    }

    //获取某投注站给定日期段内的订单信息
    public Deal getDeal(String bet_station, Date givenDate) {
        String start = CommonUtil.getDateStart(givenDate);
        String end = CommonUtil.getDateEnd(givenDate);
        Object[] params = new Object[] {bet_station, start, end};
        String queryDeal = "select Fbuy_time, Fnick, Flottery_name, Ftotal_amount, Fuser_id, Fdeal_id from t_lottery_deal_recent where Fbet_station=? and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time>=? and Fbuy_time<?";
        List<Map<String, Object>> dealInfoList = cqfcJdbcTemplate.queryForList(queryDeal, params);
        StringBuilder contentBuilder = new StringBuilder();
//        Long buyTime = 0L;
        for (Map<String, Object> dealinfo : dealInfoList) {
            contentBuilder.append(((Timestamp) dealinfo.get("Fbuy_time")).getTime()).append("##")
                    .append(dealinfo.get("Fnick")).append("##")
                    .append(dealinfo.get("Flottery_name")).append("##")
                    .append(dealinfo.get("Ftotal_amount")).append("##")
                    .append(dealinfo.get("Fuser_id")).append("##")
                    .append(dealinfo.get("Fdeal_id")).append("||");
        }
        if (contentBuilder.length()>2)    contentBuilder.setLength(contentBuilder.length() - 2);  //remove the trailing "||"
        long rdbTime = CommonUtil.getRdbTime(givenDate);
        return new Deal(rdbTime, contentBuilder.toString(), bet_station);
    }

    //获取到给定日期为止的某投注统计信息
    public StatSum getStatSum(String bet_station, Date givenDate) {
        String end = CommonUtil.getDateEnd(givenDate);
        Object[] params = new Object[] {bet_station, end};
        String queryDeal = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where Fbet_station=? and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time<?";
        Map<String, Object> dealInfo = cqfcJdbcTemplate.queryForMap(queryDeal, params);
        BigDecimal pay_money = (BigDecimal)dealInfo.get("totalAmount");
        BigDecimal pay_cnt = (BigDecimal)dealInfo.get("totalCount");
        if (pay_money==null)    pay_money = new BigDecimal(0);  //如果没有投注记录，则设置为0
        if (pay_cnt==null)      pay_cnt = new BigDecimal(0);    //如果没有投注记录，则设置为0
        String queryNewUser = "select count(Fuser_id) from t_lottery_userinfo_all where Fbet_station=?";
        Long new_user = cqfcfinanceJdbcTemplate.queryForObject(queryNewUser, new Object[] {bet_station}, Long.class);
        if (new_user==null)     new_user = 0L;
        long rdbTime = CommonUtil.getRdbTime(givenDate);
        return new StatSum(rdbTime, pay_money, pay_cnt, new_user, bet_station);
    }

    //获取到给定日期为止的某投注某彩种的统计信息
    public StatLotterySum getStatLotterySum(String bet_station, String lotteryId, Date givenDate) {
        String end = CommonUtil.getDateEnd(givenDate);
        Object[] params = new Object[] {bet_station, lotteryId, end};
        String queryStatLotterySum = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where Fbet_station=? and Flottery_id=? and Fbuy_time<?";
        Map<String, Object> dealInfo = cqfcJdbcTemplate.queryForMap(queryStatLotterySum, params);
        String lotteryName = getLotteryItemInfo().get(lotteryId);
        BigDecimal pay_money = (BigDecimal)dealInfo.get("totalAmount");
        BigDecimal pay_cnt = (BigDecimal)dealInfo.get("totalCount");
        if (pay_money==null)    pay_money = new BigDecimal(0);  //如果没有投注记录，则设置为0
        if (pay_cnt==null)      pay_cnt = new BigDecimal(0);    //如果没有投注记录，则设置为0
        long rdbTime = CommonUtil.getRdbTime(givenDate);
        return new StatLotterySum(rdbTime, lotteryName, pay_money, pay_cnt, bet_station);
    }

}
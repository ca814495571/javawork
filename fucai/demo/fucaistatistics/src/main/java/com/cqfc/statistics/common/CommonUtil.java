package com.cqfc.statistics.common;


import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jolbox.bonecp.BoneCPDataSource;

/**
 * @author: giantspider@126.com
 */

public class CommonUtil {

    public static Properties getProperties(String filename) {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = CommonUtil.class.getClassLoader().getResourceAsStream(filename);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public static synchronized String getDateStart(Date givenDate) {
        String[] tmp = dateFormat.format(givenDate).split("\\s");
        String start = null;
        try {
            start = timestampFormat.format(dateFormat.parse(tmp[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return start;
    }

    public static synchronized String getDateEnd(Date givenDate) {
        Date nextDay = DateUtils.addDays(givenDate, 1);
        String[] tmp = dateFormat.format(nextDay).split("\\s");
        String end = null;
        try {
            end = timestampFormat.format(dateFormat.parse(tmp[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return end;
    }

    

    
    
    //对应rdb数据库里面time的值
    //private static final SimpleDateFormat statisticsFormat = new SimpleDateFormat("yyyyMMdd");
    public static synchronized long getRdbTime(Date givenDate) {
        return Long.valueOf(statisticsFormat.format(givenDate));
    }

    public static final SimpleDateFormat statisticsFormat = new SimpleDateFormat("yyyyMMdd");
    public synchronized  static Date parseStatisticsTime(String str) {
        try {
            return  statisticsFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized  static String formatStatisticsTime(Date date) {
        return  statisticsFormat.format(date);
    }

    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public synchronized  static String formatTimestamp(Date date) {
        return timestampFormat.format(date);
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public synchronized  static String formatDate(Date date) {
        return dateFormat.format(date);
    }


    public static Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
    
    
    public static int halfSearch(int a[], int x) {
    	  int mid, left, right;
    	  left = 0;
    	  right = a.length - 1;
    	   mid = (left + right) / 2;
    	  while (a[mid] != x) {
    	   if (x > a[mid]) {
    	    left = mid + 1;
    	   }
    	   else if (x < a[mid]) {
    	    right = mid - 1;
    	   }
    	            mid=(left+right)/2;
    	  }
    	  return mid;
    	 }
    
    
    public synchronized static String getOffsetMonth(int offset){
    	
    	   Calendar calendar = Calendar.getInstance();
           calendar.add(Calendar.MONTH, offset);
    	
           return formatTimestamp(calendar.getTime());
    }
    
    public synchronized static String getOffsetDay(int offset){
    	
 	   Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, offset);
 	
        return formatTimestamp(calendar.getTime());
 }
    
    
    public synchronized static String getOffsetMin(Date date,int offset){
    	
    	 Calendar calendar = Calendar.getInstance();
    	 calendar.setTime(date);
         calendar.add(Calendar.MINUTE, offset);
         return formatTimestamp(calendar.getTime());
  }
    
    public synchronized static String getOffsetHour(Date date,int offset){
    	
    	  Calendar calendar = Calendar.getInstance();
    	  calendar.setTime(date);
          calendar.add(Calendar.HOUR_OF_DAY, offset);
          return formatTimestamp(calendar.getTime());
   }
    
    public synchronized static String getOffset(Date date,int offsetMonth,int offsetDay,int offsetHour,int offsetMin){
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	if(offsetMonth!=0){
    		calendar.add(Calendar.MONTH, offsetMonth);
    	}
    	if(offsetDay!=0){
    		calendar.add(Calendar.DAY_OF_MONTH, offsetDay);
    	}
    	if(offsetHour!=0){
    		calendar.add(Calendar.HOUR_OF_DAY, offsetHour);
    	}
    	if(offsetMin!=0){
    		calendar.add(Calendar.MINUTE, offsetMin);
    	}
        return formatTimestamp(calendar.getTime());
 }
    
    /**
     *  根据insert sql语句获取需要入库的字段
     */
	public static String[] getInsertColumn(String insertSql){
		
		return insertSql.substring(insertSql.indexOf("(")+1, insertSql.indexOf(")")).split(",");
		
	}
	
	/**
	 * `user_id`, `action_id`, `activity_no`, `activity_type`, `action_type`
	 * 获取update语句的所有字段(update table set a=?,b=?,c=? where user_id=? and action_id=? and activity_no=? and activity_type=? and action_type=?)
	 * @param updateSql
	 * @return
	 */
	public static String[] getUpdateColumn(String updateSql){
		
		
		ArrayList<String> columns   = new ArrayList<String>();
		String lowerSql = updateSql.toLowerCase();
		
		
		String[] lowerSqls = lowerSql.substring(lowerSql.indexOf("set")+"set".length(), lowerSql.indexOf("where")).trim().split(",");
		for (int i = 0; i < lowerSqls.length; i++) {
			
			columns.add(lowerSqls[i].split("=")[0]);
		}
		 lowerSqls = lowerSql.substring(lowerSql.indexOf("where")+"where".length()).split("and");
		
		for (int i = 0; i < lowerSqls.length; i++) {
			columns.add(lowerSqls[i].trim().split("=")[0]);
		}
		
		
		String[] strings = new String[columns.size()];
		return columns.toArray(strings);
	}
	
	
	/**
	 * 根据字段和表名获取insert sql
	 * @param columns
	 * @param tableName
	 * @return
	 */
    public static String getInsertSql(List<String> columns,String tableName){
    	
    	StringBuffer sbf =  new StringBuffer(); 
    	sbf.append(" insert into ");
    	sbf.append(tableName);
    	sbf.append(" (");
    	for (int i = 0; i < columns.size(); i++) {
			
    		sbf.append(columns.get(i));
    		if(i < columns.size()-1){
    			sbf.append(",");
    		}
		}
    	sbf.append(") values (");
    	for (int i = 0; i < columns.size(); i++) {
    		
    		sbf.append("?");
    		if(i < columns.size()-1){
    			sbf.append(",");
    		}
    	}
    	sbf.append(")");
    	return sbf.toString();
    }
    
    
    /**
     * 根据数据库的url和名称得到JdbcTemplate
     */
    private static Map<String,JdbcTemplate> jdbcTemplates = new HashMap<String,JdbcTemplate>();
    public  static JdbcTemplate getJdbcTemplate(String url,String databaseName){
    	try {
			
        	ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        	
        	if(jdbcTemplates.size()<1){
        		
        		JdbcTemplate rdbJdbcTemplate = context.getBean("rdbJdbcTemplate",JdbcTemplate.class);
        		JdbcTemplate cqfcJdbcTemplate = context.getBean("cqfcJdbcTemplate",JdbcTemplate.class);
        		JdbcTemplate cqfcfinanceJdbcTemplate = context.getBean("cqfcfinanceJdbcTemplate",JdbcTemplate.class);
        		
        		jdbcTemplates.put("rdbJdbcTemplate",rdbJdbcTemplate);
        		jdbcTemplates.put("cqfcJdbcTemplate",cqfcJdbcTemplate);
        		jdbcTemplates.put("cqfcfinanceJdbcTemplate",cqfcfinanceJdbcTemplate);
        	}
        	
        	for (JdbcTemplate jdbcTemplate :jdbcTemplates.values()) {
        		BoneCPDataSource dataSource = (BoneCPDataSource) jdbcTemplate.getDataSource();
        		
        		String jdbcUrl = dataSource.getJdbcUrl();
    			if(StringUtils.isNotBlank(jdbcUrl)){
    				
    				String urlTemp = jdbcUrl.substring(jdbcUrl.indexOf("//")+2,jdbcUrl.lastIndexOf("/"));
    				String databaseNameTemp = jdbcUrl.substring(jdbcUrl.lastIndexOf("/")+1, jdbcUrl.lastIndexOf("?"));
    				if(urlTemp.equals(url)&&databaseNameTemp.contains(databaseName)){
    					return jdbcTemplate;
    				}
    			}
    			
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    
    
    public static String selectToInsertSql(String selectSql){
    	selectSql = selectSql.toLowerCase();
    	selectSql = selectSql.substring(selectSql.indexOf("select")+"select".length(), selectSql.indexOf("from"));
    	String [] columns =  selectSql.split(",");
    	
    	List<String> strings = new ArrayList<String>();
    	
    	for (int i = 0; i < columns.length; i++) {
    		strings.add(columns[i].split("as")[1].trim());
		}
    	
    	 return getInsertSql(strings, "t_user_action");
    }
    
    public static void main(String[] args) {
    	
    }
}

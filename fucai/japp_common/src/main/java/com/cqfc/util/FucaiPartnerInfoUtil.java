package com.cqfc.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import com.cqfc.protocol.ticketissue.FucaiPartnerInfo;
import com.jami.util.Log;

public class FucaiPartnerInfoUtil {
	private static final String PARTNER_INFO_FILE = "partner_${cfg.env}.properties";
	private static final String PARTNER_PREFIX = "partner.";
	private static final String PARTNERID_SUFFIX = ".partnerId";
	private static final String SERVERUrl_SUFFIX = ".serverUrl";
	private static final String ALIAS_SUFFIX = ".alias";
	private static final String KEYSTOREPASS_SUFFIX = ".keyStorePass";
	private static final String PRIVATESECRETNAME_SUFFIX = ".privateSecretName";
	private static final String USERID_SUFFIX = ".userId";
	private static final String REALNAME_SUFFIX = ".realName";
	private static final String IDCAED_SUFFIX = ".idCard";
	private static final String PHONE_SUFFIX = ".phone";
	private static final String VERSION_SUFFIX = ".version";
	private static final String PROVINCE_SUFFIX = ".province";
	private static final String PARTNER_TYPE_SUFFIX = ".partnerType";
	//默认合作商
	private static FucaiPartnerInfo defaultPartnerInfo = new FucaiPartnerInfo();
	//合作商列表
	private static List<FucaiPartnerInfo> partnerInfoList = new ArrayList<FucaiPartnerInfo>();
	//键：合作商名  值：合作商对象
	private static Map<String, FucaiPartnerInfo> partnerNameMap = new HashMap<String, FucaiPartnerInfo>();
	//键：合作商Id  值：合作商对象
	private static Map<String, FucaiPartnerInfo> partnerIdMap = new HashMap<String, FucaiPartnerInfo>();
	//键：彩种Id  值：合作商列表
	private static Map<String, List<FucaiPartnerInfo>> partnerMap = new HashMap<String, List<FucaiPartnerInfo>>();
	
	static {
		initPartnerInfoFile();
	}
	
	/**
	 * 初始化读取文件
	 */
	private static void initPartnerInfoFile() {
		try {
			String env = System.getProperty("cfg.env");
			if (StringUtils.isEmpty(env)) {
				env = "local";
				System.out
						.println("cfg.env parameter not found, use 'local' as default.");
			}
			String filePath = PARTNER_INFO_FILE.replace("${cfg.env}", env);
			Resource resource = new ClassPathResource(filePath);
			Properties props = PropertiesLoaderUtils.loadProperties(resource);

			String partnerContent = props.getProperty("partner");
			String defaultPartnerName = props.getProperty(PARTNER_PREFIX + "default");
			
			if (partnerContent != null && !"".equals(partnerContent)) {
				String[] partnerSplit = partnerContent.split(",");
				for (int i = 0, size = partnerSplit.length; i < size; i++) {
					String partnerName = partnerSplit[i];
					String tmp = PARTNER_PREFIX + partnerName;
					FucaiPartnerInfo partnerInfo = new FucaiPartnerInfo();

					partnerInfo.setPartnerId(props.getProperty(tmp
							+ PARTNERID_SUFFIX));
					partnerInfo.setServerUrl(props.getProperty(tmp
							+ SERVERUrl_SUFFIX));
					partnerInfo.setAliasKey(props.getProperty(tmp
							+ ALIAS_SUFFIX));
					partnerInfo.setKeyStorePass(props.getProperty(tmp
							+ KEYSTOREPASS_SUFFIX));
					partnerInfo.setPrivateSecretKey(props.getProperty(tmp
							+ PRIVATESECRETNAME_SUFFIX));
					partnerInfo.setUserId(props.getProperty(tmp
							+ USERID_SUFFIX));
					partnerInfo.setRealName(props.getProperty(tmp
							+ REALNAME_SUFFIX));
					partnerInfo.setIdCard(props.getProperty(tmp
							+ IDCAED_SUFFIX));
					partnerInfo.setPhone(props.getProperty(tmp
							+ PHONE_SUFFIX));
					partnerInfo.setVersion(props.getProperty(tmp
							+ VERSION_SUFFIX));
					partnerInfo.setProvince(props.getProperty(tmp
							+ PROVINCE_SUFFIX));
					partnerInfo.setPartnerType(props.getProperty(tmp
							+ PARTNER_TYPE_SUFFIX));
					
					if(defaultPartnerName != null && !"".equals(defaultPartnerName) && partnerName.equals(defaultPartnerName)){
						defaultPartnerInfo = partnerInfo;
					}
					partnerInfoList.add(partnerInfo);			
				    partnerNameMap.put(partnerName, partnerInfo);
				    partnerIdMap.put(partnerInfo.getPartnerId(), partnerInfo);
				}
			}
			
			String lotteryIdContent = props.getProperty("lotteryId");
					
			if (lotteryIdContent != null && !"".equals(lotteryIdContent)) {
				String[] lotteryIdSplit = lotteryIdContent.split(",");
				for (int i = 0, size = lotteryIdSplit.length; i < size; i++) {
					String lotteryId = lotteryIdSplit[i];
					String partnerNameContent = props
							.getProperty(PARTNER_PREFIX + lotteryId);	
					List<String> lotteryPartnerNameList = Arrays.asList(partnerNameContent.split(","));
					List<FucaiPartnerInfo> partnerList = null;
					for(String lotteryPartnerName : lotteryPartnerNameList){
						if(partnerNameMap.containsKey(lotteryPartnerName)){
							partnerList = new ArrayList<FucaiPartnerInfo>();
							partnerList.add(partnerNameMap.get(lotteryPartnerName));
						}
					}
					partnerMap.put(lotteryId, partnerList);
				}
			}		
		} catch (IOException e) {
			System.out.println("initPartnerPropFile IOException: " + e);
		}
	}
	

	
	/**
	 * 选取默认合作商
	 * @return
	 */
	public static FucaiPartnerInfo selectDefaultFucaiPartnerInfo() {
	
		return defaultPartnerInfo;
	}

	/**
	 * 根据lottery随机选取合作商
	 * 
	 * @param lotteryId
	 * @return
	 */
	public static FucaiPartnerInfo selectRandomFucaiPartnerInfoByLotteryId(
			String lotteryId) {
		try {
			List<FucaiPartnerInfo> partnerInfoList = partnerMap.get(lotteryId);
			Random random = new Random();
			if (partnerInfoList.size() >= 1) {
				return partnerInfoList.get(random.nextInt(partnerInfoList
						.size()));
			}
		} catch (Exception e) {
			Log.run.error(
					"selectRandomFucaiPartnerInfoByLotteryId Exception: ", e);
		}

		return null;
	}

	/**
	 * 根据lottery和partnerId选取合作商
	 * 
	 * @param lotteryId
	 * @param partnerId
	 * @return
	 */
	public static FucaiPartnerInfo selectFucaiPartnerInfoByLotteryIdAndPartnerId(
			String lotteryId, String partnerId) {
		try {
			List<FucaiPartnerInfo> partnerInfoList = partnerMap.get(lotteryId);

			for (int i = 0, size = partnerInfoList.size(); i < size; i++) {
				if (partnerInfoList.get(i).equals(partnerId)) {
					return partnerInfoList.get(i);
				}
			}
		} catch (Exception e) {
			Log.run.error(
					"selectFucaiPartnerInfoByLotteryIdAndPartnerId Exception: ", e);
		}
		return null;
	}
	
	/**
	 * 根据合作商Id选择合作商
	 * @param partnerId
	 * @return
	 */
	public static FucaiPartnerInfo selectFucaiPartnerInfoByPartnerId(String partnerId){
		return partnerIdMap.get(partnerId);
	}
	
	/**
	 * 合作商Id不为空,则选择相应合作商,否则选择默认合作商
	 * @param partnerId
	 * @return
	 */
	public static FucaiPartnerInfo selectFucaiPartnerInfo(String partnerId){
		if(partnerId != null && !"".equals(partnerId)){
			if(partnerIdMap.get(partnerId) == null){
				return defaultPartnerInfo;
			}
			else{
				return partnerIdMap.get(partnerId);
			}
		}
		return defaultPartnerInfo;
	}
	
	
	/**
	 * 根据用户Id选择合作商
	 * @param userId
	 * @return
	 */
	public static FucaiPartnerInfo selectFucaiPartnerInfoByUserId(String userId){
		for(FucaiPartnerInfo partnerInfo : partnerInfoList){
			if(partnerInfo.getUserId().equals(userId)){
				return partnerInfo;
			}
		}
		return null;
	}
	
	/**
	 * 获取合作商列表
	 * @return
	 */
	public static List<FucaiPartnerInfo> getFucaiPartnerInfoList(){
		
		return partnerInfoList;
	}

	public static void main(String[] args) {
		System.out.println(getFucaiPartnerInfoList());
	}
}

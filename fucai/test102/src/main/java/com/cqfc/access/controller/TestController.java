package com.cqfc.access.controller;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cqfc.access.model.Model;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.ReturnData;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.Pair;
import com.cqfc.util.ParameterUtils;
import com.cqfc.xmlparser.util.ErrorMsgXmlHelper;
import com.jami.util.Log;

@Controller
@RequestMapping(value="/test")
public class TestController {
	private static Map<String, LotteryPartner> partnerMap = null;
	private static final AtomicBoolean isInitialization = new AtomicBoolean(
			false);
	private static String serverUrl = ParameterUtils.getParameterValue("serverUrl");
	private static String serverUrlZS = ParameterUtils.getParameterValue("serverUrlZS");
	
	static {
		initPartners();
	}
	
	@RequestMapping(value="/saveUI")
	public String savePersonUI(){				
		return "saveTest";
	}
	
	@RequestMapping(value="/saveZS")
	public String savePersonZS(){				
		return "saveTestZS";
	}
	
	@RequestMapping(value="/saveUIPost")
	public String saveUIPost(){				
		return "savePost";
	}
	
	@RequestMapping(value="/save")
	public String save(Model m) throws UnknownHostException{		
		String transcode = m.getTranscode();	
		String msg=m.getMsg();
		String partnerid =m.getPartnerid();		
		LotteryPartner partner = null;
		Pair<LotteryPartner, String> checkPartnerResult = checkPartner(partnerid, msg);
		if (checkPartnerResult.second() != null) {
			return checkPartnerResult.second();
		}
		partner = checkPartnerResult.first();
		String key = "";
		try {
		    key = Digit.sig((transcode + msg).getBytes("utf-8"),
					partner.getKeyStore(), partner.getAliasKey(),
					ClassPathUtil.getClassPathInputStream(partner.getSecretKey()));	
			key=URLEncoder.encode(key,"UTF-8");
			msg =URLEncoder.encode(msg,"UTF-8");
		} catch (Exception e) {
			Log.run.error("test102(Exception=%s)", e);			
		}			
		Log.run.debug("test102(msg=%s, partnerid=%s, key=%s, transcode=%s)", msg, partnerid, key, transcode);	
		return "redirect:"+serverUrl+"?transcode="+transcode+"&msg="+msg+"&key="+key+"&partnerid="+partnerid;
	}
	
	
	@RequestMapping(value="/zs")
	public String savezs(Model m) throws UnknownHostException{		
		String transcode = m.getTranscode();	
		String msg=m.getMsg();
		String partnerid =m.getPartnerid();		
		LotteryPartner partner = null;
		Pair<LotteryPartner, String> checkPartnerResult = checkPartner(partnerid, msg);
		if (checkPartnerResult.second() != null) {
			return checkPartnerResult.second();
		}
		partner = checkPartnerResult.first();
		String key = "";
		try {
		    key = Digit.sig((transcode + msg).getBytes("utf-8"),
					partner.getKeyStore(), partner.getAliasKey(),
					ClassPathUtil.getClassPathInputStream(partner.getSecretKey()));	
			key=URLEncoder.encode(key,"UTF-8");
			msg =URLEncoder.encode(msg,"UTF-8");
		} catch (Exception e) {
			Log.run.error("savezs(Exception=%s)", e);			
		}			
		Log.run.debug("savezs(msg=%s, partnerid=%s, key=%s, transcode=%s)", msg, partnerid, key, transcode);	
		return "redirect:"+serverUrlZS+"?transcode="+transcode+"&msg="+msg+"&key="+key+"&partnerid="+partnerid;
	}
	
	@RequestMapping(value="/access")
	public String access(Model m) throws UnknownHostException{		
		String transcode = m.getTranscode();	
		String msg=m.getMsg();
		String partnerid =m.getPartnerid();			
		LotteryPartner partner = null;
		Pair<LotteryPartner, String> checkPartnerResult = checkPartner(partnerid, msg);
		if (checkPartnerResult.second() != null) {
			return checkPartnerResult.second();
		}
		partner = checkPartnerResult.first();
		String key = "";		
		try {
			key = Digit.sig((transcode + msg).getBytes("utf-8"),
					partner.getKeyStore(), partner.getAliasKey(),
					ClassPathUtil.getClassPathInputStream(partner.getSecretKey()));	
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			msg =URLEncoder.encode(msg,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return "redirect:"+serverUrl+"?transcode="+transcode+"&msg="+msg+"&key="+key+"&partnerid="+partnerid;
	}
	
	
	/**
	 * 初始获取合作商列表
	 */
	private static boolean initPartners() {
		// 该方法基于目前partner数量不会很多的情况下直接去partner模块将所有partner(10000以内)一次性取过来，假如后续partner较多，这里需要分多次循环去取。
		if (isInitialization.compareAndSet(false, true)) {
			try {
				List<LotteryPartner> partners = new ArrayList<LotteryPartner>();
				Map<String, LotteryPartner> map = new ConcurrentHashMap<String, LotteryPartner>();
				ReturnMessage reMsg = TransactionProcessor.dynamicInvoke(
						"partner", "getLotteryPartnerList",
						new LotteryPartner(), 1, 10000);
				if (reMsg.getObj() != null) {
					ReturnData re = (ReturnData) reMsg.getObj();
					partners = re.getResultList();
					for (LotteryPartner partner : partners) {
						map.put(partner.getPartnerId(), partner);
					}
					partnerMap = map;
				}
				return true;
			} catch (Exception e) {
				Log.run.error("get partner failed.errMsg=%s", e.toString());
			} finally {
				isInitialization.set(false);
			}
		}
		return false;
	}
	
	/**
	 * 校验合作商id是否存在
	 * 
	 * @param partnerid
	 * @param msg
	 * @return
	 */
	private Pair<LotteryPartner, String> checkPartner(String partnerid, String msg) {
		Pair<LotteryPartner, String> result = new Pair<LotteryPartner, String>();
		if (partnerMap == null) {
			if (initPartners()){
				if (partnerMap == null || !partnerMap.containsKey(partnerid)){
					Log.run.error("test102,get all partner, but partnerid不存在 ,partnerid: %s, msg: %s",
							partnerid, msg);
					result.second(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "不存在的合作商",
							ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST));
					return result;
				}
				result.first(partnerMap.get(partnerid));
				return result;
			} else {
				ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
						"partner", "findLotteryPartnerById", partnerid);

				if (retMsg.getObj() == null) {
					Log.run.error("test102,get one partner when map is null, partnerid不存在 .partnerid: %s, msg: %s",
							partnerid, msg);
					result.second(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "不存在的合作商",
							ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST));
					return result;
				} else {
					LotteryPartner partner = (LotteryPartner) retMsg.getObj();
					partnerMap.put(partner.getPartnerId(), partner);
					Log.run.info(
							"test102,find lottery partner success, partnerid: %s, msg: %s",
							partnerid, msg);
					result.first(partner);
					return result;
				}
			}
		} else {
			if (partnerMap.containsKey(partnerid)) {
				result.first(partnerMap.get(partnerid));
				return result;
			}
			ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
					"partner", "findLotteryPartnerById", partnerid);

			if (retMsg.getObj() == null) {
				Log.run.error("test102,get one partner when map not contain it, partnerid不存在 .partnerid: %s, msg: %s",
						partnerid, msg);
				result.second(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "不存在的合作商",
						ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST));
				return result;
			} else {
				LotteryPartner partner = (LotteryPartner) retMsg.getObj();
				partnerMap.put(partner.getPartnerId(), partner);
				Log.run.info(
						"test102,find lottery partner success, partnerid: %s, msg: %s",
						partnerid, msg);
				result.first(partner);
				return result;
			}

		}
	}
	
}

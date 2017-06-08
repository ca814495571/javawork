package com.cqfc.access.controller;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.access.util.Digit;

@Controller
@RequestMapping("/verification")
public class VerificationController {

	public static Log logger = LogFactory.getLog(VerificationController.class);

	/**
	 * 模拟处理数据
	 */
	@RequestMapping(value = "/verify")
	@ResponseBody
	public Boolean verifyTranscodeAndMsg(@Param("transcode") String transcode,
			@Param("msg") String msg, @Param("key") String key,
			@Param("partnerid") String partnerid,
			@Param("version") String version, @Param("time") String time) {
		String txt = transcode + msg;
		String public_path = Thread.currentThread().getContextClassLoader().getResource("19_public").getFile();
;
		byte[] sigedText = null;
		sun.misc.BASE64Decoder base64 = new sun.misc.BASE64Decoder();
		try {
			sigedText = base64.decodeBuffer(key);
		} catch (IOException e) {
			e.printStackTrace();
		}	

		return Digit.veriSig(txt.getBytes(), sigedText, public_path);
	}
}

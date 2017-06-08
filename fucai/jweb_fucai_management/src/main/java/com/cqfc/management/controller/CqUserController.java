package com.cqfc.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.service.ICqUserService;


@Controller
@RequestMapping("/cqUser")
public class CqUserController {

	
	@Autowired
	ICqUserService CqUserService;
	
	@RequestMapping("/getAccountInfo")
	@ResponseBody
	public PcResultObj getCqUserAccountInfo(String paramType ,String value){
		
		PcResultObj obj = new PcResultObj();
		if(paramType == null || paramType.equals("")){
			return obj;
		}
		if(value == null || value.equals("")){
			return obj;
		}
		return CqUserService.getCqUserAccountInfo(paramType, value);
	}
	
}

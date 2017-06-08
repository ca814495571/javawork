package com.cqfc.management.interceptor;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cqfc.management.model.Fuser;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.service.IFuserService;

public class CommInterceptor extends HandlerInterceptorAdapter  {
	
	

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
	
		
		String path = request.getRequestURI().substring(
				request.getContextPath().length());

		Fuser fuser = (Fuser) request.getSession()
				.getAttribute(IFuserService.SESSION_USER);
		if (fuser != null) {
 
			Set<String> resources = fuser.getResources();
			if (resources.contains(path)) {
				return true;
			}else{
				
				PcResultObj pcResultObj = new PcResultObj();
				ObjectMapper objectMapper = new ObjectMapper();
				pcResultObj.setMsg("权限不足!");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				objectMapper.writeValue(response.getOutputStream(), pcResultObj);
				return false;
			}
			
		} else {

			PcResultObj pcResultObj = new PcResultObj();
			ObjectMapper objectMapper = new ObjectMapper();
			pcResultObj.setMsg("登陆超时!");
			pcResultObj.setMsgCode(PcResultObj.NO_RIGHT_CODE);
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			objectMapper.writeValue(response.getOutputStream(), pcResultObj);
			return false;
		}
		
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}

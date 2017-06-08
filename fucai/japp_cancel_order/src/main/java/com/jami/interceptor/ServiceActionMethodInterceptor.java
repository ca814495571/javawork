package com.jami.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;

public class ServiceActionMethodInterceptor extends AbstractMonitoringInterceptor {
	private static final String PRE_SUFFIX = "";
	private static final String PRE_SPLIT = "\t";

	protected boolean isInterceptorEnabled(MethodInvocation invocation, org.apache.commons.logging.Log logger) {
		return invocation.getMethod().isAnnotationPresent(PrintProfiler.class);
	}

	protected Object invokeUnderTrace(MethodInvocation methodInvocation, org.apache.commons.logging.Log log)
			throws Throwable {
		long startTime = System.currentTimeMillis();

		Object resp = methodInvocation.proceed();

		String className = methodInvocation.getMethod().getDeclaringClass().getSimpleName();
		String methodName = methodInvocation.getMethod().getName();
		String keyName = className + "#" + methodName;

		Method method = methodInvocation.getMethod();

		String errCode = BeanUtils.getProperty(resp, "errCode");
		String errMsg = BeanUtils.getProperty(resp, "errMsg");
		long timeCost = System.currentTimeMillis() - startTime;

		logPerf(Integer.parseInt(errCode), errMsg, keyName, timeCost);

		return resp;
	}

	private void logPerf(int errCode, String errMsg, String keyName, long timeCost) {
		StringBuffer sb = new StringBuffer("");
		sb.append(keyName).append("\t");

		if (errCode == 0) {
			errMsg = "SUCCESS";
		}
		sb.append(errCode).append("\t").append(errMsg).append("\t").append(timeCost).append("ms");

		if (errCode != 0) {
			com.jami.util.Log.perf.warn(sb.toString());
		} else
			com.jami.util.Log.perf.info(sb.toString());
	}

}

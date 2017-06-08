package com.cqfc.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.common.zookeeper.Server;
import com.cqfc.common.zookeeper.ServerCache;
import com.cqfc.common.zookeeper.route.RouteServerManager;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DeviceHelper;
import com.cqfc.util.RouteUtil;
import com.cqfc.xmlparser.devices.Device;
import com.jami.util.Log;

public class TransactionProcessor {

	private static final String HTTP_URL_START = "http://";
	private static final String SOLR_URL_CONTEXT = "/solr/";

	public static String getSolrUrl(String coreName) {
//		int solrIndex = RouteUtil.getSolrIndex(coreName);
//		Device device = DeviceHelper.getMinQueueSizeDevice(ConstantsUtil.MODULENAME_SOLR, solrIndex);
		Server server = RouteServerManager.getServerRandom(ConstantsUtil.MODULENAME_SOLR);
		if (server == null) {
			Log.run.error("找不到solr服务器");
			return null;
		}
		return HTTP_URL_START + server.getHost() + ":" + server.getPort() + SOLR_URL_CONTEXT + coreName;
	}

	private static Class[] getAgrsTypes(List<Object> args) {
		Class[] argsClassType = new Class[args.size()];
		/**
		 * 说明：为了处理传入的参数类型是基本类型，接收后调用args[i]会返回其的包装类型的class， 如果不进行转换会发生
		 * 方法找不到异常。比如传入基本类型的整型i(i=1)， i.getClass()会得到
		 * java.lang.Integer，所以需要转换成int.class。List，Map，Set也需要做类似处理。
		 */
		for (int i = 0; i < args.size(); i++) {
			argsClassType[i] = args.get(i).getClass();
			String name = argsClassType[i].getName();

			if (name.contains("java.lang.Integer")) {
				argsClassType[i] = int.class;
			} else if (name.contains("java.lang.Long")) {
				argsClassType[i] = long.class;
			} else if (name.contains("java.lang.Float")) {
				argsClassType[i] = float.class;
			} else if (name.contains("java.lang.Short")) {
				argsClassType[i] = short.class;
			} else if (name.contains("java.lang.Double")) {
				argsClassType[i] = double.class;
			} else if (name.contains("java.lang.Boolean")) {
				argsClassType[i] = boolean.class;
			} else if (name.contains("java.lang.Byte")) {
				argsClassType[i] = byte.class;
			} else if (name.contains("java.lang.Character")) {
				argsClassType[i] = char.class;
			} else if (name.contains("java.lang.Integer")) {
				argsClassType[i] = int.class;
			} else if (name.contains("java.util.ArrayList")) {
				argsClassType[i] = List.class;
			} else if (name.contains("java.util.HashMap")) {
				argsClassType[i] = Map.class;
			} else if (name.contains("java.util.LinkedList")) {
				argsClassType[i] = List.class;
			} else if (name.contains("java.util.HashSet")) {
				argsClassType[i] = Set.class;
			} else if (name.contains("java.util.TreeSet")) {
				argsClassType[i] = Set.class;
			} else if (name.contains("java.util.TreeMap")) {
				argsClassType[i] = Map.class;
			}
		}
		return argsClassType;
	}

	private static List<Object> convertArray2List(Object... args) {
		List<Object> argList = new ArrayList<Object>();
		for (int i = 0; i < args.length; i++) {
			argList.add(args[i]);
		}
		return argList;
	}

	public static ReturnMessage dynamicInvoke(String moduleName, String methodName, Object... args) {
		ReturnMessage retMsg = new ReturnMessage();
		Object retObj = null;
		TTransport transport = null;
		String className = "";
		try {
			List<Object> argList = convertArray2List(args);
//			int index = RouteUtil.getModuleIndex(moduleName, methodName, argList);
			// 1.通用逻辑，根据className找到路由地址
//			Device minQueueSizeDevice = DeviceHelper.getMinQueueSizeDevice(moduleName, index);
			String serverKey = ServerCache.getServerKey(moduleName, methodName);
			Server server = RouteServerManager.getServerRandom(serverKey);
			if (server == null) {
				retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_RETURN_CONNECTION_ERROR);
				retMsg.setMsg(serverKey + "找不到对应的模块");
				return retMsg;
			}
			Log.run.debug("dynamicInvoke(moduleName=%s,methodName=%s,argsLength=%d,ip=%s,port=%d)",
					moduleName, methodName, args.length, server.getHost(), server.getPort());
			transport = new TSocket(server.getHost(), server.getPort());
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);

			// 2.根据className，methodName，arguments调用，并得到返回

			// 根据className得到其Class
			className = getClassNameByModuleName(moduleName);
			Class<?> clazz = Class.forName(className);

			// 得到传入方法名中参数的Class类型
			Class[] argsClassType = getAgrsTypes(argList);

			// 根据方法名和参数调用方法
			Object obj = clazz.getConstructor(new Class[] { TProtocol.class }).newInstance(new Object[] { protocol });
			Method method2 = clazz.getMethod(methodName, argsClassType);
			retObj = method2.invoke(obj, argList.toArray());

			retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS);
			retMsg.setMsg("success");
		} catch (ClassNotFoundException e) {
			retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_RETURN_CLASS_NOTEXIST);
			retMsg.setMsg("类找不到");
			Log.run.error("dynamicInvoke(result): " + className + "类找不到");
			Log.error("dynamicInvoke(result): " + className + "类找不到");
		} catch (SecurityException e) {
			retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_RETURN_CLASS_NOTEXIST);
			retMsg.setMsg("反射类不安全(可能原因：访问了私有属性或调用了私有方法)");
			Log.run.error("dynamicInvoke(result): " + className + "." + methodName + "反射类不安全(可能原因：访问了私有属性或调用了私有方法)");
			Log.error("dynamicInvoke(result): " + className + "." + methodName + "反射类不安全(可能原因：访问了私有属性或调用了私有方法)");
		} catch (NoSuchMethodException e) {
			retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_RETURN_METHOD_NOTEXIST);
			retMsg.setMsg("方法找不到");
			Log.run.error("dynamicInvoke(result): " + className + "." + methodName + "方法找不到");
			Log.error("dynamicInvoke(result): " + className + "." + methodName + "方法找不到");
		} catch (IllegalArgumentException e) {
			retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_RETURN_ARGUMENT_ERROR);
			retMsg.setMsg("参数不正确");
			Log.run.error("dynamicInvoke(result): " + className + "." + methodName + "参数不正确");
			Log.error("dynamicInvoke(result): " + className + "." + methodName + "参数不正确");
		} catch (InstantiationException e) {
			retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_RETURN_ARGUMENT_ERROR);
			retMsg.setMsg("类无法实例化");
			Log.run.error("dynamicInvoke(result): " + className + "类无法实例化");
			Log.error("dynamicInvoke(result): " + className + "类无法实例化");
		} catch (IllegalAccessException e) {
			retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_RETURN_METHOD_INVOKE_ERROR);
			retMsg.setMsg("方法调用错误");
			Log.run.error("dynamicInvoke(result): " + className + "." + methodName + "方法调用错误");
			Log.error("dynamicInvoke(result): " + className + "." + methodName + "方法调用错误");
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof TApplicationException) {
				retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				retMsg.setMsg("未查询到数据");
				Log.run.debug("dynamicInvoke(result): " + className + "." + methodName + "未查询到数据");
			} else {
				retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
				retMsg.setMsg("系统错误");
				Log.run.debug("", e.getTargetException());
				Log.run.error("dynamicInvoke(result): " + className + "." + methodName + "系统错误");
			}
		} catch (TTransportException e) {
			if (e instanceof TTransportException && e.getType() == 1) {
				retMsg.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
				retMsg.setMsg("系统错误");
				Log.run.error("dynamicInvoke(result): " + className + "." + methodName + "Socket连接错误");
			}
			Log.run.error("dynamicInvoke(result):", e);
		} catch (Throwable e) {
			Log.run.error("dynamicInvoke(result):", e);
		} finally {
			// 关闭TTransport
			if(transport != null){
				transport.close();
			}
		}
		retMsg.setObj(retObj);
		return retMsg;
	}

	private static String getClassNameByModuleName(String moduleName) {
		return "com.cqfc.protocol." + moduleName.toLowerCase(Locale.ENGLISH) + "."
				+ moduleName.substring(0, 1).toUpperCase(Locale.ENGLISH) + moduleName.substring(1) + "Service$Client";
	}
}

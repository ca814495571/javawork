package com.cqfc.util;

import java.util.List;

/**
 * 路由的工具类，用来生成Set的序号
 * 
 * @author HowKeyond
 * 
 */
public class RouteUtil {
	/**
	 * 获取模块的位置
	 * 
	 * @param moduleName
	 *            模块名，方法名，参数
	 * @param methodName
	 * @param args
	 * @return
	 */
	public static int getModuleIndex(String moduleName, String methodName, List<Object> args) {
		if (ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER.equals(moduleName)) {
			if (ConstantsUtil.METHODNAME_PROCESSMESSAGE.equals(methodName)
					|| ConstantsUtil.METHODNAME_ISTICKETSUCCESS.equals(methodName)
					|| ConstantsUtil.METHODNAME_CREATEAPPENDORDER.equals(methodName)
					|| ConstantsUtil.METHODNAME_CREATEORDERPROCESS.equals(methodName)
					|| ConstantsUtil.METHODNAME_FINDORDERPROCESS.equals(methodName)) {
				String key = (String) args.remove(0);
				int hash = Math.abs(key.hashCode());
				hash = hash % ConstantsUtil.MAX_BUSINESSCONTROLLER_NUM;
				int moduleType = (Integer) args.remove(0);
				int moduleNum = DeviceHelper.getModuleNum(moduleName, moduleType);
				return DeviceHelper.getBusinessIndex(moduleType, hash % moduleNum);
			}
		} else if (ConstantsUtil.MODULENAME_ACCESS_BACK.equals(moduleName)) {
			// 其他的路由在这里生成
		}
		return ConstantsUtil.ANY;
	}

	public static int getSolrIndex(String coreName) {
		return ConstantsUtil.ANY;
	}
}

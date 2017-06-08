package com.cqfc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cqfc.common.zookeeper.ServerUtil;

public class GenerateIdlCfg {
	private static final String SUBBIX = "Service.class";

	private static final Map<String, String> portMap = new HashMap<String, String>();

	static {
		portMap.put("AccessBackService", "10090");
		portMap.put("AppendTaskService", "10013");
		portMap.put("BusinessControllerService", "10010");
		portMap.put("CancelOrderService", "10017");
		portMap.put("LotteryIssueService", "10006");
		portMap.put("PartnerService", "10005");
		portMap.put("PartnerAccountService", "10008");
		portMap.put("PartnerOrderService", "10088");
		portMap.put("RiskControlService", "10087");
		portMap.put("TicketIssueService", "10066");
		portMap.put("TicketWinningService", "10091");
		portMap.put("UserAccountService", "10009");
		portMap.put("UserOrderService", "10089");
		portMap.put("IdGenerateService", "10040");
		portMap.put("solr", "8080");
	}

	private static void gennerateConfig() throws IOException {
		String env = System.getProperty("cfg.env");
		if (StringUtils.isEmpty(env)) {
			env = "dev";
			System.out.println("cfg.env parameter not found, use 'dev' as default.");
		}
		String localip = ServerUtil.getLocalNetWorkIp();
		FileOutputStream fos = null;
		try {
			Resource resource = null;
			Properties props = null;
			String filePath = "";
			try {
				resource = new ClassPathResource("service_ip_" + env
						+ ".properties");
				props = PropertiesLoaderUtils.loadProperties(resource);
				filePath = resource.getFile().getParent();
			} catch (Exception e) {
				filePath = GenerateIdlCfg.class.getResource("/").getPath();
				System.out.println("service_ip_" + env + ".properties file not found, use local ip and default port.");
			}
			String cfgFile = filePath + "/idl_route_" + env
					+ ".properties";
			fos = new FileOutputStream(cfgFile, false);
			File file = new File(filePath, "com/cqfc/protocol");

			File[] files = file.listFiles();
			List<File> fileList = Arrays.asList(files);
			Collections.sort(fileList);
			for (File f : fileList) {
				if (!f.exists() || f.isFile()) {
					continue;
				}
				String[] list = f.list();
				for (String fileName : list) {
					if (fileName.endsWith(SUBBIX)) {
						String serviceName = fileName.substring(0,
								fileName.length() - 6);
						List<String> idlList = ServerUtil.getIdlList(Class
								.forName("com.cqfc.protocol." + f.getName()
										+ "." + serviceName + "$Iface"));
						String ips = (props != null && props
								.containsKey(serviceName)) ? (String) props
								.get(serviceName) : "";
						for (String idl : idlList) {
							if (props != null && props.containsKey(idl)) {
								fos.write((idl + "=" + props.getProperty(idl) + "\n")
										.getBytes());
							} else {
								if (ips.isEmpty()) {
									fos.write((idl + "=" + localip + ":"
											+ portMap.get(serviceName) + "\n")
											.getBytes());
								} else {
									fos.write((idl + "=" + ips + "\n")
											.getBytes());
								}
							}
						}
					}
				}
			}
			if (props != null
					&& props.containsKey(ConstantsUtil.MODULENAME_SOLR)) {
				fos.write((ConstantsUtil.MODULENAME_SOLR + "="
						+ props.getProperty(ConstantsUtil.MODULENAME_SOLR) + "\n")
						.getBytes());
			} else {
				fos.write((ConstantsUtil.MODULENAME_SOLR + "=" + localip + ":"
						+ portMap.get(ConstantsUtil.MODULENAME_SOLR) + "\n")
						.getBytes());
			}
			System.out.println("Generate idl cfg file ok, file=" + cfgFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		gennerateConfig();
	}
}

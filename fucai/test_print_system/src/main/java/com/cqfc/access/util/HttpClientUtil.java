package com.cqfc.access.util;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientUtil {

	private static PoolingHttpClientConnectionManager connManager = null;

	static {
		connManager = new PoolingHttpClientConnectionManager();
		 // 将最大连接数增加到200
		connManager.setMaxTotal(200);
	    // 将每个路由基础的连接增加到20
		connManager.setDefaultMaxPerRoute(20);
	}

	public static CloseableHttpClient getHttpClient() {
		return HttpClients.custom().setConnectionManager(connManager)
				.build();
	}

}

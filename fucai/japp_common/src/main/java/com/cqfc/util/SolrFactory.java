package com.cqfc.util;


public class SolrFactory {
	public static SolrServer getSolrServer(String coreName) {
		return new SolrServer(coreName);
	}
}

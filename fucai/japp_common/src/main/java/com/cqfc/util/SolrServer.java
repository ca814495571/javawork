package com.cqfc.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.thrift.TFieldIdEnum;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.protocol.useraccount.UserInfo._Fields;
import com.jami.util.Log;

public class SolrServer {
	private HttpSolrServer server;

	public SolrServer(String coreName) {
		String solrUrl = TransactionProcessor.getSolrUrl(coreName);
		if (solrUrl == null){
			throw new IllegalArgumentException("solr服务器不存在");
		}
		server = new HttpSolrServer(solrUrl);
	}

	public UpdateResponse deleteData(String queryStr) throws SolrServerException, IOException{
		return server.deleteByQuery(queryStr);
	}
	
	public UpdateResponse deleteById(List<String> ids) throws SolrServerException, IOException{
		return server.deleteById(ids);
	}
	public void addData(List<String> fieldName, List<List<Object>> values)
			throws SolrServerException, IOException {
		for (List<Object> data : values) {
			int size = data.size();
			SolrInputDocument doc = new SolrInputDocument();
			for (int i = 0; i < fieldName.size(); i++) {
				if (i >= size) {
					Log.run.warn("fields of data is less then fieldNames.");
					break;
				}
				doc.addField(fieldName.get(i), data.get(i));
			}
			server.add(doc);
		}
	}

	public List<SolrDocument> findData(String queryStr, int start,
			int pageSize) throws SolrServerException {
		SolrQuery query = new SolrQuery(queryStr);

		query.setStart(start);// 设置起始位置
		query.setRows(pageSize); // 查询组数
		QueryResponse response = server.query(query);
		List<SolrDocument> docs = response.getResults();// 得到结果集
		return docs;
	}
	
	public List<SolrDocument> findData(Map<String, Object> fields, int start, int pageSize) throws SolrServerException{
		StringBuffer sb = new StringBuffer();
		String and = " AND　";
		for(Entry<String, Object> entry:fields.entrySet()){
			sb.append(entry.getKey()).append(":");
			if (entry.getValue() instanceof String){
				sb.append("\"").append(entry.getValue()).append("\"");
			}else{
				sb.append(entry.getValue());
			}
			sb.append(and);
		}
		sb.delete(sb.length()-and.length(), sb.length());
		return findData(sb.toString(), start, pageSize);
	}
	
	public UpdateResponse commit() throws SolrServerException, IOException{
		return server.commit();
	}

	public void addData(TFieldIdEnum[] fields, Set<String> excludeFields, Object obj) throws SolrServerException, IOException {
		Pair<List<String>, List<Object>> pair = getObjectFields(fields, obj, excludeFields);
		List<List<Object>> values = new ArrayList<List<Object>>();
		values.add(pair.second());
		addData(pair.first(), values);
	}
	
	public static Pair<List<String>, List<Object>> getObjectFields(
			TFieldIdEnum[] fields, Object obj, Set<String> excludeFields) {
		List<String> fieldNames = new ArrayList<String>();
		List<Object> fieldValues = new ArrayList<Object>();
		String fieldName = null;
		String methodName = null;
		for (TFieldIdEnum field : fields) {
			fieldName = field.getFieldName();
			if (excludeFields.contains(fieldName)) {
				continue;
			}
			try {
				methodName = "get" + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH)
						+ fieldName.substring(1);
				Method method = obj.getClass().getMethod(methodName);
				Object value = method.invoke(obj);
				fieldNames.add(fieldName);
				fieldValues.add(value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new Pair<List<String>, List<Object>>(fieldNames, fieldValues);
	}
	
	public static void main(String[] args) throws SolrServerException, IOException {
		SolrServer tmpSer = new SolrServer("recharge");
		List<String> fieldName = new ArrayList<String>();
		fieldName.add("userId");
		fieldName.add("nickName");
		fieldName.add("mobile");
		List<List<Object>> datas = new ArrayList<List<Object>>();
		for (int i = 0; i < 1000; ++i) {
			List<Object>value = new ArrayList<Object>();
			value.add("user-" + i);
			value.add("用户" + i);
			value.add("134567" + "8901" + i);
			datas.add(value);
		}
//		tmpSer.addData(fieldName, datas);
//		tmpSer.commit();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", "1");
//		map.put("mobile", "13456789012");
		List<SolrDocument> findData = tmpSer.findData(map, 0, 100);
		System.out.println(findData);
//		List<SolrDocument> findData = tmpSer.findData("userId:'1'", 0, 100);
//		System.out.println(findData);
//		UserInfo info = new UserInfo();
//		info.setUserId(123);
//		info.setPartnerId("test");
//		Set<String> excludeFields = new HashSet<String>();
//		excludeFields.add("userAccount");
//		excludeFields.add("userHandselList");
//		System.out.println(tmpSer.getUserFields(info, excludeFields));
	}
}

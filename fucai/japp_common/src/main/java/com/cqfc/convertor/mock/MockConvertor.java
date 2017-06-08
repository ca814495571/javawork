package com.cqfc.convertor.mock;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cqfc.convertor.AbstractConvertor;

public class MockConvertor extends AbstractConvertor {
	@Override
	public String convert4out(String content) {
		String[] matchContents = content.split("/");
		Random r = new Random();
		StringBuffer result = new StringBuffer();;
		Pattern pattern = Pattern.compile("(\\d:\\d)|(\\d+)");
		for (int i = 0, len = matchContents.length; i < len; i++) {
			String[] matchSplit = matchContents[i].split("~");
			if (matchSplit.length != 3) {
				result.append(matchContents[i]).append("/");
				continue;
			}
			result.append(matchSplit[0]).append("~");
			Matcher matcher = pattern.matcher(matchSplit[1]);
			while(matcher.find()) {
				float f = r.nextFloat();
				matcher.appendReplacement(result, matcher.group());
				result.append("@").append(String.format("%.2f", 1 + f*4));
			}			
			matcher.appendTail(result);
			result.append("~").append(matchSplit[2]).append("/");
		}
		return result.substring(0, result.length() - 1);
	}

	@Override
	public String convert4In(String content) {
		return super.convert4In(content);
	}

	public static void main(String[] args) {
		String ticket = "1201503253011~[JQS#106,JQS#2,JQS#3,SPF#0]~0/1201503253012~[BQC#33,BQC#31,BQC#30]~0/1201503253013~[SPF#1,SPF#2]~0/1201503253013~[BF#1:2,BF#3:1]~0";//"1201503253002~[3:1,0:2]~0/1201503253003~[3:2]~0";//"1201503253003~[0,1,2,3]~0/1201503253004~[0,3,2]~0/20150325005~[0,1]~0";//"1201503253002~[3,0]~0/1201503253003~[3]~0";
		String result = new MockConvertor().convert4out(ticket);
		System.out.println(result);
	}

}

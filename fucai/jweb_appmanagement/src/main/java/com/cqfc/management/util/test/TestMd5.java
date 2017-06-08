package com.cqfc.management.util.test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestMd5 {
	
	
	
	public static String get(String str){
		
			
			try {
				MessageDigest md= MessageDigest.getInstance("MD5");
				md.update(str.getBytes());   
				String pwd = new BigInteger(1, md.digest()).toString(16);  
				// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符   
				// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值   
				return pwd;
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		    // 计算md5函数   
	}

	public static String getMD5(String str) {

		try {

			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
			// 16位的加密
			// return buf.toString().substring(8, 24);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;

	}
	


	public static void main(String[] args) {

		//4f22e11dbbfd367bdb3be128af98aec3
			System.out.println(getMD5("Cq_Fucai_408c612767384dfecfdfd73d1d82596c"));
			System.out.println(get("123"));
	}

}

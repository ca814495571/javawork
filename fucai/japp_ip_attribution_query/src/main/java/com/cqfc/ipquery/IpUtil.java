package com.cqfc.ipquery;

public class IpUtil {
	public static Long convertIp(String ip){
		String[] sec = ip.split("\\.");
		if (sec.length != 4){
			return null;
		}
		long index = 0;
		int total=3;
		for(int i=0;i<sec.length;i++){
			try {
				long tmp = Long.parseLong(sec[i]);
				index |= tmp << 8*(total-i);
			} catch (Exception e) {
				return null;
			}
		}
		return index;
	}
	public static void main(String[] args) {
		System.out.println(convertIp("10.3.8.186"));
	}
}

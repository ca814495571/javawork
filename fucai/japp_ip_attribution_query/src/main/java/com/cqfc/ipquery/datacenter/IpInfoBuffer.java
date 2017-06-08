package com.cqfc.ipquery.datacenter;

import java.util.List;

import com.cqfc.ipquery.IpUtil;

public class IpInfoBuffer {
	private final static IpInfoBuffer instance = new IpInfoBuffer();
	private List<Long> ipStartIndexs = null;

	public static IpInfoBuffer getInstance() {
		return instance;
	}

	private IpInfoBuffer() {
	}

	public void initDatas(List<Long> startIndexs) {
		ipStartIndexs = startIndexs;
	}

	public Long locateIndex(String ip) {
		Long convertIp = IpUtil.convertIp(ip);
		if (convertIp == null) {
			return null;
		}
		int index = binarySearch(ipStartIndexs, 0, ipStartIndexs.size(), convertIp);
		if (index < 0){
			return null;
		}
		return ipStartIndexs.get(index);
	}

	private static int binarySearch(List<Long> datas, int fromIndex,
			int toIndex, Long key) {
		int low = fromIndex;
		int high = toIndex - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			Long midVal = datas.get(mid);
			if (midVal < key){
				low = mid + 1;
			} else if(midVal > key){
				high = mid - 1;
			}else{
				return mid; 
			}
		}
		return low - 1; 
	}
}

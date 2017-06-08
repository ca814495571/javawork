package com.cqfc.ipquery;
import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cqfc.ipquery.service.IIpQueryService;
import com.cqfc.protocol.ipquery.IpInfo;
import com.cqfc.protocol.ipquery.IpQueryService;

@Service
public class IpQueryServiceWrapper implements IpQueryService.Iface {
	@Resource
	private IIpQueryService ipQueryService;
	
	@Override
	public IpInfo queryIpAttribution(String ipAddr) throws TException {
		return ipQueryService.queryIpAttribution(ipAddr);
	}

	@Override
	public int parseIp2DB() throws TException {
		// TODO Auto-generated method stub
		return ipQueryService.parseIp2DB();
	}

}

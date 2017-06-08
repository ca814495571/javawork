package com.cqfc.ipquery.service;

import com.cqfc.protocol.ipquery.IpInfo;

public interface IIpQueryService {
    public IpInfo queryIpAttribution(String ipAddr);

	public int parseIp2DB();
}

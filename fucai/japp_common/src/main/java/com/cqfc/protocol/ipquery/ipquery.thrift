namespace java com.cqfc.protocol.ipquery

struct IpInfo{
	1:string address, //Ip地址
	2:string province //所属省份
	3:string detail  //详细
	4:string used    //用途
}

service IpQueryService{
	//查询IP地址归属地
	IpInfo queryIpAttribution(1:string ipAddr);
	
	//解析IP信息入库
	i32 parseIp2DB();
}
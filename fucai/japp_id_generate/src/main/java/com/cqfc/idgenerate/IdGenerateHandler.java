package com.cqfc.idgenerate;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cqfc.idgenerate.service.IIdGenerateService;
import com.cqfc.protocol.idgenerate.IdGenerateService;

/**
 * @author liwh
 */
@Service
public class IdGenerateHandler implements IdGenerateService.Iface {

	@Resource
	private IIdGenerateService idGenerateService;

	@Override
	public long idGen(String idName) throws TException {
		return idGenerateService.idGen(idName);
	}

}

package com.cqfc.idgenerate.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.idgenerate.dao.mapper.IdGenerateMapper;
import com.cqfc.protocol.idgenerate.IdGenerate;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Repository
public class IdGenerateDao {

	@Autowired
	private IdGenerateMapper idGenerateMapper;

	public List<IdGenerate> queryByIdName(String idName) {
		List<IdGenerate> idGenerateList = null;
		try {
			idGenerateList = idGenerateMapper.queryByCondition(idName);
		} catch (Exception e) {
			Log.run.error("根据idName查询信息发生异常,idName=" + idName, e);
		}
		return idGenerateList;
	}

	public int updateId(String idName, long currentId) {
		int returnValue = 0;
		try {
			returnValue = idGenerateMapper.updateId(idName, currentId);
		} catch (Exception e) {
			Log.run.error("根据idName更新ID信息发生异常,idName=" + idName, e);
		}
		return returnValue;
	}

	public long getMaxId(String idName) {
		synchronized (idName.intern()) {
			List<IdGenerate> ret = idGenerateMapper.queryByCondition(idName);
			if (ret.size() == 0) {
				Log.run.error("根据idName未查询到当前ID信息,idName=" + idName);
			}
			return ret.get(0).getCurrentId();
		}
	}

}

package com.cqfc.idgenerate.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqfc.idgenerate.dao.IdGenerateDao;
import com.cqfc.idgenerate.service.IIdGenerateService;
import com.cqfc.protocol.idgenerate.IdGenerate;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class IdGenerateServiceImpl implements IIdGenerateService {

	@Resource
	private IdGenerateDao idGenerateDao;

	static Map<String, Long> mapIdGen = new HashMap<String, Long>();
	static Map<String, Long> mapMaxId = new HashMap<String, Long>();

	@Override
	public long idGen(String idName) {
		synchronized (idName.intern()) {
			Long finalId = mapIdGen.get(idName);
			Long maxId = mapMaxId.get(idName);
			if (null == finalId || finalId >= maxId) {
				try {
					finalId = updateCurrent(idName);
				} catch (Exception e) {
					// 出现异常，重试10次
					for (int i = 0; i < 10; i++) {
						try {
							finalId = updateCurrent(idName);
							if (finalId >= maxId) {
								break;
							}
						} catch (Exception e2) {
							Log.run.error("根据idName获取finalID发生异常,idName=" + idName, e2);
						}
					}
				}
				try {
					maxId = idGenerateDao.getMaxId(idName);
				} catch (Exception e) {
					// 出现异常，重试10次
					for (int i = 0; i < 10; i++) {
						try {
							maxId = idGenerateDao.getMaxId(idName);
							break;
						} catch (Exception e2) {
							Log.run.error("根据idName获取MAXID发生异常,idName=" + idName, e2);
						}
					}
				}
				mapMaxId.put(idName, maxId);
			}
			mapIdGen.put(idName, finalId + 1);
			return finalId;
		}
	}

	@Transactional
	public long updateCurrent(String idName) {
		long currentId = 0;
		try {
			synchronized (idName.intern()) {
				List<IdGenerate> ret = idGenerateDao.queryByIdName(idName);
				if (ret.size() > 0) {
					long current = ret.get(0).getCurrentId();
					int updateRet = idGenerateDao.updateId(idName, current);
					if (updateRet > 0) {
						currentId = current;
					} else {
						throw new Exception("更新信息异常,rollback.");
					}
				}
			}
		} catch (Exception e) {
			Log.run.error("updateCurrent发生异常,idName=" + idName, e);
		}
		return currentId;
	}

}

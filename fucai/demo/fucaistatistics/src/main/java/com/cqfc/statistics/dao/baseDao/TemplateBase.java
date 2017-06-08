package com.cqfc.statistics.dao.baseDao;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cqfc.statistics.common.IConstantUtil;

public abstract class TemplateBase implements ItemplateBase,IConstantUtil{
	
	@Autowired
	@Qualifier("cqfcJdbcTemplate")
	protected JdbcTemplate cqfcDbTemplate;

	@Resource(name="cqfcfinanceJdbcTemplate")
	protected JdbcTemplate cqfcFinanceTemplate;

	@Resource(name="rdbJdbcTemplate")
	protected JdbcTemplate rdbTemplate;

}

package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;

public class Function implements Serializable {

	private static final long serialVersionUID = -279623203607496743L;

	private Integer functionId;

	private String url;
	
	private String level;
	
	private String sequence;

	private String remark;

	private Integer parentId;

	private Integer type;

	private String createTime;

	private String lastUpdateTime;

	
	private List<Function> children;
	
	
	public Function() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Function(Integer functionId, String url, String remark, Integer parentId,
			Integer type, String createTime, String lastUpdateTime) {
		super();
		this.functionId = functionId;
		this.url = url;
		this.remark = remark;
		this.parentId = parentId;
		this.type = type;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public List<Function> getChildren() {
		return children;
	}

	public void setChildren(List<Function> children) {
		this.children = children;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}


	
	
}

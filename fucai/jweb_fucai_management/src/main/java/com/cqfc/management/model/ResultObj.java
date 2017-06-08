package com.cqfc.management.model;

import java.io.Serializable;

public class ResultObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8321229384228372693L;

	private Object objects;

	private Integer recordTotal;

	public ResultObj() {
		super();
	}

	public ResultObj(Object objects, Integer recordTotal) {
		super();
		this.objects = objects;
		this.recordTotal = recordTotal;
	}

	public Object getObjects() {
		return objects;
	}

	public void setObjects(Object objects) {
		this.objects = objects;
	}

	public Integer getRecordTotal() {
		return recordTotal;
	}

	public void setRecordTotal(Integer recordTotal) {
		this.recordTotal = recordTotal;
	}

}

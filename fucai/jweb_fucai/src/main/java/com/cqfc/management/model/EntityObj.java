package com.cqfc.management.model;

import java.io.Serializable;

public class EntityObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8321229384228372693L;

	private Object objects;

	private int recordTotal;

	public EntityObj() {
		super();
	}

	public EntityObj(Object objects, int recordTotal) {
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

	public int getRecordTotal() {
		return recordTotal;
	}

	public void setRecordTotal(int recordTotal) {
		this.recordTotal = recordTotal;
	}

}

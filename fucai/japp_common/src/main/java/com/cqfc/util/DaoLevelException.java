package com.cqfc.util;

/**
 * @author liwh
 */
public class DaoLevelException extends RuntimeException {

	private static final long serialVersionUID = -4459418743639540923L;

	public DaoLevelException () {
		super();
	}
	
	public DaoLevelException (String arg0) {
		super(arg0);
	}
	
	public DaoLevelException (Throwable arg1) {
		super(arg1);
	}
	
	public DaoLevelException (String arg0,Throwable arg1) {
		super(arg0,arg1);
	}

}

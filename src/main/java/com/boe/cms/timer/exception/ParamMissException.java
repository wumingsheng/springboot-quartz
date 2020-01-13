package com.boe.cms.timer.exception;

import java.util.Formatter;

public class ParamMissException extends RuntimeException {


	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5967191799096924434L;

	public ParamMissException() {
	    super("param miss error");
	}

	@SuppressWarnings("resource")
	public ParamMissException(String format, Object... args) {
	    super(new Formatter().format(format, args).toString());
	}
}

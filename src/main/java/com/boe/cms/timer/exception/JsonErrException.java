package com.boe.cms.timer.exception;

import java.util.Formatter;

public class JsonErrException extends RuntimeException {

	


	private static final long serialVersionUID = 7062020292122794649L;

	public JsonErrException() {
	    super("Json parse error");
	}

	@SuppressWarnings("resource")
	public JsonErrException(String format, Object... args) {
	    super(new Formatter().format(format, args).toString());
	}
}

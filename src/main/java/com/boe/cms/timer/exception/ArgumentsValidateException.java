package com.boe.cms.timer.exception;

import java.util.Formatter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class ArgumentsValidateException extends ConstraintViolationException {
	
	

	private static final long serialVersionUID = 8743225553357180234L;

	public ArgumentsValidateException(Set<? extends ConstraintViolation<?>> constraintViolations) {
	    super("params validate error", constraintViolations);
	}

	@SuppressWarnings("resource")
	public ArgumentsValidateException(Set<? extends ConstraintViolation<?>> constraintViolations, String format, Object... args) {
	    super(new Formatter().format(format, args).toString(), constraintViolations);
	}


}

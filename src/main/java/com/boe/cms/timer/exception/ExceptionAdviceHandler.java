package com.boe.cms.timer.exception;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.boe.cms.timer.common.ResponseResult;
import com.boe.cms.timer.valid.BeanValidator;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionAdviceHandler {
	
	/** json 格式错误全局异常处理 */
	@ExceptionHandler(value = JsonErrException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Object jsonErrException(Exception e, HttpServletResponse response) {
		String message = e.getMessage();
		log.error(message);
		return f(ExceptionEnum.JSON_PARSE_ERR, e.getMessage());
	}

	/** 请求参数校验错误全局异常处理 */
	@ExceptionHandler(value = ArgumentsValidateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Object argumentsValidateException(ArgumentsValidateException e) {
		List<String> list = BeanValidator.extractPropertyAndMessageAsList(e);
		return f(ExceptionEnum.ARGS_VALIDATE_ERR, list);
	}

	/** 类型转换错误错误全局异常处理 */
	@ExceptionHandler(value = ClassCastException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Object classCastException(ClassCastException e) {
		log.error(ExceptionUtils.getStackTrace(e));
		return f(ExceptionEnum.ARGS_VALIDATE_ERR, e.getMessage());
	}
	
	/** 非法参数异常处理 */
	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Object IllegalArgumentException(IllegalArgumentException e) {
		log.error(ExceptionUtils.getStackTrace(e));
		return f(ExceptionEnum.ARGS_VALIDATE_ERR, e.getMessage());
	}

	/** 参数null全局异常处理 */
	@ExceptionHandler(value = ParamMissException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Object paramMissException(ParamMissException e) {
		log.error(e.getMessage(), e);
		return f(ExceptionEnum.ARGS_VALIDATE_ERR, e.getMessage());
	}
	/** JobExistException全局异常处理 */
	@ExceptionHandler(value = JobExistException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Object JobExistException(JobExistException e) {
		log.error(e.getMessage(), e);
		return f(ExceptionEnum.JOB_EXIST_ERR, e.getMessage());
	}
	
	/** CustomException全局异常处理 */
	@ExceptionHandler(value = CustomException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Object CustomException(CustomException e) {
		log.error(e.getMessage(), e);
		return f(ExceptionEnum.JOB_EXIST_ERR, e.getMessage());
	}
	/** ServerException全局异常处理 */
	@ExceptionHandler(value = ServerException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Object ServerException(ServerException e) {
		log.error(e.getMessage(), e);
		return f(ExceptionEnum.SERVER_ERR, e.getMessage());
	}

	private Object f(ExceptionEnum exceptionEnum, Object obj) {
		return new ResponseResult<>(exceptionEnum, obj);
		 
	}

}

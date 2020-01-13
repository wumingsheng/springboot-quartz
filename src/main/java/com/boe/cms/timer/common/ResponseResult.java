package com.boe.cms.timer.common;

import com.boe.cms.timer.exception.ExceptionEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {

	private Integer code;

	private String message;

	private T data;

	public ResponseResult(ExceptionEnum exceptionEnum, T data) {
		this.code = exceptionEnum.getCode();
		this.message = exceptionEnum.getMessage();
		this.data = data;
	}

	public ResponseResult(ResponseEnum responseEnum, T data) {
		this.code = responseEnum.getCode();
		this.message = responseEnum.getMessage();
		this.data = data;
	}

	

	@Override
	public String toString() {
		return "Result [retMsg=" + message + ", data=" + data + ", retCode=" + code + "]";
	}
	
	public static <T> ResponseResult<T> success(Integer code, String msg, T data) {
		ResponseResult<T> responseResult = new ResponseResult<>(code, msg, data);
		return responseResult;
	}

	public static <T> ResponseResult<T> success(ResponseEnum responseE, T data) {
		ResponseResult<T> responseResult = new ResponseResult<>(responseE.getCode(), responseE.getMessage(), data);
		return responseResult;
	}

}

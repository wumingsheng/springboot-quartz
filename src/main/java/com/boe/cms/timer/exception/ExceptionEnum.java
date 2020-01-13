package com.boe.cms.timer.exception;

public enum ExceptionEnum {
	
	
	JSON_PARSE_ERR(200001, "JSON解析错误"),
    
    ARGS_VALIDATE_ERR(200002, "请求参数错误"),
	
	
	JOB_EXIST_ERR(200003, "Job已近存在"),
	
	CUSTOM_ERR(20004, "ERR"),
	
	SERVER_ERR(20005, "服务器内部运行错误");
	
	private Integer code;

    private String message;

    ExceptionEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}

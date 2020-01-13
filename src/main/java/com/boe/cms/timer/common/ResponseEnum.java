package com.boe.cms.timer.common;

public enum ResponseEnum {
	
	

	SUCCESS(100000, "请求成功");
    
    
	
	private Integer code;

    private String message;

    ResponseEnum(Integer code, String message){
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

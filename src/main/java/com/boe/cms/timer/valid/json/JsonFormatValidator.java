package com.boe.cms.timer.valid.json;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JsonFormatValidator implements ConstraintValidator<Json,String>  {

	@Override
	public void initialize(Json constraintAnnotation) {

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		//如果是空字符串，直接返回true
		if (StringUtils.isBlank(value)) {
			return true;
		}

		return validateJson(value);

	}
	private boolean validateJson(String jsonStr) {
		JsonElement jsonElement;
		try {
			jsonElement = JsonParser.parseString(jsonStr);
		} catch (Exception e) {
			return false;
		}
		if (jsonElement == null) {
			return false;
		}
		if (!jsonElement.isJsonObject()) {
			return false;
		}
		return true;
	}
	

}

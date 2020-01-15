package com.boe.cms.timer;

import javax.validation.Validator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
@ConfigurationPropertiesScan
@MapperScan(basePackages = "com.boe.cms.timer.dao", annotationClass = Repository.class)
public class CmsTimerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsTimerApplication.class, args);
	}
	
	@Bean
    public Validator getValidator() {
        return new LocalValidatorFactoryBean();
    }

}

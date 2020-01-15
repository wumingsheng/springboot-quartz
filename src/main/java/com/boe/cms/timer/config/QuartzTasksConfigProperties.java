package com.boe.cms.timer.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;



import lombok.Data;

@Configuration
@PropertySource(value = "classpath:quartz-task.properties", ignoreResourceNotFound = true, encoding = "UTF-8")
@ConfigurationProperties(prefix = "quartz")
@Data
public class QuartzTasksConfigProperties {
	
	private List<TimeTask> tasks = new ArrayList<>();

}

package com.boe.cms.timer.timer;

import java.util.Date;

import com.boe.cms.timer.enums.JobStatusEnum;
import com.boe.cms.timer.enums.TriggerTypeEnum;

import lombok.Data;


@Data
public class ScheduleJob {
	
	private String jobName;
	
	private String jobGroup;

	private String jobDescription;

	private TriggerTypeEnum triggerType;

	private String triggerCron;

	private Integer triggerIntervalMinutes;

	private String triggerName;

	private String triggerGroup;

	private String triggerDescription;

	private Date startTime;

	private String className;
	
	private JobStatusEnum jobStatus;
	
	private Integer totalCount;
}

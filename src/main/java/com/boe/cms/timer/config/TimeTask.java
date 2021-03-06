package com.boe.cms.timer.config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.boe.cms.timer.enums.JobStatusEnum;
import com.boe.cms.timer.enums.TriggerTypeEnum;
import com.boe.cms.timer.valid.AddValidGroup;
import com.boe.cms.timer.valid.UpdateValidGroup;
import com.boe.cms.timer.valid.json.Json;

import lombok.Data;

@Data
public class TimeTask {
	

	@NotBlank(message = "jobName field err", groups = { AddValidGroup.class, UpdateValidGroup.class })
	private String jobName;
	
	@NotBlank(message = "jobGroup field err", groups = { AddValidGroup.class, UpdateValidGroup.class })
	private String jobGroup;

	@NotBlank(message = "jobDescription field err", groups = { AddValidGroup.class })
	private String jobDescription;

	@NotNull(message = "triggerType field err", groups = { AddValidGroup.class })
	private TriggerTypeEnum triggerType;

	private String triggerCron;

	private Integer triggerIntervalMinutes;

	@NotBlank(message = "triggerName field err", groups = { AddValidGroup.class, UpdateValidGroup.class })
	private String triggerName;

	@NotBlank(message = "triggerGroup field err", groups = { AddValidGroup.class, UpdateValidGroup.class })
	private String triggerGroup;

	@NotBlank(message = "triggerDescription field err", groups = { AddValidGroup.class, UpdateValidGroup.class })
	private String triggerDescription;

	@NotBlank(message = "startTime field err", groups = { AddValidGroup.class, UpdateValidGroup.class })
	private String startTime;

	private String endTime;

	@NotBlank(message = "className field err", groups = { AddValidGroup.class })
	private String className;
	
	@NotNull(message = "jobStatus field err", groups = {AddValidGroup.class, UpdateValidGroup.class})
	private JobStatusEnum jobStatus;
	
	private Integer totalCount;

	@Json(message = "extraInfo field err", groups = {AddValidGroup.class, UpdateValidGroup.class})
	@NotBlank(message = "extraInfo field err", groups = {AddValidGroup.class, UpdateValidGroup.class})
	private String extraInfo;

}

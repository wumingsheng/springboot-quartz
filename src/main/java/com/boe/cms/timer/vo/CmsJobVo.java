package com.boe.cms.timer.vo;

import com.boe.cms.timer.enums.JobStatusEnum;
import com.boe.cms.timer.enums.StatusEnum;
import com.boe.cms.timer.enums.TriggerTypeEnum;
import com.boe.cms.timer.valid.AddValidGroup;
import com.boe.cms.timer.valid.UpdateValidGroup;
import com.boe.cms.timer.valid.json.Json;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CmsJobVo {
	
	private Long id;
	
	private String createrId;
	
	private String updaterId;

	private StatusEnum status;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	

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

	@NotNull(message = "startTime field err", groups = { AddValidGroup.class, UpdateValidGroup.class })
	@Future(message = "startTime need future", groups = { AddValidGroup.class, UpdateValidGroup.class })
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date startTime;

	@Future(message = "endTime need future", groups = { AddValidGroup.class, UpdateValidGroup.class })
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date endTime;

	@NotBlank(message = "className field err", groups = { AddValidGroup.class })
	private String className;
	
	@NotNull(message = "jobStatus field err", groups = {AddValidGroup.class, UpdateValidGroup.class})
	private JobStatusEnum jobStatus;
	
	private Integer totalCount;

	@Json(message = "extraInfo field err", groups = {AddValidGroup.class, UpdateValidGroup.class})
	@NotBlank(message = "extraInfo field err", groups = {AddValidGroup.class, UpdateValidGroup.class})
	private String extraInfo;

}

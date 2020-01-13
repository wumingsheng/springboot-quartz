package com.boe.cms.timer.po;


import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boe.cms.timer.enums.JobStatusEnum;
import com.boe.cms.timer.enums.StatusEnum;
import com.boe.cms.timer.enums.TriggerTypeEnum;

import lombok.Data;
/**
 * 
 * @author wumingsheng
 *
 */
@Data
@TableName("CMS_JOB")
public class CmsJobPo {


	@TableId("id")
	private Long id;
	
	private String createrId;
	
	private String updaterId;

	private StatusEnum status;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	/** ================================== */

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

	private String extraInfo;

}


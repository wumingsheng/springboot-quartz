package com.boe.cms.timer.controller;

import com.boe.cms.timer.enums.TriggerTypeEnum;
import com.boe.cms.timer.po.CmsJobPo;
import com.boe.cms.timer.service.JobService;
import com.boe.cms.timer.valid.AddValidGroup;
import com.boe.cms.timer.valid.UpdateValidGroup;
import com.boe.cms.timer.vo.CmsJobVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("job")
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Validated
public class JobController {
	
	
	@Autowired
	private JobService jobService;
	
	@PostMapping("add")
	public Object addJob(@RequestBody @Validated(AddValidGroup.class) CmsJobVo cmsJobVo) throws Exception {
		
		log.info(">>>>>> add job begin +++++++++++++++");
		log.info(">>>>>> request body is : {}", cmsJobVo);
		
		TriggerTypeEnum triggerType = cmsJobVo.getTriggerType();
		
		if(TriggerTypeEnum.CRON == triggerType) {//cron类型触发
			
			String triggerCron = cmsJobVo.getTriggerCron();
			
			if(StringUtils.isBlank(triggerCron) || !CronExpression.isValidExpression(triggerCron)) {
				throw new IllegalArgumentException("triggerCron field err");
			}
			
		}
		
		if(TriggerTypeEnum.SIMPLE == triggerType) {//simple类型
			
			Integer totalCount = cmsJobVo.getTotalCount();
			Integer triggerIntervalMinutes = cmsJobVo.getTriggerIntervalMinutes();
			if(null == totalCount || totalCount < 0) {
				throw new IllegalArgumentException("totalCount field err");
			}
			
			if(null == triggerIntervalMinutes || triggerIntervalMinutes <= 0) {
				throw new IllegalArgumentException("triggerIntervalMinutes field err");
			}
		}
		
		
		CmsJobPo cmsJobPo = new CmsJobPo();
		BeanUtils.copyProperties(cmsJobPo, cmsJobVo);
		
		
		return jobService.addJob(cmsJobPo);


	}

	/**
	 * 通过jobkey： jobname&jobgroup 更新 job
	 */
	@PostMapping("update")
	public Object updateJob(@RequestBody @Validated(UpdateValidGroup.class) CmsJobVo cmsJobVo) throws  Exception {

		log.info(">>>>>> update job begin +++++++++++++++");
		log.info(">>>>>> request body is : {}", cmsJobVo);

		TriggerTypeEnum triggerType = cmsJobVo.getTriggerType();

		if(TriggerTypeEnum.CRON == triggerType) {//cron类型触发

			String triggerCron = cmsJobVo.getTriggerCron();

			if(StringUtils.isBlank(triggerCron) || !CronExpression.isValidExpression(triggerCron)) {
				throw new IllegalArgumentException("triggerCron field err");
			}

		}

		if(TriggerTypeEnum.SIMPLE == triggerType) {//simple类型

			Integer totalCount = cmsJobVo.getTotalCount();
			Integer triggerIntervalMinutes = cmsJobVo.getTriggerIntervalMinutes();
			if(null == totalCount || totalCount < 0) {
				throw new IllegalArgumentException("totalCount field err");
			}

			if(null == triggerIntervalMinutes || triggerIntervalMinutes <= 0) {
				throw new IllegalArgumentException("triggerIntervalMinutes field err");
			}
		}

		CmsJobPo cmsJobPo = new CmsJobPo();
		BeanUtils.copyProperties(cmsJobPo, cmsJobVo);


		return jobService.updateJob(cmsJobPo);
	}

	@DeleteMapping("/delete/{jobGroup}/{jobName}")
	public Object deleteJob(@PathVariable("jobGroup") String jobGroup,@PathVariable("jobName") String jobName) throws  Exception {

		log.info(">>>>>>> begin delete job, [jobGroup:{}],[jobName:{}]", jobGroup, jobName);
		CmsJobPo cmsJobPo = new CmsJobPo();
		cmsJobPo.setJobGroup(jobGroup);
		cmsJobPo.setJobName(jobName);
		return jobService.deleteJob(cmsJobPo);
	}

	@PutMapping("/pause/{jobGroup}/{jobName}")
	public Object pauseJob(@PathVariable("jobGroup") String jobGroup, @PathVariable("jobName") String jobName) throws  Exception {
		log.info(">>>>>>>>>>>> begin pause job, [jobGroup:{}], [jobName:{}]", jobGroup, jobName);
		CmsJobPo cmsJobPo = new CmsJobPo();
		cmsJobPo.setJobGroup(jobGroup);
		cmsJobPo.setJobName(jobName);
		return jobService.pauseJob(cmsJobPo);
	}

	@PutMapping("/resume/{jobGroup}/{jobName}")
	public Object resumeJob(@PathVariable("jobGroup") String jobGroup, @PathVariable("jobName") String jobName) throws  Exception {
		log.info(">>>>>>>>>>>> begin resume job, [jobGroup:{}], [jobName:{}]", jobGroup, jobName);
		CmsJobPo cmsJobPo = new CmsJobPo();
		cmsJobPo.setJobGroup(jobGroup);
		cmsJobPo.setJobName(jobName);
		return jobService.resumeJob(cmsJobPo);
	}

	@PostMapping("/trigger/{jobGroup}/{jobName}")
	public Object triggerJob(@PathVariable("jobGroup") String jobGroup, @PathVariable("jobName") String jobName,
							 @RequestParam(value = "extraInfo", required = false) String extraInfo) throws  Exception {
		log.info(">>>>>>>>>>>> begin trigger job, [jobGroup:{}], [jobName:{}], [dataMap:{}]", jobGroup, jobName, extraInfo);
		CmsJobPo cmsJobPo = new CmsJobPo();
		cmsJobPo.setJobGroup(jobGroup);
		cmsJobPo.setJobName(jobName);
		cmsJobPo.setExtraInfo(extraInfo);
		return jobService.triggerJob(cmsJobPo);
	}

	@GetMapping("/get/{jobGroup}/{jobName}")
	public Object getOneJob(@PathVariable("jobGroup") String jobGroup, @PathVariable("jobName") String jobName) throws  Exception {
		log.info(">>>>>>>>>>>> begin get one job, [jobGroup:{}], [jobName:{}]", jobGroup, jobName);
		CmsJobPo cmsJobPo = new CmsJobPo();
		cmsJobPo.setJobGroup(jobGroup);
		cmsJobPo.setJobName(jobName);
		return jobService.getJob(cmsJobPo);
	}

	@GetMapping("/list/all")
	public Object listAllJob() throws  Exception {
		log.info(">>>>>>>>>>>> begin list all job");

		return jobService.listAll();
	}
	
	@GetMapping("/reset/{jobGroup}/{jobName}")
	public Object resetTriggerFromErrorState(@PathVariable("jobGroup") String jobGroup, @PathVariable("jobName") String jobName) throws  Exception {
		log.info(">>>>>>>>>>>> begin reset trigger, [jobGroup:{}], [jobName:{}]", jobGroup, jobName);
		CmsJobPo cmsJobPo = new CmsJobPo();
		cmsJobPo.setJobGroup(jobGroup);
		cmsJobPo.setJobName(jobName);
		return jobService.resetTriggerFromErrorState(cmsJobPo);
	}


	


}

package com.boe.cms.timer.controller;

import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boe.cms.timer.enums.TriggerTypeEnum;
import com.boe.cms.timer.po.CmsJobPo;
import com.boe.cms.timer.service.JobService;
import com.boe.cms.timer.valid.AddValidGroup;
import com.boe.cms.timer.vo.CmsJobVo;

import lombok.extern.slf4j.Slf4j;

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

}

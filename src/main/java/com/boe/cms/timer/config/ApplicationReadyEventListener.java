package com.boe.cms.timer.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import com.boe.cms.timer.common.Constant;
import com.boe.cms.timer.enums.StatusEnum;
import com.boe.cms.timer.po.CmsJobPo;
import com.boe.cms.timer.service.JobService;
import com.boe.cms.timer.tools.DateUtil;
import com.boe.cms.timer.valid.AddValidGroup;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
//@EnableConfigurationProperties(QuartzTasksConfigProperties.class)
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

	private static Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventListener.class);
	
	@Autowired
	private QuartzTasksConfigProperties tasksProperties;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private JobService jobService;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event)  {
		
		LOGGER.info("......ApplicationReadyEvent......");
		List<TimeTask> tasks = tasksProperties.getTasks();
		
		log.info(">>>>>>>>>>>> -- {}", tasks.size());
		for (TimeTask timeTask : tasks) {
			//数据校验
			Set<ConstraintViolation<TimeTask>> set = validator.validate(timeTask, AddValidGroup.class);
			if (set != null && set.size() > 0) {
				log.error("配置有错误，校验不通过,[{}],[{}]", timeTask, set);
				continue;
			}
			
			//jobkey and triggerkey 都不存在  才回去添加
			
			String jobGroup = timeTask.getJobGroup();
			String jobName = timeTask.getJobName();
			String triggerGroup = timeTask.getTriggerGroup();
			String triggerName = timeTask.getTriggerName();
			
			JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
			boolean jobKeyExists;
			try {
				jobKeyExists = scheduler.checkExists(jobKey);
			} catch (SchedulerException e2) {
				log.error("checkExist jobKey err");
				continue;
			}
			boolean triggerKeyExists;
			try {
				triggerKeyExists = scheduler.checkExists(triggerKey);
			} catch (SchedulerException e1) {
				log.error("checkExist triggerKey err");
				continue;
			}
			
			
			if(!jobKeyExists && !triggerKeyExists) {//作为新task 添加
				log.info(">>>>>>>有新任务添加");
				CmsJobPo cmsJobPo = convertTo(timeTask);
			    
			    
				try {
					jobService.addJob(cmsJobPo);
				} catch (Exception e) {
					log.error("add job err");
					continue;
				}
			}
			
			
			
		}
		
	}

	private CmsJobPo convertTo(TimeTask timeTask) {
		CmsJobPo cmsJobPo = new CmsJobPo();
		cmsJobPo.setClassName(timeTask.getClassName());
		cmsJobPo.setCreaterId(Constant.ADMIN_ID);
		cmsJobPo.setUpdaterId(Constant.ADMIN_ID);
		LocalDateTime now = DateUtil.now();
		cmsJobPo.setCreateTime(now);
		cmsJobPo.setUpdateTime(now);
		String endTime = timeTask.getEndTime();
		
		cmsJobPo.setEndTime(DateUtil.str2Date(endTime, null));
		cmsJobPo.setExtraInfo(timeTask.getExtraInfo());
		cmsJobPo.setJobDescription(timeTask.getJobDescription());
		cmsJobPo.setJobGroup(timeTask.getJobGroup());
		cmsJobPo.setJobName(timeTask.getJobName());
		cmsJobPo.setJobStatus(timeTask.getJobStatus());
		cmsJobPo.setStartTime(DateUtil.str2Date(timeTask.getStartTime(), null));
		cmsJobPo.setStatus(StatusEnum.ENABLED);
		cmsJobPo.setTotalCount(timeTask.getTotalCount());
		cmsJobPo.setTriggerCron(timeTask.getTriggerCron());
		cmsJobPo.setTriggerDescription(timeTask.getTriggerDescription());
		cmsJobPo.setTriggerGroup(timeTask.getTriggerGroup());
		cmsJobPo.setTriggerName(timeTask.getTriggerName());
		cmsJobPo.setTriggerIntervalMinutes(timeTask.getTriggerIntervalMinutes());
		cmsJobPo.setTriggerType(timeTask.getTriggerType());
		return cmsJobPo;
	}

}
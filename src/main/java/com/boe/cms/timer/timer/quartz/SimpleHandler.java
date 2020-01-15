package com.boe.cms.timer.timer.quartz;

import org.quartz.*;
import org.springframework.stereotype.Component;

import com.boe.cms.timer.enums.TriggerTypeEnum;
import com.boe.cms.timer.exception.ClientException;
import com.boe.cms.timer.exception.ServerException;
import com.boe.cms.timer.timer.ScheduleJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@HandlerType(TriggerTypeEnum.SIMPLE)
public class SimpleHandler extends AbstractHandler {

	@Override
	public void addJob(Scheduler scheduler, ScheduleJob scheduleJob, JobDataMap jobDataMap, JobDataMap triggerDataMap) throws Exception {

		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		boolean jobExists = scheduler.checkExists(jobKey);
		if (jobExists) {
			log.error(">>>>>>> jobKey already exists");
			throw new ClientException("jobKey already exists");
		}
		
		
		
		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());
		boolean triggerExists = scheduler.checkExists(triggerKey);
		if (triggerExists) {
			log.error(">>>>>>> triggerKey already exists");
			throw new ClientException("triggerKey already exists");
		}
		
		
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(scheduleJob.getClassName());

			// 创建任务
			JobDetail jobDetail = JobBuilder.newJob(jobClass)
					.withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
					.withDescription(scheduleJob.getJobDescription())
					.storeDurably(true)
					.usingJobData(jobDataMap)
					.build();

			Integer totalCount = scheduleJob.getTotalCount();
			SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(scheduleJob.getTriggerIntervalMinutes());
			if(totalCount >= 1) {//有指定次数限制
				scheduleBuilder.withRepeatCount(totalCount - 1);
			} else {//没有次数限制
				scheduleBuilder.repeatForever();
			}
			
			
			
			// 创建任务触发器
			Trigger simpleTrigger = TriggerBuilder.newTrigger()
					.withIdentity(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup())
					.forJob(jobDetail)
					.startAt(scheduleJob.getStartTime())
					.endAt(scheduleJob.getEndTime())
					.withDescription(scheduleJob.getTriggerDescription())
					.withSchedule(scheduleBuilder)
					.usingJobData(triggerDataMap)
					// .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(scheduleJob.getTriggerIntervalMinutes()))
					//.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(scheduleJob.getTriggerIntervalHours()))
					.build();

			scheduler.scheduleJob(jobDetail, simpleTrigger);
			log.info("Create schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());

		} catch (Exception e) {
			log.error(e.getMessage());
			log.error(">>>>>>> createSimpleScheduleJob failed : {}", "SimpleHandler.handle");
			throw new ServerException("createSimpleScheduleJob failed");
		}

	}

}

package com.boe.cms.timer.timer.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import com.boe.cms.timer.enums.TriggerTypeEnum;
import com.boe.cms.timer.exception.ServerException;
import com.boe.cms.timer.timer.ScheduleJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@HandlerType(TriggerTypeEnum.CRON)
public class CronHandler extends AbstractHandler {

	@Override
	public void addJob(Scheduler scheduler, ScheduleJob scheduleJob) throws Exception {
		try {
			// 要执行的 Job 的类
			@SuppressWarnings("unchecked")
			Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(scheduleJob.getClassName());

			JobDetail jobDetail = JobBuilder.newJob(jobClass)
					.withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
					.withDescription(scheduleJob.getJobDescription())
					.storeDurably(true)
					.build();

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getTriggerCron())
					.withMisfireHandlingInstructionDoNothing();

			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup())
					.forJob(jobDetail)
					.withDescription(scheduleJob.getTriggerDescription()).withSchedule(scheduleBuilder)
					.startAt(scheduleJob.getStartTime())
					.build();

			scheduler.scheduleJob(jobDetail, cronTrigger);

			log.info("Create schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());

			
		} catch (Exception e) {
			log.error("Execute schedule job failed", e);
			throw new ServerException("Execute schedule job failed");
		}
		
	}

	@Override
	public void updateJob(Scheduler scheduler, ScheduleJob scheduleJob) throws Exception {
		try {

			TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());

			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
					.cronSchedule(scheduleJob.getTriggerCron())
					.withMisfireHandlingInstructionDoNothing();

			CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			cronTrigger = cronTrigger.getTriggerBuilder()
					.withIdentity(triggerKey)
					.withDescription(scheduleJob.getTriggerDescription())
					.startAt(scheduleJob.getStartTime())
					.withSchedule(cronScheduleBuilder)
					.build();

			scheduler.rescheduleJob(triggerKey, cronTrigger);

			log.info("Update schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());

		} catch (Exception e) {
			log.error("Update schedule job failed", e);
			throw new ServerException("Update schedule job failed");
		}
		
	}

	
}

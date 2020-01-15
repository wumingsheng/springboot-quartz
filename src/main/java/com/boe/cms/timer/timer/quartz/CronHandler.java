package com.boe.cms.timer.timer.quartz;

import com.boe.cms.timer.enums.TriggerTypeEnum;
import com.boe.cms.timer.exception.ServerException;
import com.boe.cms.timer.timer.ScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@HandlerType(TriggerTypeEnum.CRON)
public class CronHandler extends AbstractHandler {

	@Override
	public void addJob(Scheduler scheduler, ScheduleJob scheduleJob, JobDataMap jobDataMap, JobDataMap triggerDataMap) throws Exception {
		try {
			// 要执行的 Job 的类
			@SuppressWarnings("unchecked")
			Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(scheduleJob.getClassName());

			JobDetail jobDetail = JobBuilder.newJob(jobClass)
					.withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
					.withDescription(scheduleJob.getJobDescription())
					.storeDurably(true)
					.usingJobData(jobDataMap)
					.build();

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getTriggerCron())
					.withMisfireHandlingInstructionDoNothing();

			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup())
					.forJob(jobDetail)
					.withDescription(scheduleJob.getTriggerDescription()).withSchedule(scheduleBuilder)
					.startAt(scheduleJob.getStartTime())
					.endAt(scheduleJob.getEndTime())
					.usingJobData(triggerDataMap)
					.build();

			scheduler.scheduleJob(jobDetail, cronTrigger);

			log.info("Create schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());

			
		} catch (Exception e) {
			log.error("Execute schedule job failed", e);
			throw new ServerException("Execute schedule job failed");
		}
		
	}

	

	
}

package com.boe.cms.timer.timer.quartz;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import com.boe.cms.timer.enums.TriggerTypeEnum;
import com.boe.cms.timer.exception.ServerException;
import com.boe.cms.timer.timer.ScheduleJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@HandlerType(TriggerTypeEnum.SIMPLE)
public class SimpleHandler extends AbstractHandler {

	@Override
	public void addJob(Scheduler scheduler, ScheduleJob scheduleJob) throws Exception {

		try {
			@SuppressWarnings("unchecked")
			Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(scheduleJob.getClassName());

			// 创建任务
			JobDetail jobDetail = JobBuilder.newJob(jobClass)
					.withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
					.withDescription(scheduleJob.getJobDescription())
					.storeDurably(true)
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
					.withDescription(scheduleJob.getTriggerDescription())
					.withSchedule(scheduleBuilder)
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

	@Override
	public void updateJob(Scheduler scheduler, ScheduleJob scheduleJob) throws Exception {
		try {

			TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());

			SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
					.repeatMinutelyForever(scheduleJob.getTriggerIntervalMinutes())
					.withMisfireHandlingInstructionNextWithExistingCount();

			SimpleTrigger simpleTrigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);

			simpleTrigger = simpleTrigger.getTriggerBuilder()
					.withIdentity(triggerKey)
					.startAt(scheduleJob.getStartTime())
					.withDescription(scheduleJob.getTriggerDescription())
					.withSchedule(simpleScheduleBuilder)
					.build();

			scheduler.rescheduleJob(triggerKey, simpleTrigger);

			log.info("Update schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
			
//			if (scheduleJob.getPause()) {
//				pauseJob(scheduler, scheduleJob);
//			}
		} catch (SchedulerException e) {
			log.error("Update schedule job failed", e);
			throw new ServerException("Update schedule job failed");
		}
	}

}

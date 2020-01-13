package com.boe.cms.timer.timer.quartz;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boe.cms.timer.exception.ServerException;
import com.boe.cms.timer.timer.IJobHandler;
import com.boe.cms.timer.timer.ScheduleJob;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class QuartzJobHandler implements IJobHandler {
	
	@Autowired
	private Scheduler scheduler;
	
	@Autowired
	private QuartzHandlerContext handlerContext;

	@Override
	public void addJob(ScheduleJob scheduleJob) throws Exception {
		log.info(">>>>>>>scheduleJob : {}", scheduleJob);
		AbstractHandler abstractHandler = handlerContext.getInstance(scheduleJob);
		abstractHandler.addJob(scheduler, scheduleJob);
		
	}

	@Override
	public void updateJob(ScheduleJob scheduleJob) throws Exception {
		log.info(">>>>>>>scheduleJob : {}", scheduleJob);
		
	}

	@Override
	public void pauseJob(ScheduleJob scheduleJob) throws Exception {
		log.info(">>>>>>>scheduleJob : {}", scheduleJob);
		
		try {
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.pauseJob(jobKey);
			log.info("Pause schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
		} catch (SchedulerException e) {
			log.error("Pause schedule job failed", e);
			throw new ServerException("Pause job failed");
		}
		
	}

	@Override
	public void resumeJob(ScheduleJob scheduleJob) throws Exception {
		log.info(">>>>>>>scheduleJob : {}", scheduleJob);
		
		try {
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.resumeJob(jobKey);
			log.info("Resume schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
		} catch (SchedulerException e) {
			log.error("Resume schedule job failed", e);
			throw new ServerException("Resume job failed");
		}
		
	}

	@Override
	public void triggerJob(ScheduleJob scheduleJob) throws Exception {
		log.info(">>>>>>>scheduleJob : {}", scheduleJob);
		
		try {
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.triggerJob(jobKey);
			log.info("Run schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
		} catch (SchedulerException e) {
			log.error("Run schedule job failed", e);
			throw new ServerException("Run schedule job failed");
		}
		
	}

	@Override
	public void deleteJob(ScheduleJob scheduleJob) throws Exception {
		
		log.info(">>>>>>>scheduleJob : {}", scheduleJob);
		
		try {
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			scheduler.deleteJob(jobKey);
			log.info("Delete schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
		} catch (SchedulerException e) {
			log.error("Delete schedule job failed", e);
			throw new ServerException("Delete job failed");
		}
		
	}
	


}

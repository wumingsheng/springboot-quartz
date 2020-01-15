package com.boe.cms.timer.timer;

import org.quartz.JobDataMap;

public interface IJobHandler {
	
	
	public void addJob(ScheduleJob scheduleJob, JobDataMap jobDataMap, JobDataMap triggerDataMap) throws Exception;
	
	
	
	public void pauseJob(ScheduleJob scheduleJob) throws Exception;
	
	public void resumeJob(ScheduleJob scheduleJob) throws Exception;
	
	public void triggerJob(ScheduleJob scheduleJob, JobDataMap jobDataMap) throws Exception;
	
	public void deleteJob(ScheduleJob scheduleJob) throws Exception;
	
	public void resetTriggerFromErrorState(ScheduleJob scheduleJob) throws Exception;


}

package com.boe.cms.timer.timer;

public interface IJobHandler {
	
	
	public void addJob(ScheduleJob scheduleJob) throws Exception;
	
	public void updateJob(ScheduleJob scheduleJob) throws Exception;
	
	public void pauseJob(ScheduleJob scheduleJob) throws Exception;
	
	public void resumeJob(ScheduleJob scheduleJob) throws Exception;
	
	public void triggerJob(ScheduleJob scheduleJob) throws Exception;
	
	public void deleteJob(ScheduleJob scheduleJob) throws Exception;


}

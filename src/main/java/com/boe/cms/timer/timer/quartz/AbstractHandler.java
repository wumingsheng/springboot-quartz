package com.boe.cms.timer.timer.quartz;

import org.quartz.Scheduler;

import com.boe.cms.timer.timer.ScheduleJob;

public abstract class AbstractHandler {
	


    abstract public void addJob(Scheduler scheduler, ScheduleJob scheduleJob) throws Exception;
    
   
    
    abstract public void updateJob(Scheduler scheduler, ScheduleJob scheduleJob) throws Exception;
    
    

	

}















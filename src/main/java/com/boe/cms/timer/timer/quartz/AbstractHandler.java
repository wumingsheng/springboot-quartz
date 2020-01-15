package com.boe.cms.timer.timer.quartz;

import com.boe.cms.timer.timer.ScheduleJob;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;

public abstract class AbstractHandler {
	


    abstract public void addJob(Scheduler scheduler, ScheduleJob scheduleJob, JobDataMap jobDataMap, JobDataMap triggerDataMap) throws Exception;
    
   
 

	

}















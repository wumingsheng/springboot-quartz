package com.boe.cms.timer.timer.quartz;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boe.cms.timer.enums.TriggerTypeEnum;
import com.boe.cms.timer.timer.ScheduleJob;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QuartzHandlerContext {

   
	@Autowired
    private List<AbstractHandler> handlers;

   

    public AbstractHandler getInstance(ScheduleJob scheduleJob) {
    	
    	TriggerTypeEnum triggerTypeEnum = scheduleJob.getTriggerType();
    	
    	for (AbstractHandler abstractHandler : handlers) {
    		
			HandlerType handlerType = abstractHandler.getClass().getAnnotation(HandlerType.class);
			TriggerTypeEnum triggerType = handlerType.value();
			if(triggerTypeEnum == triggerType) {
				return abstractHandler;
			}
		}
    	log.error(">>>>>>>> not find match handler");
    	return null;
        
    }
    


}

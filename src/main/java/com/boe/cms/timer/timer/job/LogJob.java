package com.boe.cms.timer.timer.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.boe.cms.timer.dao.CmsJobMapper;
import com.boe.cms.timer.po.CmsJobPo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Optional;




@Component
@Slf4j
@DisallowConcurrentExecution
public class LogJob extends QuartzJobBean {
	
	@Autowired
	private CmsJobMapper cmsJobMapper;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		
		JobDetail jobDetail = context.getJobDetail();
		JobKey key = jobDetail.getKey();
		String jobName = key.getName();
		String jobGroup = key.getGroup();
//		JobDataMap jobDataMap = jobDetail.getJobDataMap();
//		String extraInfo = jobDataMap.getString("extraInfo");
		//获取JobDetail通过JobDataMap传递的参数信息(获取两者合并后的JobDataMap)
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String extraInfo = jobDataMap.getString("extraInfo");
		log.info(">>>>>>>>>> jobDetail is [jobGroup={}],[jobName={}],[extraInfo={}]", jobGroup, jobName, extraInfo);
		//通过jobName和jobGroup获取taskId
		LambdaQueryWrapper<CmsJobPo> lambdaQuery = Wrappers.lambdaQuery();
		lambdaQuery.eq(CmsJobPo::getJobGroup, jobGroup).eq(CmsJobPo::getJobName, jobName);
		CmsJobPo cmsJobPo = cmsJobMapper.selectOne(lambdaQuery);
		log.info(">>>>>>>cmsJobPo is :{}", cmsJobPo);
		Optional.ofNullable(cmsJobPo).ifPresent(job -> {
			log.info(">>>>>>>>> extra info params is : {}", job.getExtraInfo());
		});
		
		
	
		
	}

}

package com.boe.cms.timer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.boe.cms.timer.common.Constant;
import com.boe.cms.timer.common.ResponseEnum;
import com.boe.cms.timer.common.ResponseResult;
import com.boe.cms.timer.dao.CmsJobMapper;
import com.boe.cms.timer.enums.JobStatusEnum;
import com.boe.cms.timer.enums.StatusEnum;
import com.boe.cms.timer.exception.ClientException;
import com.boe.cms.timer.exception.JobExistException;
import com.boe.cms.timer.po.CmsJobPo;
import com.boe.cms.timer.service.JobService;
import com.boe.cms.timer.timer.IJobHandler;
import com.boe.cms.timer.timer.ScheduleJob;
import com.boe.cms.timer.tools.DateUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class JobServiceImpl implements JobService {
	
	@Autowired
	private CmsJobMapper cmsJobMapper;
	
	@Autowired
	private IJobHandler jobHandler;

	@Override
	@Transactional
	public ResponseResult<String> addJob(CmsJobPo cmsJobPo) throws Exception {
		
		//判断数据是否重复（存在）
		LambdaQueryWrapper<CmsJobPo> jobQuery = Wrappers.lambdaQuery();
		jobQuery.eq(CmsJobPo::getJobGroup, cmsJobPo.getJobGroup()).eq(CmsJobPo::getJobName, cmsJobPo.getJobName());
		Integer jobCount = cmsJobMapper.selectCount(jobQuery);
		if(jobCount > 0) {
			throw new JobExistException("job already exist");
		}
		LambdaQueryWrapper<CmsJobPo> triggerQuery = Wrappers.lambdaQuery();
		triggerQuery.eq(CmsJobPo::getTriggerGroup, cmsJobPo.getTriggerGroup()).eq(CmsJobPo::getTriggerName, cmsJobPo.getTriggerName());
		Integer triggerCount = cmsJobMapper.selectCount(triggerQuery);
		if(triggerCount > 0) {
			throw new ClientException("trigger already exist");
		}
		
		
		//完善字段信息
		LocalDateTime now = DateUtil.now();
		cmsJobPo.setCreateTime(now);
		cmsJobPo.setUpdateTime(now);
		cmsJobPo.setCreaterId(StringUtils.isBlank(cmsJobPo.getCreaterId())? Constant.ADMIN_ID : cmsJobPo.getCreaterId());
		cmsJobPo.setUpdaterId(StringUtils.isBlank(cmsJobPo.getUpdaterId())? Constant.ADMIN_ID : cmsJobPo.getUpdaterId());
		cmsJobPo.setStatus(StatusEnum.ENABLED);
		
		//保存
		cmsJobMapper.insert(cmsJobPo);
		
		//创建定时任务
		ScheduleJob scheduleJob = new ScheduleJob();
		BeanUtils.copyProperties(scheduleJob, cmsJobPo);
		jobHandler.addJob(scheduleJob);
		if(scheduleJob.getJobStatus() == JobStatusEnum.PAUSE) {//暂停任务
			jobHandler.pauseJob(scheduleJob);
		}
		
		return ResponseResult.success(ResponseEnum.SUCCESS, "job add success");
	}

}

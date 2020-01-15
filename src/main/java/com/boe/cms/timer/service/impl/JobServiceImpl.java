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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
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
		JobDataMap jobDataMap = new JobDataMap();
		String extraInfo = cmsJobPo.getExtraInfo();
		if (StringUtils.isNotBlank(extraInfo)) {
			jobDataMap.put("extraInfo", extraInfo);
		}
		jobHandler.addJob(scheduleJob, jobDataMap, new JobDataMap());
		if(scheduleJob.getJobStatus() == JobStatusEnum.PAUSE) {//暂停任务
			jobHandler.pauseJob(scheduleJob);
		}
		
		return ResponseResult.success(ResponseEnum.SUCCESS, "job add success");
	}


	/**
	 * throw jobKey: jobName and jobGroup 更新 job
	 */
	@Override
	@Transactional
	public ResponseResult<String> updateJob(CmsJobPo cmsJobPo) throws Exception {


		ScheduleJob scheduleJob = new ScheduleJob();
		BeanUtils.copyProperties(scheduleJob, cmsJobPo);

		//1. 删除原来的
		LambdaQueryWrapper<CmsJobPo> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(CmsJobPo::getJobGroup, cmsJobPo.getJobGroup()).eq(CmsJobPo::getJobName, cmsJobPo.getJobName());
		CmsJobPo cmsJobPoOld = cmsJobMapper.selectOne(wrapper);
		if (cmsJobPoOld == null) {
			return ResponseResult.fail(ResponseEnum.FAIL, "JOB_KEY NOT EXISTS");
		}

		cmsJobMapper.deleteById(cmsJobPoOld.getId());

		//2. 删除任务
		jobHandler.deleteJob(scheduleJob);

		//3. 添加新任务
		cmsJobPo.setCreateTime(cmsJobPoOld.getCreateTime());
		cmsJobPo.setUpdateTime(DateUtil.now());
		cmsJobPo.setCreaterId(cmsJobPoOld.getCreaterId());
		cmsJobPo.setUpdaterId(StringUtils.isBlank(cmsJobPo.getUpdaterId())? Constant.ADMIN_ID : cmsJobPo.getUpdaterId());
		cmsJobPo.setStatus(StatusEnum.ENABLED);

		//保存
		cmsJobMapper.insert(cmsJobPo);

		//创建定时任务
		JobDataMap jobDataMap = new JobDataMap();
		String extraInfo = cmsJobPo.getExtraInfo();
		if (StringUtils.isNotBlank(extraInfo)) {
			jobDataMap.put("extraInfo", extraInfo);
		}
		jobHandler.addJob(scheduleJob, jobDataMap, new JobDataMap());
		if(scheduleJob.getJobStatus() == JobStatusEnum.PAUSE) {//暂停任务
			jobHandler.pauseJob(scheduleJob);
		}

		return ResponseResult.success(ResponseEnum.SUCCESS, "job update success");
	}

	@Override
	@Transactional
	public ResponseResult<String> deleteJob(CmsJobPo cmsJobPo) throws Exception {
		ScheduleJob scheduleJob = new ScheduleJob();
		BeanUtils.copyProperties(scheduleJob, cmsJobPo);

		//1. 删除原来的
		LambdaQueryWrapper<CmsJobPo> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(CmsJobPo::getJobGroup, cmsJobPo.getJobGroup()).eq(CmsJobPo::getJobName, cmsJobPo.getJobName());
		CmsJobPo cmsJobPoOld = cmsJobMapper.selectOne(wrapper);
		if (cmsJobPoOld == null) {
			return ResponseResult.fail(ResponseEnum.FAIL, "JOB_KEY NOT EXISTS");
		}

		cmsJobMapper.deleteById(cmsJobPoOld.getId());

		//2. 删除任务
		jobHandler.deleteJob(scheduleJob);
		return ResponseResult.success(ResponseEnum.SUCCESS, "job delete success");

	}

	@Override
	@Transactional
	public ResponseResult<String> pauseJob(CmsJobPo cmsJobPo) throws Exception {

		LambdaQueryWrapper<CmsJobPo> queryWrapper = Wrappers.lambdaQuery();

		queryWrapper.eq(CmsJobPo::getJobGroup, cmsJobPo.getJobGroup()).eq(CmsJobPo::getJobName, cmsJobPo.getJobName());
		CmsJobPo one = cmsJobMapper.selectOne(queryWrapper);
		if (one == null) {
			return ResponseResult.fail(ResponseEnum.FAIL, "JOB_KEY NOT EXISTS");
		}

		one.setJobStatus(JobStatusEnum.PAUSE);//状态改为 pause
		one.setUpdateTime(DateUtil.now()); //修改更新时间
		cmsJobMapper.updateById(one);


		ScheduleJob scheduleJob = new ScheduleJob();
		BeanUtils.copyProperties(scheduleJob, cmsJobPo);

		jobHandler.pauseJob(scheduleJob);


		return ResponseResult.success(ResponseEnum.SUCCESS, "job pause success");
	}

	@Override
	@Transactional
	public ResponseResult<String> triggerJob(CmsJobPo cmsJobPo) throws Exception {
		LambdaQueryWrapper<CmsJobPo> queryWrapper = Wrappers.lambdaQuery();

		queryWrapper.eq(CmsJobPo::getJobGroup, cmsJobPo.getJobGroup()).eq(CmsJobPo::getJobName, cmsJobPo.getJobName());
		CmsJobPo one = cmsJobMapper.selectOne(queryWrapper);
		if (one == null) {
			return ResponseResult.fail(ResponseEnum.FAIL, "JOB_KEY NOT EXISTS");
		}
		ScheduleJob scheduleJob = new ScheduleJob();
		BeanUtils.copyProperties(scheduleJob, cmsJobPo);

		JobDataMap jobDataMap = new JobDataMap();

		String extraInfo = cmsJobPo.getExtraInfo();
		if (StringUtils.isNotBlank(extraInfo)) {
			
			jobDataMap.put("extraInfo", extraInfo);
		}

		jobHandler.triggerJob(scheduleJob, jobDataMap);


		return ResponseResult.success(ResponseEnum.SUCCESS, "job trigger success");
	}

	@Override
	@Transactional
	public ResponseResult<String> resumeJob(CmsJobPo cmsJobPo) throws Exception {
		LambdaQueryWrapper<CmsJobPo> queryWrapper = Wrappers.lambdaQuery();

		queryWrapper.eq(CmsJobPo::getJobGroup, cmsJobPo.getJobGroup()).eq(CmsJobPo::getJobName, cmsJobPo.getJobName());
		CmsJobPo one = cmsJobMapper.selectOne(queryWrapper);
		if (one == null) {
			return ResponseResult.fail(ResponseEnum.FAIL, "JOB_KEY NOT EXISTS");
		}

		one.setJobStatus(JobStatusEnum.RUNNING);//状态改为 pause
		one.setUpdateTime(DateUtil.now()); //修改更新时间
		cmsJobMapper.updateById(one);


		ScheduleJob scheduleJob = new ScheduleJob();
		BeanUtils.copyProperties(scheduleJob, cmsJobPo);

		jobHandler.resumeJob(scheduleJob);


		return ResponseResult.success(ResponseEnum.SUCCESS, "job resume success");
	}

	@Override
	public ResponseResult<CmsJobPo> getJob(CmsJobPo cmsJobPo) throws Exception {
		LambdaQueryWrapper<CmsJobPo> queryWrapper = Wrappers.lambdaQuery();

		final String jobGroup = cmsJobPo.getJobGroup();
		final String jobName = cmsJobPo.getJobName();

		queryWrapper.eq(CmsJobPo::getJobGroup, jobGroup).eq(CmsJobPo::getJobName, jobName);
		CmsJobPo one = cmsJobMapper.selectOne(queryWrapper);

		if (one == null) {
			log.error("jobKey not exist, [jobGroup={}] [jobName={}]", jobGroup, jobName);
			return ResponseResult.fail(ResponseEnum.FAIL, null);
		}


		return ResponseResult.success(ResponseEnum.SUCCESS, one);
	}

	@Override
	public ResponseResult<List<CmsJobPo>> listAll() throws Exception {
		List<CmsJobPo> list = cmsJobMapper.selectList(null);
		return ResponseResult.success(ResponseEnum.SUCCESS, list);
	}


	@Override
	public ResponseResult<String> resetTriggerFromErrorState(CmsJobPo cmsJobPo) throws Exception {
		ScheduleJob scheduleJob = new ScheduleJob();
		BeanUtils.copyProperties(scheduleJob, cmsJobPo);
		jobHandler.resetTriggerFromErrorState(scheduleJob);
		return ResponseResult.success(ResponseEnum.SUCCESS, "job reset success");
	}


}

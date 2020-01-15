package com.boe.cms.timer.service;

import com.boe.cms.timer.common.ResponseResult;
import com.boe.cms.timer.po.CmsJobPo;

import java.util.List;

public interface JobService {
	
	
	public ResponseResult<String> addJob(CmsJobPo cmsJobPo) throws Exception;

    public ResponseResult<String> updateJob(CmsJobPo cmsJobPo) throws  Exception;

    public ResponseResult<String> deleteJob(CmsJobPo cmsJobPo) throws  Exception;

    public ResponseResult<String> pauseJob(CmsJobPo cmsJobPo) throws  Exception;

    public ResponseResult<String> triggerJob(CmsJobPo cmsJobPo) throws  Exception;

    public ResponseResult<String> resumeJob(CmsJobPo cmsJobPo) throws  Exception;

    public ResponseResult<CmsJobPo> getJob(CmsJobPo cmsJobPo) throws Exception;

    public ResponseResult<List<CmsJobPo>> listAll() throws  Exception;

	public ResponseResult<String> resetTriggerFromErrorState(CmsJobPo cmsJobPo) throws Exception;
}

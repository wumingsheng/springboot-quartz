package com.boe.cms.timer.service;

import com.boe.cms.timer.common.ResponseResult;
import com.boe.cms.timer.po.CmsJobPo;

public interface JobService {
	
	
	public ResponseResult<String> addJob(CmsJobPo cmsJobPo) throws Exception;

}

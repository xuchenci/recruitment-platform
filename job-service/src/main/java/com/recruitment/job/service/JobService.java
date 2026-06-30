package com.recruitment.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.recruitment.common.entity.Job;
import java.util.List;

/**
 * 职位服务接口
 */
public interface JobService extends IService<Job> {
    
    /**
     * 发布职位
     */
    Job publishJob(Job job);
    
    /**
     * 更新职位
     */
    Job updateJob(Long jobId, Job job);
    
    /**
     * 下架职位
     */
    boolean removeJob(Long jobId);
    
    /**
     * 根据条件查询职位
     */
    List<Job> searchJobs(String keyword, String city, Integer salaryMin, Integer salaryMax, Long categoryId);
    
    /**
     * 增加职位浏览次数
     */
    void incrementViewCount(Long jobId);
    
    /**
     * 增加职位申请次数
     */
    void incrementApplyCount(Long jobId);
}

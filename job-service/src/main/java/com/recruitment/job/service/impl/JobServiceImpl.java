package com.recruitment.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.recruitment.common.entity.Job;
import com.recruitment.common.mapper.JobMapper;
import com.recruitment.job.service.JobService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 职位服务实现类
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {
    
    @Override
    public Job publishJob(Job job) {
        job.setStatus(1); // 招聘中
        job.setPublishTime(LocalDateTime.now());
        job.setViewCount(0);
        job.setApplyCount(0);
        
        save(job);
        return job;
    }
    
    @Override
    public Job updateJob(Long jobId, Job job) {
        Job existingJob = getById(jobId);
        if (existingJob == null) {
            throw new RuntimeException("职位不存在");
        }
        
        job.setId(jobId);
        updateById(job);
        return getById(jobId);
    }
    
    @Override
    public boolean removeJob(Long jobId) {
        Job job = getById(jobId);
        if (job == null) {
            throw new RuntimeException("职位不存在");
        }
        
        job.setStatus(0); // 下架
        return updateById(job);
    }
    
    @Override
    public List<Job> searchJobs(String keyword, String city, Integer salaryMin, 
                                 Integer salaryMax, Long categoryId) {
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        
        // 只查询招聘中的职位
        queryWrapper.eq(Job::getStatus, 1);
        
        // 关键词搜索
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                .like(Job::getJobTitle, keyword)
                .or()
                .like(Job::getJobDescription, keyword)
            );
        }
        
        // 城市筛选
        if (city != null && !city.trim().isEmpty()) {
            queryWrapper.eq(Job::getCity, city);
        }
        
        // 薪资筛选
        if (salaryMin != null) {
            queryWrapper.ge(Job::getSalaryMax, salaryMin);
        }
        if (salaryMax != null) {
            queryWrapper.le(Job::getSalaryMin, salaryMax);
        }
        
        // 分类筛选
        if (categoryId != null) {
            queryWrapper.eq(Job::getCategoryId, categoryId);
        }
        
        // 按发布时间倒序
        queryWrapper.orderByDesc(Job::getPublishTime);
        
        return list(queryWrapper);
    }
    
    @Override
    public void incrementViewCount(Long jobId) {
        Job job = getById(jobId);
        if (job != null) {
            job.setViewCount(job.getViewCount() + 1);
            updateById(job);
        }
    }
    
    @Override
    public void incrementApplyCount(Long jobId) {
        Job job = getById(jobId);
        if (job != null) {
            job.setApplyCount(job.getApplyCount() + 1);
            updateById(job);
        }
    }
}

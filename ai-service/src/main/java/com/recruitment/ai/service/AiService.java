package com.recruitment.ai.service;

import reactor.core.publisher.Flux;

/**
 * AI服务接口
 */
public interface AiService {

    /**
     * 获取简历优化建议
     * @param resumeContent 简历内容
     * @return 优化建议
     */
    String getResumeOptimization(String resumeContent);

    /**
     * 获取简历优化建议（流式输出）
     * @param resumeContent 简历内容
     * @return 流式优化建议
     */
    Flux<String> getResumeOptimizationStream(String resumeContent);

    /**
     * 获取简历评分
     * @param resumeContent 简历内容
     * @return 评分结果
     */
    String getResumeScore(String resumeContent);

    /**
     * 人岗智能匹配
     * @param resumeContent 简历内容
     * @param jobInfo 岗位信息（可选）
     * @return 匹配推荐结果
     */
    String jobMatching(String resumeContent, String jobInfo);

    /**
     * 人岗智能匹配（流式输出）
     * @param resumeContent 简历内容
     * @param jobInfo 岗位信息（可选）
     * @return 流式匹配推荐结果
     */
    Flux<String> jobMatchingStream(String resumeContent, String jobInfo);

    /**
     * 预测面试问题
     * @param resumeContent 简历内容
     * @param jobTitle 目标岗位
     * @return 预测的面试问题
     */
    String predictInterviewQuestions(String resumeContent, String jobTitle);

    /**
     * 通用问答
     * @param question 用户问题
     * @return AI回答
     */
    String askQuestion(String question);

    /**
     * 通用问答（流式输出）
     * @param question 用户问题
     * @return 流式AI回答
     */
    Flux<String> askQuestionStream(String question);
}
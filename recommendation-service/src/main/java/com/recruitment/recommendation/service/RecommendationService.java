package com.recruitment.recommendation.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * 智能推荐服务
 * 使用SpringAI实现基于学生画像的职位推荐
 */
@Service
public class RecommendationService {
    
    @Autowired
    private ChatClient chatClient;
    
    @Autowired(required = false)
    private EmbeddingClient embeddingClient;
    
    @Autowired(required = false)
    private VectorStore vectorStore;
    
    /**
     * 根据学生信息生成职业建议
     */
    public String generateCareerAdvice(String studentProfile, String preferences) {
        String promptText = String.format("""
                作为一个专业的职业规划顾问，请根据以下学生信息提供个性化的职业建议：
                
                学生信息：
                %s
                
                求职偏好：
                %s
                
                请提供：
                1. 适合的职业方向（3-5个）
                2. 每个方向的发展前景
                3. 需要提升的技能
                4. 求职建议
                
                请用专业、鼓励的语气回答。
                """, studentProfile, preferences);
        
        Prompt prompt = new Prompt(promptText);
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
    
    /**
     * 推荐匹配的职位
     */
    public String recommendJobs(String studentProfile, String availableJobs) {
        String promptText = String.format("""
                作为一个智能招聘顾问，请根据学生信息推荐最匹配的职位：
                
                学生信息：
                %s
                
                可用职位：
                %s
                
                请分析并推荐最适合的5个职位，并说明推荐理由。
                考虑因素：
                1. 技能匹配度
                2. 薪资期望
                3. 地理位置
                4. 职业发展
                
                请按匹配度从高到低排序。
                """, studentProfile, availableJobs);
        
        Prompt prompt = new Prompt(promptText);
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
    
    /**
     * 优化简历建议
     */
    public String optimizeResume(String resumeContent, String targetJob) {
        String promptText = String.format("""
                作为一个简历优化专家，请分析并提供简历改进建议：
                
                当前简历内容：
                %s
                
                目标职位：
                %s
                
                请提供：
                1. 简历评分（1-100分）
                2. 存在的问题
                3. 具体的改进建议
                4. 优化后的简历要点
                
                要求具体、可操作。
                """, resumeContent, targetJob);
        
        Prompt prompt = new Prompt(promptText);
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
    
    /**
     * 面试准备建议
     */
    public String interviewPreparation(String jobDescription, String companyInfo) {
        String promptText = String.format("""
                作为一个面试辅导专家，请为以下职位面试提供准备建议：
                
                职位描述：
                %s
                
                公司信息：
                %s
                
                请提供：
                1. 可能被问到的问题（10个）
                2. 如何回答这些问题的建议
                3. 需要准备的技术知识点
                4. 提问面试官的好问题（5个）
                5. 面试注意事项
                
                要实用、具体。
                """, jobDescription, companyInfo);
        
        Prompt prompt = new Prompt(promptText);
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
    
    /**
     * 使用向量搜索推荐相似职位
     */
    public List<Document> findSimilarJobs(String jobDescription) {
        if (vectorStore == null) {
            throw new RuntimeException("向量存储未配置");
        }
        
        // 在向量数据库中搜索相似的职位
        return vectorStore.similaritySearch(
            org.springframework.ai.vectorstore.SearchRequest.query(jobDescription)
                .withTopK(5)
        );
    }
    
    /**
     * 将职位信息存储到向量数据库
     */
    public void indexJob(String jobId, String jobDescription) {
        if (vectorStore == null) {
            throw new RuntimeException("向量存储未配置");
        }
        
        Document document = new Document(
            jobId,
            jobDescription,
            Map.of("jobId", jobId)
        );
        
        vectorStore.add(List.of(document));
    }
}

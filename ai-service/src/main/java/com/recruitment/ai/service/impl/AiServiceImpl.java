package com.recruitment.ai.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.ai.prompt.PromptConstants;
import com.recruitment.ai.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiServiceImpl implements AiService {

    private static final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);

    private final String apiKey;
    private final String baseUrl;
    private final String model;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public AiServiceImpl(@Value("${spring.ai.openai.api-key}") String apiKey,
                         @Value("${spring.ai.openai.base-url}") String baseUrl,
                         @Value("${spring.ai.openai.chat.options.model:qwen-plus}") String model) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 调用阿里百炼API（非流式）
     */
    private String callDashscope(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 3000);
            requestBody.put("temperature", 0.7);

            String responseJson = webClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(responseJson);
            return root.path("choices").path(0).path("message").path("content").asText();
        } catch (Exception e) {
            logger.error("调用阿里百炼API失败: {}", e.getMessage(), e);
            return "AI服务暂时不可用，请稍后重试。错误信息：" + e.getMessage();
        }
    }

    @Override
    public String getResumeOptimization(String resumeContent) {
        String prompt = PromptConstants.RESUME_OPTIMIZATION_PROMPT + "\n\n用户简历内容：\n" + resumeContent;
        return callDashscope(prompt);
    }

    @Override
    public Flux<String> getResumeOptimizationStream(String resumeContent) {
        try {
            String prompt = PromptConstants.RESUME_OPTIMIZATION_PROMPT + "\n\n用户简历内容：\n" + resumeContent;
            return callDashscopeStream(prompt);
        } catch (Exception e) {
            logger.error("简历优化流式失败: {}", e.getMessage(), e);
            return Flux.just("AI服务暂时不可用，请稍后重试。");
        }
    }

    @Override
    public String getResumeScore(String resumeContent) {
        String prompt = PromptConstants.RESUME_SCORING_PROMPT + "\n\n用户简历内容：\n" + resumeContent;
        return callDashscope(prompt);
    }

    @Override
    public String jobMatching(String resumeContent, String jobInfo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(PromptConstants.JOB_MATCH_PROMPT);
        prompt.append("\n\n用户简历内容：\n").append(resumeContent);
        if (jobInfo != null && !jobInfo.isEmpty()) {
            prompt.append("\n\n目标岗位信息：\n").append(jobInfo);
        }
        return callDashscope(prompt.toString());
    }

    @Override
    public Flux<String> jobMatchingStream(String resumeContent, String jobInfo) {
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append(PromptConstants.JOB_MATCH_PROMPT);
            prompt.append("\n\n用户简历内容：\n").append(resumeContent);
            if (jobInfo != null && !jobInfo.isEmpty()) {
                prompt.append("\n\n目标岗位信息：\n").append(jobInfo);
            }
            return callDashscopeStream(prompt.toString());
        } catch (Exception e) {
            logger.error("人岗匹配流式失败: {}", e.getMessage(), e);
            return Flux.just("AI服务暂时不可用，请稍后重试。");
        }
    }

    @Override
    public String predictInterviewQuestions(String resumeContent, String jobTitle) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(PromptConstants.INTERVIEW_PREDICTION_PROMPT);
        prompt.append("\n\n用户简历内容：\n").append(resumeContent);
        prompt.append("\n\n目标岗位：").append(jobTitle);
        return callDashscope(prompt.toString());
    }

    @Override
    public String askQuestion(String question) {
        return callDashscope(question);
    }

    @Override
    public Flux<String> askQuestionStream(String question) {
        try {
            return callDashscopeStream(question);
        } catch (Exception e) {
            logger.error("通用问答流式失败: {}", e.getMessage(), e);
            return Flux.just("AI服务暂时不可用，请稍后重试。");
        }
    }

    /**
     * 调用阿里百炼API（流式输出）
     */
    private Flux<String> callDashscopeStream(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("stream", true);

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 3000);
            requestBody.put("temperature", 0.7);

            return webClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToFlux(String.class)
                    .filter(line -> !line.isEmpty() && !line.equals("[DONE]"))
                    .map(line -> {
                        try {
                            if (line.startsWith("data: ")) {
                                line = line.substring(6).trim();
                            }
                            if (line.isEmpty() || line.equals("[DONE]")) {
                                return "";
                            }
                            JsonNode node = objectMapper.readTree(line);
                            return node.path("choices").path(0).path("delta").path("content").asText("");
                        } catch (Exception e) {
                            return "";
                        }
                    })
                    .filter(content -> !content.isEmpty());
        } catch (Exception e) {
            logger.error("调用阿里百炼流式API失败: {}", e.getMessage(), e);
            return Flux.just("AI服务暂时不可用，请稍后重试。");
        }
    }
}

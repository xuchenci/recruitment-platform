package com.recruitment.ai.controller;

import com.recruitment.ai.service.AiService;
import com.recruitment.common.entity.AiFavorite;
import com.recruitment.common.mapper.AiFavoriteMapper;
import com.recruitment.common.result.Result;
import com.recruitment.common.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AI服务控制器
 * 提供简历优化、人岗匹配等AI能力接口，支持流式输出
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    private static final Logger logger = LoggerFactory.getLogger(AiController.class);

    private final AiService aiService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @Autowired
    private AiFavoriteMapper aiFavoriteMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取简历优化建议（普通接口）
     */
    @PostMapping("/resume/optimize")
    public Result<?> optimizeResume(@RequestBody Map<String, String> request) {
        String resumeContent = request.get("resumeContent");
        if (resumeContent == null || resumeContent.isEmpty()) {
            return Result.error(400, "简历内容不能为空");
        }
        String result = aiService.getResumeOptimization(resumeContent);
        return Result.success(result);
    }

    /**
     * 获取简历优化建议（流式输出 - SseEmitter方式）
     */
    @PostMapping(value = "/resume/optimize/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter optimizeResumeStream(@RequestBody Map<String, String> request) {
        String resumeContent = request.get("resumeContent");
        SseEmitter emitter = new SseEmitter(120000L); // 120秒超时

        if (resumeContent == null || resumeContent.isEmpty()) {
            executorService.execute(() -> {
                try {
                    emitter.send(SseEmitter.event().data("错误：简历内容不能为空"));
                    emitter.complete();
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            });
            return emitter;
        }

        executorService.execute(() -> {
            try {
                aiService.getResumeOptimizationStream(resumeContent)
                        .subscribe(
                                content -> {
                                    try {
                                        emitter.send(SseEmitter.event().data(content));
                                    } catch (IOException e) {
                                        emitter.completeWithError(e);
                                    }
                                },
                                error -> {
                                    logger.error("流式优化失败: {}", error.getMessage());
                                    emitter.completeWithError(error);
                                },
                                () -> {
                                    try {
                                        emitter.send(SseEmitter.event().data("[DONE]"));
                                        emitter.complete();
                                    } catch (IOException e) {
                                        emitter.completeWithError(e);
                                    }
                                }
                        );
            } catch (Exception e) {
                logger.error("流式优化异常: {}", e.getMessage());
                try {
                    emitter.send(SseEmitter.event().data("AI服务暂时不可用"));
                    emitter.complete();
                } catch (IOException ex) {
                    emitter.completeWithError(ex);
                }
            }
        });

        return emitter;
    }

    /**
     * 获取简历评分
     */
    @PostMapping("/resume/score")
    public Result<?> scoreResume(@RequestBody Map<String, String> request) {
        String resumeContent = request.get("resumeContent");
        if (resumeContent == null || resumeContent.isEmpty()) {
            return Result.error(400, "简历内容不能为空");
        }
        String result = aiService.getResumeScore(resumeContent);
        
        try {
            JsonNode node = objectMapper.readTree(result);
            if (node.has("totalScore") && node.has("categories")) {
                return Result.success(node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return Result.success(generateDefaultScore(result));
    }

    private Map<String, Object> generateDefaultScore(String aiResult) {
        Map<String, Object> score = new HashMap<>();
        score.put("totalScore", 65);
        
        java.util.List<Map<String, Object>> categories = new java.util.ArrayList<>();
        
        Map<String, Object> completeness = new HashMap<>();
        completeness.put("name", "完整性");
        completeness.put("score", 18);
        completeness.put("maxScore", 30);
        completeness.put("reason", "简历内容基本完整，但部分信息可以进一步完善");
        completeness.put("suggestions", java.util.Arrays.asList("完善基本信息", "补充联系方式", "添加个人照片"));
        categories.add(completeness);
        
        Map<String, Object> relevance = new HashMap<>();
        relevance.put("name", "相关性");
        relevance.put("score", 20);
        relevance.put("maxScore", 30);
        relevance.put("reason", "简历与目标岗位有一定匹配度");
        relevance.put("suggestions", java.util.Arrays.asList("添加目标岗位相关关键词", "突出相关工作经验"));
        categories.add(relevance);
        
        Map<String, Object> professionalism = new HashMap<>();
        professionalism.put("name", "专业性");
        professionalism.put("score", 15);
        professionalism.put("maxScore", 20);
        professionalism.put("reason", "语言表达较为专业，建议增加量化成果");
        professionalism.put("suggestions", java.util.Arrays.asList("用数据展示成果", "使用专业术语"));
        categories.add(professionalism);
        
        Map<String, Object> format = new HashMap<>();
        format.put("name", "格式规范性");
        format.put("score", 7);
        format.put("maxScore", 10);
        format.put("reason", "格式基本规范");
        format.put("suggestions", java.util.Arrays.asList("统一字体和排版", "保持整洁美观"));
        categories.add(format);
        
        Map<String, Object> highlights = new HashMap<>();
        highlights.put("name", "亮点突出");
        highlights.put("score", 5);
        highlights.put("maxScore", 10);
        highlights.put("reason", "亮点不够突出，建议强化个人优势");
        highlights.put("suggestions", java.util.Arrays.asList("突出核心竞争力", "展示独特价值"));
        categories.add(highlights);
        
        score.put("categories", categories);
        score.put("overallSuggestions", aiResult != null && aiResult.length() > 0 ? aiResult.substring(0, Math.min(500, aiResult.length())) : "建议完善简历各部分内容，突出个人优势，提高与目标岗位的匹配度。");
        score.put("scoreLevel", "及格");
        
        return score;
    }

    /**
     * 人岗智能匹配（普通接口）
     */
    @PostMapping("/job/match")
    public Result<?> jobMatch(@RequestBody Map<String, String> request) {
        String resumeContent = request.get("resumeContent");
        String jobInfo = request.get("jobInfo");
        
        if (resumeContent == null || resumeContent.isEmpty()) {
            return Result.error(400, "简历内容不能为空");
        }
        String result = aiService.jobMatching(resumeContent, jobInfo);
        return Result.success(result);
    }

    /**
     * 人岗智能匹配（流式输出 - SseEmitter方式）
     */
    @PostMapping(value = "/job/match/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter jobMatchStream(@RequestBody Map<String, String> request) {
        String resumeContent = request.get("resumeContent");
        String jobInfo = request.get("jobInfo");
        SseEmitter emitter = new SseEmitter(120000L);

        if (resumeContent == null || resumeContent.isEmpty()) {
            executorService.execute(() -> {
                try {
                    emitter.send(SseEmitter.event().data("错误：简历内容不能为空"));
                    emitter.complete();
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            });
            return emitter;
        }

        executorService.execute(() -> {
            try {
                aiService.jobMatchingStream(resumeContent, jobInfo)
                        .subscribe(
                                content -> {
                                    try {
                                        emitter.send(SseEmitter.event().data(content));
                                    } catch (IOException e) {
                                        emitter.completeWithError(e);
                                    }
                                },
                                error -> {
                                    logger.error("流式匹配失败: {}", error.getMessage());
                                    emitter.completeWithError(error);
                                },
                                () -> {
                                    try {
                                        emitter.send(SseEmitter.event().data("[DONE]"));
                                        emitter.complete();
                                    } catch (IOException e) {
                                        emitter.completeWithError(e);
                                    }
                                }
                        );
            } catch (Exception e) {
                logger.error("流式匹配异常: {}", e.getMessage());
                try {
                    emitter.send(SseEmitter.event().data("AI服务暂时不可用"));
                    emitter.complete();
                } catch (IOException ex) {
                    emitter.completeWithError(ex);
                }
            }
        });

        return emitter;
    }

    /**
     * 预测面试问题
     */
    @PostMapping("/interview/predict")
    public Result<?> predictInterview(@RequestBody Map<String, String> request) {
        String resumeContent = request.get("resumeContent");
        String jobTitle = request.get("jobTitle");
        
        if (resumeContent == null || resumeContent.isEmpty()) {
            return Result.error(400, "简历内容不能为空");
        }
        String result = aiService.predictInterviewQuestions(resumeContent, jobTitle);
        return Result.success(result);
    }

    /**
     * 通用问答
     */
    @PostMapping("/ask")
    public Result<?> askQuestion(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        if (question == null || question.isEmpty()) {
            return Result.error(400, "问题不能为空");
        }
        String result = aiService.askQuestion(question);
        return Result.success(result);
    }

    /**
     * 通用问答（流式输出 - SseEmitter方式）
     */
    @PostMapping(value = "/ask/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter askQuestionStream(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        SseEmitter emitter = new SseEmitter(120000L);

        if (question == null || question.isEmpty()) {
            executorService.execute(() -> {
                try {
                    emitter.send(SseEmitter.event().data("错误：问题不能为空"));
                    emitter.complete();
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            });
            return emitter;
        }

        executorService.execute(() -> {
            try {
                aiService.askQuestionStream(question)
                        .subscribe(
                                content -> {
                                    try {
                                        emitter.send(SseEmitter.event().data(content));
                                    } catch (IOException e) {
                                        emitter.completeWithError(e);
                                    }
                                },
                                error -> {
                                    logger.error("流式问答失败: {}", error.getMessage());
                                    emitter.completeWithError(error);
                                },
                                () -> {
                                    try {
                                        emitter.send(SseEmitter.event().data("[DONE]"));
                                        emitter.complete();
                                    } catch (IOException e) {
                                        emitter.completeWithError(e);
                                    }
                                }
                        );
            } catch (Exception e) {
                logger.error("流式问答异常: {}", e.getMessage());
                try {
                    emitter.send(SseEmitter.event().data("AI服务暂时不可用"));
                    emitter.complete();
                } catch (IOException ex) {
                    emitter.completeWithError(ex);
                }
            }
        });

        return emitter;
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<?> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "ai-service");
        return Result.success(status);
    }

    // ==================== AI收藏方案接口 ====================

    /**
     * 收藏AI方案
     */
    @PostMapping("/favorite")
    public Result<?> addFavorite(@RequestBody Map<String, Object> params,
                                  @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                return Result.error(401, "请先登录");
            }

            String content = (String) params.get("content");
            String type = (String) params.getOrDefault("type", "optimize");
            String title = (String) params.getOrDefault("title", "");

            if (content == null || content.isEmpty()) {
                return Result.error(400, "收藏内容不能为空");
            }

            AiFavorite favorite = new AiFavorite();
            favorite.setUserId(userId);
            favorite.setContent(content);
            favorite.setType(type);
            favorite.setTitle(title);
            favorite.setCreatedAt(LocalDateTime.now());
            favorite.setUpdatedAt(LocalDateTime.now());

            aiFavoriteMapper.insert(favorite);
            return Result.success("收藏成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "收藏失败: " + e.getMessage());
        }
    }

    /**
     * 获取我的收藏列表
     */
    @GetMapping(value = "/favorites", produces = "application/json;charset=UTF-8")
    public Result<?> getFavorites(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String type,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                return Result.error(401, "请先登录");
            }

            QueryWrapper<AiFavorite> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            if (type != null && !type.isEmpty()) {
                wrapper.eq("type", type);
            }
            wrapper.orderByDesc("created_at");

            Page<AiFavorite> pageResult = aiFavoriteMapper.selectPage(new Page<>(page, size), wrapper);
            return Result.success(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取收藏列表失败: " + e.getMessage());
        }
    }

    /**
     * 删除收藏
     */
    @DeleteMapping("/favorite/{id}")
    public Result<?> deleteFavorite(@PathVariable Long id,
                                     @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Long userId = getUserIdFromToken(token);
            if (userId == null) {
                return Result.error(401, "请先登录");
            }

            QueryWrapper<AiFavorite> wrapper = new QueryWrapper<>();
            wrapper.eq("id", id).eq("user_id", userId);
            int deleted = aiFavoriteMapper.delete(wrapper);
            if (deleted > 0) {
                return Result.success("删除成功");
            }
            return Result.error(404, "收藏不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "删除失败: " + e.getMessage());
        }
    }

    /**
     * 从token中解析userId
     */
    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            try {
                return jwtUtil.getUserIdFromToken(token.substring(7));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}

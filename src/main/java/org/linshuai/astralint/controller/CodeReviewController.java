package org.linshuai.astralint.controller;


import org.linshuai.astralint.dto.CodeReviewRequest;
import org.linshuai.astralint.dto.CodeReviewResponse;
import org.linshuai.astralint.service.AiCodeReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 代码审查控制器
 */
@RestController
@RequestMapping("/api/code-review")
@CrossOrigin(origins = "*")
public class CodeReviewController {
    
    private static final Logger logger = LoggerFactory.getLogger(CodeReviewController.class);
    
    @Autowired
    private AiCodeReviewService aiCodeReviewService;
    
    /**
     * 提交代码审查请求
     */
    @PostMapping("/submit")
    public ResponseEntity<CodeReviewResponse> submitCodeReview(@RequestBody CodeReviewRequest request) {
        try {
            logger.info("收到代码审查请求: projectId={}, mergeRequestId={}", 
                request.getProjectId(), request.getMergeRequestId());
            
            String reviewId = UUID.randomUUID().toString();
            CodeReviewResponse response = new CodeReviewResponse(reviewId, request.getProjectId(), request.getMergeRequestId());
            response.setStatus(CodeReviewResponse.ReviewStatus.PROCESSING);
            
            // 异步处理代码审查
            CompletableFuture.runAsync(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    
                    // 根据审查类型选择不同的处理方法
                    String analysisResult;
                    switch (request.getReviewType()) {
                        case SECURITY:
                            analysisResult = aiCodeReviewService.checkCodeSecurity(request.getDiffContent());
                            break;
                        case PERFORMANCE:
                            analysisResult = aiCodeReviewService.provideOptimizationSuggestions(request.getDiffContent());
                            break;
                        case BASIC:
                            analysisResult = aiCodeReviewService.analyzeCodeDiffs(request.getDiffContent());
                            break;
                        case COMPREHENSIVE:
                        default:
                            analysisResult = aiCodeReviewService.generateComprehensiveReview(request.getDiffContent());
                            break;
                    }
                    
                    response.setDetailedAnalysis(analysisResult);
                    response.setStatus(CodeReviewResponse.ReviewStatus.COMPLETED);
                    response.setProcessingTimeMs(System.currentTimeMillis() - startTime);
                    
                    logger.info("代码审查完成: reviewId={}, processingTime={}ms", 
                        reviewId, response.getProcessingTimeMs());
                    
                } catch (Exception e) {
                    logger.error("代码审查处理失败: reviewId={}", reviewId, e);
                    response.setStatus(CodeReviewResponse.ReviewStatus.FAILED);
                    response.setDetailedAnalysis("代码审查失败: " + e.getMessage());
                }
            });
            
            return ResponseEntity.accepted().body(response);
            
        } catch (Exception e) {
            logger.error("提交代码审查请求失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 获取代码审查结果
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<CodeReviewResponse> getCodeReviewResult(@PathVariable String reviewId) {
        try {
            // 这里应该从数据库或缓存中获取结果
            // 暂时返回模拟数据
            CodeReviewResponse response = new CodeReviewResponse(reviewId, "project-1", "mr-1");
            response.setStatus(CodeReviewResponse.ReviewStatus.COMPLETED);
            response.setSummary("代码审查已完成");
            response.setDetailedAnalysis("详细的代码审查分析结果...");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取代码审查结果失败: reviewId={}", reviewId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 快速代码审查（同步）
     */
    @PostMapping("/quick")
    public ResponseEntity<String> quickCodeReview(@RequestBody CodeReviewRequest request) {
        try {
            logger.info("收到快速代码审查请求: projectId={}", request.getProjectId());
            
            String result = aiCodeReviewService.analyzeCodeDiffs(request.getDiffContent());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("快速代码审查失败", e);
            return ResponseEntity.internalServerError().body("代码审查失败: " + e.getMessage());
        }
    }
    
    /**
     * 安全检查
     */
    @PostMapping("/security")
    public ResponseEntity<String> securityCheck(@RequestBody CodeReviewRequest request) {
        try {
            logger.info("收到安全检查请求: projectId={}", request.getProjectId());
            
            String result = aiCodeReviewService.checkCodeSecurity(request.getDiffContent());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("安全检查失败", e);
            return ResponseEntity.internalServerError().body("安全检查失败: " + e.getMessage());
        }
    }
    
    /**
     * 性能优化建议
     */
    @PostMapping("/performance")
    public ResponseEntity<String> performanceOptimization(@RequestBody CodeReviewRequest request) {
        try {
            logger.info("收到性能优化请求: projectId={}", request.getProjectId());
            
            String result = aiCodeReviewService.provideOptimizationSuggestions(request.getDiffContent());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("性能优化分析失败", e);
            return ResponseEntity.internalServerError().body("性能优化分析失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成代码摘要
     */
    @PostMapping("/summary")
    public ResponseEntity<String> generateSummary(@RequestBody CodeReviewRequest request) {
        try {
            logger.info("收到代码摘要请求: projectId={}", request.getProjectId());
            
            String result = aiCodeReviewService.generateCodeSummary(request.getDiffContent());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("生成代码摘要失败", e);
            return ResponseEntity.internalServerError().body("生成代码摘要失败: " + e.getMessage());
        }
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Code Review Service is running");
    }
} 
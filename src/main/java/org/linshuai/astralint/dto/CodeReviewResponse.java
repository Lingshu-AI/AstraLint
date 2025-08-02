package org.linshuai.astralint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 代码审查响应DTO
 */
public class CodeReviewResponse {
    
    @JsonProperty("review_id")
    private String reviewId;
    
    @JsonProperty("project_id")
    private String projectId;
    
    @JsonProperty("merge_request_id")
    private String mergeRequestId;
    
    @JsonProperty("status")
    private ReviewStatus status;
    
    @JsonProperty("summary")
    private String summary;
    
    @JsonProperty("detailed_analysis")
    private String detailedAnalysis;
    
    @JsonProperty("security_issues")
    private List<SecurityIssue> securityIssues;
    
    @JsonProperty("performance_issues")
    private List<PerformanceIssue> performanceIssues;
    
    @JsonProperty("code_quality_issues")
    private List<CodeQualityIssue> codeQualityIssues;
    
    @JsonProperty("recommendations")
    private List<String> recommendations;
    
    @JsonProperty("overall_score")
    private double overallScore;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("processing_time_ms")
    private long processingTimeMs;
    
    public enum ReviewStatus {
        PENDING,    // 待处理
        PROCESSING, // 处理中
        COMPLETED,  // 已完成
        FAILED      // 失败
    }
    
    /**
     * 安全问题
     */
    public static class SecurityIssue {
        @JsonProperty("severity")
        private Severity severity;
        
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("file_path")
        private String filePath;
        
        @JsonProperty("line_number")
        private Integer lineNumber;
        
        @JsonProperty("suggestion")
        private String suggestion;
        
        public enum Severity {
            LOW, MEDIUM, HIGH, CRITICAL
        }
        
        // 构造函数
        public SecurityIssue() {}
        
        public SecurityIssue(Severity severity, String type, String description) {
            this.severity = severity;
            this.type = type;
            this.description = description;
        }
        
        // Getter和Setter方法
        public Severity getSeverity() { return severity; }
        public void setSeverity(Severity severity) { this.severity = severity; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        
        public Integer getLineNumber() { return lineNumber; }
        public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }
        
        public String getSuggestion() { return suggestion; }
        public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    }
    
    /**
     * 性能问题
     */
    public static class PerformanceIssue {
        @JsonProperty("severity")
        private Severity severity;
        
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("file_path")
        private String filePath;
        
        @JsonProperty("line_number")
        private Integer lineNumber;
        
        @JsonProperty("optimization_suggestion")
        private String optimizationSuggestion;
        
        public enum Severity {
            LOW, MEDIUM, HIGH, CRITICAL
        }
        
        // 构造函数
        public PerformanceIssue() {}
        
        public PerformanceIssue(Severity severity, String type, String description) {
            this.severity = severity;
            this.type = type;
            this.description = description;
        }
        
        // Getter和Setter方法
        public Severity getSeverity() { return severity; }
        public void setSeverity(Severity severity) { this.severity = severity; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        
        public Integer getLineNumber() { return lineNumber; }
        public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }
        
        public String getOptimizationSuggestion() { return optimizationSuggestion; }
        public void setOptimizationSuggestion(String optimizationSuggestion) { this.optimizationSuggestion = optimizationSuggestion; }
    }
    
    /**
     * 代码质量问题
     */
    public static class CodeQualityIssue {
        @JsonProperty("severity")
        private Severity severity;
        
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("file_path")
        private String filePath;
        
        @JsonProperty("line_number")
        private Integer lineNumber;
        
        @JsonProperty("best_practice_suggestion")
        private String bestPracticeSuggestion;
        
        public enum Severity {
            LOW, MEDIUM, HIGH, CRITICAL
        }
        
        // 构造函数
        public CodeQualityIssue() {}
        
        public CodeQualityIssue(Severity severity, String type, String description) {
            this.severity = severity;
            this.type = type;
            this.description = description;
        }
        
        // Getter和Setter方法
        public Severity getSeverity() { return severity; }
        public void setSeverity(Severity severity) { this.severity = severity; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        
        public Integer getLineNumber() { return lineNumber; }
        public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }
        
        public String getBestPracticeSuggestion() { return bestPracticeSuggestion; }
        public void setBestPracticeSuggestion(String bestPracticeSuggestion) { this.bestPracticeSuggestion = bestPracticeSuggestion; }
    }
    
    // 构造函数
    public CodeReviewResponse() {
        this.createdAt = LocalDateTime.now();
    }
    
    public CodeReviewResponse(String reviewId, String projectId, String mergeRequestId) {
        this.reviewId = reviewId;
        this.projectId = projectId;
        this.mergeRequestId = mergeRequestId;
        this.status = ReviewStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getter和Setter方法
    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    
    public String getMergeRequestId() { return mergeRequestId; }
    public void setMergeRequestId(String mergeRequestId) { this.mergeRequestId = mergeRequestId; }
    
    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }
    
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    
    public String getDetailedAnalysis() { return detailedAnalysis; }
    public void setDetailedAnalysis(String detailedAnalysis) { this.detailedAnalysis = detailedAnalysis; }
    
    public List<SecurityIssue> getSecurityIssues() { return securityIssues; }
    public void setSecurityIssues(List<SecurityIssue> securityIssues) { this.securityIssues = securityIssues; }
    
    public List<PerformanceIssue> getPerformanceIssues() { return performanceIssues; }
    public void setPerformanceIssues(List<PerformanceIssue> performanceIssues) { this.performanceIssues = performanceIssues; }
    
    public List<CodeQualityIssue> getCodeQualityIssues() { return codeQualityIssues; }
    public void setCodeQualityIssues(List<CodeQualityIssue> codeQualityIssues) { this.codeQualityIssues = codeQualityIssues; }
    
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    
    public double getOverallScore() { return overallScore; }
    public void setOverallScore(double overallScore) { this.overallScore = overallScore; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
} 
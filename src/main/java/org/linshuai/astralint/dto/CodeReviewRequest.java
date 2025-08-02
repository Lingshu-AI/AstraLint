package org.linshuai.astralint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 代码审查请求DTO
 */
public class CodeReviewRequest {
    
    @JsonProperty("project_id")
    private String projectId;
    
    @JsonProperty("merge_request_id")
    private String mergeRequestId;
    
    @JsonProperty("commit_sha")
    private String commitSha;
    
    @JsonProperty("diff_content")
    private String diffContent;
    
    @JsonProperty("review_type")
    private ReviewType reviewType;
    
    @JsonProperty("file_paths")
    private String[] filePaths;
    
    @JsonProperty("language")
    private String language;
    
    @JsonProperty("priority")
    private Priority priority;
    
    public enum ReviewType {
        BASIC,          // 基础审查
        SECURITY,       // 安全检查
        PERFORMANCE,    // 性能优化
        COMPREHENSIVE   // 综合审查
    }
    
    public enum Priority {
        LOW,    // 低优先级
        MEDIUM, // 中等优先级
        HIGH    // 高优先级
    }
    
    // 构造函数
    public CodeReviewRequest() {}
    
    public CodeReviewRequest(String projectId, String mergeRequestId, String diffContent) {
        this.projectId = projectId;
        this.mergeRequestId = mergeRequestId;
        this.diffContent = diffContent;
        this.reviewType = ReviewType.COMPREHENSIVE;
        this.priority = Priority.MEDIUM;
    }
    
    // Getter和Setter方法
    public String getProjectId() {
        return projectId;
    }
    
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
    public String getMergeRequestId() {
        return mergeRequestId;
    }
    
    public void setMergeRequestId(String mergeRequestId) {
        this.mergeRequestId = mergeRequestId;
    }
    
    public String getCommitSha() {
        return commitSha;
    }
    
    public void setCommitSha(String commitSha) {
        this.commitSha = commitSha;
    }
    
    public String getDiffContent() {
        return diffContent;
    }
    
    public void setDiffContent(String diffContent) {
        this.diffContent = diffContent;
    }
    
    public ReviewType getReviewType() {
        return reviewType;
    }
    
    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }
    
    public String[] getFilePaths() {
        return filePaths;
    }
    
    public void setFilePaths(String[] filePaths) {
        this.filePaths = filePaths;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
} 
package org.linshuai.astralint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 代码审查请求DTO
 */
public class CodeReviewRequest {

    @JsonProperty("project_id")
    @NotBlank(message = "项目ID不能为空")
    @Size(max = 100, message = "项目ID长度不能超过100个字符")
    private String projectId;

    @JsonProperty("merge_request_id")
    @NotBlank(message = "合并请求ID不能为空")
    @Size(max = 50, message = "合并请求ID长度不能超过50个字符")
    private String mergeRequestId;

    @JsonProperty("commit_sha")
    @Size(max = 40, message = "提交SHA长度不能超过40个字符")
    private String commitSha;

    @JsonProperty("diff_content")
    @NotBlank(message = "差异内容不能为空")
    @Size(max = 1000000, message = "差异内容过大，请减少变更内容")
    private String diffContent;

    @JsonProperty("review_type")
    @NotNull(message = "审查类型不能为空")
    private ReviewType reviewType;

    @JsonProperty("file_paths")
    @Size(max = 100, message = "文件路径数量不能超过100个")
    private String[] filePaths;

    @JsonProperty("language")
    @Size(max = 20, message = "编程语言标识长度不能超过20个字符")
    private String language;

    @JsonProperty("priority")
    private Priority priority;

    public enum ReviewType {
        BASIC, // 基础审查
        SECURITY, // 安全检查
        PERFORMANCE, // 性能优化
        COMPREHENSIVE // 综合审查
    }

    public enum Priority {
        LOW, // 低优先级
        MEDIUM, // 中等优先级
        HIGH // 高优先级
    }

    // 构造函数
    public CodeReviewRequest() {
    }

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
        return filePaths != null ? filePaths.clone() : new String[0];
    }

    public void setFilePaths(String[] filePaths) {
        this.filePaths = filePaths != null ? filePaths.clone() : null;
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
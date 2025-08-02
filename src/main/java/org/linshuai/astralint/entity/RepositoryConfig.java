package org.linshuai.astralint.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 仓库配置实体
 */
@Entity
@Table(name = "repository_configs")
public class RepositoryConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "repository_name", nullable = false)
    private String repositoryName;
    
    @Column(name = "repository_url", nullable = false)
    private String repositoryUrl;
    
    @Column(name = "repository_type", nullable = false)
    private String repositoryType; // GITLAB, GITHUB, etc.
    
    @Column(name = "project_id")
    private String projectId;
    
    @Column(name = "access_token")
    private String accessToken;
    
    @Column(name = "webhook_secret")
    private String webhookSecret;
    
    @Column(name = "webhook_url")
    private String webhookUrl;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "auto_review_enabled")
    private Boolean autoReviewEnabled = true;
    
    @Column(name = "review_threshold")
    private Integer reviewThreshold = 100; // 文件大小阈值
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // 构造函数
    public RepositoryConfig() {}
    
    public RepositoryConfig(String repositoryName, String repositoryUrl, String repositoryType) {
        this.repositoryName = repositoryName;
        this.repositoryUrl = repositoryUrl;
        this.repositoryType = repositoryType;
    }
    
    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRepositoryName() { return repositoryName; }
    public void setRepositoryName(String repositoryName) { this.repositoryName = repositoryName; }
    
    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }
    
    public String getRepositoryType() { return repositoryType; }
    public void setRepositoryType(String repositoryType) { this.repositoryType = repositoryType; }
    
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    
    public String getWebhookSecret() { return webhookSecret; }
    public void setWebhookSecret(String webhookSecret) { this.webhookSecret = webhookSecret; }
    
    public String getWebhookUrl() { return webhookUrl; }
    public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Boolean getAutoReviewEnabled() { return autoReviewEnabled; }
    public void setAutoReviewEnabled(Boolean autoReviewEnabled) { this.autoReviewEnabled = autoReviewEnabled; }
    
    public Integer getReviewThreshold() { return reviewThreshold; }
    public void setReviewThreshold(Integer reviewThreshold) { this.reviewThreshold = reviewThreshold; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 
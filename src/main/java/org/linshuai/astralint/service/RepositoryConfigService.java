package org.linshuai.astralint.service;


import org.linshuai.astralint.entity.RepositoryConfig;
import org.linshuai.astralint.repository.RepositoryConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RepositoryConfigService {
    
    private static final Logger logger = LoggerFactory.getLogger(RepositoryConfigService.class);
    
    @Autowired
    private RepositoryConfigRepository repositoryConfigRepository;
    
    @Autowired
    private GitLabService gitLabService;
    
    @Autowired
    private GitHubService gitHubService;
    
    @Autowired
    private GiteeService giteeService;
    
    /**
     * 创建仓库配置
     */
    public RepositoryConfig createRepositoryConfig(RepositoryConfig config) {
        logger.info("创建仓库配置: {}", config.getRepositoryName());
        
        // 检查仓库名称是否已存在
        if (repositoryConfigRepository.existsByRepositoryName(config.getRepositoryName())) {
            throw new IllegalArgumentException("仓库名称已存在: " + config.getRepositoryName());
        }
        
        return repositoryConfigRepository.save(config);
    }
    
    /**
     * 更新仓库配置
     */
    public RepositoryConfig updateRepositoryConfig(Long id, RepositoryConfig config) {
        logger.info("更新仓库配置: id={}", id);
        
        Optional<RepositoryConfig> existingConfig = repositoryConfigRepository.findById(id);
        if (existingConfig.isEmpty()) {
            throw new IllegalArgumentException("仓库配置不存在: " + id);
        }
        
        RepositoryConfig existing = existingConfig.get();
        
        // 检查仓库名称是否被其他配置使用
        if (!existing.getRepositoryName().equals(config.getRepositoryName()) && 
            repositoryConfigRepository.existsByRepositoryName(config.getRepositoryName())) {
            throw new IllegalArgumentException("仓库名称已存在: " + config.getRepositoryName());
        }
        
        // 更新字段
        existing.setRepositoryName(config.getRepositoryName());
        existing.setRepositoryUrl(config.getRepositoryUrl());
        existing.setRepositoryType(config.getRepositoryType());
        existing.setProjectId(config.getProjectId());
        existing.setAccessToken(config.getAccessToken());
        existing.setWebhookSecret(config.getWebhookSecret());
        existing.setWebhookUrl(config.getWebhookUrl());
        existing.setIsActive(config.getIsActive());
        existing.setAutoReviewEnabled(config.getAutoReviewEnabled());
        existing.setReviewThreshold(config.getReviewThreshold());
        existing.setDescription(config.getDescription());
        
        return repositoryConfigRepository.save(existing);
    }
    
    /**
     * 删除仓库配置
     */
    public void deleteRepositoryConfig(Long id) {
        logger.info("删除仓库配置: id={}", id);
        
        Optional<RepositoryConfig> config = repositoryConfigRepository.findById(id);
        if (config.isEmpty()) {
            throw new IllegalArgumentException("仓库配置不存在: " + id);
        }
        
        repositoryConfigRepository.deleteById(id);
    }
    
    /**
     * 获取所有仓库配置
     */
    @Transactional(readOnly = true)
    public List<RepositoryConfig> getAllRepositoryConfigs() {
        return repositoryConfigRepository.findAll();
    }
    
    /**
     * 获取激活的仓库配置
     */
    @Transactional(readOnly = true)
    public List<RepositoryConfig> getActiveRepositoryConfigs() {
        return repositoryConfigRepository.findByIsActiveTrue();
    }
    
    /**
     * 根据ID获取仓库配置
     */
    @Transactional(readOnly = true)
    public Optional<RepositoryConfig> getRepositoryConfigById(Long id) {
        return repositoryConfigRepository.findById(id);
    }
    
    /**
     * 根据仓库名称获取配置
     */
    @Transactional(readOnly = true)
    public Optional<RepositoryConfig> getRepositoryConfigByName(String repositoryName) {
        return repositoryConfigRepository.findByRepositoryName(repositoryName);
    }
    
    /**
     * 获取启用自动审查的仓库
     */
    @Transactional(readOnly = true)
    public List<RepositoryConfig> getAutoReviewEnabledRepositories() {
        return repositoryConfigRepository.findByAutoReviewEnabledTrueAndIsActiveTrue();
    }
    
    /**
     * 切换仓库状态
     */
    public RepositoryConfig toggleRepositoryStatus(Long id) {
        logger.info("切换仓库状态: id={}", id);
        
        Optional<RepositoryConfig> config = repositoryConfigRepository.findById(id);
        if (config.isEmpty()) {
            throw new IllegalArgumentException("仓库配置不存在: " + id);
        }
        
        RepositoryConfig repositoryConfig = config.get();
        repositoryConfig.setIsActive(!repositoryConfig.getIsActive());
        
        return repositoryConfigRepository.save(repositoryConfig);
    }
    
    /**
     * 切换自动审查状态
     */
    public RepositoryConfig toggleAutoReview(Long id) {
        logger.info("切换自动审查状态: id={}", id);
        
        Optional<RepositoryConfig> config = repositoryConfigRepository.findById(id);
        if (config.isEmpty()) {
            throw new IllegalArgumentException("仓库配置不存在: " + id);
        }
        
        RepositoryConfig repositoryConfig = config.get();
        repositoryConfig.setAutoReviewEnabled(!repositoryConfig.getAutoReviewEnabled());
        
        return repositoryConfigRepository.save(repositoryConfig);
    }
    
    /**
     * 测试仓库连接
     */
    public boolean testRepositoryConnection(Long id) {
        logger.info("测试仓库连接: id={}", id);
        
        Optional<RepositoryConfig> config = repositoryConfigRepository.findById(id);
        if (config.isEmpty()) {
            throw new IllegalArgumentException("仓库配置不存在: " + id);
        }
        
        RepositoryConfig repositoryConfig = config.get();
        
        // 这里应该实现具体的连接测试逻辑
        // 根据不同的仓库类型（GitLab、GitHub、Gitee）进行相应的API测试
        try {
            switch (repositoryConfig.getRepositoryType().toUpperCase()) {
                case "GITLAB":
                    return testGitLabConnection(repositoryConfig);
                case "GITHUB":
                    return testGitHubConnection(repositoryConfig);
                case "GITEE":
                    return testGiteeConnection(repositoryConfig);
                default:
                    logger.warn("不支持的仓库类型: {}", repositoryConfig.getRepositoryType());
                    return false;
            }
        } catch (Exception e) {
            logger.error("测试仓库连接失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 测试GitLab连接
     */
    private boolean testGitLabConnection(RepositoryConfig config) {
        try {
            return gitLabService.testConnection(config);
        } catch (Exception e) {
            logger.error("GitLab连接测试失败: {}", config.getRepositoryUrl(), e);
            return false;
        }
    }
    
    /**
     * 测试GitHub连接
     */
    private boolean testGitHubConnection(RepositoryConfig config) {
        try {
            return gitHubService.testConnection(config);
        } catch (Exception e) {
            logger.error("GitHub连接测试失败: {}", config.getRepositoryUrl(), e);
            return false;
        }
    }
    
    /**
     * 测试Gitee连接
     */
    private boolean testGiteeConnection(RepositoryConfig config) {
        try {
            return giteeService.testConnection(config);
        } catch (Exception e) {
            logger.error("Gitee连接测试失败: {}", config.getRepositoryUrl(), e);
            return false;
        }
    }
} 
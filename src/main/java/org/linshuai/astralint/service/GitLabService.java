package org.linshuai.astralint.service;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Diff;
import org.gitlab4j.api.models.MergeRequest;
import org.linshuai.astralint.entity.RepositoryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GitLab服务类
 */
@Service
public class GitLabService {
    
    private static final Logger logger = LoggerFactory.getLogger(GitLabService.class);

    /**
     * 获取GitLab API客户端
     */
    private GitLabApi getGitLabApi(RepositoryConfig config) {
        // 从仓库URL中提取GitLab基础URL
        String baseUrl = extractGitLabBaseUrl(config.getRepositoryUrl());
        return new GitLabApi(baseUrl, config.getAccessToken());
    }
    
    /**
     * 从仓库URL中提取GitLab基础URL
     */
    private String extractGitLabBaseUrl(String repositoryUrl) {
        // 例如: https://gitlab.com/group/project -> https://gitlab.com
        if (repositoryUrl.contains("gitlab.com")) {
            return "https://gitlab.com";
        } else if (repositoryUrl.contains("gitlab.cn")) {
            return "https://gitlab.cn";
        } else {
            // 自定义GitLab实例
            int thirdSlashIndex = repositoryUrl.indexOf("/", repositoryUrl.indexOf("//") + 2);
            if (thirdSlashIndex > 0) {
                return repositoryUrl.substring(0, thirdSlashIndex);
            }
            return repositoryUrl;
        }
    }
    
    /**
     * 获取Merge Request的diff内容
     */
    public String getMergeRequestDiff(RepositoryConfig config, String mergeRequestId) {
        try {
            GitLabApi gitLabApi = getGitLabApi(config);
            Integer projectId = Integer.parseInt(config.getProjectId());
            Long mrId = Long.parseLong(mergeRequestId);
            
            MergeRequest mergeRequest = gitLabApi.getMergeRequestApi().getMergeRequest(projectId, mrId);
            if (mergeRequest == null) {
                logger.warn("未找到Merge Request: projectId={}, mrId={}", projectId, mrId);
                return null;
            }
            
            // 获取MR的变更内容
            List<String> changes = gitLabApi.getMergeRequestApi().getMergeRequestChanges(projectId, mrId).getChanges().stream().map(Diff::getDiff).collect(Collectors.toUnmodifiableList());
            return String.join("\n", changes);
            
        } catch (GitLabApiException e) {
            logger.error("获取GitLab MR diff失败: projectId={}, mrId={}", config.getProjectId(), mergeRequestId, e);
            return null;
        } catch (Exception e) {
            logger.error("获取GitLab MR diff时发生异常: projectId={}, mrId={}", config.getProjectId(), mergeRequestId, e);
            return null;
        }
    }
    
    /**
     * 添加Merge Request评论
     */
    public boolean addMergeRequestComment(RepositoryConfig config, String mergeRequestId, String comment) {
        try {
            GitLabApi gitLabApi = getGitLabApi(config);
            Integer projectId = Integer.parseInt(config.getProjectId());
            Long mrId = Long.parseLong(mergeRequestId);
            
            // 使用MergeRequestApi添加评论
            gitLabApi.getNotesApi().createMergeRequestNote(projectId,mrId,comment,new Date(), true);
            logger.info("成功添加GitLab MR评论: projectId={}, mrId={}", projectId, mrId);
            return true;
            
        } catch (GitLabApiException e) {
            logger.error("添加GitLab MR评论失败: projectId={}, mrId={}", config.getProjectId(), mergeRequestId, e);
            return false;
        } catch (Exception e) {
            logger.error("添加GitLab MR评论时发生异常: projectId={}, mrId={}", config.getProjectId(), mergeRequestId, e);
            return false;
        }
    }
    
    /**
     * 测试GitLab连接
     */
    public boolean testConnection(RepositoryConfig config) {
        try {
            GitLabApi gitLabApi = getGitLabApi(config);
            // 尝试获取当前用户信息来测试连接
            gitLabApi.getUserApi().getCurrentUser();
            logger.info("GitLab连接测试成功: {}", config.getRepositoryUrl());
            return true;
        } catch (Exception e) {
            logger.error("GitLab连接测试失败: {}", config.getRepositoryUrl(), e);
            return false;
        }
    }
    
    /**
     * 获取项目信息
     */
    public org.gitlab4j.api.models.Project getProject(RepositoryConfig config) {
        try {
            GitLabApi gitLabApi = getGitLabApi(config);
            Integer projectId = Integer.parseInt(config.getProjectId());
            return gitLabApi.getProjectApi().getProject(projectId);
        } catch (Exception e) {
            logger.error("获取GitLab项目信息失败: projectId={}", config.getProjectId(), e);
            return null;
        }
    }
} 
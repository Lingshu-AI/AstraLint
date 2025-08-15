package org.linshuai.astralint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.linshuai.astralint.dto.CodeReviewRequest;
import org.linshuai.astralint.entity.RepositoryConfig;
import org.linshuai.astralint.service.*;
import org.linshuai.astralint.util.WebhookSecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Webhook控制器 - 处理代码仓库的Webhook事件
 */
@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Autowired
    private AiCodeReviewService aiCodeReviewService;

    @Autowired
    private RepositoryConfigService repositoryConfigService;

    @Autowired
    private GitLabService gitLabService;

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private GiteeService giteeService;

    @Autowired
    private WebhookSecurityUtils webhookSecurityUtils;

    @Value("${gitlab.webhook.secret:}")
    private String gitlabWebhookSecret;

    @Value("${github.webhook.secret:}")
    private String githubWebhookSecret;

    @Value("${gitee.webhook.secret:}")
    private String giteeWebhookSecret;

    /**
     * GitLab Webhook处理
     */
    @PostMapping("/gitlab")
    public ResponseEntity<String> handleGitLabWebhook(
            @RequestHeader(value = "X-Gitlab-Event", required = false) String event,
            @RequestHeader(value = "X-Gitlab-Token", required = false) String token,
            @RequestBody Map<String, Object> payload) {

        logger.info("收到GitLab Webhook事件: {}", event);

        try {
            // 验证Webhook签名
            if (!validateGitLabWebhook(token, payload)) {
                logger.warn("GitLab Webhook签名验证失败");
                return ResponseEntity.badRequest().body("签名验证失败");
            }

            // 处理不同类型的GitLab事件
            switch (event) {
                case "Merge Request Hook":
                    return handleGitLabMergeRequest(payload);
                case "Push Hook":
                    return handleGitLabPush(payload);
                default:
                    logger.info("未处理的GitLab事件类型: {}", event);
                    return ResponseEntity.ok("事件已接收");
            }

        } catch (Exception e) {
            logger.error("处理GitLab Webhook失败", e);
            return ResponseEntity.internalServerError().body("处理失败");
        }
    }

    /**
     * GitHub Webhook处理
     */
    @PostMapping("/github")
    public ResponseEntity<String> handleGitHubWebhook(
            @RequestHeader(value = "X-GitHub-Event", required = false) String event,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
            @RequestBody String payload) {

        logger.info("收到GitHub Webhook事件: {}", event);

        try {
            // 验证Webhook签名
            if (!validateGitHubWebhook(signature, payload)) {
                logger.warn("GitHub Webhook签名验证失败");
                return ResponseEntity.badRequest().body("签名验证失败");
            }

            // 解析payload
            Map<String, Object> payloadMap = parseJsonPayload(payload);

            // 处理不同类型的GitHub事件
            switch (event) {
                case "pull_request":
                    return handleGitHubPullRequest(payloadMap);
                case "push":
                    return handleGitHubPush(payloadMap);
                default:
                    logger.info("未处理的GitHub事件类型: {}", event);
                    return ResponseEntity.ok("事件已接收");
            }

        } catch (Exception e) {
            logger.error("处理GitHub Webhook失败", e);
            return ResponseEntity.internalServerError().body("处理失败");
        }
    }

    /**
     * Gitee Webhook处理
     */
    @PostMapping("/gitee")
    public ResponseEntity<String> handleGiteeWebhook(
            @RequestHeader(value = "X-Gitee-Event", required = false) String event,
            @RequestHeader(value = "X-Gitee-Token", required = false) String token,
            @RequestBody Map<String, Object> payload) {

        logger.info("收到Gitee Webhook事件: {}", event);

        try {
            // 验证Webhook签名
            if (!validateGiteeWebhook(token, payload)) {
                logger.warn("Gitee Webhook签名验证失败");
                return ResponseEntity.badRequest().body("签名验证失败");
            }

            // 处理不同类型的Gitee事件
            switch (event) {
                case "Merge Request Hook":
                    return handleGiteeMergeRequest(payload);
                case "Push Hook":
                    return handleGiteePush(payload);
                default:
                    logger.info("未处理的Gitee事件类型: {}", event);
                    return ResponseEntity.ok("事件已接收");
            }

        } catch (Exception e) {
            logger.error("处理Gitee Webhook失败", e);
            return ResponseEntity.internalServerError().body("处理失败");
        }
    }

    /**
     * 处理GitLab Merge Request事件
     */
    private ResponseEntity<String> handleGitLabMergeRequest(Map<String, Object> payload) {
        try {
            Map<String, Object> objectAttributes = (Map<String, Object>) payload.get("object_attributes");
            Map<String, Object> project = (Map<String, Object>) payload.get("project");

            if (objectAttributes == null || project == null) {
                return ResponseEntity.badRequest().body("无效的payload格式");
            }

            String projectId = String.valueOf(project.get("id"));
            String mergeRequestId = String.valueOf(objectAttributes.get("iid"));
            String action = String.valueOf(objectAttributes.get("action"));

            // 只处理opened和updated的MR
            if (!"opened".equals(action) && !"updated".equals(action)) {
                return ResponseEntity.ok("跳过非opened/updated事件");
            }

            // 获取仓库配置 - 通过项目ID查找对应的仓库配置
            // 这里需要根据实际情况调整查找逻辑
            List<RepositoryConfig> allRepos = repositoryConfigService.getAllRepositoryConfigs();
            RepositoryConfig repoConfig = allRepos.stream()
                    .filter(repo -> repo.getProjectId() != null && repo.getProjectId().equals(projectId))
                    .findFirst()
                    .orElse(null);

            if (repoConfig == null || !repoConfig.getIsActive() || !repoConfig.getAutoReviewEnabled()) {
                logger.info("项目 {} 未配置自动审查或已禁用", projectId);
                return ResponseEntity.ok("项目未启用自动审查");
            }

            // 获取MR的diff内容
            String diffContent = gitLabService.getMergeRequestDiff(repoConfig, mergeRequestId);

            if (diffContent == null || diffContent.trim().isEmpty()) {
                return ResponseEntity.ok("无代码变更");
            }

            // 创建代码审查请求
            CodeReviewRequest reviewRequest = new CodeReviewRequest();
            reviewRequest.setProjectId(projectId);
            reviewRequest.setMergeRequestId(mergeRequestId);
            reviewRequest.setDiffContent(diffContent);
            reviewRequest.setReviewType(CodeReviewRequest.ReviewType.COMPREHENSIVE);

            // 异步执行代码审查
            CompletableFuture.runAsync(() -> {
                try {
                    String reviewResult = aiCodeReviewService
                            .generateComprehensiveReview(reviewRequest.getDiffContent());
                    // 将审查结果添加到MR评论中
                    gitLabService.addMergeRequestComment(repoConfig, reviewRequest.getMergeRequestId(), reviewResult);
                    logger.info("GitLab MR {} 代码审查完成", reviewRequest.getMergeRequestId());
                } catch (Exception e) {
                    logger.error("GitLab MR {} 代码审查失败", reviewRequest.getMergeRequestId(), e);
                }
            });

            return ResponseEntity.ok("代码审查已启动");

        } catch (Exception e) {
            logger.error("处理GitLab Merge Request失败", e);
            return ResponseEntity.internalServerError().body("处理失败");
        }
    }

    /**
     * 处理GitLab Push事件
     */
    private ResponseEntity<String> handleGitLabPush(Map<String, Object> payload) {
        // 实现Push事件的代码审查逻辑
        logger.info("处理GitLab Push事件");
        return ResponseEntity.ok("Push事件已接收");
    }

    /**
     * 处理GitHub Pull Request事件
     */
    private ResponseEntity<String> handleGitHubPullRequest(Map<String, Object> payload) {
        try {
            Map<String, Object> pullRequest = (Map<String, Object>) payload.get("pull_request");
            Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
            String action = String.valueOf(payload.get("action"));

            if (pullRequest == null || repository == null) {
                return ResponseEntity.badRequest().body("无效的payload格式");
            }

            // 只处理opened和synchronize的PR
            if (!"opened".equals(action) && !"synchronize".equals(action)) {
                return ResponseEntity.ok("跳过非opened/synchronize事件");
            }

            String repoName = String.valueOf(repository.get("full_name"));
            String prNumber = String.valueOf(pullRequest.get("number"));

            // 获取仓库配置
            RepositoryConfig repoConfig = repositoryConfigService.getRepositoryConfigByName(repoName)
                    .orElse(null);

            if (repoConfig == null || !repoConfig.getIsActive() || !repoConfig.getAutoReviewEnabled()) {
                logger.info("仓库 {} 未配置自动审查或已禁用", repoName);
                return ResponseEntity.ok("仓库未启用自动审查");
            }

            // 获取PR的diff内容
            String diffContent = gitHubService.getPullRequestDiff(repoConfig, prNumber);

            if (diffContent == null || diffContent.trim().isEmpty()) {
                return ResponseEntity.ok("无代码变更");
            }

            // 创建代码审查请求
            CodeReviewRequest reviewRequest = new CodeReviewRequest();
            reviewRequest.setProjectId(repoName);
            reviewRequest.setMergeRequestId(prNumber);
            reviewRequest.setDiffContent(diffContent);
            reviewRequest.setReviewType(CodeReviewRequest.ReviewType.COMPREHENSIVE);

            // 异步执行代码审查
            CompletableFuture.runAsync(() -> {
                try {
                    String reviewResult = aiCodeReviewService
                            .generateComprehensiveReview(reviewRequest.getDiffContent());
                    // 将审查结果添加到PR评论中
                    gitHubService.addPullRequestComment(repoConfig, reviewRequest.getMergeRequestId(), reviewResult);
                    logger.info("GitHub PR {} 代码审查完成", reviewRequest.getMergeRequestId());
                } catch (Exception e) {
                    logger.error("GitHub PR {} 代码审查失败", reviewRequest.getMergeRequestId(), e);
                }
            });

            return ResponseEntity.ok("代码审查已启动");

        } catch (Exception e) {
            logger.error("处理GitHub Pull Request失败", e);
            return ResponseEntity.internalServerError().body("处理失败");
        }
    }

    /**
     * 处理GitHub Push事件
     */
    private ResponseEntity<String> handleGitHubPush(Map<String, Object> payload) {
        // 实现Push事件的代码审查逻辑
        logger.info("处理GitHub Push事件");
        return ResponseEntity.ok("Push事件已接收");
    }

    /**
     * 处理Gitee Merge Request事件
     */
    private ResponseEntity<String> handleGiteeMergeRequest(Map<String, Object> payload) {
        try {
            Map<String, Object> pullRequest = (Map<String, Object>) payload.get("pull_request");
            Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
            String action = String.valueOf(payload.get("action"));

            if (pullRequest == null || repository == null) {
                return ResponseEntity.badRequest().body("无效的payload格式");
            }

            // 只处理opened和updated的PR
            if (!"opened".equals(action) && !"updated".equals(action)) {
                return ResponseEntity.ok("跳过非opened/updated事件");
            }

            String repoName = String.valueOf(repository.get("full_name"));
            String prNumber = String.valueOf(pullRequest.get("number"));

            // 获取仓库配置
            RepositoryConfig repoConfig = repositoryConfigService.getRepositoryConfigByName(repoName)
                    .orElse(null);

            if (repoConfig == null || !repoConfig.getIsActive() || !repoConfig.getAutoReviewEnabled()) {
                logger.info("仓库 {} 未配置自动审查或已禁用", repoName);
                return ResponseEntity.ok("仓库未启用自动审查");
            }

            // 获取PR的diff内容
            String diffContent = giteeService.getPullRequestDiff(repoConfig, prNumber);

            if (diffContent == null || diffContent.trim().isEmpty()) {
                return ResponseEntity.ok("无代码变更");
            }

            // 创建代码审查请求
            CodeReviewRequest reviewRequest = new CodeReviewRequest();
            reviewRequest.setProjectId(repoName);
            reviewRequest.setMergeRequestId(prNumber);
            reviewRequest.setDiffContent(diffContent);
            reviewRequest.setReviewType(CodeReviewRequest.ReviewType.COMPREHENSIVE);

            // 异步执行代码审查
            CompletableFuture.runAsync(() -> {
                try {
                    String reviewResult = aiCodeReviewService
                            .generateComprehensiveReview(reviewRequest.getDiffContent());
                    // 将审查结果添加到PR评论中
                    giteeService.addPullRequestComment(repoConfig, reviewRequest.getMergeRequestId(), reviewResult);
                    logger.info("Gitee PR {} 代码审查完成", reviewRequest.getMergeRequestId());
                } catch (Exception e) {
                    logger.error("Gitee PR {} 代码审查失败", reviewRequest.getMergeRequestId(), e);
                }
            });

            return ResponseEntity.ok("代码审查已启动");

        } catch (Exception e) {
            logger.error("处理Gitee Merge Request失败", e);
            return ResponseEntity.internalServerError().body("处理失败");
        }
    }

    /**
     * 处理Gitee Push事件
     */
    private ResponseEntity<String> handleGiteePush(Map<String, Object> payload) {
        // 实现Gitee Push的代码审查逻辑
        logger.info("处理Gitee Push事件");
        return ResponseEntity.ok("Gitee Push事件已接收");
    }

    /**
     * 验证GitLab Webhook签名
     */
    private boolean validateGitLabWebhook(String token, Map<String, Object> payload) {
        if (gitlabWebhookSecret == null || gitlabWebhookSecret.isEmpty()) {
            logger.warn("GitLab Webhook secret未配置，跳过验证");
            return true; // 如果没有配置secret，则跳过验证（开发环境）
        }

        return webhookSecurityUtils.verifyGitLabToken(token, gitlabWebhookSecret);
    }

    /**
     * 验证GitHub Webhook签名
     */
    private boolean validateGitHubWebhook(String signature, String payload) {
        if (githubWebhookSecret == null || githubWebhookSecret.isEmpty()) {
            logger.warn("GitHub Webhook secret未配置，跳过验证");
            return true; // 如果没有配置secret，则跳过验证（开发环境）
        }

        return webhookSecurityUtils.verifyGitHubSignature(payload, signature, githubWebhookSecret);
    }

    /**
     * 验证Gitee Webhook签名
     */
    private boolean validateGiteeWebhook(String token, Map<String, Object> payload) {
        if (giteeWebhookSecret == null || giteeWebhookSecret.isEmpty()) {
            logger.warn("Gitee Webhook secret未配置，跳过验证");
            return true; // 如果没有配置secret，则跳过验证（开发环境）
        }

        // Gitee可以使用token验证或者签名验证
        return webhookSecurityUtils.verifyGitLabToken(token, giteeWebhookSecret);
    }

    /**
     * 解析JSON payload
     */
    private Map<String, Object> parseJsonPayload(String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(payload, Map.class);
        } catch (Exception e) {
            logger.error("解析JSON payload失败", e);
            return Map.of();
        }
    }

    /**
     * Webhook健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", System.currentTimeMillis(),
                "webhooks", Map.of(
                        "gitlab", "/api/webhook/gitlab",
                        "github", "/api/webhook/github",
                        "gitee", "/api/webhook/gitee")));
    }
}
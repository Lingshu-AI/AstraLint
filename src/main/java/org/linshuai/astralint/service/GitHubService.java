package org.linshuai.astralint.service;

import org.linshuai.astralint.entity.RepositoryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * GitHub服务类
 */
@Service
public class GitHubService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 获取GitHub API请求头
     */
    private HttpHeaders getGitHubHeaders(RepositoryConfig config) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + config.getAccessToken());
        headers.set("Accept", "application/vnd.github.v3+json");
        return headers;
    }

    /**
     * 获取Pull Request的diff内容
     */
    public String getPullRequestDiff(RepositoryConfig config, String prNumber) {
        try {
            String apiUrl = String.format("https://api.github.com/repos/%s/pulls/%s",
                    config.getRepositoryName(), prNumber);

            HttpHeaders headers = getGitHubHeaders(config);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> pr = response.getBody();
                if (pr != null) {
                    String diffUrl = (String) pr.get("diff_url");

                    if (diffUrl != null) {
                        // 获取diff内容
                        ResponseEntity<String> diffResponse = restTemplate.exchange(
                                diffUrl, HttpMethod.GET, entity, String.class);

                        if (diffResponse.getStatusCode().is2xxSuccessful() && diffResponse.getBody() != null) {
                            return diffResponse.getBody();
                        }
                    }
                }
            }

            logger.warn("获取GitHub PR diff失败: repo={}, pr={}", config.getRepositoryName(), prNumber);
            return null;

        } catch (Exception e) {
            logger.error("获取GitHub PR diff时发生异常: repo={}, pr={}", config.getRepositoryName(), prNumber, e);
            return null;
        }
    }

    /**
     * 添加Pull Request评论
     */
    public boolean addPullRequestComment(RepositoryConfig config, String prNumber, String comment) {
        try {
            String apiUrl = String.format("https://api.github.com/repos/%s/issues/%s/comments",
                    config.getRepositoryName(), prNumber);

            HttpHeaders headers = getGitHubHeaders(config);
            Map<String, String> requestBody = Map.of("body", comment);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("成功添加GitHub PR评论: repo={}, pr={}", config.getRepositoryName(), prNumber);
                return true;
            } else {
                logger.error("添加GitHub PR评论失败: repo={}, pr={}, status={}",
                        config.getRepositoryName(), prNumber, response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            logger.error("添加GitHub PR评论时发生异常: repo={}, pr={}", config.getRepositoryName(), prNumber, e);
            return false;
        }
    }

    /**
     * 测试GitHub连接
     */
    public boolean testConnection(RepositoryConfig config) {
        try {
            String apiUrl = "https://api.github.com/user";

            HttpHeaders headers = getGitHubHeaders(config);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("GitHub连接测试成功: {}", config.getRepositoryName());
                return true;
            } else {
                logger.error("GitHub连接测试失败: repo={}, status={}",
                        config.getRepositoryName(), response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            logger.error("GitHub连接测试失败: {}", config.getRepositoryName(), e);
            return false;
        }
    }

    /**
     * 获取仓库信息
     */
    public Map<String, Object> getRepository(RepositoryConfig config) {
        try {
            String apiUrl = String.format("https://api.github.com/repos/%s", config.getRepositoryName());

            HttpHeaders headers = getGitHubHeaders(config);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                logger.error("获取GitHub仓库信息失败: repo={}, status={}",
                        config.getRepositoryName(), response.getStatusCode());
                return null;
            }

        } catch (Exception e) {
            logger.error("获取GitHub仓库信息时发生异常: {}", config.getRepositoryName(), e);
            return null;
        }
    }
}
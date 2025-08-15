package org.linshuai.astralint.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

@Service
public class CodeReviewService {

    private static final Logger logger = LoggerFactory.getLogger(CodeReviewService.class);

    @Value("${gitlab.url}")
    private String gitlabUrl;

    @Value("${gitlab.token}")
    private String gitlabToken;

    @Autowired
    private AiCodeReviewService aiCodeReviewService;

    private ObjectMapper objectMapper;

    public void init() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 处理推送事件
     */
    public void handlePushEvent(Map<String, Object> payload) {
        try {
            logger.info("Processing push event");

            // 这里可以实现推送事件的处理逻辑
            // 例如：分析新提交的代码，进行代码审查等

        } catch (Exception e) {
            logger.error("Error handling push event", e);
        }
    }

    /**
     * 处理合并请求事件
     */
    public void handleMergeRequestEvent(Map<String, Object> payload) {
        try {
            logger.info("Processing merge request event");

            // 获取合并请求的详细信息
            Integer projectId = (Integer) ((Map<String, Object>) payload.get("project")).get("id");
            Map<String, Object> mrObject = (Map<String, Object>) payload.get("merge_request");
            Integer mrIid = (Integer) mrObject.get("iid");
            String state = (String) mrObject.get("state");

            // 如果是打开或更新合并请求，则进行代码审查
            if ("opened".equals(state) || "updated".equals(state)) {
                performCodeReview(projectId, mrIid);
            }

        } catch (Exception e) {
            logger.error("Error handling merge request event", e);
        }
    }

    /**
     * 执行代码审查
     */
    private void performCodeReview(Integer projectId, Integer mrIid) {
        try {
            // 获取合并请求的差异
            String diffs = getMergeRequestDiffs(projectId, mrIid);

            // 使用AI分析代码差异
            String reviewComments = aiCodeReviewService.analyzeCodeDiffs(diffs);

            // 在实际应用中，我们会将审查结果作为评论添加到合并请求中
            logger.info("Code review for MR #{} completed:\n{}", mrIid, reviewComments);

        } catch (Exception e) {
            logger.error("Error performing code review for MR #" + mrIid, e);
        }
    }

    /**
     * 获取合并请求的差异
     */
    private String getMergeRequestDiffs(Integer projectId, Integer mrIid) {
        try {
            String url = gitlabUrl + "/api/v4/projects/" + projectId + "/merge_requests/" + mrIid + "/changes";
            HttpHeaders headers = new HttpHeaders();
            headers.set("PRIVATE-TOKEN", gitlabToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();
        } catch (Exception e) {
            logger.error("Error fetching merge request diffs for project " + projectId + " MR #" + mrIid, e);
            return "";
        }
    }

}
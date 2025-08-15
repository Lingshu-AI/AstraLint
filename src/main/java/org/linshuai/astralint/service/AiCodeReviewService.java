package org.linshuai.astralint.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Service
public class AiCodeReviewService {

    private static final Logger logger = LoggerFactory.getLogger(AiCodeReviewService.class);

    @Autowired
    private AiModelService aiModelService;

    @Value("${ai.code-review.model:qwen-plus}")
    private String modelName;

    @Value("${ai.code-review.max-file-size:10000}")
    private int maxFileSize;

    /**
     * 使用AI分析代码差异并生成审查建议
     */
    public String analyzeCodeDiffs(String diffs) {
        try {
            StringBuilder comments = new StringBuilder();
            comments.append("## 🤖 智能代码审查结果\n\n");

            JsonNode rootNode = new ObjectMapper().readTree(diffs);
            JsonNode changesNode = rootNode.get("changes");

            if (changesNode != null && changesNode.isArray()) {
                for (JsonNode change : changesNode) {
                    String fileName = change.get("new_path").asText();
                    String diffContent = change.get("diff").asText();

                    comments.append("### 📁 文件: ").append(fileName).append("\n");

                    // 检查文件大小
                    if (diffContent.length() > maxFileSize) {
                        comments.append("⚠️ 文件过大，跳过AI分析\n\n");
                        continue;
                    }

                    // 使用AI分析代码
                    String aiAnalysis = aiModelService.analyzeCode(diffContent, fileName);
                    comments.append(aiAnalysis).append("\n");

                    // 统计代码行数
                    String[] lines = diffContent.split("\n");
                    long addedLines = Arrays.stream(lines).filter(line -> line.startsWith("+")).count();
                    // removedLines 用于统计删除行数，虽然在当前逻辑中未使用，但保留用于后续可能的功能扩展

                    if (addedLines > 100) {
                        comments.append("- ⚠️ 此文件有大量新增代码(").append(addedLines).append("行)，建议拆分为更小的提交\n");
                    }

                    comments.append("\n---\n\n");
                }
            }

            return comments.toString();
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("Error parsing JSON in code diff analysis", e);
            return "JSON解析失败: " + e.getMessage();
        } catch (RuntimeException e) {
            logger.error("Error analyzing code diffs with AI", e);
            return "AI分析代码时发生错误: " + e.getMessage();
        }
    }

    /**
     * 异步分析代码差异
     */
    public CompletableFuture<String> analyzeCodeDiffsAsync(String diffs) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return analyzeCodeDiffs(diffs);
            } catch (Exception e) {
                logger.error("Error in async code diff analysis", e);
                return "异步代码分析失败: " + e.getMessage();
            }
        });
    }

    /**
     * 生成代码变更摘要
     */
    public String generateCodeSummary(String diffs) {
        try {
            StringBuilder summary = new StringBuilder();
            summary.append("## 📋 代码变更摘要\n\n");

            JsonNode rootNode = new ObjectMapper().readTree(diffs);
            JsonNode changesNode = rootNode.get("changes");

            if (changesNode != null && changesNode.isArray()) {
                for (JsonNode change : changesNode) {
                    String fileName = change.get("new_path").asText();
                    String diffContent = change.get("diff").asText();

                    String fileSummary = aiModelService.generateCodeSummary(diffContent, fileName);
                    summary.append("### ").append(fileName).append("\n");
                    summary.append(fileSummary).append("\n\n");
                }
            }

            return summary.toString();
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("Error parsing JSON in code summary generation", e);
            return "JSON解析失败: " + e.getMessage();
        } catch (RuntimeException e) {
            logger.error("Error generating code summary", e);
            return "生成代码摘要失败: " + e.getMessage();
        }
    }

    /**
     * 检查代码安全性
     */
    public String checkCodeSecurity(String diffs) {
        try {
            StringBuilder securityReport = new StringBuilder();
            securityReport.append("## 🔒 代码安全检查报告\n\n");

            JsonNode rootNode = new ObjectMapper().readTree(diffs);
            JsonNode changesNode = rootNode.get("changes");

            if (changesNode != null && changesNode.isArray()) {
                for (JsonNode change : changesNode) {
                    String fileName = change.get("new_path").asText();
                    String diffContent = change.get("diff").asText();

                    // 提取实际代码内容（去除diff标记）
                    String codeContent = extractCodeContent(diffContent);

                    String securityAnalysis = aiModelService.checkCodeSecurity(codeContent, fileName);
                    securityReport.append("### ").append(fileName).append("\n");
                    securityReport.append(securityAnalysis).append("\n\n");
                }
            }

            return securityReport.toString();
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("Error parsing JSON in security check", e);
            return "JSON解析失败: " + e.getMessage();
        } catch (RuntimeException e) {
            logger.error("Error checking code security", e);
            return "代码安全检查失败: " + e.getMessage();
        }
    }

    /**
     * 提供代码优化建议
     */
    public String provideOptimizationSuggestions(String diffs) {
        try {
            StringBuilder optimizationReport = new StringBuilder();
            optimizationReport.append("## ⚡ 代码优化建议\n\n");

            JsonNode rootNode = new ObjectMapper().readTree(diffs);
            JsonNode changesNode = rootNode.get("changes");

            if (changesNode != null && changesNode.isArray()) {
                for (JsonNode change : changesNode) {
                    String fileName = change.get("new_path").asText();
                    String diffContent = change.get("diff").asText();

                    // 提取实际代码内容
                    String codeContent = extractCodeContent(diffContent);

                    String optimizationSuggestions = aiModelService.provideOptimizationSuggestions(codeContent,
                            fileName);
                    optimizationReport.append("### ").append(fileName).append("\n");
                    optimizationReport.append(optimizationSuggestions).append("\n\n");
                }
            }

            return optimizationReport.toString();
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("Error parsing JSON in optimization suggestions", e);
            return "JSON解析失败: " + e.getMessage();
        } catch (RuntimeException e) {
            logger.error("Error providing optimization suggestions", e);
            return "生成优化建议失败: " + e.getMessage();
        }
    }

    /**
     * 从diff内容中提取实际代码
     */
    private String extractCodeContent(String diffContent) {
        StringBuilder codeContent = new StringBuilder();
        String[] lines = diffContent.split("\n");

        for (String line : lines) {
            if (line.startsWith("+") && !line.startsWith("+++")) {
                codeContent.append(line.substring(1)).append("\n");
            } else if (!line.startsWith("-") && !line.startsWith("@@") && !line.startsWith("index")) {
                codeContent.append(line).append("\n");
            }
        }

        return codeContent.toString();
    }

    /**
     * 综合代码审查报告
     */
    public String generateComprehensiveReview(String diffs) {
        try {
            StringBuilder comprehensiveReport = new StringBuilder();
            comprehensiveReport.append("# 🔍 综合代码审查报告\n\n");

            // 生成摘要
            comprehensiveReport.append(generateCodeSummary(diffs)).append("\n");

            // 安全检查
            comprehensiveReport.append(checkCodeSecurity(diffs)).append("\n");

            // 优化建议
            comprehensiveReport.append(provideOptimizationSuggestions(diffs)).append("\n");

            // 详细分析
            comprehensiveReport.append(analyzeCodeDiffs(diffs));

            return comprehensiveReport.toString();
        } catch (Exception e) {
            logger.error("Error generating comprehensive review", e);
            return "生成综合审查报告失败: " + e.getMessage();
        }
    }
}
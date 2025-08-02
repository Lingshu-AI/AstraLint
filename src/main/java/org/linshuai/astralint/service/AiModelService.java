package org.linshuai.astralint.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class AiModelService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiModelService.class);
    
    @Autowired
    private ChatClient chatClient;
    
    @Autowired
    @Qualifier("codeReviewPromptTemplate")
    private PromptTemplate codeReviewPromptTemplate;
    
    @Autowired
    @Qualifier("codeSummaryPromptTemplate")
    private PromptTemplate codeSummaryPromptTemplate;
    
    @Autowired
    @Qualifier("optimizationPromptTemplate")
    private PromptTemplate codeOptimizationPromptTemplate;
    
    @Value("${ai.code-review.timeout:30000}")
    private int timeout;
    
    @Value("${ai.code-review.enabled:true}")
    private boolean aiEnabled;
    
    /**
     * 使用AI分析代码变更
     */
    public String analyzeCode(String codeDiff, String fileName) {
        if (!aiEnabled) {
            return "AI代码审查功能已禁用";
        }
        
        try {
            Map<String, Object> parameters = Map.of(
                "fileName", fileName,
                "codeDiff", codeDiff
            );
            
            String prompt = codeReviewPromptTemplate.render(parameters);
            ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
            return response.getResult().getOutput().getText();
        } catch (Exception e) {
            logger.error("Error analyzing code with AI", e);
            return "AI分析失败: " + e.getMessage();
        }
    }
    
    /**
     * 异步分析代码变更
     */
    public CompletableFuture<String> analyzeCodeAsync(String codeDiff, String fileName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return analyzeCode(codeDiff, fileName);
            } catch (Exception e) {
                logger.error("Error in async code analysis", e);
                return "异步AI分析失败: " + e.getMessage();
            }
        }).orTimeout(timeout, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 生成代码变更摘要
     */
    public String generateCodeSummary(String codeDiff, String fileName) {
        if (!aiEnabled) {
            return "AI摘要功能已禁用";
        }
        
        try {
            Map<String, Object> parameters = Map.of(
                "fileName", fileName,
                "codeDiff", codeDiff
            );
            
            String prompt = codeSummaryPromptTemplate.render(parameters);
            ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
            return response.getResult().getOutput().getText();
        } catch (Exception e) {
            logger.error("Error generating code summary", e);
            return "生成代码摘要失败: " + e.getMessage();
        }
    }
    
    /**
     * 提供代码优化建议
     */
    public String provideOptimizationSuggestions(String codeContent, String fileName) {
        if (!aiEnabled) {
            return "AI优化建议功能已禁用";
        }
        
        try {
            Map<String, Object> parameters = Map.of(
                "fileName", fileName,
                "codeContent", codeContent
            );
            
            String prompt = codeOptimizationPromptTemplate.render(parameters);
            ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
            return response.getResult().getOutput().getText();
        } catch (Exception e) {
            logger.error("Error providing optimization suggestions", e);
            return "生成优化建议失败: " + e.getMessage();
        }
    }
    
    /**
     * 检查代码安全性
     */
    public String checkCodeSecurity(String codeContent, String fileName) {
        if (!aiEnabled) {
            return "AI安全检查功能已禁用";
        }
        
        try {
            String securityPrompt = String.format("""
                作为安全专家，请分析以下代码的潜在安全风险:
                
                文件: %s
                代码内容:
                %s
                
                请重点关注:
                1. SQL注入风险
                2. XSS攻击风险
                3. 权限控制问题
                4. 敏感信息泄露
                5. 输入验证不足
                
                请用中文回答，严重安全问题用"🚨"标记。
                """, fileName, codeContent);
            
            ChatResponse response = chatClient.prompt(securityPrompt).call().chatResponse();
            return response.getResult().getOutput().getText();
        } catch (Exception e) {
            logger.error("Error checking code security", e);
            return "安全检查失败: " + e.getMessage();
        }
    }
}
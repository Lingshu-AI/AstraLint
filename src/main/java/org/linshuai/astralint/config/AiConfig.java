package org.linshuai.astralint.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * AI配置类
 */
@Configuration
public class AiConfig {

    /**
     * 配置代码审查的PromptTemplate
     */
    @Bean
    public PromptTemplate codeReviewPromptTemplate() {
        return new PromptTemplate("""
            请对以下代码进行全面的代码审查，重点关注：
            1. 代码质量和可读性
            2. 潜在的安全漏洞
            3. 性能优化机会
            4. 最佳实践建议
            
            代码内容：
            {code}
            
            请提供详细的审查报告，包括问题描述、严重程度和改进建议。
            """);
    }

    /**
     * 配置代码摘要的PromptTemplate
     */
    @Bean
    public PromptTemplate codeSummaryPromptTemplate() {
        return new PromptTemplate("""
            请为以下代码生成简洁的摘要，包括：
            1. 主要功能描述
            2. 关键逻辑说明
            3. 重要变更点
            
            代码内容：
            {code}
            
            请提供清晰、准确的摘要。
            """);
    }

    /**
     * 配置优化建议的PromptTemplate
     */
    @Bean
    public PromptTemplate optimizationPromptTemplate() {
        return new PromptTemplate("""
            请分析以下代码的性能和优化机会，重点关注：
            1. 算法复杂度
            2. 内存使用
            3. 数据库查询优化
            4. 并发处理
            5. 缓存策略
            
            代码内容：
            {code}
            
            请提供具体的优化建议和实现方案。
            """);
    }
    /**
     * 配置ChatClient bean
     * 提供一个简单的ChatClient实现，避免Spring AI自动配置问题
     */
    @Bean
    @Primary
    public ChatClient researchAgent(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                        你是一个专业的代码审查和优化助手，具备以下能力：
                        1. 代码质量分析（可读性、结构、命名规范）
                        2. 安全漏洞检测（SQL注入、XSS、权限控制等）
                        3. 性能优化建议（算法、内存、数据库查询等）
                        4. 最佳实践指导（设计模式、架构原则等）
                        5. 技术债务识别和改进建议
                        
                        请以专业、准确、实用的方式提供代码分析和优化建议。
                        """)
                .build();
    }


}
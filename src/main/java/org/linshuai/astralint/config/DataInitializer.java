package org.linshuai.astralint.config;


import org.linshuai.astralint.entity.AiModelConfig;
import org.linshuai.astralint.entity.RepositoryConfig;
import org.linshuai.astralint.repository.AiModelConfigRepository;
import org.linshuai.astralint.repository.RepositoryConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private AiModelConfigRepository aiModelConfigRepository;
    
    @Autowired
    private RepositoryConfigRepository repositoryConfigRepository;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("开始初始化示例数据...");
        
        // 初始化AI模型配置
        initializeAiModels();
        
        // 初始化仓库配置
        initializeRepositories();
        
        logger.info("示例数据初始化完成");
    }
    
    private void initializeAiModels() {
        if (aiModelConfigRepository.count() == 0) {
            logger.info("创建示例AI模型配置...");
            
            // 阿里云通义千问模型
            AiModelConfig qwenModel = new AiModelConfig("qwen-plus", "ALIBABA");
            qwenModel.setApiKey("your_dashscope_api_key_here");
            qwenModel.setApiEndpoint("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation");
            qwenModel.setTemperature(0.7);
            qwenModel.setMaxTokens(4000);
            qwenModel.setTimeoutMs(30000);
            qwenModel.setDescription("阿里云通义千问Plus模型，支持代码审查和分析");
            qwenModel.setIsActive(true);
            qwenModel.setIsDefault(true);
            aiModelConfigRepository.save(qwenModel);
            
            // OpenAI GPT-4模型
            AiModelConfig gpt4Model = new AiModelConfig("gpt-4", "OPENAI");
            gpt4Model.setApiKey("your_openai_api_key_here");
            gpt4Model.setApiEndpoint("https://api.openai.com/v1/chat/completions");
            gpt4Model.setTemperature(0.7);
            gpt4Model.setMaxTokens(4000);
            gpt4Model.setTimeoutMs(30000);
            gpt4Model.setDescription("OpenAI GPT-4模型，强大的代码理解和分析能力");
            gpt4Model.setIsActive(true);
            gpt4Model.setIsDefault(false);
            aiModelConfigRepository.save(gpt4Model);
            
            // Anthropic Claude模型
            AiModelConfig claudeModel = new AiModelConfig("claude-3-sonnet", "ANTHROPIC");
            claudeModel.setApiKey("your_anthropic_api_key_here");
            claudeModel.setApiEndpoint("https://api.anthropic.com/v1/messages");
            claudeModel.setTemperature(0.7);
            claudeModel.setMaxTokens(4000);
            claudeModel.setTimeoutMs(30000);
            claudeModel.setDescription("Anthropic Claude 3 Sonnet模型，优秀的代码分析能力");
            claudeModel.setIsActive(false);
            claudeModel.setIsDefault(false);
            aiModelConfigRepository.save(claudeModel);
            
            logger.info("创建了 {} 个AI模型配置", aiModelConfigRepository.count());
        }
    }
    
    private void initializeRepositories() {
        if (repositoryConfigRepository.count() == 0) {
            logger.info("创建示例仓库配置...");
            
            // GitLab示例仓库
            RepositoryConfig gitlabRepo = new RepositoryConfig("示例GitLab项目", "https://gitlab.com/example/project", "GITLAB");
            gitlabRepo.setProjectId("123");
            gitlabRepo.setAccessToken("your_gitlab_token_here");
            gitlabRepo.setWebhookSecret("your_webhook_secret_here");
            gitlabRepo.setWebhookUrl("http://localhost:8080/api/webhook/gitlab");
            gitlabRepo.setReviewThreshold(100);
            gitlabRepo.setDescription("示例GitLab项目，用于演示代码审查功能");
            gitlabRepo.setIsActive(true);
            gitlabRepo.setAutoReviewEnabled(true);
            repositoryConfigRepository.save(gitlabRepo);
            
            // GitHub示例仓库
            RepositoryConfig githubRepo = new RepositoryConfig("示例GitHub项目", "https://github.com/example/project", "GITHUB");
            githubRepo.setProjectId("456");
            githubRepo.setAccessToken("your_github_token_here");
            githubRepo.setWebhookSecret("your_webhook_secret_here");
            githubRepo.setWebhookUrl("http://localhost:8080/api/webhook/github");
            githubRepo.setReviewThreshold(150);
            githubRepo.setDescription("示例GitHub项目，支持自动代码审查");
            githubRepo.setIsActive(true);
            githubRepo.setAutoReviewEnabled(true);
            repositoryConfigRepository.save(githubRepo);
            
            // Gitee示例仓库
            RepositoryConfig giteeRepo = new RepositoryConfig("示例Gitee项目", "https://gitee.com/example/project", "GITEE");
            giteeRepo.setProjectId("789");
            giteeRepo.setAccessToken("your_gitee_token_here");
            giteeRepo.setWebhookSecret("your_webhook_secret_here");
            giteeRepo.setWebhookUrl("http://localhost:8080/api/webhook/gitee");
            giteeRepo.setReviewThreshold(80);
            giteeRepo.setDescription("示例Gitee项目，国内代码托管平台");
            giteeRepo.setIsActive(false);
            giteeRepo.setAutoReviewEnabled(false);
            repositoryConfigRepository.save(giteeRepo);
            
            logger.info("创建了 {} 个仓库配置", repositoryConfigRepository.count());
        }
    }
} 
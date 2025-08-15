package org.linshuai.astralint.controller;

import org.linshuai.astralint.entity.AiModelConfig;
import org.linshuai.astralint.entity.RepositoryConfig;
import org.linshuai.astralint.service.AiModelConfigService;
import org.linshuai.astralint.service.RepositoryConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台管理控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AiModelConfigService aiModelConfigService;

    @Autowired
    private RepositoryConfigService repositoryConfigService;

    // ==================== AI模型配置管理 ====================

    /**
     * 获取所有AI模型配置
     */
    @GetMapping("/ai-models")
    public ResponseEntity<List<AiModelConfig>> getAllAiModels() {
        try {
            List<AiModelConfig> models = aiModelConfigService.getAllModelConfigs();
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            logger.error("获取AI模型配置失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取激活的AI模型配置
     */
    @GetMapping("/ai-models/active")
    public ResponseEntity<List<AiModelConfig>> getActiveAiModels() {
        try {
            List<AiModelConfig> models = aiModelConfigService.getActiveModelConfigs();
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            logger.error("获取激活AI模型配置失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 根据ID获取AI模型配置
     */
    @GetMapping("/ai-models/{id}")
    public ResponseEntity<AiModelConfig> getAiModelById(@PathVariable Long id) {
        try {
            return aiModelConfigService.getModelConfigById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("获取AI模型配置失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 创建AI模型配置
     */
    @PostMapping("/ai-models")
    public ResponseEntity<AiModelConfig> createAiModel(@RequestBody AiModelConfig config) {
        try {
            AiModelConfig created = aiModelConfigService.createModelConfig(config);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            logger.warn("创建AI模型配置失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("创建AI模型配置失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 更新AI模型配置
     */
    @PutMapping("/ai-models/{id}")
    public ResponseEntity<AiModelConfig> updateAiModel(@PathVariable Long id, @RequestBody AiModelConfig config) {
        try {
            AiModelConfig updated = aiModelConfigService.updateModelConfig(id, config);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            logger.warn("更新AI模型配置失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("更新AI模型配置失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 删除AI模型配置
     */
    @DeleteMapping("/ai-models/{id}")
    public ResponseEntity<Void> deleteAiModel(@PathVariable Long id) {
        try {
            aiModelConfigService.deleteModelConfig(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.warn("删除AI模型配置失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("删除AI模型配置失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 设置默认AI模型
     */
    @PostMapping("/ai-models/{id}/set-default")
    public ResponseEntity<AiModelConfig> setDefaultAiModel(@PathVariable Long id) {
        try {
            AiModelConfig defaultModel = aiModelConfigService.setDefaultModel(id);
            return ResponseEntity.ok(defaultModel);
        } catch (IllegalArgumentException e) {
            logger.warn("设置默认AI模型失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("设置默认AI模型失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 切换AI模型状态
     */
    @PostMapping("/ai-models/{id}/toggle-status")
    public ResponseEntity<AiModelConfig> toggleAiModelStatus(@PathVariable Long id) {
        try {
            AiModelConfig updated = aiModelConfigService.toggleModelStatus(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            logger.warn("切换AI模型状态失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("切换AI模型状态失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ==================== 仓库配置管理 ====================

    /**
     * 获取所有仓库配置
     */
    @GetMapping("/repositories")
    public ResponseEntity<List<RepositoryConfig>> getAllRepositories() {
        try {
            List<RepositoryConfig> repositories = repositoryConfigService.getAllRepositoryConfigs();
            return ResponseEntity.ok(repositories);
        } catch (Exception e) {
            logger.error("获取仓库配置失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取激活的仓库配置
     */
    @GetMapping("/repositories/active")
    public ResponseEntity<List<RepositoryConfig>> getActiveRepositories() {
        try {
            List<RepositoryConfig> repositories = repositoryConfigService.getActiveRepositoryConfigs();
            return ResponseEntity.ok(repositories);
        } catch (Exception e) {
            logger.error("获取激活仓库配置失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 根据ID获取仓库配置
     */
    @GetMapping("/repositories/{id}")
    public ResponseEntity<RepositoryConfig> getRepositoryById(@PathVariable Long id) {
        try {
            return repositoryConfigService.getRepositoryConfigById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("获取仓库配置失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 创建仓库配置
     */
    @PostMapping("/repositories")
    public ResponseEntity<RepositoryConfig> createRepository(@RequestBody RepositoryConfig config) {
        try {
            RepositoryConfig created = repositoryConfigService.createRepositoryConfig(config);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            logger.warn("创建仓库配置失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("创建仓库配置失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 更新仓库配置
     */
    @PutMapping("/repositories/{id}")
    public ResponseEntity<RepositoryConfig> updateRepository(@PathVariable Long id,
            @RequestBody RepositoryConfig config) {
        try {
            RepositoryConfig updated = repositoryConfigService.updateRepositoryConfig(id, config);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            logger.warn("更新仓库配置失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("更新仓库配置失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 删除仓库配置
     */
    @DeleteMapping("/repositories/{id}")
    public ResponseEntity<Void> deleteRepository(@PathVariable Long id) {
        try {
            repositoryConfigService.deleteRepositoryConfig(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.warn("删除仓库配置失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("删除仓库配置失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 切换仓库状态
     */
    @PostMapping("/repositories/{id}/toggle-status")
    public ResponseEntity<RepositoryConfig> toggleRepositoryStatus(@PathVariable Long id) {
        try {
            RepositoryConfig updated = repositoryConfigService.toggleRepositoryStatus(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            logger.warn("切换仓库状态失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("切换仓库状态失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 切换自动审查状态
     */
    @PostMapping("/repositories/{id}/toggle-auto-review")
    public ResponseEntity<RepositoryConfig> toggleAutoReview(@PathVariable Long id) {
        try {
            RepositoryConfig updated = repositoryConfigService.toggleAutoReview(id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            logger.warn("切换自动审查状态失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("切换自动审查状态失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 测试仓库连接
     */
    @PostMapping("/repositories/{id}/test-connection")
    public ResponseEntity<Map<String, Object>> testRepositoryConnection(@PathVariable Long id) {
        try {
            boolean isConnected = repositoryConfigService.testRepositoryConnection(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", isConnected);
            response.put("message", isConnected ? "连接成功" : "连接失败");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("测试仓库连接失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("测试仓库连接失败: id={}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ==================== 系统概览 ====================

    /**
     * 获取系统概览信息
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardInfo() {
        try {
            Map<String, Object> dashboard = new HashMap<>();

            // AI模型统计
            List<AiModelConfig> allModels = aiModelConfigService.getAllModelConfigs();
            List<AiModelConfig> activeModels = aiModelConfigService.getActiveModelConfigs();
            dashboard.put("totalAiModels", allModels.size());
            dashboard.put("activeModels", activeModels.size());
            dashboard.put("defaultModel", aiModelConfigService.getDefaultModelConfig().orElse(null));

            // 仓库统计
            List<RepositoryConfig> allRepositories = repositoryConfigService.getAllRepositoryConfigs();
            List<RepositoryConfig> activeRepositories = repositoryConfigService.getActiveRepositoryConfigs();
            List<RepositoryConfig> autoReviewRepositories = repositoryConfigService.getAutoReviewEnabledRepositories();
            dashboard.put("totalRepositories", allRepositories.size());
            dashboard.put("activeRepositories", activeRepositories.size());
            dashboard.put("autoReviewRepositories", autoReviewRepositories.size());

            // 添加默认值以确保前端可以正确显示
            dashboard.put("totalReviews", 0);
            dashboard.put("totalIssues", 0);

            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            logger.error("获取系统概览失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(health);
    }
}
package org.linshuai.astralint.service;


import org.linshuai.astralint.entity.AiModelConfig;
import org.linshuai.astralint.repository.AiModelConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AiModelConfigService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiModelConfigService.class);
    
    @Autowired
    private AiModelConfigRepository aiModelConfigRepository;
    
    /**
     * 创建AI模型配置
     */
    public AiModelConfig createModelConfig(AiModelConfig config) {
        logger.info("创建AI模型配置: {}", config.getModelName());
        
        // 检查模型名称是否已存在
        if (aiModelConfigRepository.existsByModelName(config.getModelName())) {
            throw new IllegalArgumentException("模型名称已存在: " + config.getModelName());
        }
        
        // 如果设置为默认模型，先取消其他默认模型
        if (config.getIsDefault()) {
            aiModelConfigRepository.findByIsDefaultTrue().ifPresent(existingDefault -> {
                existingDefault.setIsDefault(false);
                aiModelConfigRepository.save(existingDefault);
            });
        }
        
        return aiModelConfigRepository.save(config);
    }
    
    /**
     * 更新AI模型配置
     */
    public AiModelConfig updateModelConfig(Long id, AiModelConfig config) {
        logger.info("更新AI模型配置: id={}", id);
        
        Optional<AiModelConfig> existingConfig = aiModelConfigRepository.findById(id);
        if (existingConfig.isEmpty()) {
            throw new IllegalArgumentException("模型配置不存在: " + id);
        }
        
        AiModelConfig existing = existingConfig.get();
        
        // 检查模型名称是否被其他配置使用
        if (!existing.getModelName().equals(config.getModelName()) && 
            aiModelConfigRepository.existsByModelName(config.getModelName())) {
            throw new IllegalArgumentException("模型名称已存在: " + config.getModelName());
        }
        
        // 更新字段
        existing.setModelName(config.getModelName());
        existing.setModelProvider(config.getModelProvider());
        existing.setApiKey(config.getApiKey());
        existing.setApiEndpoint(config.getApiEndpoint());
        existing.setTemperature(config.getTemperature());
        existing.setMaxTokens(config.getMaxTokens());
        existing.setTimeoutMs(config.getTimeoutMs());
        existing.setIsActive(config.getIsActive());
        existing.setDescription(config.getDescription());
        
        // 处理默认模型设置
        if (config.getIsDefault() && !existing.getIsDefault()) {
            // 取消其他默认模型
            aiModelConfigRepository.findByIsDefaultTrue().ifPresent(otherDefault -> {
                otherDefault.setIsDefault(false);
                aiModelConfigRepository.save(otherDefault);
            });
            existing.setIsDefault(true);
        }
        
        return aiModelConfigRepository.save(existing);
    }
    
    /**
     * 删除AI模型配置
     */
    public void deleteModelConfig(Long id) {
        logger.info("删除AI模型配置: id={}", id);
        
        Optional<AiModelConfig> config = aiModelConfigRepository.findById(id);
        if (config.isEmpty()) {
            throw new IllegalArgumentException("模型配置不存在: " + id);
        }
        
        // 不允许删除默认模型
        if (config.get().getIsDefault()) {
            throw new IllegalArgumentException("不能删除默认模型配置");
        }
        
        aiModelConfigRepository.deleteById(id);
    }
    
    /**
     * 获取所有AI模型配置
     */
    @Transactional(readOnly = true)
    public List<AiModelConfig> getAllModelConfigs() {
        return aiModelConfigRepository.findAll();
    }
    
    /**
     * 获取激活的AI模型配置
     */
    @Transactional(readOnly = true)
    public List<AiModelConfig> getActiveModelConfigs() {
        return aiModelConfigRepository.findByIsActiveTrue();
    }
    
    /**
     * 根据ID获取AI模型配置
     */
    @Transactional(readOnly = true)
    public Optional<AiModelConfig> getModelConfigById(Long id) {
        return aiModelConfigRepository.findById(id);
    }
    
    /**
     * 根据模型名称获取配置
     */
    @Transactional(readOnly = true)
    public Optional<AiModelConfig> getModelConfigByName(String modelName) {
        return aiModelConfigRepository.findByModelName(modelName);
    }
    
    /**
     * 获取默认模型配置
     */
    @Transactional(readOnly = true)
    public Optional<AiModelConfig> getDefaultModelConfig() {
        return aiModelConfigRepository.findByIsDefaultTrue();
    }
    
    /**
     * 设置默认模型
     */
    public AiModelConfig setDefaultModel(Long id) {
        logger.info("设置默认模型: id={}", id);
        
        Optional<AiModelConfig> config = aiModelConfigRepository.findById(id);
        if (config.isEmpty()) {
            throw new IllegalArgumentException("模型配置不存在: " + id);
        }
        
        // 取消当前默认模型
        aiModelConfigRepository.findByIsDefaultTrue().ifPresent(existingDefault -> {
            existingDefault.setIsDefault(false);
            aiModelConfigRepository.save(existingDefault);
        });
        
        // 设置新的默认模型
        AiModelConfig newDefault = config.get();
        newDefault.setIsDefault(true);
        return aiModelConfigRepository.save(newDefault);
    }
    
    /**
     * 激活/停用模型配置
     */
    public AiModelConfig toggleModelStatus(Long id) {
        logger.info("切换模型状态: id={}", id);
        
        Optional<AiModelConfig> config = aiModelConfigRepository.findById(id);
        if (config.isEmpty()) {
            throw new IllegalArgumentException("模型配置不存在: " + id);
        }
        
        AiModelConfig modelConfig = config.get();
        modelConfig.setIsActive(!modelConfig.getIsActive());
        
        // 如果停用默认模型，需要设置其他模型为默认
        if (!modelConfig.getIsActive() && modelConfig.getIsDefault()) {
            List<AiModelConfig> activeConfigs = aiModelConfigRepository.findByIsActiveTrue();
            if (!activeConfigs.isEmpty()) {
                activeConfigs.get(0).setIsDefault(true);
                aiModelConfigRepository.save(activeConfigs.get(0));
            }
            modelConfig.setIsDefault(false);
        }
        
        return aiModelConfigRepository.save(modelConfig);
    }
    
    /**
     * 获取所有激活的模型名称
     */
    @Transactional(readOnly = true)
    public List<String> getAllActiveModelNames() {
        return aiModelConfigRepository.findAllActiveModelNames();
    }
} 
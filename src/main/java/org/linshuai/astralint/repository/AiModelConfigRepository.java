package org.linshuai.astralint.repository;

import org.linshuai.astralint.entity.AiModelConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiModelConfigRepository extends JpaRepository<AiModelConfig, Long> {
    
    /**
     * 根据模型名称查找配置
     */
    Optional<AiModelConfig> findByModelName(String modelName);
    
    /**
     * 查找所有激活的模型配置
     */
    List<AiModelConfig> findByIsActiveTrue();
    
    /**
     * 查找默认模型配置
     */
    Optional<AiModelConfig> findByIsDefaultTrue();
    
    /**
     * 根据提供商查找模型配置
     */
    List<AiModelConfig> findByModelProvider(String modelProvider);
    
    /**
     * 查找所有激活的模型名称
     */
    @Query("SELECT a.modelName FROM AiModelConfig a WHERE a.isActive = true")
    List<String> findAllActiveModelNames();
    
    /**
     * 检查模型名称是否存在
     */
    boolean existsByModelName(String modelName);
} 
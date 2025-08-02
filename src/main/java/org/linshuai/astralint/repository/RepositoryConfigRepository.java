package org.linshuai.astralint.repository;

import org.linshuai.astralint.entity.RepositoryConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryConfigRepository extends JpaRepository<RepositoryConfig, Long> {
    
    /**
     * 根据仓库名称查找配置
     */
    Optional<RepositoryConfig> findByRepositoryName(String repositoryName);
    
    /**
     * 查找所有激活的仓库配置
     */
    List<RepositoryConfig> findByIsActiveTrue();
    
    /**
     * 根据仓库类型查找配置
     */
    List<RepositoryConfig> findByRepositoryType(String repositoryType);
    
    /**
     * 查找启用自动审查的仓库
     */
    List<RepositoryConfig> findByAutoReviewEnabledTrueAndIsActiveTrue();
    
    /**
     * 查找所有激活的仓库名称
     */
    @Query("SELECT r.repositoryName FROM RepositoryConfig r WHERE r.isActive = true")
    List<String> findAllActiveRepositoryNames();
    
    /**
     * 检查仓库名称是否存在
     */
    boolean existsByRepositoryName(String repositoryName);
} 
package com.isxcode.acorn.modules.work.repository;

import com.isxcode.acorn.modules.work.entity.WorkConfigEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 只负责数据库查询逻辑.
 */
@Repository
@CacheConfig(cacheNames = {"sy_work_config"})
public interface WorkConfigRepository extends JpaRepository<WorkConfigEntity, String> {
}

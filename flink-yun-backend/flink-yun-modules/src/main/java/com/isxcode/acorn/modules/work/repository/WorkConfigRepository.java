package com.isxcode.acorn.modules.work.repository;

import com.isxcode.acorn.api.main.constants.ModuleCode;
import com.isxcode.acorn.modules.work.entity.WorkConfigEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = {ModuleCode.WORK})
public interface WorkConfigRepository extends JpaRepository<WorkConfigEntity, String> {
}

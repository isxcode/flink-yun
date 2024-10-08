package com.isxcode.acorn.modules.workflow.repository;

import com.isxcode.acorn.api.main.constants.ModuleCode;
import com.isxcode.acorn.modules.workflow.entity.WorkflowVersionEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = {ModuleCode.WORKFLOW})
public interface WorkflowVersionRepository extends JpaRepository<WorkflowVersionEntity, String> {
}

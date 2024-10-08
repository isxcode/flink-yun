package com.isxcode.acorn.modules.meta.repository;

import com.isxcode.acorn.api.main.constants.ModuleVipCode;
import com.isxcode.acorn.modules.meta.entity.MetaTableId;
import com.isxcode.acorn.modules.meta.entity.MetaTableInfoEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = {ModuleVipCode.VIP_META})
public interface MetaTableInfoRepository extends JpaRepository<MetaTableInfoEntity, MetaTableId> {

    Optional<MetaTableInfoEntity> findByDatasourceIdAndTableName(String datasourceId, String tableName);
}

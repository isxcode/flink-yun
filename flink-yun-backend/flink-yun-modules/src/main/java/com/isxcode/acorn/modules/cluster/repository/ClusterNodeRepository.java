package com.isxcode.acorn.modules.cluster.repository;

import com.isxcode.acorn.api.main.constants.ModuleCode;
import com.isxcode.acorn.modules.cluster.entity.ClusterNodeEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = {ModuleCode.CLUSTER_NODE})
public interface ClusterNodeRepository extends JpaRepository<ClusterNodeEntity, String> {

    List<ClusterNodeEntity> findAllByClusterId(String clusterId);

    List<ClusterNodeEntity> findAllByClusterIdAndStatus(String clusterId, String status);

    List<ClusterNodeEntity> findAllByStatus(String status);

    @Query("SELECT E FROM ClusterNodeEntity E" + " WHERE E.clusterId = :engineId  " + "and ( E.name LIKE %:keyword% "
        + "OR E.remark LIKE %:keyword% " + "OR E.host LIKE %:keyword%) order by E.createDateTime desc ")
    Page<ClusterNodeEntity> searchAll(@Param("keyword") String searchKeyWord, @Param("engineId") String engineId,
        Pageable pageable);

    Optional<ClusterNodeEntity> findByIdAndClusterId(String id, String clusterId);
}

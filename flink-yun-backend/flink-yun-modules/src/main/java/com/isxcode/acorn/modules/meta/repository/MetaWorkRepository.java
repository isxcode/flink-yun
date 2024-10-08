package com.isxcode.acorn.modules.meta.repository;

import com.isxcode.acorn.api.main.constants.ModuleVipCode;
import com.isxcode.acorn.modules.meta.entity.MetaWorkEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
@CacheConfig(cacheNames = {ModuleVipCode.VIP_META})
public interface MetaWorkRepository extends JpaRepository<MetaWorkEntity, String> {

    @Query("SELECT M FROM MetaWorkEntity M WHERE  M.name LIKE %:keyword% OR M.remark LIKE %:keyword% order by M.createDateTime desc")
    Page<MetaWorkEntity> searchAll(@Param("keyword") String searchKeyWord, Pageable pageable);

    boolean existsByName(String name);
}

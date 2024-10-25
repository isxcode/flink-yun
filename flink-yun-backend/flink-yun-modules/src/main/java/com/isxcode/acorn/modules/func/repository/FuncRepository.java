package com.isxcode.acorn.modules.func.repository;

import com.isxcode.acorn.api.main.constants.ModuleCode;
import com.isxcode.acorn.modules.func.entity.FuncEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = {ModuleCode.FUNC})
public interface FuncRepository extends JpaRepository<FuncEntity, String> {

    @Query("SELECT F FROM FuncEntity F WHERE (F.funcName LIKE %:searchKeyWord% OR F.className LIKE %:searchKeyWord%) order by F.createDateTime desc")
    Page<FuncEntity> pageSearch(@Param("searchKeyWord") String searchKeyWord, Pageable pageable);

    Optional<FuncEntity> findByFuncName(String funcName);
}

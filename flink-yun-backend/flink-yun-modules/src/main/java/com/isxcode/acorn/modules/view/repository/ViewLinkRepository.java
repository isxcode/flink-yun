package com.isxcode.acorn.modules.view.repository;

import com.isxcode.acorn.modules.view.entity.ViewLinkEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@CacheConfig(cacheNames = {"FY_VIEW"})
public interface ViewLinkRepository extends JpaRepository<ViewLinkEntity, String> {

    List<ViewLinkEntity> findAllByInvalidDateTimeBefore(LocalDateTime localDateTime);
}

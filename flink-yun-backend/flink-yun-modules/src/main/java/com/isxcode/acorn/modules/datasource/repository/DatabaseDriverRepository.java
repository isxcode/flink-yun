package com.isxcode.acorn.modules.datasource.repository;

import com.isxcode.acorn.modules.datasource.entity.DatabaseDriverEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = {"SY_DATABASE_DRIVER"})
public interface DatabaseDriverRepository extends JpaRepository<DatabaseDriverEntity, String> {

	@Query("select D from DatabaseDriverEntity  D where (D.name like %:keyword% or D.remark like %:keyword% or D.dbType like %:keyword% ) and (D.tenantId = :tenantId or D.driverType = 'SYSTEM_DRIVER') order by D.createDateTime desc,D.name desc ")
	Page<DatabaseDriverEntity> searchAll(@Param("keyword") String searchKeyWord, @Param("tenantId") String tenantId,
			Pageable pageable);

	List<DatabaseDriverEntity> findAllByDbType(String dbType);

	Optional<DatabaseDriverEntity> findByDriverTypeAndDbTypeAndIsDefaultDriver(String driverType, String dbType,
			boolean defaultDriver);
}
package com.isxcode.acorn.modules.tenant.service;

import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import com.isxcode.acorn.modules.tenant.entity.TenantEntity;
import com.isxcode.acorn.modules.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantEntity getTenant(String tenantId) {

        return tenantRepository.findById(tenantId).orElseThrow(() -> new IsxAppException("404", "租户不存在"));
    }
}

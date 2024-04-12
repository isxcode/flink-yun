package com.isxcode.acorn.modules.func.service;

import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import com.isxcode.acorn.modules.func.entity.FuncEntity;
import com.isxcode.acorn.modules.func.repository.FuncRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FuncService {

    private final FuncRepository funcRepository;

    public FuncEntity getFunc(String funcId) {

        return funcRepository.findById(funcId).orElseThrow(() -> new IsxAppException("自定义函数不存在"));
    }
}

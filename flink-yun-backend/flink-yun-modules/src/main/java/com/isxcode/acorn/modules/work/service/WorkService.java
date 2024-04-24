package com.isxcode.acorn.modules.work.service;

import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import com.isxcode.acorn.modules.work.entity.WorkEntity;
import com.isxcode.acorn.modules.work.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkService {

    private final WorkRepository workRepository;

    public WorkEntity getWorkEntity(String workId) {

        return workRepository.findById(workId).orElseThrow(() -> new IsxAppException("作业不存在"));
    }

    public void checkWork(String workId) {

        workRepository.findById(workId).orElseThrow(() -> new IsxAppException("作业不存在"));
    }
}

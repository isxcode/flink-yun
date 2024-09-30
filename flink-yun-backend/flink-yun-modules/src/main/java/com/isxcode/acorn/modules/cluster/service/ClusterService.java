package com.isxcode.acorn.modules.cluster.service;

import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import com.isxcode.acorn.modules.cluster.entity.ClusterEntity;
import com.isxcode.acorn.modules.cluster.repository.ClusterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClusterService {

    private final ClusterRepository clusterRepository;

    public ClusterEntity getCluster(String clusterId) {

        return clusterRepository.findById(clusterId).orElseThrow(() -> new IsxAppException("计算引擎不存在"));
    }

    public String getClusterName(String clusterId) {

        ClusterEntity clusterEntity = clusterRepository.findById(clusterId).orElse(null);
        return clusterEntity == null ? clusterId : clusterEntity.getName();
    }

    public String fixWindowsChar(String path, String command) {

        System.out.println(System.getProperty("os.name"));

        return System.getProperty("os.name").contains("Windows") ? "sed -i 's/\\r//g' " + path + " && " + command
            : command;
    }
}

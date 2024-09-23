package com.isxcode.acorn.modules.work.service;

import com.alibaba.fastjson.JSON;
import com.isxcode.acorn.api.work.constants.ResourceLevel;
import com.isxcode.acorn.api.work.constants.SetMode;
import com.isxcode.acorn.api.work.constants.WorkType;
import com.isxcode.acorn.api.work.pojos.dto.ClusterConfig;
import com.isxcode.acorn.api.work.pojos.dto.CronConfig;
import com.isxcode.acorn.api.work.pojos.dto.SyncRule;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import com.isxcode.acorn.modules.datasource.entity.DatasourceEntity;
import com.isxcode.acorn.modules.datasource.service.DatasourceService;
import com.isxcode.acorn.modules.work.entity.WorkConfigEntity;
import com.isxcode.acorn.modules.work.repository.WorkConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WorkConfigService {

    private final WorkConfigRepository workConfigRepository;

    private final DatasourceService datasourceService;

    public WorkConfigEntity getWorkConfigEntity(String workConfigId) {

        Optional<WorkConfigEntity> workConfigEntityOptional = workConfigRepository.findById(workConfigId);
        if (!workConfigEntityOptional.isPresent()) {
            throw new IsxAppException("作业异常，请联系开发者");
        }
        return workConfigEntityOptional.get();
    }

    public void initWorkScript(WorkConfigEntity workConfig, String workType) {

        switch (workType) {
            case WorkType.FLINK_SQL:
            case WorkType.QUERY_JDBC_SQL:
            case WorkType.EXECUTE_JDBC_SQL:
            case WorkType.FLINK_CONTAINER_SQL:
                workConfig.setScript(datasourceService.genDefaultSql(workConfig.getDatasourceId()));
                break;
            case WorkType.BASH:
                workConfig.setScript("#!/bin/bash \n" + "\n" + "pwd");
                break;
            case WorkType.PYTHON:
                workConfig.setScript("print('hello world')");
                break;
            case WorkType.PRQL:
                workConfig.setScript("from table_name");
                break;
        }
    }

    public void initClusterConfig(WorkConfigEntity workConfig, String clusterId, String clusterNodeId,
        Boolean enableHive, String datasourceId) {

        Map<String, String> flinkConfig = initFlinkConfig(ResourceLevel.LOW);
        if (enableHive) {
            DatasourceEntity datasource = datasourceService.getDatasource(datasourceId);
            flinkConfig.put("hive.metastore.uris", datasource.getMetastoreUris());
        }

        workConfig.setClusterConfig(JSON.toJSONString(
            ClusterConfig.builder().setMode(SetMode.SIMPLE).clusterId(clusterId).clusterNodeId(clusterNodeId)
                .enableHive(enableHive).flinkConfig(flinkConfig).resourceLevel(ResourceLevel.LOW).build()));
    }

    public void initSyncRule(WorkConfigEntity workConfig) {
        workConfig.setSyncRule(
            JSON.toJSONString(SyncRule.builder().setMode(SetMode.SIMPLE).numConcurrency(1).numPartitions(1).build()));
    }

    public void initCronConfig(WorkConfigEntity workConfig) {
        workConfig.setCronConfig(
            JSON.toJSONString(CronConfig.builder().setMode(SetMode.SIMPLE).type("ALL").enable(false).build()));
    }

    public Map<String, String> initFlinkConfig(String resourceLevel) {

        Map<String, String> flinkConfig = new HashMap<>();
        switch (resourceLevel) {
            case ResourceLevel.HIGH:
                flinkConfig.put("flink.executor.instances", "10");
                flinkConfig.put("flink.executor.cores", "4");
                flinkConfig.put("flink.executor.memory", "4g");
                flinkConfig.put("flink.driver.memory", "2g");
                flinkConfig.put("flink.driver.cores", "1");
                flinkConfig.put("flink.cores.max", "10");
                break;
            case ResourceLevel.MEDIUM:
                flinkConfig.put("flink.executor.instances", "5");
                flinkConfig.put("flink.executor.cores", "2");
                flinkConfig.put("flink.executor.memory", "2g");
                flinkConfig.put("flink.driver.memory", "1g");
                flinkConfig.put("flink.driver.cores", "1");
                flinkConfig.put("flink.cores.max", "5");
                break;
            case ResourceLevel.LOW:
                flinkConfig.put("flink.executor.instances", "1");
                flinkConfig.put("flink.executor.cores", "1");
                flinkConfig.put("flink.executor.memory", "2g");
                flinkConfig.put("flink.driver.memory", "1g");
                flinkConfig.put("flink.driver.cores", "1");
                flinkConfig.put("flink.cores.max", "1");
                break;
        }

        flinkConfig.put("flink.driver.extraJavaOptions", "-Dfile.encoding=utf-8");
        flinkConfig.put("flink.executor.extraJavaOptions", "-Dfile.encoding=utf-8");
        flinkConfig.put("flink.sql.storeAssignmentPolicy", "LEGACY");
        flinkConfig.put("flink.sql.legacy.timeParserPolicy", "LEGACY");
        flinkConfig.put("flink.sql.autoBroadcastJoinThreshold", "-1");
        flinkConfig.put("flink.sql.parquet.writeLegacyFormat", "true");
        flinkConfig.put("flink.sql.parquet.enableVectorizedReader", "false");
        flinkConfig.put("flink.sql.parquet.int96RebaseModeInRead", "LEGACY");
        flinkConfig.put("flink.sql.parquet.int96RebaseModeInWrite", "LEGACY");
        flinkConfig.put("flink.sql.parquet.datetimeRebaseModeInRead", "LEGACY");
        flinkConfig.put("flink.sql.parquet.datetimeRebaseModeInWrite", "LEGACY");
        return flinkConfig;
    }
}

package com.isxcode.acorn.api.agent.pojos.req;

import java.util.List;
import java.util.Map;

import com.isxcode.acorn.api.datasource.pojos.dto.KafkaConfig;
import com.isxcode.acorn.api.func.pojos.dto.FuncInfo;
import com.isxcode.acorn.api.work.pojos.dto.ExcelSyncConfig;
import com.isxcode.acorn.api.work.pojos.dto.SyncRule;
import com.isxcode.acorn.api.work.pojos.dto.SyncWorkConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginReq {

    private String sql;

    private String database;

    private Integer limit;

    private String applicationId;

    private Map<String, String> flinkConfig;

    private SyncWorkConfig syncWorkConfig;

    private ExcelSyncConfig excelSyncConfig;

    private SyncRule syncRule;

    private List<FuncInfo> funcInfoList;

    private int containerPort;

    private KafkaConfig kafkaConfig;

    private String csvFilePath;

    private String csvFileName;

    private String agentType;

    private String[] args;
}

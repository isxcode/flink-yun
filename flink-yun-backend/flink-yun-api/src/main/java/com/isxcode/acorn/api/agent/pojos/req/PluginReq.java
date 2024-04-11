package com.isxcode.acorn.api.agent.pojos.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import com.isxcode.acorn.api.datasource.pojos.dto.KafkaConfig;
import com.isxcode.acorn.api.func.pojos.dto.FuncInfo;
import com.isxcode.acorn.api.work.pojos.dto.SyncRule;
import com.isxcode.acorn.api.work.pojos.dto.SyncWorkConfig;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginReq {

	private String flinkSql;

    private String sql;

	private String database;

	private Integer limit;

	private String applicationId;

	private Map<String, String> sparkConfig;

	private SyncWorkConfig syncWorkConfig;

	private SyncRule syncRule;

	private List<FuncInfo> funcInfoList;

	private int containerPort;

	private KafkaConfig kafkaConfig;
}

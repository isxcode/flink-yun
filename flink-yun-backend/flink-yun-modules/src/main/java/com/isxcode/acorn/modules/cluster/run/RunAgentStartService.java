package com.isxcode.acorn.modules.cluster.run;

import static com.isxcode.acorn.common.config.CommonConfig.TENANT_ID;
import static com.isxcode.acorn.common.config.CommonConfig.USER_ID;
import static com.isxcode.acorn.common.utils.ssh.SshUtils.executeCommand;
import static com.isxcode.acorn.common.utils.ssh.SshUtils.scpFile;

import com.alibaba.fastjson.JSON;
import com.isxcode.acorn.api.cluster.constants.ClusterNodeStatus;
import com.isxcode.acorn.api.cluster.dto.AgentInfo;
import com.isxcode.acorn.api.cluster.dto.ScpFileEngineNodeDto;
import com.isxcode.acorn.api.main.properties.FlinkYunProperties;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import com.isxcode.acorn.common.utils.os.OsUtils;
import com.isxcode.acorn.modules.cluster.entity.ClusterNodeEntity;
import com.isxcode.acorn.modules.cluster.repository.ClusterNodeRepository;
import com.isxcode.acorn.modules.cluster.service.ClusterService;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(noRollbackFor = {IsxAppException.class})
public class RunAgentStartService {

    private final FlinkYunProperties flinkYunProperties;

    private final ClusterNodeRepository clusterNodeRepository;

    private final ClusterService clusterService;

    @Async("flinkYunWorkThreadPool")
    public void run(String clusterNodeId, ScpFileEngineNodeDto scpFileEngineNodeDto, String tenantId, String userId) {

        USER_ID.set(userId);
        TENANT_ID.set(tenantId);

        // 获取节点信息
        Optional<ClusterNodeEntity> clusterNodeEntityOptional = clusterNodeRepository.findById(clusterNodeId);
        if (!clusterNodeEntityOptional.isPresent()) {
            return;
        }
        ClusterNodeEntity clusterNodeEntity = clusterNodeEntityOptional.get();

        try {
            startAgent(scpFileEngineNodeDto, clusterNodeEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            clusterNodeEntity.setCheckDateTime(LocalDateTime.now());
            clusterNodeEntity.setAgentLog(e.getMessage());
            clusterNodeEntity.setStatus(ClusterNodeStatus.CHECK_ERROR);
            clusterNodeRepository.saveAndFlush(clusterNodeEntity);
        }
    }

    public void startAgent(ScpFileEngineNodeDto scpFileEngineNodeDto, ClusterNodeEntity engineNode)
        throws JSchException, IOException, InterruptedException, SftpException {

        String bashFilePath = flinkYunProperties.getTmpDir() + "/agent-start.sh";

        // 拷贝检测脚本
        scpFile(scpFileEngineNodeDto, "classpath:bash/agent-start.sh", bashFilePath);

        // 运行启动脚本
        String startCommand = "bash " + bashFilePath + " --home-path=" + engineNode.getAgentHomePath()
            + " --agent-port=" + engineNode.getAgentPort();

        if (engineNode.getInstallFlinkLocal() != null) {
            startCommand = startCommand + " --flink-local=" + engineNode.getInstallFlinkLocal();
        }

        log.debug("执行远程命令:{}", startCommand);

        // 获取返回结果
        String executeLog =
            executeCommand(scpFileEngineNodeDto, OsUtils.fixWindowsChar(bashFilePath, startCommand), false);
        log.debug("远程返回值:{}", executeLog);

        AgentInfo agentStartInfo = JSON.parseObject(executeLog, AgentInfo.class);

        // 修改状态
        engineNode.setStatus(agentStartInfo.getStatus());
        engineNode.setAgentLog(agentStartInfo.getLog());
        engineNode.setCheckDateTime(LocalDateTime.now());
        clusterNodeRepository.saveAndFlush(engineNode);

        // 刷新集群信息
        clusterService.checkCluster(engineNode.getClusterId());
    }
}

package com.isxcode.acorn.modules.cluster.run;

import static com.isxcode.acorn.common.config.CommonConfig.TENANT_ID;
import static com.isxcode.acorn.common.config.CommonConfig.USER_ID;
import static com.isxcode.acorn.common.utils.ssh.SshUtils.executeCommand;
import static com.isxcode.acorn.common.utils.ssh.SshUtils.scpFile;

import com.alibaba.fastjson.JSON;
import com.isxcode.acorn.api.cluster.constants.ClusterNodeStatus;
import com.isxcode.acorn.api.cluster.pojos.dto.AgentInfo;
import com.isxcode.acorn.api.cluster.pojos.dto.ScpFileEngineNodeDto;
import com.isxcode.acorn.api.main.properties.SparkYunProperties;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import com.isxcode.acorn.modules.cluster.entity.ClusterEntity;
import com.isxcode.acorn.modules.cluster.entity.ClusterNodeEntity;
import com.isxcode.acorn.modules.cluster.repository.ClusterNodeRepository;
import com.isxcode.acorn.modules.cluster.service.ClusterService;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.File;
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

    private final SparkYunProperties sparkYunProperties;

    private final ClusterNodeRepository clusterNodeRepository;

    private final ClusterService clusterService;

    @Async("sparkYunWorkThreadPool")
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
            log.error(e.getMessage());
            clusterNodeEntity.setCheckDateTime(LocalDateTime.now());
            clusterNodeEntity.setAgentLog(e.getMessage());
            clusterNodeEntity.setStatus(ClusterNodeStatus.CHECK_ERROR);
            clusterNodeRepository.saveAndFlush(clusterNodeEntity);
        }
    }

    public void startAgent(ScpFileEngineNodeDto scpFileEngineNodeDto, ClusterNodeEntity engineNode)
        throws JSchException, IOException, InterruptedException, SftpException {

        // 拷贝检测脚本
        scpFile(scpFileEngineNodeDto, "classpath:bash/agent-start.sh",
            sparkYunProperties.getTmpDir() + File.separator + "agent-start.sh");

        ClusterEntity cluster = clusterService.getCluster(engineNode.getClusterId());

        // 运行启动脚本
        String startCommand = "bash " + sparkYunProperties.getTmpDir() + File.separator + "agent-start.sh"
            + " --home-path=" + engineNode.getAgentHomePath() + " --agent-port=" + engineNode.getAgentPort()
            + " --agent-type=" + cluster.getClusterType().toLowerCase();
        log.debug("执行远程命令:{}", startCommand);

        // 获取返回结果
        String executeLog = executeCommand(scpFileEngineNodeDto, startCommand, false);
        log.debug("远程返回值:{}", executeLog);

        AgentInfo agentStartInfo = JSON.parseObject(executeLog, AgentInfo.class);

        // 修改状态
        engineNode.setStatus(agentStartInfo.getStatus());
        engineNode.setAgentLog(agentStartInfo.getLog());
        engineNode.setCheckDateTime(LocalDateTime.now());
        clusterNodeRepository.saveAndFlush(engineNode);
    }
}

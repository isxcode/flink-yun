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
import com.isxcode.acorn.modules.cluster.entity.ClusterNodeEntity;
import com.isxcode.acorn.modules.cluster.repository.ClusterNodeRepository;
import com.isxcode.acorn.modules.cluster.service.ClusterNodeService;
import com.jcraft.jsch.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RunAgentInstallService {

    private final ClusterNodeService clusterNodeService;
    private final SparkYunProperties sparkYunProperties;

    private final ClusterNodeRepository clusterNodeRepository;

    @Async("sparkYunWorkThreadPool")
    public void run(String clusterNodeId, String clusterType, ScpFileEngineNodeDto scpFileEngineNodeDto,
        String tenantId, String userId) {

        USER_ID.set(userId);
        TENANT_ID.set(tenantId);

        // 获取节点信息
        Optional<ClusterNodeEntity> clusterNodeEntityOptional = clusterNodeRepository.findById(clusterNodeId);
        if (!clusterNodeEntityOptional.isPresent()) {
            return;
        }
        ClusterNodeEntity clusterNodeEntity = clusterNodeEntityOptional.get();

        try {
            installAgent(scpFileEngineNodeDto, clusterNodeEntity, clusterType);
        } catch (Exception e) {
            log.error(e.getMessage());
            clusterNodeEntity.setCheckDateTime(LocalDateTime.now());
            clusterNodeEntity.setAgentLog(e.getMessage());
            clusterNodeEntity.setStatus(ClusterNodeStatus.UN_INSTALL);
            clusterNodeRepository.saveAndFlush(clusterNodeEntity);
        }
    }

    public void installAgent(ScpFileEngineNodeDto scpFileEngineNodeDto, ClusterNodeEntity engineNode,
        String clusterType) throws JSchException, IOException, InterruptedException, SftpException {

        // 先检查节点是否可以安装
        scpFile(scpFileEngineNodeDto, "classpath:bash/" + String.format("check-%s.sh", clusterType.toLowerCase()),
            sparkYunProperties.getTmpDir() + File.separator + String.format("check-%s.sh", clusterType.toLowerCase()));

        // 运行安装脚本
        String envCommand = "bash " + sparkYunProperties.getTmpDir() + File.separator
            + String.format("check-%s.sh", clusterType.toLowerCase()) + " --home-path=" + engineNode.getAgentHomePath()
            + " --agent-port=" + engineNode.getAgentPort();
        log.debug("执行远程命令:{}", envCommand);

        // 获取返回结果
        String executeLog = executeCommand(scpFileEngineNodeDto, envCommand, false);
        log.debug("远程返回值:{}", executeLog);

        AgentInfo agentEnvInfo = JSON.parseObject(executeLog, AgentInfo.class);

        // 如果不可以安装直接返回
        if (!ClusterNodeStatus.CAN_INSTALL.equals(agentEnvInfo.getStatus())) {
            engineNode.setStatus(agentEnvInfo.getStatus());
            engineNode.setAgentLog(agentEnvInfo.getLog());
            engineNode.setCheckDateTime(LocalDateTime.now());
            clusterNodeRepository.saveAndFlush(engineNode);
            return;
        }

        // 异步上传安装包
        clusterNodeService.scpAgentFile(scpFileEngineNodeDto, "classpath:agent/zhiliuyun-agent.tar.gz",
            sparkYunProperties.getTmpDir() + File.separator + "zhiliuyun-agent.tar.gz");
        log.debug("代理安装包上传中");

        // 同步监听进度
        clusterNodeService.checkScpPercent(scpFileEngineNodeDto, "classpath:agent/zhiliuyun-agent.tar.gz",
            sparkYunProperties.getTmpDir() + File.separator + "zhiliuyun-agent.tar.gz", engineNode);
        log.debug("下载安装包成功");

        // 拷贝安装脚本
        scpFile(scpFileEngineNodeDto, "classpath:bash/agent-install.sh",
            sparkYunProperties.getTmpDir() + File.separator + "agent-install.sh");
        log.debug("下载安装脚本成功");

        // 运行安装脚本
        String installCommand = "bash " + sparkYunProperties.getTmpDir() + File.separator + "agent-install.sh"
            + " --home-path=" + engineNode.getAgentHomePath() + " --agent-port=" + engineNode.getAgentPort()
            + " --agent-type=" + clusterType.toLowerCase();
        log.debug("执行远程安装命令:{}", installCommand);

        executeLog = executeCommand(scpFileEngineNodeDto, installCommand, false);
        log.debug("远程安装返回值:{}", executeLog);

        AgentInfo agentInstallInfo = JSON.parseObject(executeLog, AgentInfo.class);

        engineNode = clusterNodeService.getClusterNode(engineNode.getId());
        if ("ERROR".equals(agentInstallInfo.getExecStatus())) {
            engineNode.setStatus(agentInstallInfo.getExecStatus());
        } else {
            engineNode.setStatus(agentInstallInfo.getStatus());
        }
        engineNode.setAgentLog(engineNode.getAgentLog() + "\n" + agentInstallInfo.getLog());
        engineNode.setCheckDateTime(LocalDateTime.now());
        clusterNodeRepository.saveAndFlush(engineNode);
    }
}

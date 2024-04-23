#!/bin/bash

##############################
# 环境检测通过,开始执行安装
##############################

# 获取脚本文件当前路径
BASE_PATH=$(cd "$(dirname "$0")" || exit ; pwd)

# 初始化环境变量
if [[ "$OSTYPE" == "linux-gnu" ]]; then
    source /etc/profile
    source ~/.bashrc
elif [[ "$OSTYPE" == "darwin"* ]]; then
    source /etc/profile
    source ~/.zshrc
else
    json_output="{ \
                      \"status\": \"INSTALL_ERROR\", \
                      \"log\": \"该系统不支持安装\" \
                    }"
      echo $json_output
      rm /tmp/agent-install.sh
      exit 0
fi

# 获取外部参数
home_path=""
agent_port=""
agent_type=""
for arg in "$@"; do
  case "$arg" in
  --home-path=*) home_path="${arg#*=}" ;;
  --agent-port=*) agent_port="${arg#*=}" ;;
  --agent-type=*) agent_type="${arg#*=}" ;;
  *) echo "未知参数: $arg" && exit 1 ;;
  esac
done

# 初始化agent_path
agent_path="${home_path}/zhiliuyun-agent"

# 导入用户自己配置的环境变量
source ${agent_path}/conf/agent-env.sh

# 将文件解压到指定目录
tar -xf /tmp/zhiliuyun-agent.tar.gz -C ${home_path} > /dev/null
cp -r ${agent_path}/lib ${agent_path}/flink-min/lib

# 进入代理目录,防止logs文件夹生成错位
cd ${agent_path}

# 判断zhiliuyun-agent.log是否存在,不存在则新建
if [ ! -f "${agent_path}/logs/zhiliuyun-agent.log" ]; then
  mkdir "${agent_path}/logs"
  touch "${agent_path}/logs/zhiliuyun-agent.log"
fi

# 运行代理程序
if ! command -v java &>/dev/null; then
  nohup $JAVA_HOME/bin/java -jar -Xmx2048m ${agent_path}/lib/zhiliuyun-agent.jar --server.port=${agent_port} --spring.config.additional-location=${agent_path}/conf/ > /dev/null 2>&1 &
else
  nohup java -jar -Xmx2048m ${agent_path}/lib/zhiliuyun-agent.jar --server.port=${agent_port} --spring.config.additional-location=${agent_path}/conf/ > /dev/null 2>&1 &
fi
echo $! >${agent_path}/zhiliuyun-agent.pid

# 检查是否安装
if [ -e "${agent_path}/zhiliuyun-agent.pid" ]; then
  pid=$(cat "${agent_path}/zhiliuyun-agent.pid")
  sleep 10
  if ps -p $pid >/dev/null 2>&1; then
    json_output="{ \
              \"status\": \"RUNNING\",\
              \"log\": \"安装成功\" \
            }"
    echo $json_output
  else
    json_output="{ \
              \"status\": \"STOP\",\
              \"log\": \"已安装，请激活\" \
            }"
    echo $json_output
  fi
fi

# flinkcluster模型,自动安装flink
if [[ "$agent_type" == "flinkcluster" ]]; then
  # 如果端口已启动,则不安装flink
  if netstat -tln | awk '$4 ~ /:'8081'$/ {exit 1}'; then
    nohup bash flink-min/bin/start-cluster.sh > /dev/null 2>&1 &
  fi
fi

# 删除安装包 和 安装脚本
rm ${BASE_PATH}/zhiliuyun-agent.tar.gz && rm ${BASE_PATH}/agent-install.sh
#!/bin/bash

######################
# 安装脚本
######################

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
      rm ${BASE_PATH}/agent-install.sh
      exit 0
fi

BASE_PATH=$(cd "$(dirname "$0")" || exit ; pwd)

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

# 判断路径是否存在
if [ ! -d "${home_path}/zhiliuyun-agent" ]; then
  json_output="{ \
            \"status\": \"INSTALL_ERROR\",\
            \"log\": \"安装目录不存在"${home_path}/zhiliuyun-agent"\" \
            }"
  echo $json_output
  exit 0
fi

# 将文件解压到指定目录
tar -xf ${BASE_PATH}/zhiliuyun-agent.tar.gz -C ${home_path} > /dev/null

# 运行jar包
nohup java -jar -Xmx2048m ${home_path}/zhiliuyun-agent/lib/zhiliuyun-agent.jar --server.port=${agent_port} >>${home_path}/zhiliuyun-agent/logs/zhiliuyun-agent.log 2>&1 &
echo $! >${home_path}/zhiliuyun-agent/zhiliuyun-agent.pid

# 检查是否安装
if [ -e "${home_path}/zhiliuyun-agent/zhiliuyun-agent.pid" ]; then
  pid=$(cat "${home_path}/zhiliuyun-agent/zhiliuyun-agent.pid")
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

# 如果是集群模式,自动安装flink cluster
if [[ "$OSTYPE" == "flinkcluster" ]]; then
  bash ${home_path}/zhiliuyun-agent/flink-min/bin/start-cluster.sh
fi

# 删除安装包 和 安装脚本
rm ${BASE_PATH}/zhiliuyun-agent.tar.gz && rm ${BASE_PATH}/agent-install.sh
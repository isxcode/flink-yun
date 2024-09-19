#!/bin/bash

######################
# 单独卸载代理器
######################

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
      rm ${BASE_PATH}/agent-uninstall.sh
      exit 0
fi

# 获取外部参数
home_path=""
for arg in "$@"; do
  case "$arg" in
  --home-path=*) home_path="${arg#*=}" ;;
  *) echo "未知参数: $arg" && exit 1 ;;
  esac
done

# 初始化agent_path
agent_path="${home_path}/zhiliuyun-agent"

# 如果进程存在,杀死进程
if [ -e "${agent_path}/zhiliuyun-agent.pid" ]; then
  pid=$(cat "${agent_path}/zhiliuyun-agent.pid")
  if ps -p $pid >/dev/null 2>&1; then
    kill -9 ${pid}
  fi
fi

# 停止flink-local
nohup bash ${agent_path}/flink-min/bin/stop-cluster.sh > /dev/null 2>&1 &
sleep 5

# 删除安装目录
rm -rf ${agent_path}

# 返回结果
json_output="{ \
          \"status\": \"UN_INSTALL\",
          \"log\": \"卸载成功\"
        }"
echo $json_output

# 删除脚本
rm ${BASE_PATH}/agent-uninstall.sh
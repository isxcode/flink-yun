#!/bin/bash

######################
# 启动脚本
######################

# 获取当前路径
BASE_PATH=$(cd "$(dirname "$0")" || exit ; pwd)
cd "${BASE_PATH}" || exit
cd ".." || exit

# 执行agent-env.sh
source "conf/agent-env.sh"

# 如果存在则不启动
if [ -e "zhiliuyun-agent.pid" ]; then
  pid=$(cat "zhiliuyun-agent.pid")
  if ps -p $pid >/dev/null 2>&1; then
    echo "【至流云代理】: HAS RUNNING"
    exit 0
  fi
fi

# 判断zhiliuyun-agent.log是否存在,不存在则新建
if [ ! -f logs/zhiliuyun-agent.log ]; then
  mkdir logs
  touch logs/zhiliuyun-agent.log
fi

# 运行代理程序
if [ -n "$JAVA_HOME" ]; then
  nohup $JAVA_HOME/bin/java -jar -Xmx2048m lib/zhiliuyun-agent.jar --spring.config.additional-location=conf/ > /dev/null 2>&1 &
else
  nohup java -jar -Xmx2048m lib/zhiliuyun-agent.jar --spring.config.additional-location=conf/ > /dev/null 2>&1 &
fi
echo $! >zhiliuyun-agent.pid

echo "【至流云代理】: STARTING"
tail -f logs/zhiliuyun-agent.log
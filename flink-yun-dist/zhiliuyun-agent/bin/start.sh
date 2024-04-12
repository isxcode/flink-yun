#!/bin/bash

######################
# 启动脚本
######################

BASE_PATH=$(cd "$(dirname "$0")" || exit ; pwd)
cd "${BASE_PATH}" || exit
cd ".." || exit

if [ -e "zhiliuyun-agent.pid" ]; then
  pid=$(cat "zhiliuyun-agent.pid")
  if ps -p $pid >/dev/null 2>&1; then
    echo "【至流云代理】: RUNNING"
    exit 0
  fi
fi

# 运行jar包
nohup java -jar -Xmx2048m lib/zhiliuyun-agent.jar --spring.config.additional-location=conf/ >>logs/zhiliuyun-agent.log 2>&1 &
echo $! >zhiliuyun-agent.pid
echo "【至流云代理】: RUNNING"

tail -f logs/zhiliuyun-agent.log
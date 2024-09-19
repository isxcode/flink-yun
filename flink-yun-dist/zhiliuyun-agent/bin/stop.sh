#!/bin/bash

######################
# 停止脚本
######################

BASE_PATH=$(cd "$(dirname "$0")" || exit ; pwd)
cd "${BASE_PATH}" || exit
cd ".." || exit

if [ -e "zhiliuyun-agent.pid" ]; then
  pid=$(cat "zhiliuyun-agent.pid")
  if ps -p $pid >/dev/null 2>&1; then
   kill -9 ${pid}
   rm zhiliuyun-agent.pid
   echo "【至流云代理】: CLOSED"
   exit 0
  fi
fi

echo "【至流云代理】: HAS CLOSED"
exit 0
#!/bin/bash

# 获取当前路径
BASE_PATH=$(cd "$(dirname "$0")" || exit ; pwd)
cd "${BASE_PATH}" || exit
cd ".." || exit

# 项目启动后，关闭进程
if [ -e "zhiliuyun.pid" ]; then
  pid=$(cat "zhiliuyun.pid")
  if ps -p $pid >/dev/null 2>&1; then
   kill -9 ${pid}
   echo "【至流云】: CLOSED"
   exit 0
  fi
fi

echo "【至流云】: HAS CLOSED"
exit 0
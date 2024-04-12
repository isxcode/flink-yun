#!/bin/bash

# 获取当前路径
BASE_PATH=$(cd "$(dirname "$0")" || exit ; pwd)
cd "${BASE_PATH}" || exit
cd ".." || exit

# 项目已启动不执行
if [ -e "zhiliuyun.pid" ]; then
  pid=$(cat "zhiliuyun.pid")
  if ps -p $pid >/dev/null 2>&1; then
    echo "【至流云】: HAS RUNNING"
    exit 0
  fi
fi

# 判断flink-yun.log是否存在,不存在则新建
if [ ! -f logs/flink-yun.log ]; then
  touch logs/flink-yun.log
fi

# 启动项目
nohup java -jar -Xmx2048m lib/zhiliuyun.jar --spring.profiles.active=local --spring.config.additional-location=conf/ > /dev/null 2>&1 &
echo $! >zhiliuyun.pid
echo "【至流云】: RUNNING"
tail -f logs/flink-yun.log
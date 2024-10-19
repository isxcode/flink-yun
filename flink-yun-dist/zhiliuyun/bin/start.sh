#!/bin/bash

# 获取当前路径
BASE_PATH=$(cd "$(dirname "$0")" || exit ; pwd)
cd "${BASE_PATH}" || exit
cd ".." || exit

print_log="true"
for arg in "$@"; do
  case "$arg" in
    --print-log=*) print_log="${arg#*=}" ;;
    *) echo "未知参数: $arg" && exit 1 ;;
  esac
done

# 导入用户指定环境变量
source "conf/zhiliuyun-env.sh"

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
  mkdir logs
  touch logs/flink-yun.log
fi

# 运行至流云程序
if [ -n "$JAVA_HOME" ]; then
  nohup $JAVA_HOME/bin/java -jar -Xmx2048m lib/zhiliuyun.jar --spring.profiles.active=local --spring.config.additional-location=conf/ > /dev/null 2>&1 &
else
  nohup java -jar -Xmx2048m lib/zhiliuyun.jar --spring.profiles.active=local --spring.config.additional-location=conf/ > /dev/null 2>&1 &
fi
echo $! >zhiliuyun.pid

echo "【至流云】: RUNNING"
if [ "$print_log" == "true" ]; then
  tail -f logs/flink-yun.log
fi
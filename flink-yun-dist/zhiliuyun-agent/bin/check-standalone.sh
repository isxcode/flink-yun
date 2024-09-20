#!/bin/bash

######################
# 检测安装环境脚本
######################

# 判断tar解压命令
if ! command -v tar &>/dev/null; then
  echo "【结果】：未检测到tar命令"
  exit 0
fi

# 判断是否有java命令
if ! command -v java &>/dev/null; then
  echo "【结果】：未检测到java1.8.x环境"
  exit 0
fi

# 判断java版本是否为1.8
java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
if [[ "$java_version" != "1.8"* ]]; then
  echo "【结果】：未检测到java1.8.x环境"
  exit 0
fi

# 判断是否安装flink，判断是否配置FLINK_HOME
if [ -n "$FLINK_HOME" ]; then
  FLINK_PATH=$FLINK_HOME
else
  echo "【结果】：未配置FLINK_HOME环境变量"
  exit 0
fi

# 从$FLINK_HOME/conf/flink-defaults.conf 配置文件中，获取FLINK_MASTER_PORT和FLINK_MASTER_WEBUI_PORT
# flink.master          flink://isxcode:7077
# flink.master.web.url  http://isxcode:8081
config_file="$FLINK_PATH/conf/flink-defaults.conf"

if [ ! -f "$config_file" ]; then
  echo "【结果】：$FLINK_HOME/conf/flink-defaults.conf配置文件不存在"
  exit 0
fi

flink_master_web_url=$(grep -E "^flink.master\.web\.url[[:space:]]+" "$config_file" | awk '{print $2}')
flink_master=$(grep -E "^flink.master[[:space:]]+" "$config_file" | awk '{print $2}')

if [[ -z $flink_master_web_url ]]; then
  echo "【结果】：$FLINK_HOME/conf/flink-defaults.conf配置文件中未配置flink.master.web.url，例如：flink.master.web.url  http://localhost:8081"
  exit 0
fi

if [[ -z $flink_master ]]; then
 echo "【结果】：$FLINK_HOME/conf/flink-defaults.conf配置文件中未配置flink.master，例如：flink.master          flink://localhost:7077"
 exit 0
fi

# 判断是否可以访问FLINK_MASTER_PORT=7077
regex="^flink://([^:]+):([0-9]+)$"
if [[ $flink_master =~ $regex ]]; then
  host=${BASH_REMATCH[1]}
  port=${BASH_REMATCH[2]}
  if ! (echo >/dev/tcp/$host/$port) &>/dev/null; then
     echo "【结果】：无法访问flink.master.web.url的服务，请检查flink服务。"
     exit 0
  fi
else
  echo "【结果】：flink master url 填写格式异常"
  exit 0
fi

# 判断是否可以访问FLINK_MASTER_WEBUI_PORT=8081
regex="^(http|https)://([^:/]+):([0-9]+)"
if [[ $flink_master_web_url =~ $regex ]]; then
  protocol=${BASH_REMATCH[1]}
  host=${BASH_REMATCH[2]}
  port=${BASH_REMATCH[3]}
  if ! (echo >/dev/tcp/$host/$port) &>/dev/null; then
     echo "【结果】：无法访问 flink web ui，请检查flink服务。"
     exit 0
  fi
else
  echo "【结果】：flink web ui url 填写格式异常"
  exit 0
fi

echo "【结果】：允许启动"

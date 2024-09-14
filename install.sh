#!/bin/bash

echo "开始安装"

# 检查tar命令
if ! command -v tar &>/dev/null; then
  echo "【安装结果】：未检测到tar命令,请安装tar"
  exit 1
fi

# 检查java命令
if ! command -v java &>/dev/null; then
  echo "【安装结果】：未检测到java命令，请安装java"
  exit 1
fi

# 检查node命令
if ! command -v node &>/dev/null; then
  echo "【安装结果】：未检测到node命令，请安装nodejs"
  exit 1
fi

# 检查pnpm命令
if ! command -v pnpm &>/dev/null; then
   echo "【提示】：未检测到pnpm命令，已经执行命令: npm install pnpm@9.0.6 -g"
   npm install pnpm@9.0.6 -g
fi

# 进入项目目录
BASE_PATH=$(cd "$(dirname "$0")" || exit ; pwd)
cd "${BASE_PATH}" || exit

# 创建tmp目录
TMP_DIR="${BASE_PATH}"/resources/tmp
FLINK_MIN_FILE=flink-1.18.1-bin-scala_2.12.tgz
OSS_DOWNLOAD_URL=https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/install
FLINK_MIN_DOWNLOAD_URL="${OSS_DOWNLOAD_URL}"/"${FLINK_MIN_FILE}"
FLINK_MIN_DIR="${BASE_PATH}"/flink-yun-dist/flink-min

# 创建tmp目录
if [ ! -d "${TMP_DIR}" ]; then
    mkdir -p "${TMP_DIR}"
fi

# 下载flink二进制文件
if [ ! -f "${TMP_DIR}"/"${FLINK_MIN_FILE}" ]; then
    echo "flink-1.18.1开始下载，请耐心等待"
    curl -ssL "${FLINK_MIN_DOWNLOAD_URL}" -o "${TMP_DIR}"/"${FLINK_MIN_FILE}"
    if [ $? -eq 0 ]; then
        echo "fink-1.18.1下载成功"
    else
        echo "【安装结果】：fink-1.18.1下载失败"
        exit 1
    fi
fi

# 创建flink-min目录
if [ ! -d "${FLINK_MIN_DIR}" ]; then
    mkdir -p "${FLINK_MIN_DIR}"
fi

# 解压flink程序，并删除不需要的文件
if [ ! -f "${FLINK_MIN_DIR}"/README.txt ]; then
  tar vzxf "${TMP_DIR}"/"${FLINK_MIN_FILE}" --strip-components=1 -C "${FLINK_MIN_DIR}"
  rm -rf "${FLINK_MIN_DIR}"/examples
  rm -rf "${FLINK_MIN_DIR}"/licenses
  rm "${FLINK_MIN_DIR}"/LICENSE
  rm "${FLINK_MIN_DIR}"/NOTICE
  echo "flink解压成功"
fi

# 创建cdc文件夹
CDC_DIR="${BASE_PATH}"/resources/cdc

if [ ! -d "${CDC_DIR}" ]; then
    mkdir -p "${CDC_DIR}"
fi

# 下载mysql8驱动
if [ ! -f "${CDC_DIR}"/flink-connector-jdbc-3.1.2-1.18.jar ]; then
  curl -ssL https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/install/flink-connector-jdbc-3.1.2-1.18.jar -o ${CDC_DIR}/flink-connector-jdbc-3.1.2-1.18.jar
  echo "flink-connector-jdbc-3.1.2-1.18.jar下载成功"
fi

# 创建resources文件夹
JDBC_DIR="${BASE_PATH}"/resources/jdbc/system

if [ ! -d "${JDBC_DIR}" ]; then
    mkdir -p "${JDBC_DIR}"
fi

# 下载mysql8驱动
if [ ! -f "${JDBC_DIR}"/mysql-connector-j-8.1.0.jar ]; then
  curl -ssL https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/install/mysql-connector-j-8.1.0.jar -o ${JDBC_DIR}/mysql-connector-j-8.1.0.jar
  echo "mysql-connector-j-8.1.0.jar驱动下载成功"
fi


# 创建项目依赖libs文件夹
LIBS_DIR="${BASE_PATH}"/resources/libs

if [ ! -d "${LIBS_DIR}" ]; then
    mkdir -p "${LIBS_DIR}"
fi

# prql jar依赖文件
if [ ! -f "${LIBS_DIR}"/prql-java-0.5.2.jar ]; then
  curl -ssL https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiqingyun/install/prql-java-0.5.2.jar -o ${LIBS_DIR}/prql-java-0.5.2.jar
  echo "prql-java-0.5.2.jar下载成功"
fi

# prql 二进制文件(mac arm64)
if [ ! -f "${BASE_PATH}"/flink-yun-backend/flink-yun-main/src/main/resources/libprql_java-osx-arm64.dylib ]; then
  curl -ssL https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiqingyun/install/libprql_java-osx-arm64.dylib -o ${BASE_PATH}/flink-yun-backend/flink-yun-main/src/main/resources/libprql_java-osx-arm64.dylib
  echo "prql_java-osx-arm64.dylib下载成功"
fi

# prql 二进制文件(linux amd64)
if [ ! -f "${BASE_PATH}"/flink-yun-backend/flink-yun-main/src/main/resources/libprql_java-linux64.so ]; then
  curl -ssL https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiqingyun/install/libprql_java-linux64.so -o ${BASE_PATH}/flink-yun-backend/flink-yun-main/src/main/resources/libprql_java-linux64.so
  echo "prql_java-linux64.so下载成功"
fi

# 返回状态
echo "【安装结果】：安装成功"
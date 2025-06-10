# 至流云

### 超轻量级实时流分析平台/数据同步/实时同步

[![Docker Pulls](https://img.shields.io/docker/pulls/isxcode/zhiliuyun)](https://hub.docker.com/r/isxcode/zhiliuyun)
[![build](https://github.com/isxcode/flink-yun/actions/workflows/build-app.yml/badge.svg?branch=main)](https://github.com/isxcode/flink-yun/actions/workflows/build-app.yml)
[![GitHub Repo stars](https://img.shields.io/github/stars/isxcode/flink-yun)](https://github.com/isxcode/flink-yun)
[![GitHub forks](https://img.shields.io/github/forks/isxcode/flink-yun)](https://github.com/isxcode/flink-yun/fork)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fisxcode%2Fflink-yun.svg?type=shield&issueType=license)](https://app.fossa.com/projects/git%2Bgithub.com%2Fisxcode%2Fflink-yun?ref=badge_shield&issueType=license)
[![GitHub License](https://img.shields.io/github/license/isxcode/flink-yun)](https://github.com/isxcode/flink-yun/blob/main/LICENSE)

|             |                                                                                                                                                         |
|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| 官网地址:       | https://zhiliuyun.isxcode.com                                                                                                                           |
| 源码地址:       | https://github.com/isxcode/flink-yun                                                                                                                    |
| 演示环境:       | https://zhiliuyun-demo.isxcode.com                                                                                                                      |
| 安装包下载:      | https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/zhiliuyun.tar.gz                                                                                 |
| 许可证下载:      | https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/license.lic                                                                                      |
| Docker Hub: | https://hub.docker.com/r/isxcode/zhiliuyun                                                                                                              |
| 产品矩阵:       | [至轻云](https://zhiqingyun.isxcode.com), [至流云](https://zhiliuyun.isxcode.com), [至慧云](https://zhihuiyun.isxcode.com), [至数云](https://zhishuyun.isxcode.com) |
| 关键词:        | 流数据分析, 实时同步, 离线同步, 整库同步, 数据清洗, Flink, Hadoop, Kafka                                                                                                     |
|             |                                                                                                                                                         |

### 产品介绍

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;至流云是一款超轻量级、企业级实时流分析平台，基于Flink生态打造。一键部署，开箱即用。可快速实现数据实时同步、离线同步、整库同步、数据清洗、可视化任务调度等功能，为企业提供高效便捷的数据解决方案。

### 功能特点

- **轻量级产品**: 无需额外组件安装，一键部署，开箱即用。
- **云原生私有化**: 兼容云原生架构，支持多节点安装与高可用集群部署。
- **分布式实时计算**: 基于原生Flink分布式架构，高效执行数据流实时计算。

### 立即体验

> [!TIP]
> 演示地址：https://zhiliuyun-demo.isxcode.com </br>
> 体验账号：zhiyao </br>
> 账号密码：zhiyao123

### 快速部署

> [!NOTE]
> 访问地址：http://localhost:8080 <br/>
> 管理员账号：admin <br/>
> 管理员密码：admin123

```bash
docker run -p 8080:8080 -d isxcode/zhiliuyun
```

### 相关文档

- [快速入门](https://zhiliuyun.isxcode.com/zh/docs/zh/1/0)
- [产品手册](https://zhiliuyun.isxcode.com/zh/docs/zh/2/0)
- [开发手册](https://zhiliuyun.isxcode.com/zh/docs/zh/5/0)
- [博客](https://ispong.isxcode.com/tags/flink/)

### 源码构建

> [!IMPORTANT]
> 安装包路径: /tmp/flink-yun/flink-yun-dist/build/distributions/zhiliuyun.tar.gz

```bash
cd /tmp
git clone https://github.com/isxcode/flink-yun.git
docker run --rm \
  -v /tmp/flink-yun:/flink-yun \
  -w /flink-yun -it registry.cn-shanghai.aliyuncs.com/isxcode/zhiliuyun-build:amd-latest \
  /bin/bash -c "source /etc/profile && gradle install clean package"
```

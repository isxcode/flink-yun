# 至流云

### 超轻量级实时流分析平台/数据同步/实时同步

[![Docker Pulls](https://img.shields.io/docker/pulls/isxcode/zhiliuyun)](https://hub.docker.com/r/isxcode/zhiliuyun)
[![build](https://github.com/isxcode/flink-yun/actions/workflows/build-app.yml/badge.svg?branch=main)](https://github.com/isxcode/flink-yun/actions/workflows/build-app.yml)
[![GitHub Repo stars](https://img.shields.io/github/stars/isxcode/flink-yun)](https://github.com/isxcode/flink-yun)
[![GitHub forks](https://img.shields.io/github/forks/isxcode/flink-yun)](https://github.com/isxcode/flink-yun/fork)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fisxcode%2Fflink-yun.svg?type=shield&issueType=license)](https://app.fossa.com/projects/git%2Bgithub.com%2Fisxcode%2Fflink-yun?ref=badge_shield&issueType=license)
[![GitHub License](https://img.shields.io/github/license/isxcode/flink-yun)](https://github.com/isxcode/flink-yun/blob/main/LICENSE)

|           |                                                                                                                                                         |
|-----------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| 官网地址:     | https://zhiliuyun.isxcode.com                                                                                                                           |
| 源码地址:     | https://github.com/isxcode/flink-yun                                                                                                                    |
| 演示环境:     | https://zhiliuyun-demo.isxcode.com                                                                                                                      |
| 安装包下载:    | https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/zhiliuyun.tar.gz                                                                                 |
| 许可证下载:    | https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/license.lic                                                                                      |
| docker镜像: | https://hub.docker.com/r/isxcode/zhiliuyun                                                                                                              |
| 阿里云镜像:    | https://zhiliuyun.isxcode.com/zh/docs/zh/1/1-docker                                                                                                     |
| 产品矩阵:     | [至轻云](https://zhiqingyun.isxcode.com), [至流云](https://zhiliuyun.isxcode.com), [至慧云](https://zhihuiyun.isxcode.com), [至数云](https://zhishuyun.isxcode.com) |
| 关键词:      | 流数据分析平台, 数据同步, 实时同步, 数据采集, Flink, Hadoop, Docker                                                                                                        |
|           |                                                                                                                                                         |

### 产品介绍

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;至流云是一款超轻量级、企业级流式数据分析平台，围绕Flink生态打造的数据计算平台。一键部署，开箱即用。可快速实现数据实时同步、离线同步、Flink计算、可视化调度、流数据ETL、自定义接口等多种功能，为企业提供高效便捷的数据解决方案。

### 功能特点

- **轻量级产品**: 无需额外组件安装，一键部署，开箱即用。
- **云原生私有化**: 兼容云原生架构，支持多节点安装与高可用集群部署。
- **实时数据处理**: 基于原生Flink分布式架构，高效地执行数据实时同步计算。

### 立即体验

> [!TIP]
> 演示地址：https://zhiliuyun-demo.isxcode.com </br>
> 体验账号：user001 </br>
> 账号密码：welcome1

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
- [开发手册](https://zhiliuyun.isxcode.com/zh/docs/zh/6/0)
- [博客](https://ispong.isxcode.com/tags/flink/)

### 源码构建

> [!WARNING]
> 编译环境需访问外网，且需提前安装Nodejs和Java，推荐版本如下: </br>
> Java: zulu8.78.0.19-ca-jdk8.0.412-x64 </br>
> Nodejs: node-v18.20.3-x64

##### MacOS/Linux

> [!IMPORTANT]
> 安装包路径: flink-yun/flink-yun-dist/build/distributions/zhiliuyun.tar.gz

```bash
git clone https://github.com/isxcode/flink-yun.git
cd flink-yun
./gradlew install clean package
```

##### Windows10/11

> [!CAUTION]
> 请使用Git Bash终端工具执行以下命令

```bash
git clone https://github.com/isxcode/flink-yun.git
cd flink-yun
./gradlew.bat install clean package
```

### 收藏历史

[![Star History Chart](https://api.star-history.com/svg?repos=isxcode/flink-yun&type=Date)](https://www.star-history.com/#isxcode/flink-yun&Date)

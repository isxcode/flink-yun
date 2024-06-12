<div align="center">
  <img width="600" alt="image" src="https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/product-img/product.jpg">
</div>

---

<h1 align="center">
  至流云
</h1>

<h3 align="center">
  打造企业级流数据分析平台
</h3>

<div align="center">

[![Docker Pulls](https://img.shields.io/docker/pulls/isxcode/zhiliuyun)](https://hub.docker.com/r/isxcode/zhiliuyun)
[![GitHub release (with filter)](https://img.shields.io/github/v/release/isxcode/flink-yun)](https://github.com/isxcode/flink-yun/releases)
[![GitHub Repo stars](https://img.shields.io/github/stars/isxcode/flink-yun)](https://github.com/isxcode/flink-yun)
[![GitHub forks](https://img.shields.io/github/forks/isxcode/flink-yun)](https://github.com/isxcode/flink-yun/fork)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fisxcode%2Fflink-yun.svg?type=shield&issueType=license)](https://app.fossa.com/projects/git%2Bgithub.com%2Fisxcode%2Fflink-yun?ref=badge_shield&issueType=license)
[![GitHub License](https://img.shields.io/github/license/isxcode/flink-yun)](https://github.com/isxcode/flink-yun/blob/main/LICENSE)

</div>

### 产品介绍
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; [**至流云**](https://zhiliuyun.isxcode.com)是一款超轻量级、企业级流数据分析平台。具有一键部署，开箱即用特色。无需额外数据组件安装，即可快速实现流数据实时ETL、数据同步、复杂作业运行等场景。助力企业探索实时数据分析领域，获得更多商业价值。

### 功能特点

- **超轻量**: 无需预装任何中间组件，一键部署即可实现流数据计算。
- **多租户**: 支持创建多租户，实现租户间的数据完全隔离。
- **高可用**: 兼容云原生架构，支持多节点安装，确保高可用性。
- **私有化部署**: 支持内网部署与访问，提升系统安全性。
- **代码开源**: 永久开源且免费，实时公开产品进度，内容持续更新。

### 立即体验

&nbsp;&nbsp;&nbsp;&nbsp; 演示地址：https://zhiliuyun-demo.isxcode.com </br>
&nbsp;&nbsp;&nbsp;&nbsp; 体验账号：user001 </br>
&nbsp;&nbsp;&nbsp;&nbsp; 账号密码：welcome1

### 快速部署

```bash
docker run -p 8080:8080 -d isxcode/zhiliuyun
```

### 相关文档

- [快速入门](https://zhiliuyun.isxcode.com/docs/zh/0/0)
- [产品手册](https://zhiliuyun.isxcode.com/docs/zh/2/0)
- [部署文档](https://zhiliuyun.isxcode.com/docs/zh/1/0-docker)
- [博客](https://ispong.isxcode.com/tags/flink/)

### 源码构建

> 打包结果路径: flink-yun/flink-yun-dist/build/distributions/zhiliuyun.tar.gz

```bash
git clone https://github.com/isxcode/flink-yun.git
cd flink-yun
./gradlew install package
```

### 源码运行

```bash
./gradlew backend
```

### 联系我们

邮箱：hello@isxcode.com

---

**Thanks for free JetBrains Open Source license**

<a href="https://www.jetbrains.com/?from=flink-yun" target="_blank" style="border-bottom: none !important;">
    <img src="https://img.isxcode.com/index_img/jetbrains/jetbrains-3.png" height="100" alt="jetbrains"/>
</a>
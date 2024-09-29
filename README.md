<div align="center">
  <img width="600" alt="image" src="https://img.isxcode.com/picgo/20240929113526.png">
</div>

---

<h1 align="center">
  至流云
</h1>

<h3 align="center">
  超轻量级Flink计算平台
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
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; [**至流云**](https://zhiliuyun.isxcode.com)是一款超轻量级、企业级流式计算产品，围绕Flink生态打造的数据计算平台。一键部署，开箱即用。可快速实现Flink计算、作业调度、数据ETL、数据同步、实时计算等场景。

### 功能特点

- **超轻量级**: 无需预装任何组件，一键部署，开箱即用。
- **私有化部署**: 兼容云原生架构，支持多节点安装，私有化部署。
- **可视化**: 基于Flink原生打造，快速便捷执行Flink计算。

### 立即体验

&nbsp;&nbsp;&nbsp;&nbsp; 演示地址：https://zhiliuyun-demo.isxcode.com </br>
&nbsp;&nbsp;&nbsp;&nbsp; 体验账号：user001 </br>
&nbsp;&nbsp;&nbsp;&nbsp; 账号密码：welcome1

### 快速部署

> 访问地址：http://localhost:8080 </br>
> 管理员账号：admin </br>
> 管理员密码：admin123

```bash
docker run -p 8080:8080 -d isxcode/zhiliuyun
```

### 相关文档

- [快速入门](https://zhiliuyun.isxcode.com/docs/zh/0/0)
- [产品手册](https://zhiliuyun.isxcode.com/docs/zh/2/0)
- [安装文档](https://zhiliuyun.isxcode.com/docs/zh/1/0-docker)
- [博客](https://ispong.isxcode.com/tags/flink/)

### 源码构建

##### mac/linux

> 打包路径: flink-yun/flink-yun-dist/build/distributions/zhiliuyun.tar.gz

```bash
git clone https://github.com/isxcode/flink-yun.git
cd flink-yun
./gradlew install package
```

##### windows

> 注意！请使用git bash工具执行

```bash
git clone https://github.com/isxcode/flink-yun.git
cd flink-yun
./gradlew.bat install package
```

### 联系我们

邮箱：ispong@outlook.com

---

**Thanks for free JetBrains Open Source license**

<a href="https://www.jetbrains.com/?from=flink-yun" target="_blank" style="border-bottom: none !important;">
    <img src="https://img.isxcode.com/index_img/jetbrains/jetbrains-3.png" height="100" alt="jetbrains"/>
</a>
<div align="center">
  <img width="600" alt="image" src="https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/product-img/product.jpg">
</div>

---

<h1 align="center">
  至流云
</h1>

<h3 align="center">
  企业级流数据分析平台
</h3>

<div align="center">

![Docker Pulls](https://img.shields.io/docker/pulls/isxcode/zhiliuyun)
![GitHub release (with filter)](https://img.shields.io/github/v/release/isxcode/flink-yun)
![GitHub Repo stars](https://img.shields.io/github/stars/isxcode/flink-yun)
![GitHub forks](https://img.shields.io/github/forks/isxcode/flink-yun)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fisxcode%2Fflink-yun.svg?type=small)](https://app.fossa.com/projects/git%2Bgithub.com%2Fisxcode%2Fflink-yun?ref=badge_small)
![GitHub License](https://img.shields.io/github/license/isxcode/flink-yun)

</div>

### 产品体验

- 演示环境：https://zhiliuyun-demo.isxcode.com
- 暂不开放

### 产品特色

- **代码开源**：允许用户二次自定义功能开发
- **高可用**：支持高可用集群部署，提高用户抗灾能力
- **多租户**：一套系统多部门使用,支持多租户数据隔离
- **云原生**：Docker快速部署，一键启动
- **私有化部署**：可内网部署，提高用户数据安全性
- **flink纯原生**：支持flink官网的所有原生用法

### 快速部署

> 推荐使用amd64架构平台

```bash
docker run -p 8080:8080 -d isxcode/zhiliuyun
```

- 访问地址：http://localhost:8080
- 管理员账号：admin
- 管理员密码：admin123

### 相关文档

- [相关博客](https://ispong.isxcode.com/tags/flink/)

### 开发手册

- java-1.8
- nodejs-18.14.2
- vue-3.3.2
- spring-2.7.9

> 推荐使用mac或者linux系统

```bash
git clone https://github.com/isxcode/flink-yun.git
cd flink-yun
./gradlew install
./gradlew clean package
# 安装包路径: flink-yun/flink-yun-dist/build/distributions/zhiliuyun.tar.gz
```

### 关于我们

邮箱：hello@isxcode.com <br/>
微信公众号：<br/>

<img width="200" alt="image" src="https://github.com/ispong/flink-yun/assets/34756621/ae6323bf-3455-434f-a919-949af1eca11f">

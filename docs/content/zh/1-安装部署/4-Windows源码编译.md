---
title: "Windows源码编译"
---

## 源码编译打包部署至流云

#### 编译环境

- Java: zulu8.78.0.19-ca-jdk8.0.412-x64 
- Nodejs: node-v18.20.3-x64

#### 源码下载

```bash
git clone https://github.com/isxcode/flink-yun.git
```

#### 安装项目依赖

```bash
cd flink-yun
./gradlew install
```

#### 编译代码

```bash
cd flink-yun
./gradlew package
```

#### 打包路径

```bash
flink-yun/flink-yun-dist/build/distributions/zhiliuyun.tar.gz
```

#### 解压部署

```bash
tar -vzxf zhiliuyun.tar.gz
bash zhiliuyun/bin/start.sh
```

#### 访问项目

- 访问地址: http://localhost:8080 
- 管理员账号：`admin` 
- 管理员密码：`admin123`
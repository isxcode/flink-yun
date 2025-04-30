---
title: "Mac源码编译"
---

## Mac系统源码编译

### 1. 安装并启动Docker

```bash
brew install docker --cask
```

### 2. 打开终端下载代码

![20250428145831](https://img.isxcode.com/picgo/20250428145831.png)

> 下载源码

```bash
cd ~/Downloads
git clone https://github.com/isxcode/flink-yun.git
```

### 3. 使用镜像打包源码

> 将${clone_path}替换成项目路径，例如：/Users/ispong/Downloads/flink-yun  
> M系列架构，使用Arm镜像 `zhiliuyun-build:arm-latest`

```bash
docker run --rm \
  -v ${clone_path}/flink-yun:/flink-yun \
  -w /flink-yun -it registry.cn-shanghai.aliyuncs.com/isxcode/zhiliuyun-build:arm-latest \
  /bin/bash -c "source /etc/profile && gradle install clean package"
```

### 4. 解压安装包运行

> 安装包路径：flink-yun/flink-yun-dist/build/distributions/zhiliuyun.tar.gz

```bash
cd /Users/ispong/Downloads/flink-yun/flink-yun-dist/build/distributions
tar -vzxf zhiliuyun.tar.gz
cd zhiliuyun/lib
java -jar zhiliuyun.jar
```

### 5. 访问系统

- 访问地址: http://localhost:8080 
- 管理员账号：`admin` 
- 管理员密码：`admin123`
---
title: "Docker部署"
---

## 使用Docker快速部署至流云

#### 镜像地址

> 国内用户可以选择以下镜像，arm64多用于macOS用户，x86服务器架构选择`latest-amd64`版本 

```bash
registry.cn-shanghai.aliyuncs.com/isxcode/zhiliuyun:latest-amd64 
registry.cn-shanghai.aliyuncs.com/isxcode/zhiliuyun:latest-arm64
```

#### 快速启动至流云

```bash
docker run \
    --restart=always \
    --name zhiliuyun\
    -e ADMIN_PASSWORD=admin123 \
    -p 8088:8080 \
    -d registry.cn-shanghai.aliyuncs.com/isxcode/zhiliuyun:latest-amd64
```

- 访问地址: http://localhost:8088 
- 管理员账号: `admin` 
- 管理员密码: `admin123`

#### 参数说明

- `ADMIN_PASSWORD`: 启动后，密码会存入数据库中，再次启动容器以数据库为准，配置不生效。若想更改，需要删除数据库中的管理员密码重新登录即可。
- `LOG_LEVEL`: 设置项目日志级别，例如info、debug。
- `ACTIVE_ENV`: 设置项目启动环境配置文件，默认值docker。
- `PARAMS`: spring项目相关配置。
- `/var/lib/zhiliuyun`: /var/lib/zhiliuyun: 项目资源目录。
- `/etc/zhiliuyun/conf`: /etc/zhiliuyun/conf: 配置文件目录。

#### 修改配置

默认配置文件模版参考链接:  
> https://raw.githubusercontent.com/isxcode/flink-yun/refs/heads/main/flink-yun-backend/flink-yun-main/src/main/resources/application-docker.yml

```bash
vim /Users/ispong/zhiliuyun/conf/application-docker.yml
```

```bash
docker run \
    --restart=always \
    --name zhiliuyun\
    -e ADMIN_PASSWORD=admin123 \
    -e LOG_LEVEL=info \
    -e ACTIVE_ENV=docker \
    -v /Users/ispong/zhiliuyun/zhiliuyun:/var/lib/zhiliuyun \
    -v /Users/ispong/zhiliuyun/conf:/etc/zhiliuyun/conf \
    -p 8080:8080 \
    -d isxcode/zhiliuyun
```
---
title: "Gitpod部署"
---

?> 使用gitpod快速启动项目

##### 注册gitpod账号

- [https://www.gitpod.io/](https://www.gitpod.io/)

##### 创建工作空间

![20230621165436](https://img.isxcode.com/picgo/20230621165436.png)

> 输入仓库地址： https://github.com/isxcode/flink-yun

![20230621165609](https://img.isxcode.com/picgo/20230621165609.png)

##### 启动并等待依赖安装

> 安装依赖过程会比较久，请耐心等待。

![20230621165726](https://img.isxcode.com/picgo/20230621165726.png)

##### 开放gitpod端口号，访问至流云

> 注意：此时的至流云没有集群节点安装功能，需要下载内置的flink二进制文件，再重新启动。

![20230621171419](https://img.isxcode.com/picgo/20230621171419.png)

![20230621171459](https://img.isxcode.com/picgo/20230621171459.png)

- https://8080-isxcode-flinkyun-tqlt6zl9uzc.ws-us100.gitpod.io
- 默认管理员账号: admin
- 默认管理员密码: admin123

##### 下载内置的flink二进制文件

```bash
cd /tmp
nohup wget https://archive.apache.org/dist/flink/flink-3.4.0/flink-3.4.0-bin-hadoop3.tgz >> download_flink.log 2>&1 &  
tail -f download_flink.log
tar vzxf flink-3.4.0-bin-hadoop3.tgz --strip-components=1 -C /workspace/flink-yun/flink-yun-dist/src/main/flink-min
```

> 重启项目即可

```bash
cd /workspace/flink-yun/
./gradlew 0-快速开始
```

##### 企业版本启动

> 需要代码授权

```bash
# 生成ssh密钥，一路回撤
ssh-keygen
cat /home/gitpod/.ssh/id_rsa.pub
```

![20230621172402](https://img.isxcode.com/picgo/20230621172402.png)

> 配置github的免密登录

- https://github.com/settings/keys

![20230621172445](https://img.isxcode.com/picgo/20230621172445.png)

```bash
cd /workspace/flink-yun/
rm -rf flink-yun-vip
git clone git@github.com:isxcode/flink-yun-vip.git
```

> 重启项目即可

```bash
cd /workspace/flink-yun/
./gradlew 0-快速开始
```
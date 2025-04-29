---
title: "Mac源码编译"
---

## 基于CentOS7.9配置至流云开机自启

#### 将脚本文件赋予可执行权限

```bash
chmod a+x /opt/zhiliuyun/bin/start.sh
chmod a+x /opt/zhiliuyun/bin/stop.sh
```

#### 配置自启文件

```bash
vim /usr/lib/systemd/system/zhiliuyun.service
```

> 配置启动脚本路径和pid文件路径和启动用户

```bash
[Unit]
Description=zhiliuyun Service unit Configuration
After=network.target

[Service]
Type=forking

ExecStart=/opt/zhiliuyun/bin/start.sh --print-log="false"
ExecStop=/opt/zhiliuyun/bin/stop.sh
ExecReload=/opt/zhiliuyun/bin/start.sh --print-log="false"
PIDFile=/opt/zhiliuyun/zhiliuyun.pid
KillMode=none
Restart=always
User=root
Group=root

[Install]
WantedBy=multi-user.target
```

#### 重载服务

```bash
systemctl daemon-reload
```

#### 设置开机自启

```bash
systemctl enable zhiliuyun
```

#### 相关操作命令

```bash
# 启动至流云
systemctl start zhiliuyun

# 查看至流云状态
systemctl status zhiliuyun

# 停止至流云
systemctl stop zhiliuyun

# 重启至流云
systemctl restart zhiliuyun
```
---
title: "Mysql离线同步"
---

## Mysql离线同步

> 实现在mysql中结果表向目标表中离线同步数据

#### 案例

> Mysql中将t_source中的数据离线同步到t_target中

#### 解决方案

```sql
CREATE TABLE from_table(
    username STRING,
    age INT
) WITH (
    'connector'='jdbc',
    'url'='jdbc:mysql://localhost:30306/isxcode_db',
    'driver'='com.mysql.cj.jdbc.Driver',
    'table-name'='t_source',
    'username'='root',
    'password'='root123',
	'scan.fetch-size'= '1'
);

CREATE TABLE target_table(
    username STRING,
    age INT
) WITH (
    'connector'='jdbc',
    'url'='jdbc:mysql://localhost:30306/isxcode_db',
    'driver'='com.mysql.cj.jdbc.Driver',
    'table-name'='t_target',
    'username'='root',
    'password'='root123',
    'sink.buffer-flush.max-rows'='1'); 

INSERT INTO target_table select * from from_table;
```

| 配置项                        | 说明            |
|----------------------------|---------------|
| connector                  | 连接方式          |
| url                        | 数据库连接url      |
| driver                     | 数据源驱动         |
| table-name                 | 表名            |
| username                   | 数据源账号         |
| password                   | 账号密码          |
| scan.fetch-size            | 每次操作多少数据      |
| sink.buffer-flush.max-rows | 多少条数据合并在一个事务里 |

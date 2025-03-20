---
title: "Oracle实时Mysql"
---

## Oracle实时Mysql

> 实现在Oracle中结果表向目标表中实时同步到Mysql数据

#### 案例

> Oracle中将cdc_source中的数据实时同步到cdc_target中

#### 前提

- 启用Oracle的logminer
- 赋权

```sql

```

#### 解决方案

> 创建FlinkSql作业类型，添加以下依赖

- [flink-sql-connector-oracle-cdc-3.2.1.jar下载](https://repo1.maven.org/maven2/org/apache/flink/flink-sql-connector-oracle-cdc/3.2.1/flink-sql-connector-oracle-cdc-3.2.1.jar)

![20250113160944](https://img.isxcode.com/picgo/20250113160944.png)

```sql
CREATE TABLE from_table
(
    USERNAME STRING,
    AGE      INT,
    PRIMARY KEY (USERNAME) NOT ENFORCED
) WITH (
      'connector' = 'oracle-cdc',
      'hostname' = 'localhost',
      'port' = '30110',
      'username' = 'root',
      'password' = 'root123',
      'database-name' = 'PUBLIC',
      'schema-name' = 'root',
      'table-name' = 'CDC_SOURCE',
      'scan.startup.mode' = 'latest-offset'
);

CREATE TABLE target_table
(
    USERNAME STRING,
    AGE      INT,
    PRIMARY KEY (USERNAME) NOT ENFORCED
) WITH (
      'connector' = 'doris',
      'fenodes' = 'localhost:8030',
      'table.identifier' = 'ispong_db.cdc_target',
      'username' = 'root',
      'password' = 'root123')
;

INSERT INTO target_table (USERNAME, AGE)
select USERNAME, AGE
from from_table;
```

| 配置项           | 说明       |
|---------------|----------|
| connector     | 连接方式     |
| uri           | 数据库连接url |
| table-name    | 表名       |
| database-name | 库名****   

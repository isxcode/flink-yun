---
title: "读取hive数据"
---

## 读取hive数据

> 读取hive的数据

#### 案例

> 将hive3.1.3版本的数据库中数据打印出来

#### 前提

> 仅支持hive2.3.0版本以上

将zhiliuyun-agent的lib目录下的hadoop-hdfs-client-3.3.5.jar包换成当前hadoop版本依赖    
例如： 当前用户使用的是3.0.0版本的hdfs  

```bash
rm ~/zhiliuyun-agent/lib/hadoop-hdfs-client-3.3.5.jar   
cd ~/zhiliuyun-agent/lib/  
wget https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/jars/hadoop-hdfs-client-3.0.0.jar
```

#### 解决方案

> 创建FlinkSql作业类型，添加以下依赖  
> 下载对应的hive依赖版本和hadoop对应的版本  

- [flink-connector-hive_2.12-1.18.1.jar下载](https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/jars/flink-connector-hive_2.12-1.18.1.jar)
- [hadoop-common-3.0.0.jar下载](https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/jars/hadoop-common-3.0.0.jar)
- [hadoop-mapreduce-client-core-3.0.0.jar下载](https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/jars/hadoop-mapreduce-client-core-3.0.0.jar)
- [hive-common-3.1.3.jar下载](https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/jars/hive-common-3.1.3.jar)
- [hive-exec-3.1.3.jar下载](https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/jars/hive-exec-3.1.3.jar)
- [hive-metastore-3.1.3.jar下载](https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/jars/hive-metastore-3.1.3.jar)
- [libfb303-0.9.3.jar下载](https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/jars/libfb303-0.9.3.jar)

![20250325113341](https://img.isxcode.com/picgo/20250325113341.png)

```sql
CREATE CATALOG my_hive_catalog WITH (
  'type' = 'hive',
  'hive-version' = '3.1.3', 
  'default-database' = 'ispong_db2',
  'hive-conf-dir' = '/data/cloudera/parcels/CDH/lib/hive/conf'
);

USE CATALOG my_hive_catalog;

CREATE TABLE print_sink ( 
    print_date string 
) WITH ( 
    'connector' = 'print' 
);

INSERT INTO print_sink SELECT username from users;
```

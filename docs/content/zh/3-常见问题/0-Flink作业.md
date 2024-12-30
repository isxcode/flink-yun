---
title: "Flink作业"
---

#### 问题1

```wikitext
standalone模式无法执行mysql同步
```

> 下载flink的cdc驱动，上传资源中心依赖

https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/install/flink-connector-jdbc-3.1.2-1.18.jar

![20241225180640](https://img.isxcode.com/picgo/20241225180640.png)


#### 问题2

```log
'scan.incremental.snapshot.chunk.key-column' is required for table without primary key when 'scan.incremental.snapshot.enabled' enabled.
```

##### 解决方案

```wikitext
在来源表添加
'scan.incremental.snapshot.enabled'='false'
```

#### 问题3

```log
The main method caused an error: please declare primary key for sink table when query contains update/delete record.
```

##### 解决方案

```wikitext
目标表添加
PRIMARY KEY(username) NOT ENFORCED
```

#### 问题4

```log
The MySQL server has a timezone offset (0 seconds ahead of UTC) which does not match the configured timezone Asia/Shanghai. Specify the right server-time-zone to avoid inconsistencies for time-related fields.
```
##### 解决方案

```sql
-- 时区问题
show variables like '%time_zone%';
set time_zone='+8:00';

-- 或者配置
来源表
'server-time-zone'='UTC'
```

#### 问题5

```bash
Caused by: java.sql.SQLSyntaxErrorException: Access denied; you need (at least one of) the RELOAD or FLUSH_TABLES privilege(s) for this operation
```

##### 解决方案

```sql
-- 权限不足
SHOW GRANTS for ispong;
GRANT SELECT,RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'ispong'@'%';
FLUSH PRIVILEGES;
SHOW GRANTS for ispong;
```

#### 问题5

```log
Caused by: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'MASTER STATUS' at line 1
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:121)
	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122)
	at com.mysql.cj.jdbc.StatementImpl.executeQuery(StatementImpl.java:1200)
	at io.debezium.jdbc.JdbcConnection.query(JdbcConnection.java:553)
	at io.debezium.jdbc.JdbcConnection.query(JdbcConnection.java:496)
	at io.debezium.connector.mysql.MySqlSnapshotChangeEventSource.determineSnapshotOffset(MySqlSnapshotChangeEventSource.java:276)
	at io.debezium.connector.mysql.MySqlSnapshotChangeEventSource.determineSnapshotOffset(MySqlSnapshotChangeEventSource.java:46)
	at io.debezium.relational.RelationalSnapshotChangeEventSource.doExecute(RelationalSnapshotChangeEventSource.java:113)
	at io.debezium.pipeline.source.AbstractSnapshotChangeEventSource.execute(AbstractSnapshotChangeEventSource.java:76)
	... 8 more
```

##### 解决方案

```bash
不支持8.4以上的版本，换8.0的mysql版本
```

#### 问题6

```log
来源表没有更新
```

##### 解决方案

```log
来源表必须要有个主键，否则只会增加不会更新
```

#### 问题6

```log
Caused by: org.apache.flink.table.api.TableException: Column 'username' is NOT NULL, however, a null value is being written into it. You can set job configuration 'table.exec.sink.not-null-enforcer'='DROP' to suppress this exception and drop such records silently.
	at org.apache.flink.table.runtime.operators.sink.ConstraintEnforcer.processNotNullConstraint(ConstraintEnforcer.java:261)
	at org.apache.flink.table.runtime.operators.sink.ConstraintEnforcer.processElement(ConstraintEnforcer.java:241)
	at org.apache.flink.streaming.runtime.tasks.CopyingChainingOutput.pushToOperator(CopyingChainingOutput.java:75)
	at org.apache.flink.streaming.runtime.tasks.CopyingChainingOutput.collect(CopyingChainingOutput.java:50)
	at org.apache.flink.streaming.runtime.tasks.CopyingChainingOutput.collect(CopyingChainingOutput.java:29)
	at org.apache.flink.streaming.api.operators.StreamSourceContexts$ManualWatermarkContext.processAndCollect(StreamSourceContexts.java:425)
	at org.apache.flink.streaming.api.operators.StreamSourceContexts$WatermarkContext.collect(StreamSourceContexts.java:520)
	at org.apache.flink.streaming.api.operators.StreamSourceContexts$SwitchingOnClose.collect(StreamSourceContexts.java:110)
	at org.apache.flink.cdc.debezium.internal.DebeziumChangeFetcher.emitRecordsUnderCheckpointLock(DebeziumChangeFetcher.java:273)
	at org.apache.flink.cdc.debezium.internal.DebeziumChangeFetcher.handleBatch(DebeziumChangeFetcher.java:258)
	at org.apache.flink.cdc.debezium.internal.DebeziumChangeFetcher.runFetchLoop(DebeziumChangeFetcher.java:155)
	at org.apache.flink.cdc.debezium.DebeziumSourceFunction.run(DebeziumSourceFunction.java:447)
	at org.apache.flink.streaming.api.operators.StreamSource.run(StreamSource.java:114)
	at org.apache.flink.streaming.api.operators.StreamSource.run(StreamSource.java:71)
	at org.apache.flink.streaming.runtime.tasks.SourceStreamTask$LegacySourceFunctionThread.run(SourceStreamTask.java:338)
```

##### 解决方案

> 在flinkConfig中添加配置
> 数据中包含null 直接跳过

```json
{
  "table.exec.sink.not-null-enforcer": "DROP"
}
```
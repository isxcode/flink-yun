drop table SY_CONTAINER;

-- standalone集群节点支持安装spark-local组件
alter table SY_CLUSTER_NODE
  add INSTALL_SPARK_LOCAL bool default false null comment '是否安装spark-local组件';

-- 将ojdbc10-19.20.0.0.jar更新为ojdbc8-19.23.0.0.jar
UPDATE SY_DATABASE_DRIVER SET ID = 'ojdbc8-19.23.0.0', NAME = 'ojdbc8-19.23.0.0', FILE_NAME = 'ojdbc8-19.23.0.0.jar' WHERE ID LIKE 'oracle#_19.20.0.0'

-- 大屏组件
create table SY_VIEW_CARD
(
  ID                      VARCHAR(200)  not null primary key unique comment '大屏组件id',
  NAME                    VARCHAR(200)  not null comment '大屏组件名称',
  REMARK                  VARCHAR(500) comment '大屏组件备注',
  STATUS                  VARCHAR(200)  not null comment '大屏组件状态',
  TYPE                    VARCHAR(200)  not null comment '大屏组件类型',
  DATASOURCE_ID           VARCHAR(200)  not null comment '数据源id',
  EXAMPLE_DATA            VARCHAR(2000) not null comment '示例数据sql',
  WEB_CONFIG              text comment '前端显示配置',
  DATA_SQL                VARCHAR(2000) comment '数据sql',
  VERSION_NUMBER          int           not null comment '版本号',
  CREATE_BY               VARCHAR(200)  not null comment '创建人',
  CREATE_DATE_TIME        Datetime      not null comment '创建时间',
  LAST_MODIFIED_BY        VARCHAR(200)  not null comment '更新人',
  LAST_MODIFIED_DATE_TIME Datetime      not null comment '更新时间',
  DELETED                 INT default 0 not null comment '逻辑删除',
  TENANT_ID               VARCHAR(200)  not null comment '租户id'
);

-- 数据大屏
create table SY_VIEW
(
  ID                      VARCHAR(200)  not null primary key unique comment '数据大屏id',
  NAME                    VARCHAR(200)  not null comment '大屏名称',
  REMARK                  VARCHAR(500) comment '大屏备注',
  STATUS                  VARCHAR(200)  not null comment '大屏状态',
  BACKGROUND_FILE_ID      VARCHAR(200) comment '背景图文件id',
  CARD_LIST               VARCHAR(2000) comment '大屏中包含的卡片',
  WEB_CONFIG              text comment '大屏显示配置',
  VERSION_NUMBER          int           not null comment '版本号',
  CREATE_BY               VARCHAR(200)  not null comment '创建人',
  CREATE_DATE_TIME        Datetime      not null comment '创建时间',
  LAST_MODIFIED_BY        VARCHAR(200)  not null comment '更新人',
  LAST_MODIFIED_DATE_TIME Datetime      not null comment '更新时间',
  DELETED                 INT default 0 not null comment '逻辑删除',
  TENANT_ID               VARCHAR(200)  not null comment '租户id'
);

-- 添加下次执行时间
alter table SY_WORKFLOW
  add NEXT_DATE_TIME datetime comment '是否安装spark-local组件';

-- 添加消息体表
create table SY_MESSAGE
(
  id                      varchar(200)  not null comment '消息消息体id'
    primary key,
  name                    varchar(200)  not null comment '消息体名称',
  status                  varchar(100)  not null comment '消息体状态',
  remark                  varchar(500) comment '消息体备注',
  msg_type                varchar(200)  not null comment '消息体类型，邮箱/阿里短信/飞书',
  msg_config              text          not null comment '消息体配置信息',
  response                text comment '检测响应',
  create_by               varchar(200)  not null comment '创建人',
  create_date_time        datetime      not null comment '创建时间',
  last_modified_by        varchar(200)  not null comment '更新人',
  last_modified_date_time datetime      not null comment '更新时间',
  version_number          int           not null comment '版本号',
  deleted                 int default 0 not null comment '逻辑删除',
  tenant_id               varchar(200)  not null comment '租户id'
);

-- 添加告警表
create table SY_ALARM
(
  id                      varchar(200)  not null comment '告警id'
    primary key,
  name                    varchar(200)  not null comment '告警名称',
  status                  varchar(100)  not null comment '消息状态，启动/停止',
  remark                  varchar(500) comment '告警备注',
  alarm_type              varchar(200)  not null comment '告警类型，作业/作业流',
  alarm_event             varchar(200)  not null comment '告警的事件，开始运行/运行失败/运行结束',
  msg_id                  varchar(100)  not null comment '使用的消息体',
  alarm_template          text          not null comment '告警的模版',
  receiver_list           text          null comment '告警接受者',
  create_by               varchar(200)  not null comment '创建人',
  create_date_time        datetime      not null comment '创建时间',
  last_modified_by        varchar(200)  not null comment '更新人',
  last_modified_date_time datetime      not null comment '更新时间',
  version_number          int           not null comment '版本号',
  deleted                 int default 0 not null comment '逻辑删除',
  tenant_id               varchar(200)  not null comment '租户id'
);

-- 添加消息告警实例表
create table SY_ALARM_INSTANCE
(
  id               varchar(200)  not null comment '告警实例id'
    primary key,
  alarm_id         varchar(200)  not null comment '告警id',
  send_status      varchar(100)  not null comment '是否发送成功',
  alarm_type       varchar(200)  not null comment '告警类型，作业/作业流',
  alarm_event      varchar(200)  not null comment '触发的事件',
  msg_id           varchar(100)  not null comment '告警的消息体',
  content          text          not null comment '发送消息的内容',
  response         text          not null comment '事件响应',
  instance_id      varchar(200)  not null comment '任务实例id',
  receiver         varchar(200)  not null comment '消息接受者',
  send_date_time   datetime      not null comment '发送的时间',
  create_date_time datetime      not null comment '创建时间',
  deleted          int default 0 not null comment '逻辑删除',
  tenant_id        varchar(200)  not null comment '租户id'
);

-- 作业添加基线
alter table SY_WORK_CONFIG
  add ALARM_LIST text comment '绑定的基线';

-- 作业流添加基线
alter table SY_WORKFLOW_CONFIG
  add ALARM_LIST text comment '绑定的基线';

-- 作业添加基线
alter table SY_WORK_VERSION
  add ALARM_LIST text comment '绑定的基线';

-- 作业流添加基线
alter table SY_WORKFLOW_VERSION
  add ALARM_LIST text comment '绑定的基线';

-- 添加外部调用开关状态
alter table SY_WORKFLOW_CONFIG
  add INVOKE_STATUS varchar(100) default 'OFF' not null comment '是否启动外部调用';

-- 扩长running_log
alter table SY_REAL
    modify running_log longtext null comment '运行日志'

-- 新增excel同步配置字段
ALTER TABLE SY_WORK_CONFIG
    ADD EXCEL_SYNC_CONFIG LONGTEXT;

-- 实例表中添加EXCEL_SYNC_CONFIG字段
ALTER TABLE SY_WORK_VERSION
    ADD EXCEL_SYNC_CONFIG LONGTEXT;

-- 元数据相关联的表
-- 元数据db表
CREATE TABLE SY_META_DATABASE (
    datasource_id           VARCHAR(200)  NOT NULL COMMENT '元数据数据源id',
    name                    VARCHAR(200)  NOT NULL COMMENT '数据源名称',
    db_name                 VARCHAR(200)  NULL COMMENT 'db名称',
    db_type                 VARCHAR(200)  NOT NULL COMMENT '数据源类型',
    db_comment              VARCHAR(500) COMMENT '数据源备注',
    create_by               VARCHAR(200)  NOT NULL COMMENT '创建人',
    create_date_time        DATETIME      NOT NULL COMMENT '创建时间',
    last_modified_by        VARCHAR(200)  NOT NULL COMMENT '更新人',
    last_modified_date_time DATETIME      NOT NULL COMMENT '更新时间',
    version_number          INT           NOT NULL COMMENT '版本号',
    deleted                 INT DEFAULT 0 NOT NULL COMMENT '逻辑删除',
    tenant_id               VARCHAR(200)  NOT NULL COMMENT '租户id',
    PRIMARY KEY (datasource_id)
);

-- 元数据表信息
CREATE TABLE SY_META_TABLE (
    datasource_id           VARCHAR(200)  NOT NULL COMMENT '元数据表id',
    table_name              VARCHAR(200)  NOT NULL COMMENT '表名',
    table_comment           VARCHAR(500) COMMENT '表备注',
    create_by               VARCHAR(200)  NOT NULL COMMENT '创建人',
    create_date_time        DATETIME      NOT NULL COMMENT '创建时间',
    last_modified_by        VARCHAR(200)  NOT NULL COMMENT '更新人',
    last_modified_date_time DATETIME      NOT NULL COMMENT '更新时间',
    version_number          INT           NOT NULL COMMENT '版本号',
    deleted                 INT DEFAULT 0 NOT NULL COMMENT '逻辑删除',
    tenant_id               VARCHAR(200)  NOT NULL COMMENT '租户id',
    PRIMARY KEY (datasource_id, table_name)
);

-- 元数据字段信息
CREATE TABLE SY_META_COLUMN (
    datasource_id           VARCHAR(200)  NOT NULL COMMENT '元数据表id',
    table_name              VARCHAR(200)  NOT NULL COMMENT '表名',
    column_name             VARCHAR(200)  NOT NULL COMMENT '字段名',
    column_type             VARCHAR(200)  NOT NULL COMMENT '字段类型',
    column_comment          VARCHAR(500) COMMENT '字段备注',
    is_partition_column     BOOLEAN NOT NULL COMMENT '是否为分区字段',
    create_by               VARCHAR(200)  NOT NULL COMMENT '创建人',
    create_date_time        DATETIME      NOT NULL COMMENT '创建时间',
    last_modified_by        VARCHAR(200)  NOT NULL COMMENT '更新人',
    last_modified_date_time DATETIME      NOT NULL COMMENT '更新时间',
    version_number          INT           NOT NULL COMMENT '版本号',
    deleted                 INT DEFAULT 0 NOT NULL COMMENT '逻辑删除',
    tenant_id               VARCHAR(200)  NOT NULL COMMENT '租户id',
    PRIMARY KEY (datasource_id, table_name, column_name)
);

-- 元数据采集任务表
CREATE TABLE SY_META_WORK (
    id                      VARCHAR(200)  NOT NULL COMMENT '元数据采集任务id',
    name                    VARCHAR(200)  NOT NULL COMMENT '任务名',
    db_type                 VARCHAR(200)  NOT NULL COMMENT '数据源类型',
    datasource_id           VARCHAR(100)  NOT NULL COMMENT '数据源id',
    collect_type            VARCHAR(100)  NOT NULL COMMENT '采集方式',
    table_pattern           VARCHAR(100)  NOT NULL COMMENT '表名表达式',
    cron_config             VARCHAR(500) COMMENT '调度配置',
    status                  VARCHAR(500)  NOT NULL COMMENT '任务状态',
    remark                  VARCHAR(500) COMMENT '表备注',
    create_by               VARCHAR(200)  NOT NULL COMMENT '创建人',
    create_date_time        DATETIME      NOT NULL COMMENT '创建时间',
    last_modified_by        VARCHAR(200)  NOT NULL COMMENT '更新人',
    last_modified_date_time DATETIME      NOT NULL COMMENT '更新时间',
    version_number          INT           NOT NULL COMMENT '版本号',
    deleted                 INT DEFAULT 0 NOT NULL COMMENT '逻辑删除',
    tenant_id               VARCHAR(200)  NOT NULL COMMENT '租户id',
    PRIMARY KEY (id)
);

-- 元数据采集实例表
CREATE TABLE SY_META_INSTANCE (
    id                      VARCHAR(200)  NOT NULL COMMENT '元数据采集实例id',
    meta_work_id            VARCHAR(200)  NOT NULL COMMENT '元数据采集任务id',
    trigger_type            VARCHAR(200)  NOT NULL COMMENT '触发类型',
    status                  VARCHAR(500)  NOT NULL COMMENT '实例类型',
    start_date_time         DATETIME      NOT NULL COMMENT '开始时间',
    end_date_time           DATETIME COMMENT '结束时间',
    collect_log             LONGTEXT COMMENT '采集日志',
    create_by               VARCHAR(200)  NOT NULL COMMENT '创建人',
    create_date_time        DATETIME      NOT NULL COMMENT '创建时间',
    last_modified_by        VARCHAR(200)  NOT NULL COMMENT '更新人',
    last_modified_date_time DATETIME      NOT NULL COMMENT '更新时间',
    version_number          INT           NOT NULL COMMENT '版本号',
    deleted                 INT DEFAULT 0 NOT NULL COMMENT '逻辑删除',
    tenant_id               VARCHAR(200)  NOT NULL COMMENT '租户id',
    PRIMARY KEY (id)
);

-- 元数据表基础信息表
CREATE TABLE SY_META_TABLE_INFO (
    datasource_id           VARCHAR(200)  NOT NULL COMMENT '元数据表id',
    table_name              VARCHAR(200)  NOT NULL COMMENT '表名',
    column_count            BIGINT COMMENT '字段数',
    total_rows              BIGINT COMMENT '总条数',
    total_size              BIGINT COMMENT '总大小',
    create_by               VARCHAR(200)  NOT NULL COMMENT '创建人',
    create_date_time        DATETIME      NOT NULL COMMENT '创建时间',
    last_modified_by        VARCHAR(200)  NOT NULL COMMENT '更新人',
    last_modified_date_time DATETIME      NOT NULL COMMENT '更新时间',
    version_number          INT           NOT NULL COMMENT '版本号',
    deleted                 INT DEFAULT 0 NOT NULL COMMENT '逻辑删除',
    tenant_id               VARCHAR(200)  NOT NULL COMMENT '租户id',
    PRIMARY KEY (datasource_id, table_name)
);
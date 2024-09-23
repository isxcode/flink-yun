drop table FY_CONTAINER;

-- standalone集群节点支持安装flink-local组件
alter table FY_CLUSTER_NODE
  add INSTALL_FLINK_LOCAL BOOLEAN default FALSE;

comment on column FY_CLUSTER_NODE.INSTALL_FLINK_LOCAL is '是否安装flink-local组件';

-- 将ojdbc10-19.20.0.0.jar更新为ojdbc8-19.23.0.0.jar
UPDATE FY_DATABASE_DRIVER SET ID = 'ojdbc8-19.23.0.0', NAME = 'ojdbc8-19.23.0.0', FILE_NAME = 'ojdbc8-19.23.0.0.jar' WHERE ID LIKE 'oracle#_19.20.0.0';

-- 大屏组件
create table FY_VIEW_CARD
(
  ID                      CHARACTER VARYING(200)  not null primary key unique comment '大屏组件id',
  NAME                    CHARACTER VARYING(200)  not null comment '大屏组件名称',
  REMARK                  CHARACTER VARYING(500) comment '大屏组件备注',
  STATUS                  CHARACTER VARYING(200)  not null comment '大屏组件状态',
  TYPE                    CHARACTER VARYING(200)  not null comment '大屏组件类型',
  DATASOURCE_ID           CHARACTER VARYING(200)  not null comment '数据源id',
  EXAMPLE_DATA            CHARACTER VARYING(2000) not null comment '示例数据sql',
  WEB_CONFIG              CHARACTER VARYING(2000) comment '前端显示配置',
  DATA_SQL                CHARACTER VARYING(2000) comment '数据sql',
  VERSION_NUMBER          int                     not null comment '版本号',
  CREATE_BY               CHARACTER VARYING(200)  not null comment '创建人',
  CREATE_DATE_TIME        TIMESTAMP               not null comment '创建时间',
  LAST_MODIFIED_BY        CHARACTER VARYING(200)  not null comment '更新人',
  LAST_MODIFIED_DATE_TIME TIMESTAMP               not null comment '更新时间',
  DELETED                 INTEGER default 0       not null comment '逻辑删除',
  TENANT_ID               CHARACTER VARYING(200)  not null comment '租户id'
);

-- 数据大屏
create table FY_VIEW
(
  ID                      CHARACTER VARYING(200) not null primary key unique comment '数据大屏id',
  NAME                    CHARACTER VARYING(200) not null comment '大屏名称',
  REMARK                  CHARACTER VARYING(500) comment '大屏备注',
  STATUS                  CHARACTER VARYING(200) not null comment '大屏状态',
  BACKGROUND_FILE_ID      CHARACTER VARYING(200) comment '背景图文件id',
  CARD_LIST               CHARACTER VARYING(2000) comment '大屏中包含的卡片',
  WEB_CONFIG              CHARACTER VARYING(2000) comment '大屏显示配置',
  VERSION_NUMBER          int                    not null comment '版本号',
  CREATE_BY               CHARACTER VARYING(200) not null comment '创建人',
  CREATE_DATE_TIME        TIMESTAMP              not null comment '创建时间',
  LAST_MODIFIED_BY        CHARACTER VARYING(200) not null comment '更新人',
  LAST_MODIFIED_DATE_TIME TIMESTAMP              not null comment '更新时间',
  DELETED                 INTEGER default 0      not null comment '逻辑删除',
  TENANT_ID               CHARACTER VARYING(200) not null comment '租户id'
);

-- 添加下次执行时间
alter table FY_WORKFLOW
    add next_date_time datetime;

comment on column FY_WORKFLOW.next_date_time is '下次执行时间';

-- 添加消息体表
create table FY_MESSAGE
(
  id                      varchar(200)  not null comment '消息消息体id'
    primary key,
  name                    varchar(200)  not null comment '消息体名称',
  remark                  varchar(500) comment '消息体备注',
  status                  varchar(100)  not null comment '消息体状态',
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
create table FY_ALARM
(
  id                      varchar(200)  not null comment '告警id'
    primary key,
  name                    varchar(200)  not null comment '告警名称',
  remark                  varchar(500) comment '告警备注',
  status                  varchar(100)  not null comment '消息状态，启动/停止',
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
create table FY_ALARM_INSTANCE
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
alter table FY_WORK_CONFIG
  add ALARM_LIST text;

comment on column FY_WORK_CONFIG.ALARM_LIST is '绑定的基线';

-- 作业流添加基线
alter table FY_WORKFLOW_CONFIG
  add ALARM_LIST text;

comment on column FY_WORKFLOW_CONFIG.ALARM_LIST is '绑定的基线';

-- 作业添加基线
alter table FY_WORK_VERSION
  add ALARM_LIST text;

comment on column FY_WORK_VERSION.ALARM_LIST is '绑定的基线';

-- 作业流添加基线
alter table FY_WORKFLOW_VERSION
  add ALARM_LIST text;

comment on column FY_WORKFLOW_VERSION.ALARM_LIST is '绑定的基线';

-- 添加外部调用开关状态
alter table FY_WORKFLOW_CONFIG
    add INVOKE_STATUS varchar(100) default 'OFF' not null;

comment on column FY_WORKFLOW_CONFIG.INVOKE_STATUS is '是否启动外部调用';

-- 扩长running_log
alter table FY_REAL
    alter column RUNNING_LOG longtext;

-- 新增excel同步配置字段
alter table FY_WORK_CONFIG
    add EXCEL_SYNC_CONFIG longtext;

-- 实例表中添加EXCEL_SYNC_CONFIG字段
alter table FY_WORK_VERSION
    add EXCEL_SYNC_CONFIG longtext;

-- 元数据相关联的表
-- 元数据db表
create table FY_META_DATABASE
(
    datasource_id           varchar(200)  not null comment '元数据数据源id'
        primary key,
    name                    varchar(200)  not null comment '数据源名称',
    db_name                 varchar(200)  null comment 'db名称',
    db_type                 varchar(200)  not null comment '数据源类型',
    db_comment              varchar(500) comment '数据源备注',
    create_by               varchar(200)  not null comment '创建人',
    create_date_time        datetime      not null comment '创建时间',
    last_modified_by        varchar(200)  not null comment '更新人',
    last_modified_date_time datetime      not null comment '更新时间',
    version_number          int           not null comment '版本号',
    deleted                 int default 0 not null comment '逻辑删除',
    tenant_id               varchar(200)  not null comment '租户id'
);

-- 元数据表信息
create table FY_META_TABLE
(
    datasource_id           varchar(200)  not null comment '元数据表id',
    table_name              varchar(200)  not null comment '表名',
    table_comment           varchar(500) comment '表备注',
    create_by               varchar(200)  not null comment '创建人',
    create_date_time        datetime      not null comment '创建时间',
    last_modified_by        varchar(200)  not null comment '更新人',
    last_modified_date_time datetime      not null comment '更新时间',
    version_number          int           not null comment '版本号',
    deleted                 int default 0 not null comment '逻辑删除',
    tenant_id               varchar(200)  not null comment '租户id',
    PRIMARY KEY (datasource_id, table_name)
);

-- 元数据字段信息
create table FY_META_COLUMN
(
    datasource_id           varchar(200)  not null comment '元数据表id',
    table_name              varchar(200)  not null comment '表名',
    column_name             varchar(200)  not null comment '字段名',
    column_type             varchar(200)  not null comment '字段类型',
    column_comment          varchar(500) comment '字段备注',
    is_partition_column     boolean not null comment '是否为分区字段',
    create_by               varchar(200)  not null comment '创建人',
    create_date_time        datetime      not null comment '创建时间',
    last_modified_by        varchar(200)  not null comment '更新人',
    last_modified_date_time datetime      not null comment '更新时间',
    version_number          int           not null comment '版本号',
    deleted                 int default 0 not null comment '逻辑删除',
    tenant_id               varchar(200)  not null comment '租户id',
    PRIMARY KEY (datasource_id, table_name, column_name)
);

-- 元数据采集任务表
create table FY_META_WORK
(
    id                      varchar(200)  not null comment '元数据采集任务id'
        primary key,
    name                    varchar(200)  not null comment '任务名',
    db_type                 varchar(200)  not null comment '数据源类型',
    datasource_id           varchar(100)  not null comment '数据源id',
    collect_type            varchar(100)  not null comment '采集方式',
    table_pattern           varchar(100)  not null comment '表名表达式',
    cron_config             varchar(500) comment '调度配置',
    status                  varchar(500)  not null comment '任务状态',
    remark                  varchar(500) comment '表备注',
    create_by               varchar(200)  not null comment '创建人',
    create_date_time        datetime      not null comment '创建时间',
    last_modified_by        varchar(200)  not null comment '更新人',
    last_modified_date_time datetime      not null comment '更新时间',
    version_number          int           not null comment '版本号',
    deleted                 int default 0 not null comment '逻辑删除',
    tenant_id               varchar(200)  not null comment '租户id'
);

-- 元数据采集实例表
create table FY_META_INSTANCE
(
    id                      varchar(200)  not null comment '元数据采集实例id'
        primary key,
    meta_work_id            varchar(200)  not null comment '元数据采集任务id',
    trigger_type            varchar(200)  not null comment '触发类型',
    status                  varchar(500)  not null comment '实例类型',
    start_date_time         datetime      not null comment '开始时间',
    end_date_time           datetime comment '结束时间',
    collect_log             longtext comment '采集日志',
    create_by               varchar(200)  not null comment '创建人',
    create_date_time        datetime      not null comment '创建时间',
    last_modified_by        varchar(200)  not null comment '更新人',
    last_modified_date_time datetime      not null comment '更新时间',
    version_number          int           not null comment '版本号',
    deleted                 int default 0 not null comment '逻辑删除',
    tenant_id               varchar(200)  not null comment '租户id'
);

-- 元数据表基础信息表
create table FY_META_TABLE_INFO
(
    datasource_id           varchar(200)  not null comment '元数据表id',
    table_name              varchar(200)  not null comment '表名',
    column_count            long comment '字段数',
    total_rows              long comment '总条数',
    total_size              long comment '总大小',
    create_by               varchar(200)  not null comment '创建人',
    create_date_time        datetime      not null comment '创建时间',
    last_modified_by        varchar(200)  not null comment '更新人',
    last_modified_date_time datetime      not null comment '更新时间',
    version_number          int           not null comment '版本号',
    deleted                 int default 0 not null comment '逻辑删除',
    tenant_id               varchar(200)  not null comment '租户id',
    PRIMARY KEY (datasource_id, table_name)
);
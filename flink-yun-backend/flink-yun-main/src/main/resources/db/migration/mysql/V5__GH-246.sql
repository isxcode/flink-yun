-- 元数据db添加状态标示
alter table FY_META_DATABASE
    add STATUS varchar(200);

-- 表元数据添加自定义备注
ALTER TABLE FY_META_TABLE_INFO
    ADD CUSTOM_COMMENT varchar(500);

-- 元数据字段信息
CREATE TABLE FY_META_COLUMN_INFO (
    datasource_id           VARCHAR(200)  NOT NULL COMMENT '元数据表id',
    table_name              VARCHAR(200)  NOT NULL COMMENT '表名',
    column_name             VARCHAR(200)  NOT NULL COMMENT '字段名',
    custom_comment          VARCHAR(500) COMMENT '字段备注',
    create_by               VARCHAR(200)  NOT NULL COMMENT '创建人',
    create_date_time        DATETIME      NOT NULL COMMENT '创建时间',
    last_modified_by        VARCHAR(200)  NOT NULL COMMENT '更新人',
    last_modified_date_time DATETIME      NOT NULL COMMENT '更新时间',
    version_number          INT           NOT NULL COMMENT '版本号',
    deleted                 INT DEFAULT 0 NOT NULL COMMENT '逻辑删除',
    tenant_id               VARCHAR(200)  NOT NULL COMMENT '租户id',
    PRIMARY KEY (datasource_id, table_name, column_name)
);

-- 添加数据大屏分享的链接表
create table FY_VIEW_LINK
(
    id                      varchar(200)  not null comment '数据大屏分享链接id'
    primary key,
    view_id                 varchar(200)  not null comment '大屏id',
    view_version            varchar(200)  not null comment '大屏版本',
    view_token              varchar(500)  not null comment '分享大屏的匿名token',
    invalid_date_time       datetime      not null comment '到期时间',
    create_by               varchar(200)  not null comment '创建人',
    create_date_time        datetime      not null comment '创建时间',
    last_modified_by        varchar(200)  not null comment '更新人',
    last_modified_date_time datetime      not null comment '更新时间',
    version_number          int           not null comment '版本号',
    deleted                 int default 0 not null comment '逻辑删除',
    tenant_id               varchar(200)  not null comment '租户id'
);

-- 添加greenplum驱动
INSERT INTO FY_DATABASE_DRIVER (id, name, db_type, file_name, driver_type, create_by, create_date_time,
                                last_modified_by, last_modified_date_time, version_number, deleted, tenant_id, remark,
                                is_default_driver)
VALUES ('greenplum(postgresql-42.6.0)', 'greenplum(postgresql-42.6.0)', 'GREENPLUM', 'postgresql-42.6.0.jar',
        'SYSTEM_DRIVER', 'zhiqingyun', '2023-11-01 16:54:34', 'zhiqingyun', '2023-11-01 16:54:39', 1, 0, 'zhiqingyun',
        '系统自带驱动', 1);
-- 添加gbase驱动
INSERT INTO FY_DATABASE_DRIVER (id, name, db_type, file_name, driver_type, create_by, create_date_time,
                                last_modified_by, last_modified_date_time, version_number, deleted, tenant_id, remark,
                                is_default_driver)
VALUES ('gbase8a', 'gbase8a', 'GBASE', 'gbase-connector-java-9.5.0.7-build1-bin.jar',
        'SYSTEM_DRIVER', 'zhiqingyun', '2023-11-01 16:54:34', 'zhiqingyun', '2023-11-01 16:54:39', 1, 0, 'zhiqingyun',
        '系统自带驱动', 1);
-- 添加sybase
INSERT INTO FY_DATABASE_DRIVER (id, name, db_type, file_name, driver_type, create_by, create_date_time,
                                last_modified_by, last_modified_date_time, version_number, deleted, tenant_id, remark,
                                is_default_driver)
VALUES ('sybase', 'sybase', 'SYBASE', 'jconn4-16.0.jar',
        'SYSTEM_DRIVER', 'zhiqingyun', '2023-11-01 16:54:34', 'zhiqingyun', '2023-11-01 16:54:39', 1, 0, 'zhiqingyun',
        '系统自带驱动', 1);
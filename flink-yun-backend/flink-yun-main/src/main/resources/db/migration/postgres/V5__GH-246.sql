-- 元数据db添加状态标示
ALTER TABLE FY_META_DATABASE
    ADD COLUMN STATUS varchar(200);

-- 表元数据添加自定义备注
ALTER TABLE FY_META_TABLE_INFO
    ADD COLUMN CUSTOM_COMMENT varchar(500);

-- 元数据字段信息
CREATE TABLE FY_META_COLUMN_INFO (
    datasource_id           VARCHAR(200)  NOT NULL, -- 元数据表id
    table_name              VARCHAR(200)  NOT NULL, -- 表名
    column_name             VARCHAR(200)  NOT NULL, -- 字段名
    custom_comment          VARCHAR(500), -- 字段备注
    create_by               VARCHAR(200)  NOT NULL, -- 创建人
    create_date_time        TIMESTAMP     NOT NULL, -- 创建时间
    last_modified_by        VARCHAR(200)  NOT NULL, -- 更新人
    last_modified_date_time TIMESTAMP     NOT NULL, -- 更新时间
    version_number          INT           NOT NULL, -- 版本号
    deleted                 INT DEFAULT 0 NOT NULL, -- 逻辑删除
    tenant_id               VARCHAR(200)  NOT NULL, -- 租户id
    PRIMARY KEY (datasource_id, table_name, column_name)
);

-- 添加数据大屏分享的链接表
CREATE TABLE FY_VIEW_LINK
(
  id                      varchar(200) NOT NULL,
  form_id                 varchar(200) NOT NULL,
  form_version            varchar(200) NOT NULL,
  form_token              varchar(500) NOT NULL,
  create_by               varchar(200) NOT NULL,
  invalid_date_time       timestamp    NOT NULL,
  create_date_time        timestamp    NOT NULL,
  last_modified_by        varchar(200) NOT NULL,
  last_modified_date_time timestamp    NOT NULL,
  version_number          int          NOT NULL,
  deleted                 int          NOT NULL DEFAULT 0,
  tenant_id               varchar(200) NOT NULL,
  PRIMARY KEY (id)
);

-- 为每个列添加注释
COMMENT ON COLUMN FY_FORM_LINK.id IS '数据大屏分享链接id';
COMMENT ON COLUMN FY_FORM_LINK.form_id IS '大屏id';
COMMENT ON COLUMN FY_FORM_LINK.form_version IS '大屏版本';
COMMENT ON COLUMN FY_FORM_LINK.form_token IS '分享大屏的匿名token';
COMMENT ON COLUMN FY_FORM_LINK.create_by IS '创建人';
COMMENT ON COLUMN FY_FORM_LINK.invalid_date_time IS '到期时间';
COMMENT ON COLUMN FY_FORM_LINK.create_date_time IS '创建时间';
COMMENT ON COLUMN FY_FORM_LINK.last_modified_by IS '更新人';
COMMENT ON COLUMN FY_FORM_LINK.last_modified_date_time IS '更新时间';
COMMENT ON COLUMN FY_FORM_LINK.version_number IS '版本号';
COMMENT ON COLUMN FY_FORM_LINK.deleted IS '逻辑删除';
COMMENT ON COLUMN FY_FORM_LINK.tenant_id IS '租户id';

-- 添加greenplum驱动
INSERT INTO FY_DATABASE_DRIVER (id, name, db_type, file_name, driver_type, create_by, create_date_time,
                                last_modified_by, last_modified_date_time, version_number, deleted, tenant_id, remark,
                                is_default_driver)
VALUES ('greenplum(postgresql-42.6.0)', 'greenplum(postgresql-42.6.0)', 'GREENPLUM', 'postgresql-42.6.0.jar',
        'SYSTEM_DRIVER', 'zhiqingyun', '2023-11-01 16:54:34', 'zhiqingyun', '2023-11-01 16:54:39', 1, 0, 'zhiqingyun',
        '系统自带驱动', true);
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
CREATE TABLE FY_SECRET_KEY
(
    ID                      VARCHAR(200) NOT NULL,
    KEY_NAME                VARCHAR(200) NOT NULL,
    SECRET_VALUE            VARCHAR(200) NOT NULL,
    REMARK                  VARCHAR(500),
    VERSION_NUMBER          INT          NOT NULL,
    CREATE_BY               VARCHAR(200) NOT NULL,
    CREATE_DATE_TIME        TIMESTAMP    NOT NULL,
    LAST_MODIFIED_BY        VARCHAR(200) NOT NULL,
    LAST_MODIFIED_DATE_TIME TIMESTAMP    NOT NULL,
    DELETED                 INT          NOT NULL DEFAULT 0,
    TENANT_ID               VARCHAR(200) NOT NULL,
    CONSTRAINT pk_fy_secret_key PRIMARY KEY (ID)
);

-- 为列添加注释
COMMENT ON COLUMN FY_SECRET_KEY.ID IS '全局变量id';
COMMENT ON COLUMN FY_SECRET_KEY.KEY_NAME IS '全局变量key';
COMMENT ON COLUMN FY_SECRET_KEY.SECRET_VALUE IS '全局变量value';
COMMENT ON COLUMN FY_SECRET_KEY.VERSION_NUMBER IS '版本号';
COMMENT ON COLUMN FY_SECRET_KEY.CREATE_BY IS '创建人';
COMMENT ON COLUMN FY_SECRET_KEY.CREATE_DATE_TIME IS '创建时间';
COMMENT ON COLUMN FY_SECRET_KEY.LAST_MODIFIED_BY IS '更新人';
COMMENT ON COLUMN FY_SECRET_KEY.LAST_MODIFIED_DATE_TIME IS '更新时间';
COMMENT ON COLUMN FY_SECRET_KEY.DELETED IS '逻辑删除';
COMMENT ON COLUMN FY_SECRET_KEY.TENANT_ID IS '租户id';
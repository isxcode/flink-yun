-- 修复作业调度信息无法配置分钟级别配置
alter table FY_WORK_CONFIG
    alter column CRON_CONFIG varchar(2000);

-- 修复作业调度信息无法配置分钟级别配置
alter table FY_WORKFLOW_VERSION
    alter column CRON_CONFIG varchar(2000);

-- 修复分享表单新建问题
alter table FY_FORM
    alter insert_sql varchar(2000) null comment '增sql语句';

alter table FY_FORM
    alter delete_sql varchar(2000) null comment '删sql语句';

alter table FY_FORM
    alter update_sql varchar(2000) null comment '改sql语句';

alter table FY_FORM
    alter select_sql varchar(2000) null comment '查sql语句';

-- 添加外部调用url
alter table FY_WORKFLOW_CONFIG
    add INVOKE_URL longtext;
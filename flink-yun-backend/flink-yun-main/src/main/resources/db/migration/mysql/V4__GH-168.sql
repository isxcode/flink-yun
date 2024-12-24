-- 修复作业调度信息无法配置分钟级别配置
alter table FY_WORK_CONFIG
    modify cron_config varchar(2000) null comment '定时表达式';

-- 修复作业调度信息无法配置分钟级别配置
alter table FY_WORKFLOW_VERSION
    modify cron_config varchar(2000) null comment '定时表达式';

-- 修复分享表单新建问题
alter table FY_FORM
    modify insert_sql varchar(2000) null comment '增sql语句';

alter table FY_FORM
    modify delete_sql varchar(2000) null comment '删sql语句';

alter table FY_FORM
    modify update_sql varchar(2000) null comment '改sql语句';

alter table FY_FORM
    modify select_sql varchar(2000) null comment '查sql语句';

-- 添加外部调用url
ALTER TABLE FY_WORKFLOW_CONFIG
    ADD INVOKE_URL LONGTEXT;
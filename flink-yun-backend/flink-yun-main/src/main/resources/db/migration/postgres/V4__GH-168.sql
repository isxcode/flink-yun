-- 修复作业调度信息无法配置分钟级别配置
alter table FY_work_config
    alter column cron_config type varchar(2000) using cron_config::varchar(2000);
          
-- 修复作业调度信息无法配置分钟级别配置
alter table FY_workflow_version
    alter column cron_config type varchar(2000) using cron_config::varchar(2000);
          
-- 修复分享表单新建问题
alter table FY_form
    alter column insert_sql drop not null;

alter table FY_form
    alter column delete_sql drop not null;

alter table FY_form
    alter column update_sql drop not null;

alter table FY_form
    alter column select_sql drop not null;

alter table FY_form_component
    add uuid varchar(50);

comment on column FY_form_component.uuid is '前端的uuid';
        
-- 添加外部调用url
ALTER TABLE FY_WORKFLOW_CONFIG
    ADD COLUMN INVOKE_URL TEXT;
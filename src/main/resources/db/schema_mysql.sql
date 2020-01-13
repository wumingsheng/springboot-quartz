-- create database timer  DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci; 
-- timer
create table if not exists CMS_JOB (
    id int unsigned auto_increment primary key comment 'ID',
--    code varchar(64) not null unique comment 'key',
    job_name varchar(64) not null default '' comment 'job name',
    job_group varchar(64) not null default '' comment 'job group',
    job_description varchar(255) not null default '' comment 'job description',
    trigger_type varchar(16) NOT NULL DEFAULT '' COMMENT 'CRON,SIMPLE',
    trigger_cron varchar(32) not null DEFAULT '' COMMENT 'cron',
    trigger_interval_minutes int not null default 1 comment 'trigger interval minutes',
    trigger_name varchar(64) NOT NULL default '' comment 'trigger name',
    trigger_group varchar(64) NOT NULL default '' comment 'trigger group',
    trigger_description varchar(255) NOT NULL default '' comment 'trigger description',
    start_time timestamp NOT NULL COMMENT '任务第一次开始执行的时间',
    class_name varchar(255) NOT NULL default '' COMMENT 'classname',
    job_status varchar(16) not null default '' comment 'job status',
    creater_id varchar(32) NOT NULL default ''  COMMENT 'creater id',
    updater_id varchar(32) NOT NULL default ''  COMMENT 'updater id',
    status varchar(16) NOT NULL DEFAULT '' COMMENT 'base status:deleted,enabled,disabled',
    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    total_count int(11) NOT NULL DEFAULT 0 COMMENT '执行总次数 0 就是没有次数限制',
    extra_info varchar(512) NOT NULL DEFAULT '' COMMENT '参数params',

--    unique index code_index(code),
    unique index group_name_job_index(job_group, job_name),
    unique index group_name_trigger_index(trigger_group, trigger_name)

) engine=innodb AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='任务表';


























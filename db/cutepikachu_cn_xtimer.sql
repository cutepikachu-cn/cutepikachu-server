create database if not exists `cutepikachu_cn_xtimer`
    default character set utf8
    collate utf8_general_ci;

use `cutepikachu_cn_xtimer`;

drop table if exists `timer`;
create table if not exists `timer`
(
    `timer_id`          bigint unsigned not null comment '定时任务 id',
    `app`               varchar(128)             default null comment '业务方标识',
    `name`              varchar(256)             default null comment '任务名称',
    `status`            tinyint         not null default 0 comment '0 新建，1 未激活，2 激活',
    `cron`              varchar(256)    not null comment 'cron 表达式',
    `notify_http_param` varchar(8192)            default null comment '回调上下文',
    `create_time`       timestamp       not null default current_timestamp comment '创建时间',
    `update_time`       timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `is_delete`         tinyint(1)      not null default 0 comment '是否删除',
    primary key (`timer_id`) using btree
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_general_ci
    comment '定时任务信息表';

drop table if exists `timer_task`;
create table if not exists `timer_task`
(
    `task_id`     bigint unsigned not null comment '任务执行 id',
    `timer_id`    bigint unsigned not null comment '定时任务 id',
    `app`         varchar(128)             default null comment '业务方标识',
    `status`      tinyint         not null default 0 comment '0 待执行，1 正在执行，2 执行成功，3 执行失败',
    `output`      varchar(1028) comment '执行结果输出',
    `run_time`    bigint unsigned comment '运行开始时间',
    `cost_time`   bigint unsigned not null comment '执行耗时（误差时间）',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `is_delete`   tinyint(1)      not null default 0 comment '是否删除',
    primary key (`task_id`) using btree,
    unique index `uk_timer_id_run_time` (`timer_id`, `run_time`)
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_general_ci
    comment '任务执行信息';

drop database if exists `cutepikachu_cn_biz`;
create database if not exists `cutepikachu_cn_biz`
    default character set utf8
    collate utf8_general_ci;

use `cutepikachu_cn_biz`;

drop table if exists `file_info`;
create table `file_info`
(
    `file_id`     bigint unsigned not null comment '文件 id',
    `path`        varchar(256)    not null comment '文件路径',
    `name`        varchar(256)    not null comment '文件名',
    `size`        int unsigned    not null comment '文件大小',
    `extension`   varchar(32)              default null comment '文件扩展名',
    `biz_tag`     varchar(128)    not null comment '业务标识',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `is_delete`   tinyint         not null default 0 comment '是否删除',
    primary key (`file_id`)
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_general_ci;

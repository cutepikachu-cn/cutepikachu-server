drop database if exists `cutepikachu_cn_ai`;
create database if not exists `cutepikachu_cn_ai`
    default character set utf8
    collate utf8_general_ci;

use `cutepikachu_cn_ai`;

drop table if exists `ai_image`;
create table `ai_image`
(
    `id`            bigint unsigned not null comment 'id',
    `user_id`       bigint unsigned not null comment '用户 id',
    `platform`      varchar(32)     not null comment '平台',
    `model`         varchar(32)     not null comment '模型',
    `prompt`        varchar(512)    not null comment '提示',
    `height`        int unsigned    not null comment '高度',
    `width`         int unsigned    not null comment '宽度',
    `options`       varchar(1024)   null comment '选项',
    `status`        varchar(16)     not null comment '状态',
    `finish_time`   timestamp       null comment '完成时间',
    `error_message` varchar(512)    null comment '错误信息',
    `image_url`     varchar(512)    null comment '图片地址',
    `create_time`   timestamp       not null default current_timestamp comment '创建时间',
    `update_time`   timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `is_delete`     tinyint(1)      not null default 0 comment '是否删除',
    primary key (`id`)
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_general_ci
    comment 'AI 文生图表';

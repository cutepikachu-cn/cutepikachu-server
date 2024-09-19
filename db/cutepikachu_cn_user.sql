create database if not exists `cutepikachu_cn_user`
    default character set utf8
    collate utf8_general_ci;

use `cutepikachu_cn_user`;

drop table if exists `user`;
create table if not exists `user`
(
    `user_id`     bigint unsigned not null comment '用户 id',
    `nick_name`   varchar(64)     not null comment '用户昵称',
    `avatar_url`  varchar(256)    not null comment '用户头像 URL',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `is_delete`   tinyint(1)      not null default 0 comment '是否删除',
    primary key (`user_id`) using btree
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_general_ci
    comment '用户表';

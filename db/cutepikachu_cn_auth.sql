create database if not exists `cutepikachu_cn_auth`
    default character set utf8
    collate utf8_general_ci;

use `cutepikachu_cn_auth`;

drop table if exists `auth_account`;
create table if not exists `auth_account`
(
    `user_id`     bigint unsigned not null comment '用户 id',
    `username`    varchar(64)     not null unique comment '用户名',
    `password`    varchar(64)     not null comment '用户密码',
    `status`      tinyint         not null default 1 comment '用户状态（0 禁用；1 启用）',
    `create_ip`   varchar(15)     not null comment '创建 ip',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `is_delete`   tinyint         not null default 0 comment '是否删除',
    primary key (`user_id`) using btree,
    unique index `ux_username` (`username`) using btree
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_general_ci
    comment '认证账户表';

drop table if exists `role`;
create table if not exists `role`
(
    `role_id`     bigint unsigned not null comment '角色 id',
    `role_name`   varchar(64)     not null comment '角色名称',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `is_delete`   tinyint         not null default 0 comment '是否删除',
    primary key (`role_id`) using btree
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_general_ci
    comment '认证角色表';

insert into `role` (`role_id`, `role_name`)
values (0, 'system'),
       (1, 'user'),
       (2, 'user_admin'),
       (3, 'blog_admin');

drop table if exists `user_role`;
create table if not exists `user_role`
(
    `id`          bigint unsigned not null comment '关联主键 id',
    `user_id`     bigint unsigned not null comment '用户 id',
    `role_id`     bigint unsigned not null comment '角色 id',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `is_delete`   tinyint         not null default 0 comment '是否删除',
    primary key (`id`) using btree,
    unique index `uk_user_role` (`user_id`, `role_id`) using btree
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_general_ci
    comment '用户角色关联表';

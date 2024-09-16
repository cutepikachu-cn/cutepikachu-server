drop database if exists `cutepikachu_cn_leaf`;
create database if not exists `cutepikachu_cn_leaf`
    default character set utf8
    collate utf8_general_ci;

use `cutepikachu_cn_leaf`;

drop table if exists `leaf_alloc`;
create table `leaf_alloc`
(
    `biz_tag`     varchar(128)    not null comment '业务标识',
    `max_id`      bigint unsigned not null default 1 comment '当前分配的ID号段的最大值',
    `step`        int unsigned    not null default 100 comment '每次分配的号段长度',
    `random_step` bigint unsigned not null default 10 comment '每次获取ID时随机增加的步长，防止ID完全连续',
    `description` varchar(256)             default null comment '描述',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`biz_tag`)
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_general_ci;

INSERT INTO `leaf_alloc` (biz_tag, max_id, step, `random_step`, description)
VALUES ('cutepikachu-server-auth-account', 10000000000, 100, 10,
        'cutepikachu_cn_auth、cutepikachu_cn_user 数据库中 auth_account、user 表的 user_id'),
       ('cutepikachu-server-xtimer', 10000000000, 100, 10,
        'cutepikachu_cn_xtimer 数据库中 timer 表的 timer_id'),
       ('cutepikachu-server-shorturl', 10000000000, 100, 10,
        'cutepikachu_cn_shorturl 数据库中 url_map 表的 url_id'),
       ('cutepikachu-server-file', 10000000000, 100, 10,
        'cutepikachu_cn_biz 数据库中 file_info 表的 file_id');

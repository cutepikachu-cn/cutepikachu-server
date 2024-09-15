drop database if exists `cutepikachu_cn_shorturl`;
create database if not exists `cutepikachu_cn_shorturl`
    default character set utf8
    collate utf8_general_ci;

use `cutepikachu_cn_shorturl`;

drop table if exists `url_map`;
create table `url_map`
(
    `url_id`      bigint unsigned not null comment '链接 id',
    `long_url`    varchar(256)    not null comment '长链接',
    `short_url`   varchar(16)     not null comment '短链接',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`url_id`),
    unique index `uk_short_url_short_url` (`long_url`, `short_url`),
    index `idx_short_url_long_url` (`short_url`, `long_url`)
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_general_ci
    comment '短链接映射表';

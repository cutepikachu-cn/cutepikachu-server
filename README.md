# 笨蛋皮卡丘的服务后端

[![Readme Card](https://github-readme-stats-sand-one-31.vercel.app/api/pin/?username=cutepikachu-cn&repo=cutepikachu-server&show_owner=true)](https://github.com/cutepikachu-cn/cutepikachu-server)

## 项目简介

包含一些通用的服务后端代码，方便快速开发

## 项目结构

```text
cutepikachu-server-dependencies -- Maven 依赖版本管理
cutepikachu-server-common -- 通用公共模块
cutepikachu-server-generator -- 代码生成器模块
cutepikachu-server-gateway -- 网关服务模块
cutepikachu-server-inner -- 内部服务模块
cutepikachu-server-auth -- 认证授权服务模块
cutepikachu-server-uer -- 用户服务模块
cutepikachu-server-leaf -- 基于美团 leaf 的分布式 ID 服务模块
cutepikachu-server-xtimer -- 定时任务服务模块
cutepikachu-server-shorturl -- 短链接服务模块
cutepikachu-server-biz -- 通用业务模块
cutepikachu-server-ai -- AI 服务模块
```

## 技术选型

| 技术        | 选型                   | 版本         |
|-----------|----------------------|------------|
| 数据库       | MySQL                | 8.4.2      |
| 缓存        | Redis                | 7.4.0      |
| 核心框架      | Spring Boot          | 3.3.2      |
| 核心框架      | Spring Cloud         | 2023.0.3   |
| 核心框架      | Spring Cloud Alibaba | 2023.0.1.0 |
| 注册中心/服务发现 | Nacos                | 2.3.2      |
| 工具库       | Hutool               | 5.8.29     |
| 代码简化      | Lombok               | 1.18.34    |
| 类型映射代码生成  | MapStruct            | 1.6.2      |
| ORM 框架    | Mybatis-Plus         | 3.5.9      |
| 接口文档      | knife4j              | 4.5.0      |
| Redis 客户端 | redisson             | 3.33.0     |
| 认证鉴权框架    | Sa-Token             | 1.38.0     |
| 数据库连接池    | Druid                | 1.2.20     |
| 对象存储      | MinIO                | 8.5.12     |

## 后续开发计划

- 通用业务模块-验证码服务（AjPlus Captcha / EasyCaptcha ?）
- 搜索服务模块（Elasitcsearch ?）
- 消息队列服务模块（RabbitMQ ?）
- 短信服务模块（支持多服务商短信服务 ?）

## 开源协议

本项目遵循 GPL 协议，具体请查看 [LICENSE](LICENSE) 文件


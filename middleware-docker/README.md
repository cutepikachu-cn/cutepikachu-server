# 中间件部署

```
docker compose up -d --build
```

## MySQL 8.4.2

image: `mysql:8.4.2`

默认用户名: root
默认密码: root

## Redis

image: `redis/redis-stack-server:7.4.0-v0`

## Nacos

image: `nacos/nacos-server:v2.3.2`

默认用户名: nacos
默认密码: nacos

## MinIO

image: `minio/minio:RELEASE.2024-09-09T16-59-28Z`

默认用户名: admin
默认密码: admin123456

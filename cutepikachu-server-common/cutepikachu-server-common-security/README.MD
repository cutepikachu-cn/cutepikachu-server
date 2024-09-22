# 安全公共模块

使用 Sa-Token 实现验证和权限控制

通过 [SecurityConfiguration.class](src/main/java/cn/cutepikachu/common/security/config/SaTokenConfigure.java) 将
Sa-Token 校验和鉴权规则配置化

## 配置说明

```yaml
# 项目配置
cutepikachu:
  # 验证鉴权配置
  security:
    # 加密盐值
    SALT: cutepikachu
    # 加密算法
    ALGORITHM: md5
    # Sa-Token 验证鉴权规则路由配置
    routers:
      # 路由规则分组
      shorturl:
        # 匹配的路径
        - match: /**
      user:
        # 多个路由配置
        - match: /**
          # 不匹配的路径
          not-match:
            - /login
            - /register
            - /info
          # 检查规则
          check: login
          # 所需角色
          role: user
        - match: /admin/**
          check: login
          role: admin
```

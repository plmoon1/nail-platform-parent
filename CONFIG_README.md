# 美甲预约平台 - 配置管理说明

## 📁 配置文件说明

### 项目中的配置文件

| 文件 | 说明 | 是否提交到 Git |
|-----|------|---------------|
| `application-local.yml.template` | 本地开发配置模板 | ✅ 是 |
| `application-local.yml` | 本地开发实际配置（各服务） | ❌ 否（自动忽略） |
| `application.yml` | 默认配置（使用环境变量） | ✅ 是 |
| `bootstrap.yml` | 启动配置（Nacos 配置中心） | ✅ 是 |

---

## 🚀 快速开始

### 方式一：使用脚本自动生成配置（推荐）

**Windows 用户：**
```cmd
cd scripts
setup-local-config.bat
```

**Linux/Mac 用户：**
```bash
cd scripts
chmod +x setup-local-config.sh
./setup-local-config.sh
```

### 方式二：手动复制配置

1. 复制 `application-local.yml.template`
2. 粘贴到各服务的 `src/main/resources/` 目录
3. 重命名为 `application-local.yml`
4. 修改数据库密码、Redis 密码等配置

---

## ⚙️ 配置说明

### 核心配置项

```yaml
# 1. 数据库配置
spring:
  datasource:
    druid:
      url: jdbc:mysql://localhost:3306/nail_platform
      username: root
      password: your_password    # 需要修改

# 2. Redis 配置
  redis:
    host: localhost
    port: 6379
    password:                    # 如果有密码需要填写

# 3. Sa-Token 配置
sa-token:
  token-name: Authorization    # Token 在 Header 中的字段名
  timeout: 2592000              # Token 有效期（秒），30天
  is-share: true                # 是否共享会话（微服务必须为 true）

# 4. 服务端口
server:
  port: 8080                     # 各服务端口见下表
```

### 服务端口分配

| 服务 | 端口 | 说明 |
|-----|------|-----|
| gateway-gw | 8080 | 网关服务 |
| auth-server | 8100 | 认证服务 |
| user-server | 8200 | 用户服务 |
| service-server | 8300 | 服务管理 |
| reserve-server | 8400 | 预约服务 |
| marketing-server | 8500 | 营销服务 |
| notice-server | 8600 | 通知服务 |

---

## 🔧 环境变量方式（可选）

如果不使用配置文件，可以通过环境变量配置：

```bash
# 数据库
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_USER=root
export MYSQL_PWD=your_password

# Redis
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_DB=0

# Nacos
export NACOS_ADDR=localhost:8848
```

---

## 📋 配置文件优先级

当多个配置文件存在时，优先级从高到低：

1. **application-{profile}.yml** （如 `application-local.yml`）
2. **application.yml**
3. **环境变量**
4. **默认配置**

---

## 🔐 安全最佳实践

### 开发环境
- ✅ 使用 `application-local.yml`（已忽略提交）
- ✅ 可以明文存储密码
- ✅ 使用本地数据库和 Redis

### 生产环境
- ❌ 不要在代码中存储密码
- ✅ 使用 Nacos Config 或配置中心
- ✅ 通过环境变量或密钥管理系统注入敏感信息
- ✅ 定期轮换密钥和密码

---

## 🐛 常见问题

### Q1: 修改了配置文件但没有生效？
**A:** 检查是否在 IDEA 中设置了正确的 Profile：
```
Run Configuration -> Active profiles: local
```

### Q2: 不想使用配置文件，怎么办？
**A:** 可以直接使用环境变量，或修改 `application.yml` 中的默认值。

### Q3: 如何在服务器上部署？
**A:** 建议使用 Nacos Config 或通过环境变量注入配置。

---

## 📚 相关文档

- [本地开发环境配置指南](LOCAL_SETUP_GUIDE.md)
- [数据库初始化脚本](reserve-server/src/main/resources/db/schema/nail_platform_all.sql)
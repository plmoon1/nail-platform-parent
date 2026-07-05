# 本地开发环境配置指南

## 📋 快速开始

### 方案一：使用统一配置文件（推荐）

1. **配置文件准备**

在项目根目录执行以下命令，将配置模板复制到各个服务：

```bash
# 为每个服务创建本地配置文件
for service in auth-server user-server service-server reserve-server marketing-server notice-server; do
  cp application-local.yml.template $service/src/main/resources/application-local.yml
done

# 网关服务单独配置
cp application-local.yml.template gateway-gw/src/main/resources/application-local.yml
```

2. **修改配置文件**

编辑任意一个 `application-local.yml`，修改以下配置：

```yaml
# 数据库配置
spring:
  datasource:
    druid:
      username: root              # 修改为你的 MySQL 用户名
      password: your_password     # 修改为你的 MySQL 密码

  # Redis 配置
  redis:
    password: your_redis_password  # 如果 Redis 有密码

  # 阿里云 OSS（可选）
  aliyun:
    oss:
      accessKeyId: your_key
      accessKeySecret: your_secret
```

3. **启动服务**

在 IDEA 中启动服务时，设置 **Active profiles: `local`**

或者在命令行启动：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

---

### 方案二：使用环境变量（适合容器化部署）

在系统环境变量中设置：

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
export NACOS_NAMESPACE=public

# Sentinel
export SENTINEL_DASH=localhost:8858

# 阿里云 OSS
export OSS_ENDPOINT=oss-cn-hangzhou.aliyuncs.com
export OSS_ACCESS_KEY_ID=your_key
export OSS_ACCESS_KEY_SECRET=your_secret
export OSS_BUCKET_NAME=nail-platform
```

---

## 🔧 各服务端口分配

| 服务名称 | 默认端口 | 说明 |
|---------|---------|------|
| gateway-gw | 8080 | 网关服务（前端统一入口） |
| auth-server | 8100 | 认证登录服务 |
| user-server | 8200 | 用户/门店/美甲师服务 |
| service-server | 8300 | 服务分类/标准项目服务 |
| reserve-server | 8400 | 预约/订单服务 |
| marketing-server | 8500 | 优惠券服务 |
| notice-server | 8600 | 评价/消息通知服务 |

---

## 🗄️ 数据库初始化

1. **创建数据库**
```sql
CREATE DATABASE nail_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **导入表结构**
```bash
mysql -u root -p nail_platform < reserve-server/src/main/resources/db/schema/nail_platform_all.sql
```

---

## 🔍 服务依赖检查清单

启动服务前，请确保以下组件已启动：

- [ ] **MySQL** (端口 3306)
- [ ] **Redis** (端口 6379)
- [ ] **Nacos** (端口 8848) - 可选，如果不使用服务发现可以跳过
- [ ] **Sentinel** (端口 8858) - 可选，仅需要限流时启动

---

## 🌐 访问地址

启动完成后，可通过以下地址访问：

- **网关入口**: http://localhost:8080
- **Knife4j 文档**: http://localhost:8080/doc.html
- **Swagger UI**: http://localhost:8080/swagger-ui.html

各服务独立访问（直连，不通过网关）：
- **认证服务**: http://localhost:8100/doc.html
- **用户服务**: http://localhost:8200/doc.html
- **预约服务**: http://localhost:8400/doc.html

---

## 🔐 默认账号密码

### Druid 监控
- 用户名: `admin`
- 密码: `admin123`

### Sa-Token
- Token 名称: `Authorization`
- 有效期: 30天

---

## 📝 配置文件优先级

Spring Boot 配置文件加载优先级（从高到低）：

1. `application-{profile}.yml` (如 `application-local.yml`)
2. `application.yml`
3. 环境变量
4. 默认配置

---

## ⚠️ 安全提示

1. **不要提交敏感信息到 Git**
   - `application-local.yml` 已添加到 `.gitignore`
   - 生产环境配置应使用 Nacos Config 或外部配置中心

2. **生产环境部署**
   - 使用独立的配置文件（如 `application-prod.yml`）
   - 通过环境变量或配置中心管理敏感信息
   - 修改默认的 Druid 监控密码

---

## 🐛 常见问题

### 问题1：连接数据库失败
**解决方案**：
- 检查 MySQL 是否启动
- 验证用户名密码是否正确
- 确认数据库 nail_platform 是否已创建

### 问题2：Redis 连接失败
**解决方案**：
- 检查 Redis 是否启动：`redis-cli ping`
- 如果 Redis 有密码，在配置文件中添加 `password` 配置

### 问题3：服务注册失败
**解决方案**：
- 检查 Nacos 是否启动
- 验证 Nacos 地址配置是否正确
- 查看 Nacos 控制台：http://localhost:8848/nacos

---

## 📞 获取帮助

如有问题，请联系项目维护者或查看项目文档。

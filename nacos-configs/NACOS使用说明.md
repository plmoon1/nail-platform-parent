# Nacos 配置中心使用说明

## 一、环境信息
- Nacos 地址: http://192.168.29.1:8848/
- 用户名/密码: nacos / nacos (默认)

## 二、配置步骤

### 1. 登录 Nacos 控制台
访问 http://192.168.29.1:8848/nacos，使用 nacos/nacos 登录

### 2. 创建命名空间（可选，用于多环境隔离）
- 进入"命名空间"菜单
- 点击"新建命名空间"
- 创建开发环境和生产环境命名空间:
  - `dev` - 开发环境
  - `prod` - 生产环境

### 3. 创建配置文件

#### 3.1 创建公共配置（必须）
在 `public` 命名空间（或 dev/prod）中创建:

- **Data ID**: `nail-platform-common.yaml`
- **Group**: `NAIL_PLATFORM`
- **配置格式**: `YAML`
- **配置内容**: 复制 `nacos-configs/nail-platform-common.yaml` 文件内容

#### 3.2 创建各服务配置
为每个服务创建独立配置:

| 服务名 | Data ID | Group | 端口 |
|--------|---------|-------|------|
| 网关服务 | gateway-gw.yaml | NAIL_PLATFORM | 8080 |
| 认证服务 | auth-server.yaml | NAIL_PLATFORM | 8081 |
| 用户服务 | user-server.yaml | NAIL_PLATFORM | 8082 |
| 服务管理 | service-server.yaml | NAIL_PLATFORM | 8083 |
| 预约服务 | reserve-server.yaml | NAIL_PLATFORM | 8084 |
| 营销服务 | marketing-server.yaml | NAIL_PLATFORM | 8085 |
| 通知服务 | notice-server.yaml | NAIL_PLATFORM | 8086 |

#### 3.3 创建 Sentinel 限流规则（可选）
- **Data ID**: `gateway-gw-sentinel-flow.json`
- **Group**: `NAIL_PLATFORM`
- **配置格式**: `JSON`
- **配置内容**: 复制 `nacos-configs/gateway-gw-sentinel-flow.json`

- **Data ID**: `gateway-gw-sentinel-api.json`
- **Group**: `NAIL_PLATFORM`
- **配置格式**: `JSON`
- **配置内容**: 复制 `nacos-configs/gateway-gw-sentinel-api.json`

## 三、启动应用

### 方式一：通过环境变量指定 Nacos 地址
```bash
# Windows PowerShell
$env:NACOS_ADDR="192.168.29.1:8848"
$env:NACOS_NAMESPACE="public"  # 或 dev/prod

# Linux/Mac
export NACOS_ADDR=192.168.29.1:8848
export NACOS_NAMESPACE=public

# 然后启动服务
mvn spring-boot:run
```

### 方式二：直接修改 bootstrap.yml 中的默认值
将所有服务 `bootstrap.yml` 中的 `127.0.0.1:8848` 改为 `192.168.29.1:8848`

## 四、配置热更新

修改 Nacos 中的配置后，点击"发布"即可实现热更新，无需重启应用。

支持的配置更新:
- 日志级别
- 业务参数
- 限流规则
- 路由配置

**注意**: 数据源、Redis 等基础配置更新需要重启应用。

## 五、环境变量说明

| 环境变量 | 说明 | 默认值 |
|---------|------|--------|
| NACOS_ADDR | Nacos 服务器地址 | 127.0.0.1:8848 |
| NACOS_NAMESPACE | Nacos 命名空间 | public |
| MYSQL_HOST | MySQL 主机 | 127.0.0.1 |
| MYSQL_PORT | MySQL 端口 | 3306 |
| MYSQL_USER | MySQL 用户名 | root |
| MYSQL_PWD | MySQL 密码 | root123 |
| REDIS_HOST | Redis 主机 | 127.0.0.1 |
| REDIS_PORT | Redis 端口 | 6379 |
| REDIS_DB | Redis 数据库 | 0 |
| REDIS_PWD | Redis 密码 | (空) |
| SENTINEL_DASH | Sentinel 控制台 | 127.0.0.1:8858 |
| OSS_ENDPOINT | 阿里云 OSS 端点 | oss-cn-hangzhou.aliyuncs.com |
| OSS_ACCESS_KEY_ID | OSS 访问密钥 ID | - |
| OSS_ACCESS_KEY_SECRET | OSS 访问密钥 Secret | - |
| OSS_BUCKET_NAME | OSS 存储桶名称 | - |
| LOG_LEVEL | 日志级别 | debug |

## 六、验证配置

启动服务后，检查 Nacos 控制台的"服务管理"->"服务列表"，确认所有服务已注册成功。

访问 http://192.168.29.1:8848/nacos 查看服务状态。

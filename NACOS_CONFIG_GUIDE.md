# Nacos Config 配置导入指南

## 📋 前置准备

### 1. 启动 Nacos 服务器

```bash
# Docker 方式启动
docker run -d \
  --name nacos-server \
  -e MODE=standalone \
  -p 8848:8848 \
  nacos/nacos-server:v2.2.0

# 或者使用本地方式
cd nacos/bin
./startup.sh -m standalone
```

### 2. 访问 Nacos 控制台

打开浏览器访问：http://localhost:8848/nacos

- **默认用户名**: `nacos`
- **默认密码**: `nacos`

---

## 🚀 方式一：通过控制台手动导入（推荐）

### 步骤 1：创建命名空间（可选）

如果你想要区分开发、测试、生产环境：

1. 进入 **命名空间** 菜单
2. 点击 **新建命名空间**
3. 输入命名空间 ID：`dev`、`test`、`prod`
4. 输入描述：**开发环境**、**测试环境**、**生产环境**

### 步骤 2：导入公共配置

1. 进入 **配置管理** -> **配置列表**
2. 选择命名空间：`public` 或你创建的命名空间
3. 点击 **+** 新建配置

**导入 nail-platform-common.yaml：**

| 配置项 | 值 |
|-------|-----|
| **Data ID** | `nail-platform-common.yaml` |
| **Group** | `NAIL_PLATFORM` |
| **配置格式** | `YAML` |
| **配置内容** | 复制 `nacos-config/nail-platform-common.yaml` 的内容 |
| **描述** | 美甲预约平台 - 公共配置 |

点击 **发布**。

### 步骤 3：导入各服务配置

按照相同的方式导入以下配置文件：

| Data ID | Group | 描述 |
|---------|-------|------|
| `auth-server.yaml` | NAIL_PLATFORM | 认证服务配置 |
| `user-server.yaml` | NAIL_PLATFORM | 用户服务配置 |
| `service-server.yaml` | NAIL_PLATFORM | 服务管理配置 |
| `reserve-server.yaml` | NAIL_PLATFORM | 预约服务配置 |
| `marketing-server.yaml` | NAIL_PLATFORM | 营销服务配置 |
| `notice-server.yaml` | NAIL_PLATFORM | 通知服务配置 |
| `gateway-gw.yaml` | NAIL_PLATFORM | 网关服务配置 |

---

## 🚀 方式二：通过 API 导入（批量）

### 使用 curl 命令批量导入

创建脚本 `import-nacos-config.sh`：

```bash
#!/bin/bash

NACOS_ADDR="localhost:8848"
NACOS_NAMESPACE="public"
NACOS_GROUP="NAIL_PLATFORM"
CONFIG_DIR="nacos-config"

for config_file in ${CONFIG_DIR}/*.yaml; do
    filename=$(basename "$config_file")
    data_id="${filename}"

    echo "导入配置: $data_id"

    curl -X POST "http://${NACOS_ADDR}/nacos/v1/cs/configs" \
      -d "dataId=${data_id}" \
      -d "group=${NACOS_GROUP}" \
      -d "tenant=${NACOS_NAMESPACE}" \
      -d "type=yaml" \
      --data-urlencode "content@${config_file}"

    echo ""
    echo "✅ ${data_id} 导入成功"
    echo ""
done

echo "所有配置导入完成！"
```

执行导入：
```bash
chmod +x import-nacos-config.sh
./import-nacos-config.sh
```

---

## 🔧 修改各服务的 bootstrap.yml

确保每个服务的 `bootstrap.yml` 已经正确配置：

```yaml
spring:
  application:
    name: {服务名称}  # 如：auth-server
  cloud:
    nacos:
      config:
        server-addr: localhost:8848
        namespace: public
        group: NAIL_PLATFORM
        file-extension: yaml
        # 共享配置（会自动加载 nail-platform-common.yaml）
        shared-configs:
          - data-id: nail-platform-common.yaml
            group: NAIL_PLATFORM
            refresh: true
```

---

## 🎯 配置优先级

Nacos 配置的加载优先级（从高到低）：

1. **主配置**: `{服务名称}.yaml`（如 `auth-server.yaml`）
2. **共享配置**: `nail-platform-common.yaml`
3. **本地配置**: `application.yml`
4. **环境变量**

---

## 🔄 动态刷新配置

### 配置自动刷新

在 Nacos 控制台修改配置后，如果希望服务自动刷新，需要：

1. 在配置中添加 `@RefreshScope` 注解的类
2. 或者使用 `@ConfigurationProperties` + `@RefreshScope`

示例：

```java
@RefreshScope
@Configuration
public class MyConfig {
    @Value("${my.custom.value}")
    private String customValue;

    // 修改 Nacos 配置后，此值会自动更新
}
```

### 刷新特定配置

在 Nacos 控制台：
1. 找到对应配置
2. 点击 **编辑**
3. 修改配置内容
4. 点击 **发布**
5. 服务会自动接收到更新通知

---

## 🔍 验证配置是否生效

### 1. 检查服务启动日志

启动服务后，查看日志：

```bash
# 应该看到类似日志：
Loading nacos config, server-addr: localhost:8848
Loaded config with name: auth-server.yaml
Loaded config with name: nail-platform-common.yaml
```

### 2. 查看环境变量

在服务中添加测试接口：

```java
@GetMapping("/config/test")
public Result<Map<String, Object>> testConfig() {
    Map<String, Object> config = new HashMap<>();
    config.put("redis.host", environment.getProperty("spring.redis.host"));
    config.put("sa-token.timeout", environment.getProperty("sa-token.timeout"));
    return Result.ok(config);
}
```

访问接口验证配置是否加载成功。

### 3. 查看服务注册

在 Nacos 控制台的 **服务管理** -> **服务列表** 中，应该能看到所有注册的服务。

---

## 🐛 常见问题

### Q1: 服务启动失败，提示无法连接 Nacos

**解决方案**：
- 检查 Nacos 是否启动：`curl http://localhost:8848/nacos/v1/console/health/liveness`
- 检查 `bootstrap.yml` 中的 Nacos 地址是否正确
- 检查网络连接

### Q2: 配置没有生效

**解决方案**：
- 确认 Data ID、Group、Namespace 是否匹配
- 检查配置格式是否正确（YAML 缩进）
- 查看服务启动日志，确认配置是否加载
- 在 Nacos 控制台的 **配置列表** 中确认配置存在

### Q3: 如何区分开发、测试、生产环境？

**解决方案**：
- 创建不同的命名空间（dev、test、prod）
- 在 `bootstrap.yml` 中设置对应的 namespace
- 或通过环境变量 `NACOS_NAMESPACE` 控制

---

## 📊 配置文件结构

```
nacos-config/
├── nail-platform-common.yaml    # 公共配置（Redis、MySQL、Sa-Token等）
├── auth-server.yaml             # 认证服务配置
├── user-server.yaml             # 用户服务配置
├── service-server.yaml          # 服务管理配置
├── reserve-server.yaml          # 预约服务配置
├── marketing-server.yaml         # 营销服务配置
├── notice-server.yaml           # 通知服务配置
└── gateway-gw.yaml              # 网关服务配置
```

---

## 🔐 安全建议

### 生产环境配置

1. **使用独立命名空间**
   - 创建 `prod` 命名空间
   - 与开发、测试环境隔离

2. **敏感信息加密**
   - Nacos 支持配置加密（使用 Jasypt）
   - 或者通过环境变量注入敏感信息

3. **访问控制**
   - 修改 Nacos 默认密码
   - 启用 Nacos 的鉴权功能

4. **配置备份**
   - 定期导出配置文件
   - 保存到版本控制（Git）

---

## 📞 获取帮助

如遇到问题，请查看：
- [Nacos 官方文档](https://nacos.io/zh-cn/docs/what-is-nacos.html)
- 项目 GitHub Issues
- 联系项目维护者

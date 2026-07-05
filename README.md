# 美甲预约平台 nail-platform-parent

Spring Cloud Alibaba 微服务多模块工程。父工程统一锁定版本，7 个后端服务各自独立部署、通过 Nacos 注册与配置中心协同。

## 技术栈与版本（父 pom 统一锁定）

| 分类 | 选型 | 版本 |
|------|------|------|
| JDK | OpenJDK | 8 |
| 基础框架 | Spring Boot | 2.7.18 |
| 微服务 | Spring Cloud | 2021.0.5 |
| 微服务 | Spring Cloud Alibaba | 2021.0.5.0 |
| 注册/配置中心 | Nacos | 随 Alibaba BOM |
| 流控 | Sentinel | 随 Alibaba BOM |
| 持久层 | MyBatis-Plus | 3.5.3.1 |
| 数据库 | MySQL | 8.x（驱动 8.0.33） |
| 连接池 | Druid | 1.2.20 |
| 缓存 | Redis | 随 Spring Boot |
| 鉴权 | Sa-Token | 1.37.0 |
| 远程调用 | OpenFeign + LoadBalancer | 随 Spring Cloud |
| 工具 | Hutool | 5.8.27 |
| 接口文档 | Knife4j | 4.3.0 |
| 网关 | Spring Cloud Gateway（WebFlux） | 随 Spring Cloud |

> 公共依赖（Lombok / Hutool / bootstrap / test）已放在父 pom `<dependencies>`，所有模块自动继承；
> 业务依赖（web / mybatis-plus / redis / sa-token / feign / nacos / sentinel / druid / knife4j）在父 pom 的 `<dependencyManagement>` 锁定版本，各模块按需引入、无需写版本号。

## 模块清单与端口

| 模块 | 说明 | 端口 |
|------|------|------|
| gateway-gw | 网关（路由 / 鉴权前置 / 跨域 / 限流） | 8080 |
| auth-server | 认证登录服务（Sa-Token） | 8100 |
| user-server | 用户 / 门店 / 美甲师 | 8200 |
| service-server | 服务分类 / 项目 / 美甲师服务关联 | 8300 |
| reserve-server | 预约 / 订单核心（**全局建表 SQL 在此**） | 8400 |
| marketing-server | 优惠券 | 8500 |
| notice-server | 评价 / 消息通知 | 8600 |

## 目录结构

```
nail-platform-parent
├── pom.xml                       # 父工程，统一版本管理
├── gateway-gw / auth-server / user-server / service-server
├── reserve-server
│   └── src/main/resources
│       ├── application.yml
│       └── db
│           ├── schema/nail_platform_all.sql   # 14 张表完整建表脚本
│           └── migration                       # Flyway 迭代脚本目录
├── marketing-server / notice-server
└── docs/                         # 架构文档
```

## 前置中间件

启动前请先在本机（或修改各 `application.yml` 中的地址）准备：

- **MySQL 8**：执行 `reserve-server/src/main/resources/db/schema/nail_platform_all.sql`，自动建库 `nail_platform` + 14 张表。
- **Redis**：默认 `127.0.0.1:6379`（Sa-Token 共享会话 + 业务缓存）。
- **Nacos**：默认 `127.0.0.1:8848`（注册中心 + 配置中心；未启动时各服务不会 fail-fast，仅打印注册失败日志）。

## 启动顺序

1. Nacos → Redis → MySQL
2. `auth-server`（鉴权基础）→ 各业务服务 → `gateway-gw`（最后起，依赖下游服务名做路由）
3. 网关统一入口：`http://localhost:8080`

## Knife4j 接口文档

任一业务服务启动后访问：`http://localhost:{端口}/doc.html`

## 数据库设计约定

来自建表脚本，代码层须遵循：

1. 主键统一雪花 ID（MyBatis-Plus `id-type=assign_id`），**禁止自增**；
2. 所有查询带 `WHERE deleted = 0`（MyBatis-Plus 逻辑删除字段 `deleted`）；
3. `create_time / update_time` 由数据库 `DEFAULT CURRENT_TIMESTAMP` 自动维护；
4. `time_slot`、`order` 为热点表，已建联合索引，承接高并发预约防超卖。

## 后续规划（暂未创建）

- `pc-admin-vue`：PC 商家后台（Vue3 + Element Plus）
- `uniapp-biz`：商家移动端（UniApp）
- `common` 公共模块：`Result` / 全局异常 / BaseEntity 等当前为各模块内联副本，后续可抽取统一。

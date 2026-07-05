# Flyway 迭代脚本目录

后续对表结构的增量变更（新增表、加字段、加索引等）以 Flyway 版本脚本形式放在本目录。

## 命名规范

```
V{版本号}__{描述}.sql      # 版本迁移，执行一次，例：V1__init_baseline.sql
U{版本号}__{描述}.sql      # 回滚（可选）
```

- 版本号全局递增，不重复、不跳号回填；
- 描述用下划线分隔，如 `V2__add_user_openid.sql`。

## 与 schema 目录的关系

- `schema/nail_platform_all.sql` 是**全量基线建表脚本**（14 张表），首次部署时手动执行；
- 本目录用于**增量迭代**。若启用 Flyway 自动迁移，请：
  1. 在父 pom 或本模块引入 `flyway-core` + `flyway-mysql`；
  2. 配置 `spring.flyway.baseline-on-migrate=true`、`spring.flyway.locations=classpath:db/migration`；
  3. 首次将基线脚本复制为 `V1__init_baseline.sql` 并对已有库执行 `baseline`，避免重复建表。

> 当前骨架未引入 Flyway 依赖，保持"手动执行 schema + 按需启用 Flyway"的灵活策略。

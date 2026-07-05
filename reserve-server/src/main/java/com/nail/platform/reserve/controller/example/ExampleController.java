package com.nail.platform.reserve.controller.example;

import com.nail.common.annotation.LogRecord;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * LogRecord 注解使用示例
 * 展示如何在 Controller 中使用 @LogRecord 注解自动记录操作日志
 *
 * @author nail-platform
 */
@RestController
@RequestMapping("/example")
public class ExampleController {

    /**
     * 示例1：基本使用
     * 只记录模块、操作类型、描述
     */
    @PostMapping("/user")
    @LogRecord(module = "用户管理", operation = "新增用户", description = "创建新用户")
    public Map<String, Object> createUser(@RequestBody Map<String, Object> user) {
        // 业务逻辑...
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "创建成功");
        result.put("data", user);
        return result;
    }

    /**
     * 示例2：记录请求参数和响应结果
     */
    @PutMapping("/user/{id}")
    @LogRecord(
        module = "用户管理",
        operation = "更新用户",
        description = "更新用户基本信息",
        logParams = true,
        logResult = true,
        logCostTime = true
    )
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> user) {
        // 业务逻辑...
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "更新成功");
        result.put("data", user);
        return result;
    }

    /**
     * 示例3：不记录请求参数（参数可能包含敏感信息）
     */
    @PostMapping("/user/password")
    @LogRecord(
        module = "用户管理",
        operation = "修改密码",
        description = "用户修改登录密码",
        logParams = false  // 不记录密码参数
    )
    public Map<String, Object> updatePassword(@RequestBody Map<String, Object> params) {
        // 业务逻辑...
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "密码修改成功");
        return result;
    }

    /**
     * 示例4：设置日志级别为WARN
     * 用于重要的操作或可能有问题的情况
     */
    @DeleteMapping("/user/{id}")
    @LogRecord(
        module = "用户管理",
        operation = "删除用户",
        description = "删除用户账号（谨慎操作）",
        level = LogRecord.LogLevel.WARN
    )
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        // 业务逻辑...
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "删除成功");
        return result;
    }

    /**
     * 示例5：查询操作，使用DEBUG级别
     */
    @GetMapping("/user/{id}")
    @LogRecord(
        module = "用户管理",
        operation = "查询用户",
        description = "根据ID查询用户信息",
        level = LogRecord.LogLevel.DEBUG
    )
    public Map<String, Object> getUser(@PathVariable Long id) {
        // 业务逻辑...
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");

        // Java 8 兼容写法
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", "张三");
        result.put("data", data);
        return result;
    }

    /**
     * 示例6：批量操作
     */
    @PostMapping("/user/batch")
    @LogRecord(
        module = "用户管理",
        operation = "批量导入",
        description = "批量导入用户数据",
        logParams = true,
        logResult = true,
        logCostTime = true
    )
    public Map<String, Object> batchImportUsers(@RequestBody Map<String, Object> params) {
        // 业务逻辑...
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "导入成功");
        result.put("count", 100);
        return result;
    }
}
package com.nail.platform.reserve.controller;

import com.nail.platform.reserve.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查 / 联通性测试。
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>(4);
        data.put("service", "reserve-server");
        data.put("status", "UP");
        return Result.ok(data);
    }
}

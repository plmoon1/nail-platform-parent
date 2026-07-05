package com.nail.platform.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.nail.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录示例。
 * 实际项目应调用 user-server 校验手机号 + 验证码/密码，登录成功后用 Sa-Token 颁发令牌，
 * 通过 Redis（sa-token-redis-jackson）共享给网关与各业务服务。
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestParam String phone,
                                             @RequestParam String code) {
        // TODO: 调用 user-server 校验手机号与验证码 / 密码
        long mockUserId = 1001L;
        StpUtil.login(mockUserId);
        Map<String, Object> data = new HashMap<>(4);
        data.put("token", StpUtil.getTokenValue());
        data.put("userId", mockUserId);
        return Result.ok("登录成功", data);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.ok();
    }

    @GetMapping("/isLogin")
    public Result<Boolean> isLogin() {
        return Result.ok(StpUtil.isLogin());
    }
}
package com.nail.platform.auth.controller;

import com.nail.common.annotation.SkipAuth;
import com.nail.common.result.Result;
import com.nail.platform.auth.dto.UserLoginRequest;
import com.nail.platform.auth.service.RegisterService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Pattern;

/**
 * 注册接口控制器
 */
@RestController
@RequestMapping("/auth")
public class RegisterController {

    @Resource
    private RegisterService registerService;

    /**
     * 用户手机号注册接口
     * 无需登录鉴权
      */
    @SkipAuth
    @PostMapping("/register")
    public Result<?> register(@RequestBody @Validated UserLoginRequest userLoginRequest){
        return registerService.register(userLoginRequest);
    }
}

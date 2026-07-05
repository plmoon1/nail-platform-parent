package com.nail.platform.auth.service;

import com.nail.common.result.Result;
import com.nail.platform.auth.dto.UserLoginRequest;

/**
 * 用户注册接口
 */
public interface RegisterService {

    /**
     * 手机号账号注册
     * @param request 注册入参
     * @return 统一返回
     */
    Result<?> register(UserLoginRequest request);


}

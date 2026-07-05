package com.nail.platform.auth.dto;

import com.nail.common.constant.GlobalConstant;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserLoginRequest {
    /**
     * 手机号，正则校验11为数字
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = GlobalConstant.REGEX_PHONE,message = "手机格式错误")
    private String phone;

    /**
     * 密码8-20位
     */
    @NotBlank(message = "密码不能为空")
    @Size(min= 8, max= 20,message = "密码长度8-20位")
    private String password;

    /**
     * 用户类型: 1顾客， 2美甲师， 3门店管理员(注册时必传，登陆可不用)
     */
    private Integer userType;

    /**
     * 昵称（注册选填，登录不用传)
     */
    private String nickname;
}

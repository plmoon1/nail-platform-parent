package com.nail.platform.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.nail.common.constant.GlobalConstant;
import com.nail.common.mybatisplus.BaseEntity;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class User extends BaseEntity {
    /**
     * 用户手机号，唯一登陆账号
     */
    private String phone;
    /**
     * 加密后的密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 用户类型:1顾客 2美甲师 3门店管理员
     */
    private Integer userType;
    /**
     * 性别 0未知 1女 2男
     */
    private Integer gender;
    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime localDateTime;
    /**
     * 帐号状态 0禁用 1正常
     */
    private Integer status;

    //默认赋值
    public void fillDefaultUserInfo(){
        //默认性别:未知
        this.gender = GlobalConstant.GENDER_UNKNOWN;
        //默认帐号状态:启用
        this.status = GlobalConstant.STATUS_ENABLED;
        //默认头像
        this.avatar = GlobalConstant.DEFAULT_AVATAR;
    }
}

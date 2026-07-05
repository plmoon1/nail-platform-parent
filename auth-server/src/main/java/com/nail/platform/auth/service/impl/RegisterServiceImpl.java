package com.nail.platform.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nail.common.constant.GlobalConstant;
import com.nail.common.exception.GlobalException;
import com.nail.common.result.Result;
import com.nail.common.util.PasswordUtil;
import com.nail.common.util.SnowIdUtil;
import com.nail.platform.auth.dto.UserLoginRequest;
import com.nail.platform.auth.entity.User;
import com.nail.platform.auth.mapper.UserMapper;
import com.nail.platform.auth.service.RegisterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 注册业务实现层
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private UserMapper userMapper;


    @Override
    public Result<?> register(UserLoginRequest userLoginRequest){
        //1. 校验密码强度
        if(!PasswordUtil.isValid(userLoginRequest.getPassword())){
            throw new GlobalException("密码需要8-20，同时包含字母与数字");
        }

        //判断手机号是否已注册（仅查询未逻辑删除正常用户）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,userLoginRequest.getPhone());
        queryWrapper.eq(User::getDeleted, GlobalConstant.DELETED_NO);
        Long existCount = userMapper.selectCount(queryWrapper);
        if(existCount > GlobalConstant.ZERO){
            throw new GlobalException("该手机号已经注册，请直接登录");
        }

        //3.组装用户实体
        User user = new User();
        user.setId(SnowIdUtil.generate());
        user.setPhone(userLoginRequest.getPhone());
        user.setPassword(PasswordUtil.encrypt(userLoginRequest.getPassword()));
        user.setUserType(userLoginRequest.getUserType());
        //昵称为空时默认使用手机号
        String nick = userLoginRequest.getNickname();
        user.setNickname(nick == null|| nick.trim().isEmpty() ? userLoginRequest.getPhone() : nick);
        //填充性别，状态，头像默认常量
        user.fillDefaultUserInfo();

        //4.插入数据库
        int insertRow = userMapper.insert(user);
        if(insertRow <= GlobalConstant.ZERO){
            throw new GlobalException("注册失败，请稍后重试");
        }
        return Result.success("注册成功");




    }

}

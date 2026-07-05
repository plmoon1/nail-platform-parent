package com.nail.platform.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nail.platform.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

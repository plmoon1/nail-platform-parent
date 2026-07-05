package com.nail.platform.reserve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类：与建表脚本统一的公共字段。
 * - id：雪花主键（ASSIGN_ID）
 * - create_time / update_time：由数据库 DEFAULT CURRENT_TIMESTAMP 自动维护
 * - deleted：逻辑删除（@TableLogic，查询自动拼接 deleted = 0）
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}

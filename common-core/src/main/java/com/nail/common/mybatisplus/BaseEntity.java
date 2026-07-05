package com.nail.common.mybatisplus;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 所有数据库实体父类
 * 包含所有表的公共字段：id、createTime、updateTime、deleted、extJson
 * 所有实体类都应继承此基类
 *
 * @author nail-platform
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID（雪花算法生成）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标志（0-未删除，1-已删除）
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 扩展JSON字段
     * 用于存储额外的扩展信息，避免频繁修改表结构
     * 示例：{"remark": "备注", "tags": ["标签1", "标签2"]}
     */
    @TableField(value = "ext_json", updateStrategy = FieldStrategy.IGNORED)
    private String extJson;

    /**
     * 获取ID
     * 子类可以覆盖此方法提供自定义ID生成逻辑
     *
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     * 子类可以覆盖此方法提供自定义ID设置逻辑
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 判断是否为新记录
     *
     * @return 是否为新记录
     */
    public boolean isNew() {
        return this.id == null;
    }

    /**
     * 判断是否已删除
     *
     * @return 是否已删除
     */
    public boolean isDeleted() {
        return this.deleted != null && this.deleted == 1;
    }

    /**
     * 标记为已删除
     */
    public void markAsDeleted() {
        this.deleted = 1;
    }

    /**
     * 标记为未删除
     */
    public void markAsUndeleted() {
        this.deleted = 0;
    }

    /**
     * 获取创建时间戳（秒）
     *
     * @return 时间戳
     */
    public Long getCreateTimeTimestamp() {
        if (createTime == null) {
            return null;
        }
        return createTime.toEpochSecond(java.time.ZoneOffset.of("+8"));
    }

    /**
     * 获取更新时间戳（秒）
     *
     * @return 时间戳
     */
    public Long getUpdateTimeTimestamp() {
        if (updateTime == null) {
            return null;
        }
        return updateTime.toEpochSecond(java.time.ZoneOffset.of("+8"));
    }
}

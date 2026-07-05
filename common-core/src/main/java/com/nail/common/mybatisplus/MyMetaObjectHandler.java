package com.nail.common.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 元数据自动填充处理器
 * 自动填充创建时间、更新时间等公共字段
 *
 * @author nail-platform
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     * 填充字段：createTime、updateTime、deleted
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充...");

        // 填充创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());

        // 填充更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        // 填充逻辑删除标志（默认未删除）
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);

        log.debug("插入填充完成：createTime={}, updateTime={}, deleted={}",
                LocalDateTime.now(), LocalDateTime.now(), 0);
    }

    /**
     * 更新时自动填充
     * 填充字段：updateTime
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新填充...");

        // 填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        log.debug("更新填充完成：updateTime={}", LocalDateTime.now());
    }

    /**
     * 获取当前时间
     * 可扩展为获取用户ID等
     *
     * @return 当前时间
     */
    protected LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前用户ID
     * 从Spring Security上下文或Sa-Token中获取
     * 可在子类中扩展实现
     *
     * @return 用户ID
     */
    protected Long getCurrentUserId() {
        // TODO: 集成Sa-Token后实现
        // return StpUtil.getLoginIdAsLong();
        return null;
    }
}

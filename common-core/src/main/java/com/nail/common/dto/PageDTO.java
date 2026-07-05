package com.nail.common.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 通用分页入参
 * 所有分页接口统一使用此 DTO 接收参数
 *
 * @author nail-platform
 */
@Data
public class PageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 默认页码
     */
    private static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页条数
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大每页条数
     */
    private static final int MAX_PAGE_SIZE = 100;

    /**
     * 页码（从1开始）
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = DEFAULT_PAGE_NUM;

    /**
     * 每页条数
     */
    @Min(value = 1, message = "每页条数必须大于0")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 获取当前页码（防止空值）
     */
    public int getPageNum() {
        return pageNum == null ? DEFAULT_PAGE_NUM : pageNum;
    }

    /**
     * 获取每页条数（防止空值，并限制最大值）
     */
    public int getPageSize() {
        if (pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    /**
     * 计算偏移量（用于数据库分页查询）
     */
    public int getOffset() {
        return (getPageNum() - 1) * getPageSize();
    }
}

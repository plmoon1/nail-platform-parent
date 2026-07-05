package com.nail.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 单ID通用入参
 * 所有单ID删除/详情接口统一使用此 DTO
 *
 * @author nail-platform
 */
@Data
public class IdDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Long 类型 ID（用于数据库实体ID）
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * String 类型 ID（用于业务ID，如订单号）
     */
    @NotBlank(message = "ID不能为空")
    private String idStr;

    /**
     * 构造函数（Long ID）
     */
    public IdDTO(Long id) {
        this.id = id;
    }

    /**
     * 构造函数（String ID）
     */
    public IdDTO(String idStr) {
        this.idStr = idStr;
    }

    public IdDTO() {
    }

    /**
     * 获取有效ID（优先返回 Long 类型）
     */
    public Long getValidId() {
        if (id != null) {
            return id;
        }
        if (idStr != null && !idStr.isEmpty()) {
            try {
                return Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取有效ID字符串
     */
    public String getValidIdStr() {
        if (idStr != null && !idStr.isEmpty()) {
            return idStr;
        }
        if (id != null) {
            return String.valueOf(id);
        }
        return null;
    }
}

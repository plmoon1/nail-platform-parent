package com.nail.common.annotation;

import java.lang.annotation.*;

/**
 * 跳过登录鉴权注解
 * 标注在Controller方法上，表示该接口不需要登录即可访问
 *
 * 使用示例：
 * <pre>
 * {@code
 * @GetMapping("/public/api")
 * @SkipAuth
 * public Result<?> publicApi() {
 *     return Result.success();
 * }
 * }
 * </pre>
 *
 * @author nail-platform
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SkipAuth {

    /**
     * 描述（可选）
     * 用于说明为什么跳过鉴权
     *
     * @return 描述信息
     */
    String value() default "";

    /**
     * 跳过Token校验
     * 默认为true，表示跳过Token校验
     *
     * @return 是否跳过Token校验
     */
    boolean skipToken() default true;

    /**
     * 跳过权限校验
     * 默认为false，表示不跳过权限校验（仅跳过登录校验）
     *
     * @return 是否跳过权限校验
     */
    boolean skipPermission() default false;
}

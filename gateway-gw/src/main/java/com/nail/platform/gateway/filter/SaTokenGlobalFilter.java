package com.nail.platform.gateway.filter;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局鉴权过滤器（响应式 Sa-Token）。
 * 1. 白名单路径直接放行（登录、注册、接口文档、健康检查）。
 * 2. 其余路径要求已登录，未登录返回 401。
 * 3. 校验通过后把登录用户 ID 透传给下游服务（请求头 loginUser）。
 */
@Component
public class SaTokenGlobalFilter implements GlobalFilter, Ordered {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /** 放行路径（网关层路径，含 /api/{服务} 前缀） */
    private static final String[] WHITELIST = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/*/doc.html",
            "/api/*/swagger-ui/**",
            "/api/*/v3/api-docs/**",
            "/api/*/webjars/**",
            "/actuator/**"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        for (String pattern : WHITELIST) {
            if (MATCHER.match(pattern, path)) {
                return chain.filter(exchange);
            }
        }
        try {
            StpUtil.checkLogin();
        } catch (NotLoginException e) {
            return unauthorized(exchange.getResponse(), e.getMessage());
        }
        // 透传登录用户信息给下游
        return chain.filter(exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("loginUser", String.valueOf(StpUtil.getLoginIdDefaultNull()))
                        .build())
                .build());
    }

    private Mono<Void> unauthorized(ServerHttpResponse response, String msg) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = new HashMap<>(4);
        body.put("code", 401);
        body.put("message", msg != null ? msg : "未登录");
        body.put("data", null);
        byte[] bytes;
        try {
            bytes = OBJECT_MAPPER.writeValueAsBytes(body);
        } catch (JsonProcessingException ex) {
            bytes = "{\"code\":401,\"message\":\"未登录\"}".getBytes(StandardCharsets.UTF_8);
        }
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

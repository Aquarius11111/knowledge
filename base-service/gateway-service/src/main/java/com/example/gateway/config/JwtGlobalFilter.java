package com.example.gateway.config;


import com.auth0.jwt.JWT;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private TokenUtils tokenUtils;

    private static final String TOKEN_HEADER = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 放行登录和注册请求
        if (path.contains("/login") || path.contains("/register")
                || path.contains("/forgot")) {
            return chain.filter(exchange);
        }

        // 从请求头中获取 token
        String token = exchange.getRequest().getHeaders().getFirst(TOKEN_HEADER);
        if (token == null || !tokenUtils.verifyToken(token)) {
            // token 无效，返回 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            // 可选：你可以解析 token 添加用户信息到 header 中，传给下游服务
            String audience = JWT.decode(token).getAudience().get(0);
            String[] parts = audience.split("-");
            String account = parts[0];
            String role = parts.length > 1 ? parts[1] : "USER";

            // 重写请求，加上用户信息头
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(builder -> builder
                            .header("X-User-Account", account)
                            .header("X-User-Role", role)
                    ).build();

            return chain.filter(modifiedExchange);

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    // 优先级越小越靠前
    @Override
    public int getOrder() {
        return -100;
    }
}

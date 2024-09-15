package cn.cutepikachu.gateway;

import cn.cutepikachu.gateway.util.IpUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-09 23:21-56
 */
@Component
public class GlobalGatewayFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 获取客户端 IP 地址
        String clientIp = IpUtils.getIpAddr(request);
        // 将 IP 地址添加到请求头
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Client-IP", clientIp)
                .build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

}

package cn.cutepikachu.gateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetSocketAddress;

/**
 * IP 工具（网关使用）
 * <p>
 * 部分方法参考来自 cn.hutool.extra.servlet.JakartaServletUtil 类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";

    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    /**
     * 获取 IP 地址
     *
     * @param exchange 服务器网络交换
     * @return IP 地址
     */
    public static String getIpAddr(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String ip;
        for (String header : IP_HEADERS) {
            ip = request.getHeaders().getFirst(header);
            if (isUnknown(ip)) {
                return getMultistageReverseProxyIp(ip);
            }
        }

        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress == null) {
            return null;
        }

        ip = remoteAddress.getHostString();
        return getMultistageReverseProxyIp(ip);
    }

    /**
     * 从多级反向代理中获得第一个非 unknown 的 IP 地址
     *
     * @param ip 获得的 IP 地址
     * @return 第一个非 unknown 的 IP 地址
     */
    private static String getMultistageReverseProxyIp(String ip) {
        if (ip != null && ip.indexOf(",") > 0) {
            final String[] ips = ip.split(",");
            for (final String subIp : ips) {
                if (!isUnknown(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 判断 IP 地址是否未知
     *
     * @param ip IP 地址
     * @return 是否未知
     */
    private static boolean isUnknown(String ip) {
        return ip == null || ip.isBlank() || UNKNOWN.equalsIgnoreCase(ip);
    }

}

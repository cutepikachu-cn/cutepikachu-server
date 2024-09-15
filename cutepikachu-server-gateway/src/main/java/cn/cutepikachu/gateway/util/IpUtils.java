package cn.cutepikachu.gateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * IP 工具
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";

    /**
     * 获取 IP 地址
     *
     * @return IP 地址
     */
    public static String getIpAddr(ServerHttpRequest request) {
        String ip = getHeaderIp(request);
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip != null ? ip.split(",")[0].trim() : "";
    }

    private static String getHeaderIp(ServerHttpRequest request) {
        String[] headers = {
                "x-forwarded-for",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
        };

        for (String header : headers) {
            String ip = request.getHeaders().getFirst(header);
            if (ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        return null;
    }

}

package cn.cutepikachu.common.security.config;

import cn.dev33.satoken.fun.SaFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-09 22:57-15
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Resource
    private SecurityConfiguration securityConfiguration;

    private static final Map<String, SaFunction> CHECK_FUNCTIONS = Map.of(
            "login", StpUtil::checkLogin
    );

    // 注册 Sa-Token 拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Map<String, List<SecurityConfiguration.Router>> routers = securityConfiguration.getRouters();
        registry.addInterceptor(new SaInterceptor(
                        handle -> routers.forEach((key, routerList) ->
                                routerList.forEach(router -> SaRouter.match(router.getMatch())
                                        .notMatch(router.getNotMatch())
                                        .check(r -> {
                                            if (router.getCheck() != null) {
                                                CHECK_FUNCTIONS.get(router.getCheck()).run();
                                            }
                                        })
                                        .check(r -> {
                                            if (router.getRole() != null) {
                                                StpUtil.checkRole(router.getRole().getText());
                                            }
                                        })))))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/inner/**",
                        "/doc.html",
                        "/v3/api-docs",
                        "/v3/api-docs/swagger-config",
                        "/v3/api-docs/**",
                        "/swagger-ui/index.html",
                        "/favicon.ico"
                );
    }
}

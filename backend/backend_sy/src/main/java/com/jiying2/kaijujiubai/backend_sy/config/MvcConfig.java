package com.jiying2.kaijujiubai.backend_sy.config;

import com.jiying2.kaijujiubai.backend_sy.filter.LoginInterceptor;
import com.jiying2.kaijujiubai.backend_sy.filter.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        //跨域配置，不可设置为*，不安全, 前后端分离项目，可能域名不一致
//        //本地测试 端口不一致 也算跨域
//        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0);
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
//
                        "/users/login",
                        "/users/register",
                        "/users/sendCode",
//
                        "/employee/login",
                        "/employee/register",
                        "/employee/sendCode",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/**",
                        "/swagger-ui.html/**",
                        "/store/**",
                        "/resources/**"
                )
                .order(1);
    }
}

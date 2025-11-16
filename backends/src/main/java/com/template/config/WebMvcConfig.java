package com.template.config;

import com.template.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web MVC 配置
 *
 * @author template
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Swagger 相关资源
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        // 静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // 文件上传资源映射（使用绝对路径）
        String projectPath = System.getProperty("user.dir");
        String uploadPath = projectPath + File.separator + "upload" + File.separator;
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + uploadPath);
    }

    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // JWT认证拦截器
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有请求
                .addPathPatterns("/**")
                // 排除不需要认证的路径
                .excludePathPatterns(
                        // 认证接口
                        "/auth/login",
                        "/auth/register",
                        // 患者端接口
                        "/patient/register",
                        "/patient/login",
                        "/appointment/schedules",
                        // 职工端接口
                        "/staff/login",
                        // 测试接口（仅开发环境使用）
                        "/test/**",
                        // Swagger相关
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/**",
                        "/v3/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/doc.html",
                        "/favicon.ico",
                        // 静态资源
                        "/static/**",
                        "/upload/**",
                        "/index.html",
                        // 错误页面
                        "/error"
                );
    }
}


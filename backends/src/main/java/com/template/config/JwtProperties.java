package com.template.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性类
 * 使用 @Value 注解从 application.yml 中读取配置
 *
 * @author template
 */
@Data
@Component
public class JwtProperties {

    /**
     * 密钥（至少32位）
     * 从配置文件读取：jwt.secret
     */
    @Value("${jwt.secret:your-secret-key-must-be-at-least-256-bits-long-for-hs256}")
    private String secret;

    /**
     * 过期时间（单位：毫秒，默认7天）
     * 从配置文件读取：jwt.expiration
     */
    @Value("${jwt.expiration:604800000}")
    private Long expiration;
}


package com.template.controller;

import com.template.common.Result;
import com.template.util.PasswordUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器（仅用于开发环境）
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 生成密码哈希
     */
    @GetMapping("/generate-password")
    public Result<Map<String, String>> generatePassword(@RequestParam String password) {
        String hash = PasswordUtil.encode(password);
        
        Map<String, String> result = new HashMap<>();
        result.put("原始密码", password);
        result.put("BCrypt哈希", hash);
        result.put("哈希长度", String.valueOf(hash.length()));
        
        // 立即验证
        boolean valid = PasswordUtil.matches(password, hash);
        result.put("验证结果", valid ? "✓ 成功" : "✗ 失败");
        
        // 生成SQL
        String sql = "UPDATE t_staff SET PasswordHash = '" + hash + "' WHERE LoginName = 'admin';";
        result.put("更新SQL", sql);
        
        return Result.success(result);
    }

    /**
     * 验证密码
     */
    @GetMapping("/verify-password")
    public Result<Map<String, Object>> verifyPassword(
            @RequestParam String password,
            @RequestParam String hash) {
        
        boolean matches = PasswordUtil.matches(password, hash);
        
        Map<String, Object> result = new HashMap<>();
        result.put("原始密码", password);
        result.put("密码哈希", hash);
        result.put("验证结果", matches);
        result.put("消息", matches ? "✓ 密码匹配" : "✗ 密码不匹配");
        
        return Result.success(result);
    }
}


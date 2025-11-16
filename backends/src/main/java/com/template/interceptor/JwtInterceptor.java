package com.template.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.common.Result;
import com.template.common.ResultCode;
import com.template.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 拦截器
 * 用于验证请求头中的 Token
 *
 * @author template
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 处理跨域预检请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 从请求头获取 Token
        String token = request.getHeader(JwtUtil.HEADER_STRING);

        // 如果没有 Token 或格式不正确
        if (!StringUtils.hasText(token)) {
            writeErrorResponse(response, ResultCode.PERMISSION_TOKEN_INVALID);
            return false;
        }

        // 去除 Bearer 前缀
        if (token.startsWith(JwtUtil.TOKEN_PREFIX)) {
            token = token.substring(JwtUtil.TOKEN_PREFIX.length());
        }

        // 验证 Token
        if (!JwtUtil.validateToken(token)) {
            // Token 无效或已过期
            if (JwtUtil.isTokenExpired(token)) {
                writeErrorResponse(response, ResultCode.PERMISSION_TOKEN_EXPIRED);
            } else {
                writeErrorResponse(response, ResultCode.PERMISSION_TOKEN_INVALID);
            }
            return false;
        }

        // Token 有效，将用户信息存入请求属性中
        Long userId = JwtUtil.getUserIdFromToken(token);
        String username = JwtUtil.getUsernameFromToken(token);
        
        // 将Long类型的userId转换为Integer类型（因为Patient表的ID是Integer）
        request.setAttribute("userId", userId != null ? userId.intValue() : null);
        request.setAttribute("username", username);

        return true;
    }

    /**
     * 写入错误响应
     */
    private void writeErrorResponse(HttpServletResponse response, ResultCode resultCode) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        Result<?> result = Result.fail(resultCode);
        String json = objectMapper.writeValueAsString(result);
        
        response.getWriter().write(json);
    }
}


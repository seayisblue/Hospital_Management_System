package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 职工登录请求
 *
 * @author template
 */
@Data
public class StaffLoginRequest {

    /**
     * 登录账号
     */
    @NotBlank(message = "登录账号不能为空")
    private String loginName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}


package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 患者登录请求DTO
 *
 * @author template
 */
@Data
public class PatientLoginRequest {

    /**
     * 手机号或身份证号
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}


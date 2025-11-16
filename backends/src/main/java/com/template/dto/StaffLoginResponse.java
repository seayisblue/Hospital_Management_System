package com.template.dto;

import lombok.Data;

/**
 * 职工登录响应
 *
 * @author template
 */
@Data
public class StaffLoginResponse {

    /**
     * Token
     */
    private String token;

    /**
     * 职工ID
     */
    private Integer staffId;

    /**
     * 职工姓名
     */
    private String staffName;

    /**
     * 角色
     */
    private String role;

    /**
     * 登录账号
     */
    private String loginName;
}


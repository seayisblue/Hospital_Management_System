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
    /**
     * 职称
     */
    private String title;

    /**
     * 科室名称
     */
    private String deptName;

    private Integer deptId;   // 科室ID
    private Boolean isDeptManager; // 是否是科室主任


}


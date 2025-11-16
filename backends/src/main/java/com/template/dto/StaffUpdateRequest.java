package com.template.dto;

import lombok.Data;

/**
 * 职工更新请求
 *
 * @author template
 */
@Data
public class StaffUpdateRequest {

    /**
     * 职工姓名
     */
    private String staffName;

    /**
     * 所属科室ID
     */
    private Integer deptId;

    /**
     * 角色(医生、药剂师、管理员)
     */
    private String role;

    /**
     * 职称
     */
    private String title;

    /**
     * 登录密码（可选，如果要修改密码）
     */
    private String password;
}


package com.template.dto;

import lombok.Data;

/**
 * 职工查询请求
 *
 * @author template
 */
@Data
public class StaffQueryRequest {

    /**
     * 职工姓名（模糊查询）
     */
    private String staffName;

    /**
     * 科室ID
     */
    private Integer deptId;

    /**
     * 角色
     */
    private String role;

    /**
     * 登录账号（模糊查询）
     */
    private String loginName;

    /**
     * 当前页
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}


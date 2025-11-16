package com.template.vo;

import lombok.Data;

/**
 * 职工信息VO
 *
 * @author template
 */
@Data
public class StaffVO {

    /**
     * 职工ID
     */
    private Integer staffId;

    /**
     * 职工姓名
     */
    private String staffName;

    /**
     * 所属科室ID
     */
    private Integer deptId;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 角色(医生、药剂师、管理员)
     */
    private String role;

    /**
     * 职称
     */
    private String title;

    /**
     * 登录账号
     */
    private String loginName;
}


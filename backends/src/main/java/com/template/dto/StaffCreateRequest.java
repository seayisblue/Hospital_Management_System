package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 职工创建请求
 *
 * @author template
 */
@Data
public class StaffCreateRequest {

    /**
     * 职工姓名
     */
    @NotBlank(message = "职工姓名不能为空")
    private String staffName;

    /**
     * 所属科室ID
     */
    private Integer deptId;

    /**
     * 角色(医生、药剂师、管理员)
     */
    @NotBlank(message = "角色不能为空")
    private String role;

    /**
     * 职称
     */
    private String title;

    /**
     * 登录账号
     */
    @NotBlank(message = "登录账号不能为空")
    private String loginName;

    /**
     * 登录密码
     */
    @NotBlank(message = "登录密码不能为空")
    private String password;
}


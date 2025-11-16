package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 职工实体类
 *
 * @author template
 */
@Data
@TableName("T_Staff")
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职工ID
     */
    @TableId(value = "StaffID", type = IdType.AUTO)
    private Integer staffId;

    /**
     * 职工姓名
     */
    @TableField("StaffName")
    private String staffName;

    /**
     * 所属科室ID
     */
    @TableField("DeptID")
    private Integer deptId;

    /**
     * 角色(医生、药剂师、管理员)
     */
    @TableField("Role")
    private String role;

    /**
     * 职称
     */
    @TableField("Title")
    private String title;

    /**
     * 登录账号
     */
    @TableField("LoginName")
    private String loginName;

    /**
     * 登录密码(哈希值)
     */
    @TableField("PasswordHash")
    private String passwordHash;
}


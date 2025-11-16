package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者实体类
 *
 * @author template
 */
@Data
@TableName("T_Patient")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 患者ID
     */
    @TableId(value = "PatientID", type = IdType.AUTO)
    private Integer patientId;

    /**
     * 姓名
     */
    @TableField("PatientName")
    private String patientName;

    /**
     * 性别
     */
    @TableField("Gender")
    private String gender;

    /**
     * 出生日期
     */
    @TableField("DateOfBirth")
    private LocalDate dateOfBirth;

    /**
     * 身份证号
     */
    @TableField("IDCardNumber")
    private String idCardNumber;

    /**
     * 手机号
     */
    @TableField("PhoneNumber")
    private String phoneNumber;

    /**
     * 登录密码(哈希值)
     */
    @TableField("PasswordHash")
    private String passwordHash;

    /**
     * 住址
     */
    @TableField("Address")
    private String address;

    /**
     * 建档时间
     */
    @TableField(value = "CreateTime", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}


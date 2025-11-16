package com.template.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者视图对象VO
 *
 * @author template
 */
@Data
public class PatientVO {

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 姓名
     */
    private String patientName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    private LocalDate dateOfBirth;

    /**
     * 身份证号(脱敏)
     */
    private String idCardNumber;

    /**
     * 手机号(脱敏)
     */
    private String phoneNumber;

    /**
     * 住址
     */
    private String address;

    /**
     * 建档时间
     */
    private LocalDateTime createTime;
}


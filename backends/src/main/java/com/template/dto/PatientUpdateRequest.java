package com.template.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 患者信息更新请求DTO
 *
 * @author template
 */
@Data
public class PatientUpdateRequest {

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
     * 手机号
     */
    private String phoneNumber;

    /**
     * 住址
     */
    private String address;
}


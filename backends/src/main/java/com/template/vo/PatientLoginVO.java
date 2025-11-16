package com.template.vo;

import lombok.Data;

/**
 * 患者登录响应VO
 *
 * @author template
 */
@Data
public class PatientLoginVO {

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 姓名
     */
    private String patientName;

    /**
     * Token
     */
    private String token;

    /**
     * 手机号
     */
    private String phoneNumber;
}


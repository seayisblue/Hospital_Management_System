package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * 患者注册请求DTO
 *
 * @author template
 */
@Data
public class PatientRegisterRequest {

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String patientName;

    /**
     * 性别(男/女)
     */
    private String gender;

    /**
     * 出生日期
     */
    private LocalDate dateOfBirth;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$", 
             message = "身份证号格式不正确")
    private String idCardNumber;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phoneNumber;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 住址
     */
    private String address;
}


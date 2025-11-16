package com.template.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 医生端就诊列表VO
 *
 * @author template
 */
@Data
public class DoctorAppointmentVO {

    /**
     * 挂号ID
     */
    private Integer appointmentId;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者性别
     */
    private String patientGender;

    /**
     * 患者年龄
     */
    private Integer patientAge;

    /**
     * 患者电话
     */
    private String patientPhone;

    /**
     * 挂号时间
     */
    private LocalDateTime appointmentTime;

    /**
     * 排班日期
     */
    private String scheduleDate;

    /**
     * 时段
     */
    private String timeSlot;

    /**
     * 状态
     */
    private String status;

    /**
     * 是否有病历
     */
    private Boolean hasMedicalRecord;
}


package com.template.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 挂号详情VO（管理后台使用）
 *
 * @author template
 */
@Data
public class AppointmentDetailVO {

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
     * 患者电话
     */
    private String patientPhone;

    /**
     * 排班ID
     */
    private Integer scheduleId;

    /**
     * 医生ID
     */
    private Integer staffId;

    /**
     * 医生姓名
     */
    private String staffName;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 排班日期
     */
    private LocalDate scheduleDate;

    /**
     * 时段
     */
    private String timeSlot;

    /**
     * 挂号状态
     */
    private String status;

    /**
     * 挂号时间
     */
    private LocalDateTime createTime;
}


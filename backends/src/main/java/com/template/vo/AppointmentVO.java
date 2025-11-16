package com.template.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 挂号视图对象VO
 *
 * @author template
 */
@Data
public class AppointmentVO {

    /**
     * 挂号ID
     */
    private Integer appointmentId;

    /**
     * 排班ID
     */
    private Integer scheduleId;

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
     * 医生ID
     */
    private Integer staffId;

    /**
     * 医生姓名
     */
    private String staffName;

    /**
     * 科室ID
     */
    private Integer deptId;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 就诊日期
     */
    private String scheduleDate;

    /**
     * 时段(上午、下午、夜班)
     */
    private String timeSlot;

    /**
     * 挂号时间
     */
    private LocalDateTime appointmentTime;

    /**
     * 创建时间(用于列表显示)
     */
    private LocalDateTime createTime;

    /**
     * 状态(待就诊、已就诊、已取消)
     */
    private String status;

    /**
     * 挂号费
     */
    private BigDecimal fee;
}


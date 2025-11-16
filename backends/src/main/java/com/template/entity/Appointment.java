package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 挂号实体类
 *
 * @author template
 */
@Data
@TableName("T_Appointment")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 挂号ID
     */
    @TableId(value = "AppointmentID", type = IdType.AUTO)
    private Integer appointmentId;

    /**
     * 关联的排班ID
     */
    @TableField("ScheduleID")
    private Integer scheduleId;

    /**
     * 患者ID
     */
    @TableField("PatientID")
    private Integer patientId;

    /**
     * 挂号的医生ID
     */
    @TableField("StaffID")
    private Integer staffId;

    /**
     * 挂号的科室ID
     */
    @TableField("DeptID")
    private Integer deptId;

    /**
     * 挂号时间
     */
    @TableField("AppointmentTime")
    private LocalDateTime appointmentTime;

    /**
     * 状态(待就诊、已就诊、已取消)
     */
    @TableField("Status")
    private String status;

    /**
     * 挂号费
     */
    @TableField("Fee")
    private BigDecimal fee;
}


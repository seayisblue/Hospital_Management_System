package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 病历实体类
 *
 * @author template
 */
@Data
@TableName("T_Medical_Record")
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 病历ID
     */
    @TableId(value = "RecordID", type = IdType.AUTO)
    private Integer recordId;

    /**
     * 关联的挂号ID(一对一)
     */
    @TableField("AppointmentID")
    private Integer appointmentId;

    /**
     * 患者ID
     */
    @TableField("PatientID")
    private Integer patientId;

    /**
     * 主治医生ID
     */
    @TableField("StaffID")
    private Integer staffId;

    /**
     * 主诉(患者自述)
     */
    @TableField("Subjective")
    private String subjective;

    /**
     * 查体(医生检查)
     */
    @TableField("Objective")
    private String objective;

    /**
     * 初步诊断
     */
    @TableField("Assessment")
    private String assessment;

    /**
     * 处理意见/治疗方案
     */
    @TableField("Plan")
    private String plan;

    /**
     * 诊疗时间
     */
    @TableField(value = "CreateTime", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}


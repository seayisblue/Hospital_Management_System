package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 处方实体类
 *
 * @author template
 */
@Data
@TableName("T_Prescription")
public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 处方ID
     */
    @TableId(value = "PrescriptionID", type = IdType.AUTO)
    private Integer prescriptionId;

    /**
     * 关联的病历ID
     */
    @TableField("RecordID")
    private Integer recordId;

    /**
     * 开处方的医生ID
     */
    @TableField("StaffID")
    private Integer staffId;

    /**
     * 患者ID
     */
    @TableField("PatientID")
    private Integer patientId;

    /**
     * 开具日期
     */
    @TableField(value = "PrescriptionDate", fill = FieldFill.INSERT)
    private LocalDateTime prescriptionDate;

    /**
     * 处方状态(未发药、已发药、已作废)
     */
    @TableField("Status")
    private String status;
}


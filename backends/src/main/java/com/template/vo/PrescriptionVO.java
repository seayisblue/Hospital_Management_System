package com.template.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 处方视图对象VO
 *
 * @author template
 */
@Data
public class PrescriptionVO {

    /**
     * 处方ID
     */
    private Integer prescriptionId;

    /**
     * 关联的病历ID
     */
    private Integer recordId;

    /**
     * 医生ID
     */
    private Integer staffId;

    /**
     * 医生姓名
     */
    private String staffName;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 开具日期
     */
    private LocalDateTime prescriptionDate;

    /**
     * 处方状态(未发药、已发药、已作废)
     */
    private String status;
}


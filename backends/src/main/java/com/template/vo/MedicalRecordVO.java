package com.template.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 病历视图对象VO
 *
 * @author template
 */
@Data
public class MedicalRecordVO {

    /**
     * 病历ID
     */
    private Integer recordId;

    /**
     * 关联的挂号ID
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
     * 主治医生ID
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
     * 主诉(患者自述)
     */
    private String subjective;

    /**
     * 查体(医生检查)
     */
    private String objective;

    /**
     * 初步诊断
     */
    private String assessment;

    /**
     * 处理意见/治疗方案
     */
    private String plan;

    /**
     * 诊疗时间
     */
    private LocalDateTime createTime;
}


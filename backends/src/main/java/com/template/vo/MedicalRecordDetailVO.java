package com.template.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 病历详情VO
 *
 * @author template
 */
@Data
public class MedicalRecordDetailVO {

    /**
     * 病历ID
     */
    private Integer recordId;

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
     * 主诉
     */
    private String subjective;

    /**
     * 查体
     */
    private String objective;

    /**
     * 诊断
     */
    private String assessment;

    /**
     * 治疗方案
     */
    private String plan;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}


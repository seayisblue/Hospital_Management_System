package com.template.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 药房处方视图对象
 *
 * @author template
 */
@Data
public class PharmacyPrescriptionVO {

    /**
     * 处方ID
     */
    private Integer prescriptionId;

    /**
     * 病历ID
     */
    private Integer recordId;

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
     * 开具日期
     */
    private LocalDateTime prescriptionDate;

    /**
     * 处方状态
     */
    private String status;

    /**
     * 医嘱说明/注意事项
     */
    private String advice;

    /**
     * 收费单ID
     */
    private Integer billId;

    /**
     * 收费单总金额
     */
    private java.math.BigDecimal billAmount;

    /**
     * 处方明细列表
     */
    private List<PrescriptionDetailVO> details;

    /**
     * 处方明细视图对象
     */
    @Data
    public static class PrescriptionDetailVO {
        /**
         * 明细ID
         */
        private Integer detailId;

        /**
         * 药品ID
         */
        private Integer medicineId;

        /**
         * 药品名称
         */
        private String medicineName;

        /**
         * 规格
         */
        private String specification;

        /**
         * 单位
         */
        private String unit;

        /**
         * 数量
         */
        private Integer quantity;

        /**
         * 单价
         */
        private java.math.BigDecimal unitPrice;

        /**
         * 小计
         */
        private java.math.BigDecimal subtotal;

        /**
         * 用法
         */
        private String usage;

        /**
         * 频次
         */
        private String frequency;

        /**
         * 天数
         */
        private Integer days;
    }
}


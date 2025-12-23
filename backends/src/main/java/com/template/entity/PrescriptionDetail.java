package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 处方明细实体类
 *
 * @author template
 */
@Data
@TableName("t_prescription_item")
public class PrescriptionDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @TableId(value = "ItemID", type = IdType.AUTO)
    private Integer detailId;

    /**
     * 处方ID
     */
    @TableField("PrescriptionID")
    private Integer prescriptionId;

    /**
     * 药品ID
     */
    @TableField("MedicineID")
    private Integer medicineId;

    /**
     * 数量
     */
    @TableField("Quantity")
    private Integer quantity;

    /**
     * 单价
     */
    @TableField("UnitPrice")
    private java.math.BigDecimal unitPrice;

    /**
     * 小计
     */
    @TableField("Subtotal")
    private java.math.BigDecimal subtotal;

    /**
     * 用法
     */
    @TableField("UsageMethod")
    private String usageMethod;

    /**
     * 频次
     */
    @TableField("Frequency")
    private String frequency;

    /**
     * 天数
     */
    @TableField("Days")
    private Integer days;
}


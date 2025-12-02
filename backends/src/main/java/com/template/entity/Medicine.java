package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 药品实体类
 *
 * @author template
 */
@Data
@TableName("T_Medicine")
public class Medicine implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 药品ID
     */
    @TableId(value = "MedicineID", type = IdType.AUTO)
    private Integer medicineId;

    /**
     * 药品名称
     */
    @TableField("MedicineName")
    private String medicineName;

    /**
     * 规格
     */
    @TableField("Specification")
    private String specification;

    /**
     * 生产厂家
     */
    @TableField("Manufacturer")
    private String manufacturer;

    /**
     * 单价
     */
    @TableField("UnitPrice")
    private BigDecimal unitPrice;

    /**
     * 库存数量
     */
    @TableField("StockLevel")
    private Integer stockLevel;

    @TableField("MinStock")
    private Integer minStock;
}


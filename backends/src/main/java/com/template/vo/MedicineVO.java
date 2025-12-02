package com.template.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 药品信息VO
 *
 * @author template
 */
@Data
public class MedicineVO {

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
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 库存数量
     */
    private Integer stockLevel;

    private Integer minStock;
}

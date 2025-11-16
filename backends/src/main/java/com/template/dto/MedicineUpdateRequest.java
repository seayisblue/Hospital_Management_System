package com.template.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 药品更新请求
 *
 * @author template
 */
@Data
public class MedicineUpdateRequest {

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
}


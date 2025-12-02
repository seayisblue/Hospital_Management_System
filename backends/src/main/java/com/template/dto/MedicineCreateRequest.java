package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 药品创建请求
 *
 * @author template
 */
@Data
public class MedicineCreateRequest {

    /**
     * 药品名称
     */
    @NotBlank(message = "药品名称不能为空")
    private String medicineName;

    /**
     * 规格
     */
    private String specification;

    /**
     * 生产厂家
     */
    private String manufacturer;


    private Integer initialStock;

    private Integer minStock;
    /**
     * 单价
     */
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;
}


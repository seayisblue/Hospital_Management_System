package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 药品入库请求
 *
 * @author template
 */
@Data
public class InventoryInboundRequest {

    /**
     * 药品ID
     */
    @NotNull(message = "药品ID不能为空")
    private Integer medicineId;

    /**
     * 入库数量
     */
    @NotNull(message = "入库数量不能为空")
    @Positive(message = "入库数量必须为正数")
    private Integer quantity;

    /**
     * 入库原因
     */
    @NotNull(message = "入库原因不能为空")
    private String reason;
}


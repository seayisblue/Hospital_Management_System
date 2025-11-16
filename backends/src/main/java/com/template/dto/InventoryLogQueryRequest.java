package com.template.dto;

import lombok.Data;

/**
 * 库存流水查询请求
 *
 * @author template
 */
@Data
public class InventoryLogQueryRequest {

    /**
     * 药品ID
     */
    private Integer medicineId;

    /**
     * 变动类型（采购入库、处方消耗等）
     */
    private String reason;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 当前页
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}


package com.template.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存流水VO
 *
 * @author template
 */
@Data
public class InventoryLogVO {

    /**
     * 流水ID
     */
    private Integer logId;

    /**
     * 药品ID
     */
    private Integer medicineId;

    /**
     * 药品名称
     */
    private String medicineName;

    /**
     * 变动数量
     */
    private Integer changeQuantity;

    /**
     * 变动原因
     */
    private String reason;

    /**
     * 关联的业务ID
     */
    private Integer relatedId;

    /**
     * 记录时间
     */
    private LocalDateTime createTime;
}


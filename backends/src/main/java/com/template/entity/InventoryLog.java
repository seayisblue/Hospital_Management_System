package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存流水实体类
 *
 * @author template
 */
@Data
@TableName("T_Inventory_Logs")
public class InventoryLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流水ID
     */
    @TableId(value = "LogID", type = IdType.AUTO)
    private Integer logId;

    /**
     * 药品ID
     */
    @TableField("MedicineID")
    private Integer medicineId;

    /**
     * 变动数量(正数为入库,负数为出库)
     */
    @TableField("ChangeQuantity")
    private Integer changeQuantity;

    /**
     * 变动原因(采购入库,处方消耗,盘点调整,过期报废)
     */
    @TableField("Reason")
    private String reason;

    /**
     * 关联的业务ID(如PrescriptionID)
     */
    @TableField("RelatedID")
    private Integer relatedId;

    /**
     * 记录时间
     */
    @TableField(value = "CreateTime", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}


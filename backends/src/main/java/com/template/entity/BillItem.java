package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收费明细实体类
 *
 * @author template
 */
@Data
@TableName("T_Bill_Items")
public class BillItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @TableId(value = "ItemID", type = IdType.AUTO)
    private Integer itemId;

    /**
     * 关联的收费单ID
     */
    @TableField("BillID")
    private Integer billId;

    /**
     * 费用类型(挂号,药品)
     */
    @TableField("ItemType")
    private String itemType;

    /**
     * 项目名称
     */
    @TableField("ItemName")
    private String itemName;

    /**
     * 金额
     */
    @TableField("Amount")
    private BigDecimal amount;

    /**
     * 关联的业务ID(如AppointmentID)
     */
    @TableField("ReferenceID")
    private Integer referenceId;
}


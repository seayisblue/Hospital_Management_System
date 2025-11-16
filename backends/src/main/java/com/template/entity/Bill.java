package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收费单实体类
 *
 * @author template
 */
@Data
@TableName("T_Bill")
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收费单ID
     */
    @TableId(value = "BillID", type = IdType.AUTO)
    private Integer billId;

    /**
     * 患者ID
     */
    @TableField("PatientID")
    private Integer patientId;

    /**
     * 总金额
     */
    @TableField("TotalAmount")
    private BigDecimal totalAmount;

    /**
     * 状态(未支付,已支付,已退款)
     */
    @TableField("Status")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "CreateTime", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 支付时间
     */
    @TableField("PayTime")
    private LocalDateTime payTime;
}


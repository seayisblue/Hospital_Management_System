package com.template.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 收费单详情VO
 *
 * @author template
 */
@Data
public class BillDetailVO {

    /**
     * 收费单ID
     */
    private Integer billId;

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者电话
     */
    private String patientPhone;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 收费明细列表
     */
    private List<BillItemVO> items;

    @Data
    public static class BillItemVO {
        /**
         * 明细ID
         */
        private Integer itemId;

        /**
         * 费用类型
         */
        private String itemType;

        /**
         * 项目名称
         */
        private String itemName;

        /**
         * 金额
         */
        private BigDecimal amount;

        /**
         * 关联的业务ID
         */
        private Integer referenceId;
    }
}


package com.template.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 收入统计VO
 *
 * @author template
 */
@Data
public class RevenueStatisticsVO {

    /**
     * 今日收入
     */
    private BigDecimal todayRevenue;

    /**
     * 本周收入
     */
    private BigDecimal weekRevenue;

    /**
     * 本月收入
     */
    private BigDecimal monthRevenue;

    /**
     * 今日订单数
     */
    private Integer todayOrderCount;

    /**
     * 本周订单数
     */
    private Integer weekOrderCount;

    /**
     * 本月订单数
     */
    private Integer monthOrderCount;

    /**
     * 挂号收入
     */
    private BigDecimal registrationRevenue;

    /**
     * 药品收入
     */
    private BigDecimal medicineRevenue;
}


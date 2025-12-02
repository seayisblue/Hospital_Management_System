package com.template.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 管理员首页统计数据 VO
 */
@Data
public class AdminStatsVO {
    private Integer patientCount;      // 注册患者总数
    private Integer doctorCount;       // 在职医生数
    private Integer todayAppointment;  // 今日预约数
    private BigDecimal todayRevenue;   // 今日收入
}
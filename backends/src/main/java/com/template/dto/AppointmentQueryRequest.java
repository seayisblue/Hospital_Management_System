package com.template.dto;

import lombok.Data;

/**
 * 挂号查询请求
 *
 * @author template
 */
@Data
public class AppointmentQueryRequest {

    /**
     * 患者ID
     */
    private Integer patientId;

    /**
     * 患者姓名（模糊查询）
     */
    private String patientName;

    /**
     * 挂号状态
     */
    private String status;

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


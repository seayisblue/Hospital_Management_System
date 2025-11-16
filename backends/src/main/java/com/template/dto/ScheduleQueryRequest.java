package com.template.dto;

import lombok.Data;

/**
 * 查询排班请求DTO
 *
 * @author template
 */
@Data
public class ScheduleQueryRequest {

    /**
     * 医生ID
     */
    private Integer staffId;

    /**
     * 科室ID
     */
    private Integer deptId;

    /**
     * 排班日期（开始）格式：yyyy-MM-dd
     */
    private String startDate;

    /**
     * 排班日期（结束）格式：yyyy-MM-dd
     */
    private String endDate;

    /**
     * 时间段
     */
    private String timeSlot;

    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}

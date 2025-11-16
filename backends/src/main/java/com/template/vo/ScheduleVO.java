package com.template.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 排班视图对象VO
 *
 * @author template
 */
@Data
public class ScheduleVO {

    /**
     * 排班ID
     */
    private Integer scheduleId;

    /**
     * 医生ID
     */
    private Integer staffId;

    /**
     * 医生姓名
     */
    private String staffName;

    /**
     * 科室ID
     */
    private Integer deptId;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 排班日期
     */
    private LocalDate scheduleDate;

    /**
     * 时间段
     */
    private String timeSlot;

    /**
     * 总号数
     */
    private Integer totalSlots;

    /**
     * 已预约号数
     */
    private Integer bookedSlots;

    /**
     * 剩余号数
     */
    private Integer availableSlots;
}


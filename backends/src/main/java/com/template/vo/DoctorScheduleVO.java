package com.template.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 医生排班视图对象VO
 *
 * @author template
 */
@Data
public class DoctorScheduleVO {

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
     * 职称
     */
    private String title;

    /**
     * 排班日期
     */
    private LocalDate scheduleDate;

    /**
     * 时段(上午、下午、夜班)
     */
    private String timeSlot;

    /**
     * 总号源数
     */
    private Integer totalSlots;

    /**
     * 已预订号数
     */
    private Integer bookedSlots;

    /**
     * 剩余号源数
     */
    private Integer availableSlots;
}


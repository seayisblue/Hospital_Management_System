package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 医生排班实体类
 *
 * @author template
 */
@Data
@TableName("T_Doctor_Schedule")
public class DoctorSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 排班ID
     */
    @TableId(value = "ScheduleID", type = IdType.AUTO)
    private Integer scheduleId;

    /**
     * 医生ID
     */
    @TableField("StaffID")
    private Integer staffId;

    /**
     * 排班日期
     */
    @TableField("ScheduleDate")
    private LocalDate scheduleDate;

    /**
     * 时段(上午、下午、夜班)
     */
    @TableField("TimeSlot")
    private String timeSlot;

    /**
     * 总号源数
     */
    @TableField("TotalSlots")
    private Integer totalSlots;

    /**
     * 已预订号数
     */
    @TableField("BookedSlots")
    private Integer bookedSlots;
}


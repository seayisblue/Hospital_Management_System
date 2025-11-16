package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import java.time.LocalDate;

/**
 * 更新排班请求DTO
 *
 * @author template
 */
@Data
public class ScheduleUpdateRequest {

    /**
     * 排班ID
     */
    @NotNull(message = "排班ID不能为空")
    private Integer scheduleId;

    /**
     * 医生ID
     */
    private Integer staffId;

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
    @Min(value = 1, message = "总号数至少为1")
    private Integer totalSlots;
}


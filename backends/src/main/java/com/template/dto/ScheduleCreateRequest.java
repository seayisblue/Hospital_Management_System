package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import java.time.LocalDate;

/**
 * 创建排班请求DTO
 *
 * @author template
 */
@Data
public class ScheduleCreateRequest {

    /**
     * 医生ID
     */
    @NotNull(message = "医生ID不能为空")
    private Integer staffId;

    /**
     * 排班日期
     */
    @NotNull(message = "排班日期不能为空")
    private LocalDate scheduleDate;

    /**
     * 时间段
     */
    @NotNull(message = "时间段不能为空")
    private String timeSlot;

    /**
     * 总号数
     */
    @NotNull(message = "总号数不能为空")
    @Min(value = 1, message = "总号数至少为1")
    private Integer totalSlots;
}


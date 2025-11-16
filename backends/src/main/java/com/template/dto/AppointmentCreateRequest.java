package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 挂号创建请求DTO
 *
 * @author template
 */
@Data
public class AppointmentCreateRequest {

    /**
     * 排班ID
     */
    @NotNull(message = "排班ID不能为空")
    private Integer scheduleId;

    /**
     * 患者ID（从Token中获取，不需要前端传递）
     */
    private Integer patientId;
}


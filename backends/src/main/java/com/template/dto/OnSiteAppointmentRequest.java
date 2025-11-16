package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 现场挂号请求
 *
 * @author template
 */
@Data
public class OnSiteAppointmentRequest {

    /**
     * 患者ID
     */
    @NotNull(message = "患者ID不能为空")
    private Integer patientId;

    /**
     * 排班ID
     */
    @NotNull(message = "排班ID不能为空")
    private Integer scheduleId;

    /**
     * 操作员ID（挂号员工ID）
     */
    @NotNull(message = "操作员ID不能为空")
    private Integer operatorId;
}


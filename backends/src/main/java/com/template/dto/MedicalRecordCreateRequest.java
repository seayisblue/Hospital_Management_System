package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 创建病历请求
 *
 * @author template
 */
@Data
public class MedicalRecordCreateRequest {

    /**
     * 挂号ID
     */
    @NotNull(message = "挂号ID不能为空")
    private Integer appointmentId;

    /**
     * 主诉
     */
    private String subjective;

    /**
     * 查体
     */
    private String objective;

    /**
     * 诊断
     */
    private String assessment;

    /**
     * 治疗方案
     */
    private String plan;
}


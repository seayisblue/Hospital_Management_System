package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 发药请求DTO
 *
 * @author template
 */
@Data
public class DispenseMedicineRequest {

    /**
     * 处方ID
     */
    @NotNull(message = "处方ID不能为空")
    private Integer prescriptionId;
}


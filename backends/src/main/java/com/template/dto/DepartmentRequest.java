package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 科室创建/更新请求
 *
 * @author template
 */
@Data
public class DepartmentRequest {

    /**
     * 科室名称
     */
    @NotBlank(message = "科室名称不能为空")
    private String deptName;

    /**
     * 科室描述
     */
    private String description;
}


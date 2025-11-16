package com.template.vo;

import lombok.Data;

/**
 * 科室视图对象VO
 *
 * @author template
 */
@Data
public class DepartmentVO {

    /**
     * 科室ID
     */
    private Integer deptId;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 科室描述
     */
    private String description;
}


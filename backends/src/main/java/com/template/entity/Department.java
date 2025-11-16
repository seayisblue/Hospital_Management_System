package com.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 科室实体类
 *
 * @author template
 */
@Data
@TableName("T_Department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 科室ID
     */
    @TableId(value = "DeptID", type = IdType.AUTO)
    private Integer deptId;

    /**
     * 科室名称
     */
    @TableField("DeptName")
    private String deptName;

    /**
     * 科室描述
     */
    @TableField("Description")
    private String description;
}


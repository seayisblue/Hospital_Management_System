package com.template.dto;

import lombok.Data;

/**
 * 药品查询请求
 *
 * @author template
 */
@Data
public class MedicineQueryRequest {

    /**
     * 药品名称（模糊查询）
     */
    private String medicineName;

    /**
     * 生产厂家（模糊查询）
     */
    private String manufacturer;

    /**
     * 当前页
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}


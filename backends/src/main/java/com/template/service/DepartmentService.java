package com.template.service;

import com.template.dto.DepartmentRequest;
import com.template.vo.DepartmentDetailVO;
import com.template.vo.DepartmentVO;

import java.util.List;

/**
 * 科室服务接口
 *
 * @author template
 */
public interface DepartmentService {

    /**
     * 创建科室
     */
    Integer createDepartment(DepartmentRequest request);

    /**
     * 更新科室
     */
    void updateDepartment(Integer deptId, DepartmentRequest request);

    /**
     * 删除科室
     */
    void deleteDepartment(Integer deptId);

    /**
     * 获取科室详情
     */
    DepartmentDetailVO getDepartmentDetail(Integer deptId);

    /**
     * 获取所有科室列表
     */
    List<DepartmentVO> getAllDepartments();
}


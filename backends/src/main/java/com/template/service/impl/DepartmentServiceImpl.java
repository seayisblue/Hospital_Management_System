package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.template.common.ResultCode;
import com.template.dto.DepartmentRequest;
import com.template.entity.Department;
import com.template.common.BusinessException;
import com.template.mapper.DepartmentMapper;
import com.template.service.DepartmentService;
import com.template.vo.DepartmentDetailVO;
import com.template.vo.DepartmentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 科室服务实现类
 *
 * @author template
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public Integer createDepartment(DepartmentRequest request) {
        // 检查科室名称是否已存在
        LambdaQueryWrapper<Department> query = new LambdaQueryWrapper<>();
        query.eq(Department::getDeptName, request.getDeptName());
        if (departmentMapper.selectCount(query) > 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "科室名称已存在");
        }

        Department department = new Department();
        BeanUtils.copyProperties(request, department);
        departmentMapper.insert(department);
        return department.getDeptId();
    }

    @Override
    public void updateDepartment(Integer deptId, DepartmentRequest request) {
        Department dept = departmentMapper.selectById(deptId);
        if (dept == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "科室不存在");
        }

        // 检查新名称是否与其他科室冲突
        if (!dept.getDeptName().equals(request.getDeptName())) {
            LambdaQueryWrapper<Department> query = new LambdaQueryWrapper<>();
            query.eq(Department::getDeptName, request.getDeptName());
            if (departmentMapper.selectCount(query) > 0) {
                throw new BusinessException(ResultCode.BUSINESS_ERROR, "科室名称已存在");
            }
        }

        Department updateDept = new Department();
        updateDept.setDeptId(deptId);
        BeanUtils.copyProperties(request, updateDept);
        departmentMapper.updateById(updateDept);
    }

    @Override
    public void deleteDepartment(Integer deptId) {
        Department dept = departmentMapper.selectById(deptId);
        if (dept == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "科室不存在");
        }

        // TODO: 检查是否有职工关联此科室
        
        departmentMapper.deleteById(deptId);
    }

    @Override
    public DepartmentDetailVO getDepartmentDetail(Integer deptId) {
        Department dept = departmentMapper.selectById(deptId);
        if (dept == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "科室不存在");
        }

        DepartmentDetailVO vo = new DepartmentDetailVO();
        BeanUtils.copyProperties(dept, vo);
        return vo;
    }

    @Override
    public List<DepartmentVO> getAllDepartments() {
        List<Department> list = departmentMapper.selectList(null);
        return list.stream().map(dept -> {
            DepartmentVO vo = new DepartmentVO();
            BeanUtils.copyProperties(dept, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}


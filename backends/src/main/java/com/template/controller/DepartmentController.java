package com.template.controller;

import com.template.common.Result;
import com.template.dto.DepartmentRequest;
import com.template.service.DepartmentService;
import com.template.vo.DepartmentDetailVO;
import com.template.vo.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 科室管理Controller
 *
 * @author template
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 创建科室
     */
    @PostMapping
    public Result<Integer> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        Integer deptId = departmentService.createDepartment(request);
        return Result.success("创建成功", deptId);
    }

    /**
     * 更新科室
     */
    @PutMapping("/{deptId}")
    public Result<Void> updateDepartment(@PathVariable Integer deptId,
                                        @Valid @RequestBody DepartmentRequest request) {
        departmentService.updateDepartment(deptId, request);
        return Result.success("更新成功", null);
    }

    /**
     * 删除科室
     */
    @DeleteMapping("/{deptId}")
    public Result<Void> deleteDepartment(@PathVariable Integer deptId) {
        departmentService.deleteDepartment(deptId);
        return Result.success("删除成功", null);
    }

    /**
     * 获取科室详情
     */
    @GetMapping("/{deptId}")
    public Result<DepartmentDetailVO> getDepartmentDetail(@PathVariable Integer deptId) {
        DepartmentDetailVO vo = departmentService.getDepartmentDetail(deptId);
        return Result.success(vo);
    }

    /**
     * 获取所有科室列表
     */
    @GetMapping
    public Result<List<DepartmentVO>> getAllDepartments() {
        List<DepartmentVO> list = departmentService.getAllDepartments();
        return Result.success(list);
    }
}


package com.template.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.Result;
import com.template.dto.StaffCreateRequest;
import com.template.dto.StaffLoginRequest;
import com.template.dto.StaffLoginResponse;
import com.template.dto.StaffQueryRequest;
import com.template.dto.StaffUpdateRequest;
import com.template.service.StaffService;
import com.template.vo.StaffVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 职工管理Controller
 *
 * @author template
 */
@RestController
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    /**
     * 职工登录
     */
    @PostMapping("/login")
    public Result<StaffLoginResponse> login(@Valid @RequestBody StaffLoginRequest request) {
        StaffLoginResponse response = staffService.login(request);
        return Result.success("登录成功", response);
    }

    /**
     * 创建职工
     */
    @PostMapping
    public Result<Integer> createStaff(@Valid @RequestBody StaffCreateRequest request) {
        Integer staffId = staffService.createStaff(request);
        return Result.success("创建成功", staffId);
    }

    /**
     * 更新职工
     */
    @PutMapping("/{staffId}")
    public Result<Void> updateStaff(@PathVariable Integer staffId, 
                                    @RequestBody StaffUpdateRequest request) {
        staffService.updateStaff(staffId, request);
        return Result.success("更新成功", null);
    }

    /**
     * 删除职工
     */
    @DeleteMapping("/{staffId}")
    public Result<Void> deleteStaff(@PathVariable Integer staffId) {
        staffService.deleteStaff(staffId);
        return Result.success("删除成功", null);
    }

    /**
     * 获取职工详情
     */
    @GetMapping("/{staffId}")
    public Result<StaffVO> getStaffDetail(@PathVariable Integer staffId) {
        StaffVO staffVO = staffService.getStaffDetail(staffId);
        return Result.success(staffVO);
    }

    /**
     * 分页查询职工列表
     */
    @GetMapping
    public Result<Page<StaffVO>> getStaffPage(StaffQueryRequest request) {
        Page<StaffVO> page = staffService.getStaffPage(request);
        return Result.success(page);
    }
}


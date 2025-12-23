package com.template.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.Result;
import com.template.dto.ScheduleCreateRequest;
import com.template.dto.ScheduleQueryRequest;
import com.template.dto.ScheduleUpdateRequest;
import com.template.service.ScheduleService;
import com.template.vo.ScheduleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 排班控制器
 * (权限变更：现在由科室主任或主任医师管理)
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 创建排班
     */
    @PostMapping
    public Result<Integer> createSchedule(@Valid @RequestBody ScheduleCreateRequest request, HttpServletRequest httpRequest) {
        // 获取当前操作者ID (从Token中解析出来的)
        Integer operatorId = (Integer) httpRequest.getAttribute("userId");
        // 传入 operatorId 进行权限校验
        Integer scheduleId = scheduleService.createSchedule(request, operatorId);
        return Result.success(scheduleId);
    }

    /**
     * 更新排班
     */
    @PutMapping
    public Result<Void> updateSchedule(@Valid @RequestBody ScheduleUpdateRequest request, HttpServletRequest httpRequest) {
        Integer operatorId = (Integer) httpRequest.getAttribute("userId");
        scheduleService.updateSchedule(request, operatorId);
        return Result.success();
    }

    /**
     * 删除排班
     */
    @DeleteMapping("/{scheduleId}")
    public Result<Void> deleteSchedule(@PathVariable Integer scheduleId, HttpServletRequest httpRequest) {
        Integer operatorId = (Integer) httpRequest.getAttribute("userId");
        scheduleService.deleteSchedule(scheduleId, operatorId);
        return Result.success();
    }

    /**
     * 分页查询排班
     *
     * @param request 查询请求
     * @return 排班分页数据
     */
    @GetMapping("/page")
    public Result<Page<ScheduleVO>> getSchedulePage(ScheduleQueryRequest request) {
        Page<ScheduleVO> page = scheduleService.getSchedulePage(request);
        return Result.success(page);
    }

    @GetMapping("/{scheduleId}")
    public Result<ScheduleVO> getScheduleDetail(@PathVariable Integer scheduleId) {
        ScheduleVO schedule = scheduleService.getScheduleDetail(scheduleId);
        return Result.success(schedule);
    }
}


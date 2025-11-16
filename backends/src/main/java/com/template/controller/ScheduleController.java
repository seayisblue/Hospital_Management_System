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

import javax.validation.Valid;

/**
 * 排班控制器
 *
 * @author template
 */
@RestController
@RequestMapping("/admin/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 创建排班
     *
     * @param request 创建请求
     * @return 排班ID
     */
    @PostMapping
    public Result<Integer> createSchedule(@Valid @RequestBody ScheduleCreateRequest request) {
        Integer scheduleId = scheduleService.createSchedule(request);
        return Result.success(scheduleId);
    }

    /**
     * 更新排班
     *
     * @param request 更新请求
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> updateSchedule(@Valid @RequestBody ScheduleUpdateRequest request) {
        scheduleService.updateSchedule(request);
        return Result.success();
    }

    /**
     * 删除排班
     *
     * @param scheduleId 排班ID
     * @return 操作结果
     */
    @DeleteMapping("/{scheduleId}")
    public Result<Void> deleteSchedule(@PathVariable Integer scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
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

    /**
     * 获取排班详情
     *
     * @param scheduleId 排班ID
     * @return 排班详情
     */
    @GetMapping("/{scheduleId}")
    public Result<ScheduleVO> getScheduleDetail(@PathVariable Integer scheduleId) {
        ScheduleVO schedule = scheduleService.getScheduleDetail(scheduleId);
        return Result.success(schedule);
    }
}


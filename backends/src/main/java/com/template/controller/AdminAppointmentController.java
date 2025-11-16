package com.template.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.Result;
import com.template.dto.AppointmentQueryRequest;
import com.template.dto.OnSiteAppointmentRequest;
import com.template.service.AdminAppointmentService;
import com.template.vo.AppointmentDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 管理员挂号Controller
 *
 * @author template
 */
@RestController
@RequestMapping("/admin/appointment")
public class AdminAppointmentController {

    @Autowired
    private AdminAppointmentService adminAppointmentService;

    /**
     * 现场挂号
     */
    @PostMapping("/onsite")
    public Result<Integer> createOnSiteAppointment(@Valid @RequestBody OnSiteAppointmentRequest request) {
        Integer appointmentId = adminAppointmentService.createOnSiteAppointment(request);
        return Result.success("挂号成功", appointmentId);
    }

    /**
     * 分页查询挂号记录
     */
    @GetMapping
    public Result<Page<AppointmentDetailVO>> getAppointmentPage(AppointmentQueryRequest request) {
        Page<AppointmentDetailVO> page = adminAppointmentService.getAppointmentPage(request);
        return Result.success(page);
    }

    /**
     * 取消挂号
     */
    @PutMapping("/{appointmentId}/cancel")
    public Result<Void> cancelAppointment(@PathVariable Integer appointmentId) {
        adminAppointmentService.cancelAppointment(appointmentId);
        return Result.success("取消成功", null);
    }

    /**
     * 获取挂号详情
     */
    @GetMapping("/{appointmentId}")
    public Result<AppointmentDetailVO> getAppointmentDetail(@PathVariable Integer appointmentId) {
        AppointmentDetailVO vo = adminAppointmentService.getAppointmentDetail(appointmentId);
        return Result.success(vo);
    }
}


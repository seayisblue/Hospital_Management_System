package com.template.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.template.common.Result;
import com.template.entity.Appointment;
import com.template.entity.Staff;
import com.template.mapper.AppointmentMapper;
import com.template.mapper.PatientMapper;
import com.template.mapper.StaffMapper;
import com.template.service.BillService;
import com.template.vo.AdminStatsVO;
import com.template.vo.RevenueStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 管理员仪表盘 Controller
 */
@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private BillService billService; // 复用财务服务的统计逻辑

    @GetMapping("/stats")
    public Result<AdminStatsVO> getDashboardStats() {
        AdminStatsVO vo = new AdminStatsVO();

        // 1. 统计注册患者总数
        vo.setPatientCount(Math.toIntExact(patientMapper.selectCount(null)));

        // 2. 统计在职医生数 (Role = '医生')
        LambdaQueryWrapper<Staff> staffQuery = new LambdaQueryWrapper<>();
        staffQuery.eq(Staff::getRole, "医生");
        vo.setDoctorCount(Math.toIntExact(staffMapper.selectCount(staffQuery)));

        // 3. 统计今日预约数
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        LambdaQueryWrapper<Appointment> appQuery = new LambdaQueryWrapper<>();
        appQuery.ge(Appointment::getAppointmentTime, todayStart)
                .le(Appointment::getAppointmentTime, todayEnd);
        vo.setTodayAppointment(Math.toIntExact(appointmentMapper.selectCount(appQuery)));

        // 4. 统计今日收入 (直接调用已有的 BillService)
        RevenueStatisticsVO revenueStats = billService.getRevenueStatistics();
        vo.setTodayRevenue(revenueStats.getTodayRevenue());

        return Result.success(vo);
    }
}
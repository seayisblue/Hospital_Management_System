package com.template.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.template.common.Result;
import com.template.entity.Appointment;
import com.template.entity.Bill;
import com.template.entity.Medicine;
import com.template.entity.Staff;
import com.template.mapper.AppointmentMapper;
import com.template.mapper.BillMapper;
import com.template.mapper.MedicineMapper;
import com.template.mapper.PatientMapper;
import com.template.mapper.StaffMapper;
import com.template.service.BillService;
import com.template.vo.AdminStatsVO;
import com.template.vo.AdminTaskVO;
import com.template.vo.RevenueStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private BillMapper billMapper;

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private BillService billService;

    @GetMapping("/stats")
    public Result<AdminStatsVO> getDashboardStats() {
        AdminStatsVO vo = new AdminStatsVO();

        vo.setPatientCount(Math.toIntExact(patientMapper.selectCount(null)));

        LambdaQueryWrapper<Staff> staffQuery = new LambdaQueryWrapper<>();
        staffQuery.eq(Staff::getRole, "医生");
        vo.setDoctorCount(Math.toIntExact(staffMapper.selectCount(staffQuery)));

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LambdaQueryWrapper<Appointment> appQuery = new LambdaQueryWrapper<>();
        appQuery.ge(Appointment::getAppointmentTime, todayStart)
                .le(Appointment::getAppointmentTime, todayEnd);
        vo.setTodayAppointment(Math.toIntExact(appointmentMapper.selectCount(appQuery)));

        RevenueStatisticsVO revenueStats = billService.getRevenueStatistics();
        vo.setTodayRevenue(revenueStats.getTodayRevenue());

        return Result.success(vo);
    }

    @GetMapping("/tasks")
    public Result<List<AdminTaskVO>> getPendingTasks() {
        List<AdminTaskVO> tasks = new ArrayList<>();

        LambdaQueryWrapper<Bill> unpaidQuery = new LambdaQueryWrapper<>();
        unpaidQuery.eq(Bill::getStatus, "未支付");
        long unpaidCount = billMapper.selectCount(unpaidQuery);
        if (unpaidCount > 0) {
            AdminTaskVO task = new AdminTaskVO();
            task.setType("财务审核");
            task.setDescription("未支付账单共 " + unpaidCount + " 笔待处理");
            task.setSubmitTime(LocalDateTime.now());
            task.setStatus("待处理");
            task.setActionUrl("finance-center.html");
            tasks.add(task);
        }

        LambdaQueryWrapper<Medicine> stockQuery = new LambdaQueryWrapper<>();
        stockQuery.isNotNull(Medicine::getMinStock)
                .apply("StockLevel < MinStock");
        long lowStockCount = medicineMapper.selectCount(stockQuery);
        if (lowStockCount > 0) {
            AdminTaskVO task = new AdminTaskVO();
            task.setType("库存预警");
            task.setDescription("低库存药品 " + lowStockCount + " 种需要补货");
            task.setSubmitTime(LocalDateTime.now());
            task.setStatus("紧急");
            task.setActionUrl("medicine-stock.html");
            tasks.add(task);
        }

        return Result.success(tasks);
    }
}

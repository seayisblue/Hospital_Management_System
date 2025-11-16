package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.template.common.ResultCode;
import com.template.dto.AppointmentCreateRequest;
import com.template.dto.ScheduleQueryRequest;
import com.template.entity.Appointment;
import com.template.entity.DoctorSchedule;
import com.template.common.BusinessException;
import com.template.mapper.*;
import com.template.service.AppointmentService;
import com.template.vo.AppointmentVO;
import com.template.vo.DoctorScheduleVO;
import com.template.vo.MedicalRecordVO;
import com.template.vo.PrescriptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 挂号服务实现类
 *
 * @author template
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private DoctorScheduleMapper doctorScheduleMapper;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Override
    public List<DoctorScheduleVO> getAvailableSchedules(ScheduleQueryRequest request) {
        // 将字符串日期转换为 LocalDate
        LocalDate scheduleDate = null;
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            scheduleDate = LocalDate.parse(request.getStartDate());
        }
        
        return doctorScheduleMapper.selectAvailableSchedules(
                request.getDeptId(),
                request.getStaffId(),
                scheduleDate,
                request.getTimeSlot()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createAppointment(AppointmentCreateRequest request) {
        // 查询排班信息
        DoctorSchedule schedule = doctorScheduleMapper.selectById(request.getScheduleId());
        if (schedule == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "排班不存在");
        }

        // 检查是否还有号源
        if (schedule.getBookedSlots() >= schedule.getTotalSlots()) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "号源已满，无法挂号");
        }

        // 检查患者是否已经在该时段挂号
        LambdaQueryWrapper<Appointment> query = new LambdaQueryWrapper<>();
        query.eq(Appointment::getPatientId, request.getPatientId())
                .eq(Appointment::getScheduleId, request.getScheduleId())
                .ne(Appointment::getStatus, "已取消");
        if (appointmentMapper.selectCount(query) > 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "您已在该时段挂号，请勿重复挂号");
        }

        // 创建挂号记录
        Appointment appointment = new Appointment();
        appointment.setScheduleId(request.getScheduleId());
        appointment.setPatientId(request.getPatientId());
        appointment.setStaffId(schedule.getStaffId());
        
        // 从排班获取医生信息，再获取科室ID
        com.template.entity.Staff staff = staffMapper.selectById(schedule.getStaffId());
        if (staff != null && staff.getDeptId() != null) {
            appointment.setDeptId(staff.getDeptId());
        }
        
        appointment.setAppointmentTime(LocalDateTime.now());
        appointment.setStatus("待就诊");
        appointment.setFee(new BigDecimal("15.00")); // 默认挂号费

        appointmentMapper.insert(appointment);

        // 更新排班的已预订号数
        schedule.setBookedSlots(schedule.getBookedSlots() + 1);
        doctorScheduleMapper.updateById(schedule);

        return appointment.getAppointmentId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAppointment(Integer appointmentId, Integer patientId) {
        // 查询挂号记录
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "挂号记录不存在");
        }

        // 验证患者身份
        if (!appointment.getPatientId().equals(patientId)) {
            throw new BusinessException(ResultCode.PERMISSION_NO_ACCESS, "无权取消该挂号");
        }

        // 检查状态
        if ("已取消".equals(appointment.getStatus())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该挂号已取消");
        }

        if ("已就诊".equals(appointment.getStatus())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "已就诊的挂号不能取消");
        }

        // 更新挂号状态
        appointment.setStatus("已取消");
        appointmentMapper.updateById(appointment);

        // 更新排班的已预订号数
        DoctorSchedule schedule = doctorScheduleMapper.selectById(appointment.getScheduleId());
        if (schedule != null && schedule.getBookedSlots() > 0) {
            schedule.setBookedSlots(schedule.getBookedSlots() - 1);
            doctorScheduleMapper.updateById(schedule);
        }
    }

    @Override
    public List<AppointmentVO> getPatientAppointments(Integer patientId) {
        return appointmentMapper.selectByPatientId(patientId);
    }

    @Override
    public List<MedicalRecordVO> getPatientMedicalRecords(Integer patientId) {
        return medicalRecordMapper.selectByPatientId(patientId);
    }

    @Override
    public List<PrescriptionVO> getPatientPrescriptions(Integer patientId) {
        return prescriptionMapper.selectByPatientId(patientId);
    }
}


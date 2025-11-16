package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.ResultCode;
import com.template.dto.AppointmentQueryRequest;
import com.template.dto.OnSiteAppointmentRequest;
import com.template.entity.Appointment;
import com.template.entity.DoctorSchedule;
import com.template.entity.Patient;
import com.template.common.BusinessException;
import com.template.mapper.AppointmentMapper;
import com.template.mapper.DoctorScheduleMapper;
import com.template.mapper.PatientMapper;
import com.template.service.AdminAppointmentService;
import com.template.vo.AppointmentDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员挂号服务实现类
 *
 * @author template
 */
@Service
public class AdminAppointmentServiceImpl implements AdminAppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private DoctorScheduleMapper scheduleMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private com.template.mapper.StaffMapper staffMapper;

    @Autowired
    private com.template.mapper.DepartmentMapper departmentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createOnSiteAppointment(OnSiteAppointmentRequest request) {
        // 检查患者是否存在
        Patient patient = patientMapper.selectById(request.getPatientId());
        if (patient == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "患者不存在");
        }

        // 检查排班信息
        DoctorSchedule schedule = scheduleMapper.selectById(request.getScheduleId());
        if (schedule == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "排班信息不存在");
        }

        // 检查是否还有号源
        if (schedule.getBookedSlots() >= schedule.getTotalSlots()) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该时段号源已满");
        }

        // 创建挂号记录
        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setScheduleId(request.getScheduleId());
        appointment.setStaffId(schedule.getStaffId()); // 设置医生ID
        appointment.setStatus("待就诊");
        
        // 查询医生信息以获取科室ID
        com.template.entity.Staff staff = staffMapper.selectById(schedule.getStaffId());
        if (staff != null && staff.getDeptId() != null) {
            appointment.setDeptId(staff.getDeptId()); // 设置科室ID
        }
        
        appointmentMapper.insert(appointment);

        // 更新号源
        schedule.setBookedSlots(schedule.getBookedSlots() + 1);
        scheduleMapper.updateById(schedule);

        return appointment.getAppointmentId();
    }

    @Override
    public Page<AppointmentDetailVO> getAppointmentPage(AppointmentQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Appointment> query = new LambdaQueryWrapper<>();
        
        if (request.getPatientId() != null) {
            query.eq(Appointment::getPatientId, request.getPatientId());
        }
        if (StringUtils.hasText(request.getStatus())) {
            query.eq(Appointment::getStatus, request.getStatus());
        }
        if (StringUtils.hasText(request.getStartDate())) {
            query.ge(Appointment::getAppointmentTime, request.getStartDate());
        }
        if (StringUtils.hasText(request.getEndDate())) {
            query.le(Appointment::getAppointmentTime, request.getEndDate() + " 23:59:59");
        }
        
        query.orderByDesc(Appointment::getAppointmentTime);

        // 分页查询
        Page<Appointment> page = new Page<>(request.getPage(), request.getPageSize());
        Page<Appointment> appointmentPage = appointmentMapper.selectPage(page, query);

        // 转换为VO并填充关联信息
        Page<AppointmentDetailVO> voPage = new Page<>(appointmentPage.getCurrent(), appointmentPage.getSize(), appointmentPage.getTotal());
        List<AppointmentDetailVO> voList = appointmentPage.getRecords().stream()
            .map(appointment -> {
                AppointmentDetailVO vo = new AppointmentDetailVO();
                vo.setAppointmentId(appointment.getAppointmentId());
                vo.setPatientId(appointment.getPatientId());
                vo.setScheduleId(appointment.getScheduleId());
                vo.setStatus(appointment.getStatus());
                vo.setCreateTime(appointment.getAppointmentTime());

                // 查询患者信息
                Patient patient = patientMapper.selectById(appointment.getPatientId());
                if (patient != null) {
                    vo.setPatientName(patient.getPatientName());
                    vo.setPatientPhone(patient.getPhoneNumber());
                    
                    // 如果有患者姓名筛选条件，在这里过滤
                    if (StringUtils.hasText(request.getPatientName()) && 
                        !patient.getPatientName().contains(request.getPatientName())) {
                        return null; // 不符合条件，返回null后面会过滤掉
                    }
                }

                // 查询排班信息
                DoctorSchedule schedule = scheduleMapper.selectById(appointment.getScheduleId());
                if (schedule != null) {
                    vo.setScheduleDate(schedule.getScheduleDate());
                    vo.setTimeSlot(schedule.getTimeSlot());
                    vo.setStaffId(schedule.getStaffId());

                    // 查询医生信息
                    LambdaQueryWrapper<com.template.entity.Staff> staffQuery = new LambdaQueryWrapper<>();
                    staffQuery.eq(com.template.entity.Staff::getStaffId, schedule.getStaffId());
                    com.template.entity.Staff staff = staffMapper.selectOne(staffQuery);
                    if (staff != null) {
                        vo.setStaffName(staff.getStaffName());

                        // 查询科室信息
                        if (staff.getDeptId() != null) {
                            com.template.entity.Department dept = departmentMapper.selectById(staff.getDeptId());
                            if (dept != null) {
                                vo.setDeptName(dept.getDeptName());
                            }
                        }
                    }
                }

                return vo;
            })
            .filter(vo -> vo != null) // 过滤掉不符合患者姓名条件的记录
            .collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAppointment(Integer appointmentId) {
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "挂号记录不存在");
        }

        if ("已取消".equals(appointment.getStatus()) || "已就诊".equals(appointment.getStatus())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该挂号记录无法取消");
        }

        // 更新挂号状态
        appointment.setStatus("已取消");
        appointmentMapper.updateById(appointment);

        // 释放号源
        DoctorSchedule schedule = scheduleMapper.selectById(appointment.getScheduleId());
        if (schedule != null && schedule.getBookedSlots() > 0) {
            schedule.setBookedSlots(schedule.getBookedSlots() - 1);
            scheduleMapper.updateById(schedule);
        }
    }

    @Override
    public AppointmentDetailVO getAppointmentDetail(Integer appointmentId) {
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "挂号记录不存在");
        }

        AppointmentDetailVO vo = new AppointmentDetailVO();
        vo.setAppointmentId(appointment.getAppointmentId());
        vo.setPatientId(appointment.getPatientId());
        vo.setScheduleId(appointment.getScheduleId());
        vo.setStatus(appointment.getStatus());
        vo.setCreateTime(appointment.getAppointmentTime());

        // 查询患者信息
        Patient patient = patientMapper.selectById(appointment.getPatientId());
        if (patient != null) {
            vo.setPatientName(patient.getPatientName());
            vo.setPatientPhone(patient.getPhoneNumber());
        }

        // 查询排班信息
        DoctorSchedule schedule = scheduleMapper.selectById(appointment.getScheduleId());
        if (schedule != null) {
            vo.setScheduleDate(schedule.getScheduleDate());
            vo.setTimeSlot(schedule.getTimeSlot());
            vo.setStaffId(schedule.getStaffId());

            // 查询医生信息
            LambdaQueryWrapper<com.template.entity.Staff> staffQuery = new LambdaQueryWrapper<>();
            staffQuery.eq(com.template.entity.Staff::getStaffId, schedule.getStaffId());
            com.template.entity.Staff staff = staffMapper.selectOne(staffQuery);
            if (staff != null) {
                vo.setStaffName(staff.getStaffName());

                // 查询科室信息
                if (staff.getDeptId() != null) {
                    com.template.entity.Department dept = departmentMapper.selectById(staff.getDeptId());
                    if (dept != null) {
                        vo.setDeptName(dept.getDeptName());
                    }
                }
            }
        }

        return vo;
    }
}


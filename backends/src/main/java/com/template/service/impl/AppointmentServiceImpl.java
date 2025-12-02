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

    @Autowired
    private PrescriptionDetailMapper prescriptionDetailMapper;

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private DepartmentMapper departmentMapper; // 如果之前没有引用的话

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillItemMapper billItemMapper;

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

        com.template.entity.Bill bill = new com.template.entity.Bill();
        bill.setPatientId(request.getPatientId());
        bill.setTotalAmount(appointment.getFee()); // 使用挂号费金额
        bill.setStatus("未支付");
        // bill.setCreateTime(LocalDateTime.now()); // 如果自动填充没生效，可以手动加这行
        billMapper.insert(bill);

        // 2. 创建收费明细
        com.template.entity.BillItem billItem = new com.template.entity.BillItem();
        billItem.setBillId(bill.getBillId());
        billItem.setItemType("挂号");
        billItem.setItemName("挂号费");
        billItem.setAmount(appointment.getFee());
        billItem.setReferenceId(appointment.getAppointmentId());
        billItemMapper.insert(billItem);
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

    @Override
    public com.template.vo.PharmacyPrescriptionVO getPrescriptionDetail(Integer prescriptionId) {
        // 1. 查询处方基本信息
        com.template.entity.Prescription prescription = prescriptionMapper.selectById(prescriptionId);
        if (prescription == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST, "处方不存在");
        }

        // 2. 组装 VO
        com.template.vo.PharmacyPrescriptionVO vo = new com.template.vo.PharmacyPrescriptionVO();
        vo.setPrescriptionId(prescription.getPrescriptionId());
        vo.setRecordId(prescription.getRecordId());
        vo.setPatientId(prescription.getPatientId());
        vo.setStaffId(prescription.getStaffId());
        vo.setPrescriptionDate(prescription.getPrescriptionDate());
        vo.setStatus(prescription.getStatus());

        // 3. 补全医生和科室信息
        com.template.entity.Staff staff = staffMapper.selectById(prescription.getStaffId());
        if (staff != null) {
            vo.setStaffName(staff.getStaffName());
            if (staff.getDeptId() != null) {
                com.template.entity.Department dept = departmentMapper.selectById(staff.getDeptId());
                if (dept != null) {
                    vo.setDeptName(dept.getDeptName());
                }
            }
        }

        // 4. 补全患者信息
        // 这里可以直接用 patientMapper，因为在类里已经注入了
        // 假设你类里已经有 @Autowired private PatientMapper patientMapper;
        // 如果没有，记得加一下
        // Patient patient = patientMapper.selectById(prescription.getPatientId());
        // if (patient != null) { vo.setPatientName(patient.getPatientName()); }

        // 5. ★★★ 核心：查询药品明细 ★★★
        LambdaQueryWrapper<com.template.entity.PrescriptionDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(com.template.entity.PrescriptionDetail::getPrescriptionId, prescriptionId);
        List<com.template.entity.PrescriptionDetail> details = prescriptionDetailMapper.selectList(detailQuery);

        List<com.template.vo.PharmacyPrescriptionVO.PrescriptionDetailVO> detailVOs = new java.util.ArrayList<>();
        for (com.template.entity.PrescriptionDetail detail : details) {
            com.template.vo.PharmacyPrescriptionVO.PrescriptionDetailVO detailVO = new com.template.vo.PharmacyPrescriptionVO.PrescriptionDetailVO();
            // 复制基本属性
            detailVO.setDetailId(detail.getDetailId());
            detailVO.setMedicineId(detail.getMedicineId());
            detailVO.setQuantity(detail.getQuantity());
            detailVO.setUnitPrice(detail.getUnitPrice());
            detailVO.setSubtotal(detail.getSubtotal());

            // 查询药品名称
            com.template.entity.Medicine medicine = medicineMapper.selectById(detail.getMedicineId());
            if (medicine != null) {
                detailVO.setMedicineName(medicine.getMedicineName());
                detailVO.setSpecification(medicine.getSpecification());
            }
            detailVOs.add(detailVO);
        }

        vo.setDetails(detailVOs);
        return vo;
    }
}


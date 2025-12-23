package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.template.common.BusinessException;
import com.template.common.ResultCode;
import com.template.dto.MedicalRecordCreateRequest;
import com.template.dto.PrescriptionCreateRequest;
import com.template.entity.*;
import com.template.mapper.*;
import com.template.service.DoctorService;
import com.template.vo.DoctorAppointmentVO;
import com.template.vo.MedicalRecordDetailVO;
import com.template.vo.PharmacyPrescriptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * 医生服务实现
 *
 * @author template
 */
@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private DoctorScheduleMapper scheduleMapper;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private PrescriptionDetailMapper prescriptionDetailMapper;

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillItemMapper billItemMapper;

    @Override
    public List<DoctorAppointmentVO> getPendingAppointments(Integer staffId) {
        // 查询该医生的待就诊挂号
        LambdaQueryWrapper<Appointment> query = new LambdaQueryWrapper<>();
        query.eq(Appointment::getStaffId, staffId)
                .eq(Appointment::getStatus, "待就诊")
                .orderByAsc(Appointment::getAppointmentTime);

        List<Appointment> appointments = appointmentMapper.selectList(query);
        List<DoctorAppointmentVO> voList = new ArrayList<>();

        for (Appointment appointment : appointments) {
            DoctorAppointmentVO vo = new DoctorAppointmentVO();
            vo.setAppointmentId(appointment.getAppointmentId());
            vo.setPatientId(appointment.getPatientId());
            vo.setAppointmentTime(appointment.getAppointmentTime());
            vo.setStatus(appointment.getStatus());

            // 查询患者信息
            Patient patient = patientMapper.selectById(appointment.getPatientId());
            if (patient != null) {
                vo.setPatientName(patient.getPatientName());
                vo.setPatientGender(patient.getGender());
                vo.setPatientPhone(patient.getPhoneNumber());

                // 计算年龄
                if (patient.getDateOfBirth() != null) {
                    Period period = Period.between(patient.getDateOfBirth(), LocalDate.now());
                    vo.setPatientAge(period.getYears());
                }
            }

            // 查询排班信息
            DoctorSchedule schedule = scheduleMapper.selectById(appointment.getScheduleId());
            if (schedule != null) {
                vo.setScheduleDate(schedule.getScheduleDate().toString());
                vo.setTimeSlot(schedule.getTimeSlot());
            }

            // 检查是否已有病历
            LambdaQueryWrapper<MedicalRecord> recordQuery = new LambdaQueryWrapper<>();
            recordQuery.eq(MedicalRecord::getAppointmentId, appointment.getAppointmentId());
            Long recordCount = medicalRecordMapper.selectCount(recordQuery);
            vo.setHasMedicalRecord(recordCount > 0);

            voList.add(vo);
        }

        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createMedicalRecord(Integer staffId, MedicalRecordCreateRequest request) {
        // 验证挂号是否存在
        Appointment appointment = appointmentMapper.selectById(request.getAppointmentId());
        if (appointment == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "挂号记录不存在");
        }

        // 验证是否是该医生的患者
        if (!appointment.getStaffId().equals(staffId)) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "无权为该患者开具病历");
        }

        // 检查是否已有病历
        LambdaQueryWrapper<MedicalRecord> query = new LambdaQueryWrapper<>();
        query.eq(MedicalRecord::getAppointmentId, request.getAppointmentId());
        Long count = medicalRecordMapper.selectCount(query);
        if (count > 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该患者已有病历");
        }

        // 创建病历
        MedicalRecord record = new MedicalRecord();
        record.setAppointmentId(request.getAppointmentId());
        record.setPatientId(appointment.getPatientId());
        record.setStaffId(staffId);
        record.setSubjective(request.getSubjective());
        record.setObjective(request.getObjective());
        record.setAssessment(request.getAssessment());
        record.setPlan(request.getPlan());
        record.setCreateTime(LocalDateTime.now());

        medicalRecordMapper.insert(record);

        // 更新挂号状态为"已就诊"
        appointment.setStatus("已就诊");
        appointmentMapper.updateById(appointment);

        return record.getRecordId();
    }

    @Override
    public MedicalRecordDetailVO getMedicalRecordDetail(Integer recordId) {
        MedicalRecord record = medicalRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "病历不存在");
        }

        return buildMedicalRecordDetail(record);
    }
    @Override
    public MedicalRecordDetailVO getMedicalRecordDetailByAppointment(Integer staffId, Integer appointmentId) {
        LambdaQueryWrapper<MedicalRecord> query = new LambdaQueryWrapper<>();
        query.eq(MedicalRecord::getAppointmentId, appointmentId);
        MedicalRecord record = medicalRecordMapper.selectOne(query);
        if (record == null) {
            return null;
        }


        return buildMedicalRecordDetail(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createPrescription(Integer staffId, PrescriptionCreateRequest request) {
        // 验证病历是否存在
        MedicalRecord record = medicalRecordMapper.selectById(request.getRecordId());
        if (record == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "病历不存在");
        }

        // 验证是否是该医生的病历
        if (!record.getStaffId().equals(staffId)) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "无权为该病历开具处方");
        }

        // 创建处方
        Prescription prescription = new Prescription();
        prescription.setRecordId(request.getRecordId());
        prescription.setPatientId(record.getPatientId());
        prescription.setStaffId(staffId);
        prescription.setPrescriptionDate(LocalDateTime.now());
        prescription.setStatus("未发药");
        prescription.setAdvice(request.getAdvice());

        prescriptionMapper.insert(prescription);

        // 创建或查询收费单
        Bill bill = getOrCreateBill(record.getPatientId(), record.getAppointmentId());

        // 计算处方总金额并插入明细
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PrescriptionCreateRequest.PrescriptionItemRequest item : request.getItems()) {
            // 查询药品信息
            Medicine medicine = medicineMapper.selectById(item.getMedicineId());
            if (medicine == null) {
                throw new BusinessException(ResultCode.BUSINESS_ERROR, "药品不存在：" + item.getMedicineId());
            }

            // 检查库存
            if (medicine.getStockLevel() < item.getQuantity()) {
                throw new BusinessException(ResultCode.BUSINESS_ERROR,
                        "药品库存不足：" + medicine.getMedicineName());
            }

            // 计算金额
            BigDecimal itemAmount = medicine.getUnitPrice()
                    .multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(itemAmount);

            // 创建处方明细
            PrescriptionDetail detail = new PrescriptionDetail();
            detail.setPrescriptionId(prescription.getPrescriptionId());
            detail.setMedicineId(item.getMedicineId());
            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(medicine.getUnitPrice());
            detail.setSubtotal(itemAmount);
            detail.setUsageMethod(item.getUsage());
            detail.setFrequency(item.getFrequency());
            detail.setDays(item.getDays());

            prescriptionDetailMapper.insert(detail);

            // 插入收费明细
            BillItem billItem = new BillItem();
            billItem.setBillId(bill.getBillId());
            billItem.setItemType("药品");
            billItem.setItemName(medicine.getMedicineName() + " x" + item.getQuantity());
            billItem.setAmount(itemAmount);
            billItem.setReferenceId(prescription.getPrescriptionId());

            billItemMapper.insert(billItem);
        }

        // 更新收费单总金额
        bill.setTotalAmount(bill.getTotalAmount().add(totalAmount));
        billMapper.updateById(bill);

        return prescription.getPrescriptionId();
    }
    @Override
    public PharmacyPrescriptionVO getPrescriptionByAppointment(Integer staffId, Integer appointmentId) {
        LambdaQueryWrapper<MedicalRecord> recordQuery = new LambdaQueryWrapper<>();
        recordQuery.eq(MedicalRecord::getAppointmentId, appointmentId);
        MedicalRecord record = medicalRecordMapper.selectOne(recordQuery);
        if (record == null) {
            return null;
        }

        LambdaQueryWrapper<Prescription> prescriptionQuery = new LambdaQueryWrapper<>();
        prescriptionQuery.eq(Prescription::getRecordId, record.getRecordId())
                .orderByDesc(Prescription::getPrescriptionDate)
                .last("LIMIT 1");
        Prescription prescription = prescriptionMapper.selectOne(prescriptionQuery);
        if (prescription == null) {
            return null;
        }

        return buildPrescriptionVO(prescription);
    }

    private MedicalRecordDetailVO buildMedicalRecordDetail(MedicalRecord record) {
        MedicalRecordDetailVO vo = new MedicalRecordDetailVO();
        vo.setRecordId(record.getRecordId());
        vo.setAppointmentId(record.getAppointmentId());
        vo.setPatientId(record.getPatientId());
        vo.setStaffId(record.getStaffId());
        vo.setSubjective(record.getSubjective());
        vo.setObjective(record.getObjective());
        vo.setAssessment(record.getAssessment());
        vo.setPlan(record.getPlan());
        vo.setCreateTime(record.getCreateTime());

        Patient patient = patientMapper.selectById(record.getPatientId());
        if (patient != null) {
            vo.setPatientName(patient.getPatientName());
        }

        Staff staff = staffMapper.selectById(record.getStaffId());
        if (staff != null) {
            vo.setStaffName(staff.getStaffName());

            Department dept = departmentMapper.selectById(staff.getDeptId());
            if (dept != null) {
                vo.setDeptName(dept.getDeptName());
            }
        }

        return vo;
    }

    private PharmacyPrescriptionVO buildPrescriptionVO(Prescription prescription) {
        PharmacyPrescriptionVO vo = new PharmacyPrescriptionVO();
        vo.setPrescriptionId(prescription.getPrescriptionId());
        vo.setRecordId(prescription.getRecordId());
        vo.setPatientId(prescription.getPatientId());
        vo.setStaffId(prescription.getStaffId());
        vo.setPrescriptionDate(prescription.getPrescriptionDate());
        vo.setStatus(prescription.getStatus());
        vo.setAdvice(prescription.getAdvice());

        Patient patient = patientMapper.selectById(prescription.getPatientId());
        if (patient != null) {
            vo.setPatientName(patient.getPatientName());
            vo.setPatientGender(patient.getGender());

            if (patient.getDateOfBirth() != null) {
                Period period = Period.between(patient.getDateOfBirth(), LocalDate.now());
                vo.setPatientAge(period.getYears());
            }
        }

        Staff staff = staffMapper.selectById(prescription.getStaffId());
        if (staff != null) {
            vo.setStaffName(staff.getStaffName());

            if (staff.getDeptId() != null) {
                Department dept = departmentMapper.selectById(staff.getDeptId());
                if (dept != null) {
                    vo.setDeptName(dept.getDeptName());
                }
            }
        }

        LambdaQueryWrapper<PrescriptionDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(PrescriptionDetail::getPrescriptionId, prescription.getPrescriptionId());
        List<PrescriptionDetail> details = prescriptionDetailMapper.selectList(detailQuery);

        List<PharmacyPrescriptionVO.PrescriptionDetailVO> detailVOs = new ArrayList<>();
        for (PrescriptionDetail detail : details) {
            PharmacyPrescriptionVO.PrescriptionDetailVO detailVO = new PharmacyPrescriptionVO.PrescriptionDetailVO();
            detailVO.setDetailId(detail.getDetailId());
            detailVO.setMedicineId(detail.getMedicineId());
            detailVO.setQuantity(detail.getQuantity());
            detailVO.setUnitPrice(detail.getUnitPrice());
            detailVO.setSubtotal(detail.getSubtotal());
            detailVO.setUsage(detail.getUsageMethod());
            detailVO.setFrequency(detail.getFrequency());
            detailVO.setDays(detail.getDays());

            Medicine medicine = medicineMapper.selectById(detail.getMedicineId());
            if (medicine != null) {
                detailVO.setMedicineName(medicine.getMedicineName());
                detailVO.setSpecification(medicine.getSpecification());
                detailVO.setUnitPrice(medicine.getUnitPrice());
            }

            detailVOs.add(detailVO);
        }
        vo.setDetails(detailVOs);

        return vo;
    }

    /**
     * 获取或创建收费单
     */
    private Bill getOrCreateBill(Integer patientId, Integer appointmentId) {
        // 查询是否已有该患者的未支付收费单
        LambdaQueryWrapper<Bill> query = new LambdaQueryWrapper<>();
        query.eq(Bill::getPatientId, patientId)
                .eq(Bill::getStatus, "未支付")
                .orderByDesc(Bill::getCreateTime)
                .last("LIMIT 1");

        Bill bill = billMapper.selectOne(query);

        if (bill == null) {
            // 创建新的收费单
            bill = new Bill();
            bill.setPatientId(patientId);
            bill.setTotalAmount(BigDecimal.ZERO);
            bill.setStatus("未支付");
            bill.setCreateTime(LocalDateTime.now());
            billMapper.insert(bill);
        }

        return bill;
    }
}


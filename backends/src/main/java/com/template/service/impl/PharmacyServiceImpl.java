package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.template.common.BusinessException;
import com.template.common.ResultCode;
import com.template.entity.*;
import com.template.mapper.*;
import com.template.service.PharmacyService;
import com.template.vo.PharmacyPrescriptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * 药房服务实现类
 *
 * @author template
 */
@Service
public class PharmacyServiceImpl implements PharmacyService {

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private PrescriptionDetailMapper prescriptionDetailMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillItemMapper billItemMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    @Override
    public List<PharmacyPrescriptionVO> getPendingPrescriptions() {
        // 直接查询所有"未发药"状态的处方
        LambdaQueryWrapper<Prescription> query = new LambdaQueryWrapper<>();
        query.eq(Prescription::getStatus, "未发药")
                .orderByDesc(Prescription::getPrescriptionDate);
        List<Prescription> prescriptions = prescriptionMapper.selectList(query);

        List<PharmacyPrescriptionVO> result = new ArrayList<>();

        for (Prescription prescription : prescriptions) {
            // 查询关联的收费单（通过 BillItem ）- 修改这里！
            LambdaQueryWrapper<BillItem> itemQuery = new LambdaQueryWrapper<>();
            itemQuery.eq(BillItem::getReferenceId, prescription.getPrescriptionId())
                    .eq(BillItem::getItemType, "药品");

            // ❌ 原代码：BillItem billItem = billItemMapper.selectOne(itemQuery);
            // ✅ 修改为：获取所有相关的收费项
            List<BillItem> billItems = billItemMapper.selectList(itemQuery);

            Bill bill = null;
            if (!billItems.isEmpty()) {
                // 假设同一个处方的所有药品收费项都关联到同一个账单
                // 取第一个billItem的billId（通常所有项都属于同一个账单）
                Integer billId = billItems.get(0).getBillId();
                bill = billMapper.selectById(billId);
            }

            PharmacyPrescriptionVO vo = buildPrescriptionVO(prescription, bill);
            if (vo != null) {
                result.add(vo);
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dispenseMedicine(Integer prescriptionId) {
        // 1. 验证处方是否存在
        Prescription prescription = prescriptionMapper.selectById(prescriptionId);
        if (prescription == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "处方不存在");
        }

        // 2. 验证处方状态
        if ("已发药".equals(prescription.getStatus())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该处方已发药");
        }

        // 3. 查询处方明细
        LambdaQueryWrapper<PrescriptionDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(PrescriptionDetail::getPrescriptionId, prescriptionId);
        List<PrescriptionDetail> details = prescriptionDetailMapper.selectList(detailQuery);

        if (details.isEmpty()) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "处方明细为空");
        }

        // 4. 验证库存并扣减
        for (PrescriptionDetail detail : details) {
            Medicine medicine = medicineMapper.selectById(detail.getMedicineId());
            if (medicine == null) {
                throw new BusinessException(ResultCode.BUSINESS_ERROR, "药品不存在：ID=" + detail.getMedicineId());
            }

            // 检查库存
            if (medicine.getStockLevel() < detail.getQuantity()) {
                throw new BusinessException(ResultCode.BUSINESS_ERROR,
                        "药品库存不足：" + medicine.getMedicineName() + "，需要" + detail.getQuantity() + "，库存" + medicine.getStockLevel());
            }

            // 扣减库存
            medicine.setStockLevel(medicine.getStockLevel() - detail.getQuantity());
            medicineMapper.updateById(medicine);

            // 记录库存流水
            InventoryLog log = new InventoryLog();
            log.setMedicineId(detail.getMedicineId());
            log.setChangeQuantity(-detail.getQuantity()); // 负数表示消耗
            log.setReason("处方消耗");
            log.setRelatedId(prescriptionId); // 关联处方ID
            // createTime 会自动填充，不需要手动设置
            inventoryLogMapper.insert(log);
        }

        // 5. 更新处方状态为"已发药"
        prescription.setStatus("已发药");
        prescriptionMapper.updateById(prescription);
    }

    /**
     * 构建处方VO对象
     */
    private PharmacyPrescriptionVO buildPrescriptionVO(Prescription prescription, Bill bill) {
        PharmacyPrescriptionVO vo = new PharmacyPrescriptionVO();
        vo.setPrescriptionId(prescription.getPrescriptionId());
        vo.setRecordId(prescription.getRecordId());
        vo.setPatientId(prescription.getPatientId());
        vo.setStaffId(prescription.getStaffId());
        vo.setPrescriptionDate(prescription.getPrescriptionDate());
        vo.setStatus(prescription.getStatus());
        vo.setAdvice(prescription.getAdvice());
        vo.setBillId(bill.getBillId());
        vo.setBillAmount(bill.getTotalAmount());

        // 查询患者信息
        Patient patient = patientMapper.selectById(prescription.getPatientId());
        if (patient != null) {
            vo.setPatientName(patient.getPatientName());
            vo.setPatientGender(patient.getGender());

            // 计算年龄
            if (patient.getDateOfBirth() != null) {
                Period period = Period.between(patient.getDateOfBirth(), LocalDate.now());
                vo.setPatientAge(period.getYears());
            }
        }

        // 查询医生信息
        Staff staff = staffMapper.selectById(prescription.getStaffId());
        if (staff != null) {
            vo.setStaffName(staff.getStaffName());

            // 查询科室信息
            if (staff.getDeptId() != null) {
                Department dept = departmentMapper.selectById(staff.getDeptId());
                if (dept != null) {
                    vo.setDeptName(dept.getDeptName());
                }
            }
        }

        // 查询处方明细
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

            // 查询药品信息
            Medicine medicine = medicineMapper.selectById(detail.getMedicineId());
            if (medicine != null) {
                detailVO.setMedicineName(medicine.getMedicineName());
                detailVO.setSpecification(medicine.getSpecification());
                // unit 字段暂不设置（Medicine 实体类中没有该字段）
                detailVO.setUnitPrice(medicine.getUnitPrice());
            }

            detailVOs.add(detailVO);
        }
        vo.setDetails(detailVOs);

        return vo;
    }
}


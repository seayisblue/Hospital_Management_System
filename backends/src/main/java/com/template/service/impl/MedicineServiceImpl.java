package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.BusinessException;
import com.template.common.ResultCode;
import com.template.dto.MedicineCreateRequest;
import com.template.dto.MedicineQueryRequest;
import com.template.dto.MedicineUpdateRequest;
import com.template.entity.Medicine;
// 补全缺失的 import
import com.template.mapper.InventoryLogMapper;
import com.template.mapper.MedicineMapper;
import com.template.service.MedicineService;
import com.template.vo.MedicineVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 补全这个
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 药品服务实现类
 *
 * @author template
 */
@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineMapper medicineMapper;

    // 注入库存流水 Mapper
    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class) // 加上事务注解
    public Integer createMedicine(MedicineCreateRequest request) {
        Medicine medicine = new Medicine();
        BeanUtils.copyProperties(request, medicine);

        // 1. 处理初始库存
        int stock = request.getInitialStock() != null ? request.getInitialStock() : 0;
        medicine.setStockLevel(stock);

        medicineMapper.insert(medicine);

        // 2. 如果有初始库存，自动记录一条流水
        if (stock > 0) {
            com.template.entity.InventoryLog log = new com.template.entity.InventoryLog();
            log.setMedicineId(medicine.getMedicineId());
            log.setChangeQuantity(stock);
            log.setReason("采购入库"); // 记录原因为初始录入
            // log.setCreateTime(LocalDateTime.now()); // 数据库若没自动填充，需手动加这行
            inventoryLogMapper.insert(log);
        }

        return medicine.getMedicineId();
    }

    @Override
    public void updateMedicine(Integer medicineId, MedicineUpdateRequest request) {
        Medicine medicine = medicineMapper.selectById(medicineId);
        if (medicine == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "药品不存在");
        }

        Medicine updateMedicine = new Medicine();
        updateMedicine.setMedicineId(medicineId);
        BeanUtils.copyProperties(request, updateMedicine);
        medicineMapper.updateById(updateMedicine);
    }

    @Override
    public void deleteMedicine(Integer medicineId) {
        Medicine medicine = medicineMapper.selectById(medicineId);
        if (medicine == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "药品不存在");
        }

        medicineMapper.deleteById(medicineId);
    }

    @Override
    public MedicineVO getMedicineDetail(Integer medicineId) {
        Medicine medicine = medicineMapper.selectById(medicineId);
        if (medicine == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "药品不存在");
        }

        MedicineVO vo = new MedicineVO();
        BeanUtils.copyProperties(medicine, vo);
        return vo;
    }

    @Override
    public Page<MedicineVO> getMedicinePage(MedicineQueryRequest request) {
        Page<Medicine> page = new Page<>(request.getPage(), request.getPageSize());

        LambdaQueryWrapper<Medicine> query = new LambdaQueryWrapper<>();

        // 按药品名称模糊查询
        if (StringUtils.hasText(request.getMedicineName())) {
            query.like(Medicine::getMedicineName, request.getMedicineName());
        }

        // 按生产厂家模糊查询
        if (StringUtils.hasText(request.getManufacturer())) {
            query.like(Medicine::getManufacturer, request.getManufacturer());
        }

        Page<Medicine> medicinePage = medicineMapper.selectPage(page, query);

        // 转换为VO
        Page<MedicineVO> voPage = new Page<>(medicinePage.getCurrent(), medicinePage.getSize(), medicinePage.getTotal());
        voPage.setRecords(
                medicinePage.getRecords().stream()
                        .map(medicine -> {
                            MedicineVO vo = new MedicineVO();
                            BeanUtils.copyProperties(medicine, vo);
                            return vo;
                        })
                        .collect(java.util.stream.Collectors.toList())
        );

        return voPage;
    }
}
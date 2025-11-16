package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.ResultCode;
import com.template.dto.MedicineCreateRequest;
import com.template.dto.MedicineQueryRequest;
import com.template.dto.MedicineUpdateRequest;
import com.template.entity.Medicine;
import com.template.common.BusinessException;
import com.template.mapper.MedicineMapper;
import com.template.service.MedicineService;
import com.template.vo.MedicineVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 药品服务实现类
 *
 * @author template
 */
@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineMapper medicineMapper;

    @Override
    public Integer createMedicine(MedicineCreateRequest request) {
        Medicine medicine = new Medicine();
        BeanUtils.copyProperties(request, medicine);
        medicine.setStockLevel(0); // 初始库存为0
        
        medicineMapper.insert(medicine);
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


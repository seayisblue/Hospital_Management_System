package com.template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.dto.MedicineCreateRequest;
import com.template.dto.MedicineQueryRequest;
import com.template.dto.MedicineUpdateRequest;
import com.template.vo.MedicineVO;

/**
 * 药品服务接口
 *
 * @author template
 */
public interface MedicineService {

    /**
     * 创建药品
     */
    Integer createMedicine(MedicineCreateRequest request);

    /**
     * 更新药品
     */
    void updateMedicine(Integer medicineId, MedicineUpdateRequest request);

    /**
     * 删除药品
     */
    void deleteMedicine(Integer medicineId);

    /**
     * 获取药品详情
     */
    MedicineVO getMedicineDetail(Integer medicineId);

    /**
     * 分页查询药品列表
     */
    Page<MedicineVO> getMedicinePage(MedicineQueryRequest request);
}


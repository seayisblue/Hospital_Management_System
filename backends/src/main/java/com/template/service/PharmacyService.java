package com.template.service;

import com.template.vo.PharmacyPrescriptionVO;

import java.util.List;

/**
 * 药房服务接口
 *
 * @author template
 */
public interface PharmacyService {

    /**
     * 获取待发药处方列表（已缴费）
     *
     * @return 处方列表
     */
    List<PharmacyPrescriptionVO> getPendingPrescriptions();

    /**
     * 确认发药
     *
     * @param prescriptionId 处方ID
     */
    void dispenseMedicine(Integer prescriptionId);
}


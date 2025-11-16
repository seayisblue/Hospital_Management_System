package com.template.service;

import com.template.dto.MedicalRecordCreateRequest;
import com.template.dto.PrescriptionCreateRequest;
import com.template.vo.DoctorAppointmentVO;
import com.template.vo.MedicalRecordDetailVO;

import java.util.List;

/**
 * 医生服务接口
 *
 * @author template
 */
public interface DoctorService {

    /**
     * 获取医生的待就诊列表
     *
     * @param staffId 医生ID
     * @return 待就诊列表
     */
    List<DoctorAppointmentVO> getPendingAppointments(Integer staffId);

    /**
     * 创建病历
     *
     * @param staffId 医生ID
     * @param request 病历信息
     * @return 病历ID
     */
    Integer createMedicalRecord(Integer staffId, MedicalRecordCreateRequest request);

    /**
     * 获取病历详情
     *
     * @param recordId 病历ID
     * @return 病历详情
     */
    MedicalRecordDetailVO getMedicalRecordDetail(Integer recordId);

    /**
     * 创建处方
     *
     * @param staffId 医生ID
     * @param request 处方信息
     * @return 处方ID
     */
    Integer createPrescription(Integer staffId, PrescriptionCreateRequest request);
}


package com.template.service;

import com.template.dto.AppointmentCreateRequest;
import com.template.dto.ScheduleQueryRequest;
import com.template.vo.AppointmentVO;
import com.template.vo.DoctorScheduleVO;
import com.template.vo.MedicalRecordVO;
import com.template.vo.PrescriptionVO;

import java.util.List;

/**
 * 挂号服务接口
 *
 * @author template
 */
public interface AppointmentService {

    /**
     * 查询可预约的排班列表
     *
     * @param request 查询请求
     * @return 排班列表
     */
    List<DoctorScheduleVO> getAvailableSchedules(ScheduleQueryRequest request);

    /**
     * 创建挂号
     *
     * @param request 挂号请求
     * @return 挂号ID
     */
    Integer createAppointment(AppointmentCreateRequest request);

    /**
     * 取消挂号
     *
     * @param appointmentId 挂号ID
     * @param patientId     患者ID
     */
    void cancelAppointment(Integer appointmentId, Integer patientId);

    /**
     * 查询患者的挂号列表
     *
     * @param patientId 患者ID
     * @return 挂号列表
     */
    List<AppointmentVO> getPatientAppointments(Integer patientId);

    /**
     * 查询患者的病历列表
     *
     * @param patientId 患者ID
     * @return 病历列表
     */
    List<MedicalRecordVO> getPatientMedicalRecords(Integer patientId);

    /**
     * 查询患者的处方列表
     *
     * @param patientId 患者ID
     * @return 处方列表
     */
    List<PrescriptionVO> getPatientPrescriptions(Integer patientId);
}


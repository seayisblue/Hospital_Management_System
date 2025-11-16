package com.template.service;

import com.template.dto.PatientLoginRequest;
import com.template.dto.PatientRegisterRequest;
import com.template.dto.PatientUpdateRequest;
import com.template.vo.PatientLoginVO;
import com.template.vo.PatientVO;

import java.util.List;

/**
 * 患者服务接口
 *
 * @author template
 */
public interface PatientService {

    /**
     * 患者注册
     *
     * @param request 注册请求
     * @return 患者ID
     */
    Integer register(PatientRegisterRequest request);

    /**
     * 患者登录
     *
     * @param request 登录请求
     * @return 登录信息（包含Token）
     */
    PatientLoginVO login(PatientLoginRequest request);

    /**
     * 查看个人信息
     *
     * @param patientId 患者ID
     * @return 患者信息
     */
    PatientVO getPatientInfo(Integer patientId);

    /**
     * 修改个人信息
     *
     * @param patientId 患者ID
     * @param request   更新请求
     */
    void updatePatientInfo(Integer patientId, PatientUpdateRequest request);

    /**
     * 搜索患者（支持模糊搜索姓名和手机号）
     *
     * @param keyword 搜索关键字（可为空）
     * @param page 页码
     * @param pageSize 每页大小
     * @return 患者列表
     */
    List<PatientVO> searchPatients(String keyword, Integer page, Integer pageSize);
}


package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.template.common.ResultCode;
import com.template.dto.PatientLoginRequest;
import com.template.dto.PatientRegisterRequest;
import com.template.dto.PatientUpdateRequest;
import com.template.entity.Patient;
import com.template.common.BusinessException;
import com.template.mapper.PatientMapper;
import com.template.service.PatientService;
import com.template.util.JwtUtil;
import com.template.util.PasswordUtil;
import com.template.vo.PatientLoginVO;
import com.template.vo.PatientVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 患者服务实现类
 *
 * @author template
 */
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public Integer register(PatientRegisterRequest request) {
        // 检查手机号是否已注册
        LambdaQueryWrapper<Patient> phoneQuery = new LambdaQueryWrapper<>();
        phoneQuery.eq(Patient::getPhoneNumber, request.getPhoneNumber());
        if (patientMapper.selectCount(phoneQuery) > 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该手机号已被注册");
        }

        // 检查身份证号是否已注册
        LambdaQueryWrapper<Patient> idCardQuery = new LambdaQueryWrapper<>();
        idCardQuery.eq(Patient::getIdCardNumber, request.getIdCardNumber());
        if (patientMapper.selectCount(idCardQuery) > 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该身份证号已被注册");
        }

        // 创建患者实体
        Patient patient = new Patient();
        BeanUtils.copyProperties(request, patient);
        // 加密密码
        patient.setPasswordHash(PasswordUtil.encode(request.getPassword()));

        // 保存到数据库
        patientMapper.insert(patient);

        return patient.getPatientId();
    }

    @Override
    public PatientLoginVO login(PatientLoginRequest request) {
        // 根据手机号或身份证号查询患者
        LambdaQueryWrapper<Patient> query = new LambdaQueryWrapper<>();
        query.and(wrapper -> wrapper
                .eq(Patient::getPhoneNumber, request.getAccount())
                .or()
                .eq(Patient::getIdCardNumber, request.getAccount())
        );

        Patient patient = patientMapper.selectOne(query);
        if (patient == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "账号不存在");
        }

        // 验证密码
        if (!PasswordUtil.matches(request.getPassword(), patient.getPasswordHash())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "密码错误");
        }

        // 生成Token
        String token = JwtUtil.generateToken(patient.getPatientId().longValue(), patient.getPatientName());

        // 构造返回对象
        PatientLoginVO loginVO = new PatientLoginVO();
        loginVO.setPatientId(patient.getPatientId());
        loginVO.setPatientName(patient.getPatientName());
        loginVO.setPhoneNumber(maskPhoneNumber(patient.getPhoneNumber()));
        loginVO.setToken(token);

        return loginVO;
    }

    @Override
    public PatientVO getPatientInfo(Integer patientId) {
        Patient patient = patientMapper.selectById(patientId);
        if (patient == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "患者不存在");
        }

        PatientVO patientVO = new PatientVO();
        BeanUtils.copyProperties(patient, patientVO);
        // 脱敏处理
        patientVO.setPhoneNumber(maskPhoneNumber(patient.getPhoneNumber()));
        patientVO.setIdCardNumber(maskIdCardNumber(patient.getIdCardNumber()));

        return patientVO;
    }

    @Override
    public void updatePatientInfo(Integer patientId, PatientUpdateRequest request) {
        Patient patient = patientMapper.selectById(patientId);
        if (patient == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "患者不存在");
        }

        // 1. 检查手机号是否变更
        if (org.springframework.util.StringUtils.hasText(request.getPhoneNumber())
                && !request.getPhoneNumber().equals(patient.getPhoneNumber())) {

            // 检查新手机号是否已存在
            LambdaQueryWrapper<Patient> query = new LambdaQueryWrapper<>();
            query.eq(Patient::getPhoneNumber, request.getPhoneNumber());
            if (patientMapper.selectCount(query) > 0) {
                throw new BusinessException(ResultCode.BUSINESS_ERROR, "该手机号已被其他用户使用");
            }
        }

        // 2. 手动更新字段 (放弃 BeanUtils，防止属性名不一致或null覆盖问题)
        Patient updatePatient = new Patient();
        updatePatient.setPatientId(patientId);

        // 只有当前端传了值，才更新
        if (org.springframework.util.StringUtils.hasText(request.getPhoneNumber())) {
            updatePatient.setPhoneNumber(request.getPhoneNumber());
        }
        if (org.springframework.util.StringUtils.hasText(request.getAddress())) {
            updatePatient.setAddress(request.getAddress());
        }
        // 还可以加其他允许修改的字段...

        patientMapper.updateById(updatePatient);
    }

    @Override
    public java.util.List<PatientVO> searchPatients(String keyword, Integer page, Integer pageSize) {
        LambdaQueryWrapper<Patient> query = new LambdaQueryWrapper<>();
        
        // 如果有关键字，模糊搜索姓名或手机号
        if (keyword != null && !keyword.isEmpty()) {
            query.and(wrapper -> wrapper
                .like(Patient::getPatientName, keyword)
                .or()
                .like(Patient::getPhoneNumber, keyword)
            );
        }
        
        query.orderByDesc(Patient::getPatientId);
        
        // 分页查询
        if (page != null && pageSize != null) {
            int offset = (page - 1) * pageSize;
            query.last("LIMIT " + offset + ", " + pageSize);
        }
        
        java.util.List<Patient> patients = patientMapper.selectList(query);
        
        // 转换为VO（不脱敏手机号，方便前端显示）
        return patients.stream().map(patient -> {
            PatientVO vo = new PatientVO();
            BeanUtils.copyProperties(patient, vo);
            vo.setPhoneNumber(patient.getPhoneNumber());  // 不脱敏
            vo.setIdCardNumber(maskIdCardNumber(patient.getIdCardNumber()));  // 身份证号脱敏
            return vo;
        }).collect(java.util.stream.Collectors.toList());
    }

    /**
     * 手机号脱敏
     */
    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 11) {
            return phoneNumber;
        }
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7);
    }

    /**
     * 身份证号脱敏
     */
    private String maskIdCardNumber(String idCardNumber) {
        if (idCardNumber == null || idCardNumber.length() < 6) {
            return idCardNumber;
        }
        return idCardNumber.substring(0, 6) + "********" + idCardNumber.substring(idCardNumber.length() - 4);
    }
}


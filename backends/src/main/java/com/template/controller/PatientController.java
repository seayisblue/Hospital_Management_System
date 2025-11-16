package com.template.controller;

import com.template.common.Result;
import com.template.dto.PatientLoginRequest;
import com.template.dto.PatientRegisterRequest;
import com.template.dto.PatientUpdateRequest;
import com.template.service.PatientService;
import com.template.vo.PatientLoginVO;
import com.template.vo.PatientVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 患者控制器
 *
 * @author template
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * 患者注册
     *
     * @param request 注册请求
     * @return 患者ID
     */
    @PostMapping("/register")
    public Result<Integer> register(@Valid @RequestBody PatientRegisterRequest request) {
        Integer patientId = patientService.register(request);
        return Result.success("注册成功", patientId);
    }

    /**
     * 患者登录
     *
     * @param request 登录请求
     * @return 登录信息（包含Token）
     */
    @PostMapping("/login")
    public Result<PatientLoginVO> login(@Valid @RequestBody PatientLoginRequest request) {
        PatientLoginVO loginVO = patientService.login(request);
        return Result.success("登录成功", loginVO);
    }

    /**
     * 查看个人信息
     *
     * @param httpRequest HTTP请求
     * @return 患者信息
     */
    @GetMapping("/info")
    public Result<PatientVO> getInfo(HttpServletRequest httpRequest) {
        // 从Token中获取患者ID
        Integer patientId = (Integer) httpRequest.getAttribute("userId");
        PatientVO patientVO = patientService.getPatientInfo(patientId);
        return Result.success(patientVO);
    }

    /**
     * 修改个人信息
     *
     * @param request     更新请求
     * @param httpRequest HTTP请求
     * @return 操作结果
     */
    @PutMapping("/info")
    public Result<Void> updateInfo(@RequestBody PatientUpdateRequest request, HttpServletRequest httpRequest) {
        // 从Token中获取患者ID
        Integer patientId = (Integer) httpRequest.getAttribute("userId");
        patientService.updatePatientInfo(patientId, request);
        return Result.success("修改成功", null);
    }

    /**
     * 搜索患者（支持模糊搜索姓名和手机号）
     *
     * @param keyword 搜索关键字（可选）
     * @param page 页码（默认1）
     * @param pageSize 每页大小（默认20）
     * @return 患者列表
     */
    @GetMapping("/search")
    public Result<List<PatientVO>> searchPatients(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        List<PatientVO> patients = patientService.searchPatients(keyword, page, pageSize);
        return Result.success(patients);
    }
}


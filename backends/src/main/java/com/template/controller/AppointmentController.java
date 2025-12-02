package com.template.controller;

import com.template.common.BusinessException;
import com.template.common.Result;
import com.template.common.ResultCode;
import com.template.dto.AppointmentCreateRequest;
import com.template.dto.ScheduleQueryRequest;
import com.template.service.AppointmentService;
import com.template.vo.AppointmentVO;
import com.template.vo.DoctorScheduleVO;
import com.template.vo.MedicalRecordVO;
import com.template.vo.PrescriptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 挂号控制器
 *
 * @author template
 */
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 查询可预约的排班列表
     *
     * @param request 查询请求
     * @return 排班列表
     */
    @GetMapping("/schedules")
    public Result<List<DoctorScheduleVO>> getAvailableSchedules(ScheduleQueryRequest request) {
        List<DoctorScheduleVO> schedules = appointmentService.getAvailableSchedules(request);
        return Result.success(schedules);
    }

    /**
     * 创建挂号
     *
     * @param request     挂号请求
     * @param httpRequest HTTP请求
     * @return 挂号ID
     */
    @PostMapping
    public Result<Integer> createAppointment(@Valid @RequestBody AppointmentCreateRequest request, 
                                             HttpServletRequest httpRequest) {
        // 从Token中获取患者ID
        Integer patientId = (Integer) httpRequest.getAttribute("userId");
        
        if (patientId == null) {
            throw new BusinessException(ResultCode.PERMISSION_TOKEN_INVALID, "未获取到患者信息，请重新登录");
        }
        
        request.setPatientId(patientId);
        
        Integer appointmentId = appointmentService.createAppointment(request);
        return Result.success("挂号成功", appointmentId);
    }

    /**
     * 取消挂号
     *
     * @param appointmentId 挂号ID
     * @param httpRequest   HTTP请求
     * @return 操作结果
     */
    @DeleteMapping("/{appointmentId}")
    public Result<Void> cancelAppointment(@PathVariable Integer appointmentId, 
                                          HttpServletRequest httpRequest) {
        // 从Token中获取患者ID
        Integer patientId = (Integer) httpRequest.getAttribute("userId");
        appointmentService.cancelAppointment(appointmentId, patientId);
        return Result.success("取消挂号成功", null);
    }

    /**
     * 查询我的挂号列表
     *
     * @param httpRequest HTTP请求
     * @return 挂号列表
     */
    @GetMapping("/my")
    public Result<List<AppointmentVO>> getMyAppointments(HttpServletRequest httpRequest) {
        // 从Token中获取患者ID
        Integer patientId = (Integer) httpRequest.getAttribute("userId");
        List<AppointmentVO> appointments = appointmentService.getPatientAppointments(patientId);
        return Result.success(appointments);
    }

    /**
     * 查询我的病历列表
     *
     * @param httpRequest HTTP请求
     * @return 病历列表
     */
    @GetMapping("/medical-records")
    public Result<List<MedicalRecordVO>> getMyMedicalRecords(HttpServletRequest httpRequest) {
        // 从Token中获取患者ID
        Integer patientId = (Integer) httpRequest.getAttribute("userId");
        List<MedicalRecordVO> records = appointmentService.getPatientMedicalRecords(patientId);
        return Result.success(records);
    }

    /**
     * 查询我的处方列表
     *
     * @param httpRequest HTTP请求
     * @return 处方列表
     */
    @GetMapping("/prescriptions")
    public Result<List<PrescriptionVO>> getMyPrescriptions(HttpServletRequest httpRequest) {
        // 从Token中获取患者ID
        Integer patientId = (Integer) httpRequest.getAttribute("userId");
        List<PrescriptionVO> prescriptions = appointmentService.getPatientPrescriptions(patientId);
        return Result.success(prescriptions);
    }

    /**
     * 获取处方详情（包含药品列表）
     */
    @GetMapping("/prescriptions/{prescriptionId}")
    public Result<com.template.vo.PharmacyPrescriptionVO> getPrescriptionDetail(@PathVariable Integer prescriptionId) {
        com.template.vo.PharmacyPrescriptionVO vo = appointmentService.getPrescriptionDetail(prescriptionId);
        return Result.success(vo);
    }
}


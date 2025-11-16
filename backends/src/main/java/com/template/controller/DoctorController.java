package com.template.controller;

import com.template.common.Result;
import com.template.dto.MedicalRecordCreateRequest;
import com.template.dto.PrescriptionCreateRequest;
import com.template.service.DoctorService;
import com.template.vo.DoctorAppointmentVO;
import com.template.vo.MedicalRecordDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 医生工作站Controller
 *
 * @author template
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取待就诊列表
     */
    @GetMapping("/appointments/pending")
    public Result<List<DoctorAppointmentVO>> getPendingAppointments(HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("userId");
        List<DoctorAppointmentVO> list = doctorService.getPendingAppointments(staffId);
        return Result.success(list);
    }

    /**
     * 创建病历
     */
    @PostMapping("/medical-record")
    public Result<Integer> createMedicalRecord(@Valid @RequestBody MedicalRecordCreateRequest req,
                                               HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("userId");
        Integer recordId = doctorService.createMedicalRecord(staffId, req);
        return Result.success("病历创建成功", recordId);
    }

    /**
     * 获取病历详情
     */
    @GetMapping("/medical-record/{recordId}")
    public Result<MedicalRecordDetailVO> getMedicalRecordDetail(@PathVariable Integer recordId) {
        MedicalRecordDetailVO vo = doctorService.getMedicalRecordDetail(recordId);
        return Result.success(vo);
    }

    /**
     * 创建处方
     */
    @PostMapping("/prescription")
    public Result<Integer> createPrescription(@Valid @RequestBody PrescriptionCreateRequest req,
                                             HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("userId");
        Integer prescriptionId = doctorService.createPrescription(staffId, req);
        return Result.success("处方创建成功", prescriptionId);
    }
}


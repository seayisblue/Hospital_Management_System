package com.template.controller;

import com.template.common.Result;
import com.template.service.DoctorService;
import com.template.vo.MedicalRecordDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 病历查询Controller
 *
 * @author template
 */
@RestController
@RequestMapping("/medical-record")
public class MedicalRecordController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 根据挂号ID查询病历详情
     */
    @GetMapping("/appointment/{appointmentId}")
    public Result<MedicalRecordDetailVO> getByAppointment(@PathVariable Integer appointmentId,
                                                          HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("userId");
        MedicalRecordDetailVO vo = doctorService.getMedicalRecordDetailByAppointment(staffId, appointmentId);
        return Result.success(vo);
    }
}
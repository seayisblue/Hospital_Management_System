package com.template.controller;

import com.template.common.Result;
import com.template.service.DoctorService;
import com.template.vo.PharmacyPrescriptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 处方查询Controller
 *
 * @author template
 */
@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 根据挂号ID查询处方详情
     */
    @GetMapping("/appointment/{appointmentId}")
    public Result<PharmacyPrescriptionVO> getByAppointment(@PathVariable Integer appointmentId,
                                                           HttpServletRequest request) {
        Integer staffId = (Integer) request.getAttribute("userId");
        PharmacyPrescriptionVO vo = doctorService.getPrescriptionByAppointment(staffId, appointmentId);
        return Result.success(vo);
    }
}
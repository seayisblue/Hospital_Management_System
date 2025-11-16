package com.template.controller;

import com.template.common.Result;
import com.template.dto.DispenseMedicineRequest;
import com.template.service.PharmacyService;
import com.template.vo.PharmacyPrescriptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 药房控制器
 *
 * @author template
 */
@RestController
@RequestMapping("/pharmacy")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    /**
     * 获取待发药处方列表
     *
     * @return 处方列表
     */
    @GetMapping("/prescriptions/pending")
    public Result<List<PharmacyPrescriptionVO>> getPendingPrescriptions() {
        List<PharmacyPrescriptionVO> prescriptions = pharmacyService.getPendingPrescriptions();
        return Result.success(prescriptions);
    }

    /**
     * 确认发药
     *
     * @param request 发药请求
     * @return 操作结果
     */
    @PostMapping("/dispense")
    public Result<Void> dispenseMedicine(@Valid @RequestBody DispenseMedicineRequest request) {
        pharmacyService.dispenseMedicine(request.getPrescriptionId());
        return Result.success();
    }
}


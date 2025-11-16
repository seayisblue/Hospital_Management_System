package com.template.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.Result;
import com.template.dto.MedicineCreateRequest;
import com.template.dto.MedicineQueryRequest;
import com.template.dto.MedicineUpdateRequest;
import com.template.service.MedicineService;
import com.template.vo.MedicineVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 药品管理Controller
 *
 * @author template
 */
@RestController
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    /**
     * 创建药品
     */
    @PostMapping
    public Result<Integer> createMedicine(@Valid @RequestBody MedicineCreateRequest request) {
        Integer medicineId = medicineService.createMedicine(request);
        return Result.success("创建成功", medicineId);
    }

    /**
     * 更新药品
     */
    @PutMapping("/{medicineId}")
    public Result<Void> updateMedicine(@PathVariable Integer medicineId,
                                       @RequestBody MedicineUpdateRequest request) {
        medicineService.updateMedicine(medicineId, request);
        return Result.success("更新成功", null);
    }

    /**
     * 删除药品
     */
    @DeleteMapping("/{medicineId}")
    public Result<Void> deleteMedicine(@PathVariable Integer medicineId) {
        medicineService.deleteMedicine(medicineId);
        return Result.success("删除成功", null);
    }

    /**
     * 获取药品详情
     */
    @GetMapping("/{medicineId}")
    public Result<MedicineVO> getMedicineDetail(@PathVariable Integer medicineId) {
        MedicineVO vo = medicineService.getMedicineDetail(medicineId);
        return Result.success(vo);
    }

    /**
     * 分页查询药品列表
     */
    @GetMapping
    public Result<Page<MedicineVO>> getMedicinePage(MedicineQueryRequest request) {
        Page<MedicineVO> page = medicineService.getMedicinePage(request);
        return Result.success(page);
    }
}


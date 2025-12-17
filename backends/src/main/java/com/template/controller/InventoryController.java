package com.template.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.Result;
import com.template.dto.InventoryInboundRequest;
import com.template.dto.InventoryLogQueryRequest;
import com.template.service.InventoryService;
import com.template.vo.InventoryLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 库存管理Controller
 *
 * @author template
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 药品入库
     */
    @PostMapping("/inbound")
    public Result<Integer> inbound(@Valid @RequestBody InventoryInboundRequest request) {
        Integer logId = inventoryService.inbound(request);
        return Result.success("入库成功", logId);
    }

    /**
     * 查询库存流水
     */
    @GetMapping("/logs")
    public Result<Page<InventoryLogVO>> getInventoryLogPage(InventoryLogQueryRequest request) {
        Page<InventoryLogVO> page = inventoryService.getInventoryLogPage(request);
        return Result.success(page);
    }
    /**
     * 库存调整 (盘点/报损/修正)
     */
    @PostMapping("/adjust")
    public Result<Boolean> adjustStock(@Valid @RequestBody com.template.dto.InventoryAdjustRequest request) {
        inventoryService.adjustStock(request);
        return Result.success("库存调整成功", true);
    }
}


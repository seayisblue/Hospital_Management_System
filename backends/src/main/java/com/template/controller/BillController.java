package com.template.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.Result;
import com.template.dto.BillQueryRequest;
import com.template.service.BillService;
import com.template.vo.BillDetailVO;
import com.template.vo.BillVO;
import com.template.vo.RevenueStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收费单Controller
 *
 * @author template
 */
@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    /**
     * 分页查询收费单
     */
    @GetMapping
    public Result<Page<BillVO>> getBillPage(BillQueryRequest request) {
        Page<BillVO> page = billService.getBillPage(request);
        return Result.success(page);
    }

    /**
     * 获取收费单详情
     */
    @GetMapping("/{billId}")
    public Result<BillDetailVO> getBillDetail(@PathVariable Integer billId) {
        BillDetailVO vo = billService.getBillDetail(billId);
        return Result.success(vo);
    }

    /**
     * 收费单支付
     */
    @PutMapping("/{billId}/pay")
    public Result<Void> payBill(@PathVariable Integer billId) {
        billService.payBill(billId);
        return Result.success("支付成功", null);
    }

    /**
     * 获取收入统计
     */
    @GetMapping("/statistics/revenue")
    public Result<RevenueStatisticsVO> getRevenueStatistics() {
        RevenueStatisticsVO vo = billService.getRevenueStatistics();
        return Result.success(vo);
    }
}


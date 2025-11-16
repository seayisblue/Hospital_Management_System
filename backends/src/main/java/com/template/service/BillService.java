package com.template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.dto.BillQueryRequest;
import com.template.vo.BillDetailVO;
import com.template.vo.BillVO;
import com.template.vo.RevenueStatisticsVO;

/**
 * 收费单服务接口
 *
 * @author template
 */
public interface BillService {

    /**
     * 分页查询收费单
     */
    Page<BillVO> getBillPage(BillQueryRequest request);

    /**
     * 获取收费单详情
     */
    BillDetailVO getBillDetail(Integer billId);

    /**
     * 收费单支付
     */
    void payBill(Integer billId);

    /**
     * 获取收入统计
     */
    RevenueStatisticsVO getRevenueStatistics();
}


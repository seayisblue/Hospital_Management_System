package com.template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.dto.InventoryInboundRequest;
import com.template.dto.InventoryLogQueryRequest;
import com.template.vo.InventoryLogVO;

/**
 * 库存服务接口
 *
 * @author template
 */
public interface InventoryService {

    /**
     * 药品入库
     */
    Integer inbound(InventoryInboundRequest request);

    /**
     * 查询库存流水
     */
    Page<InventoryLogVO> getInventoryLogPage(InventoryLogQueryRequest request);

    void adjustStock(com.template.dto.InventoryAdjustRequest request);
}


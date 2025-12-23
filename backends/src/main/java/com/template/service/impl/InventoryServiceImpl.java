package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.ResultCode;
import com.template.dto.InventoryInboundRequest;
import com.template.dto.InventoryAdjustRequest; // 确保引入了这个
import com.template.dto.InventoryLogQueryRequest;
import com.template.entity.InventoryLog;
import com.template.entity.Medicine;
import com.template.common.BusinessException;
import com.template.mapper.InventoryLogMapper;
import com.template.mapper.MedicineMapper;
import com.template.service.InventoryService;
import com.template.vo.InventoryLogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 库存服务实现类
 *
 * @author template
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    // ================== 变量声明区域 (只声明一次) ==================
    @Autowired
    private InventoryLogMapper inventoryLogMapper;

    @Autowired
    private MedicineMapper medicineMapper;
    // ===========================================================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer inbound(InventoryInboundRequest request) {
        // 检查药品是否存在
        Medicine medicine = medicineMapper.selectById(request.getMedicineId());
        if (medicine == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "药品不存在");
        }

        // 创建库存流水记录
        InventoryLog log = new InventoryLog();
        log.setMedicineId(request.getMedicineId());
        log.setChangeQuantity(request.getQuantity());
        log.setReason(request.getReason());
        log.setCreateTime(LocalDateTime.now()); // 补全时间
        inventoryLogMapper.insert(log);

        // 更新药品库存
        medicine.setStockLevel(medicine.getStockLevel() + request.getQuantity());
        medicineMapper.updateById(medicine);

        return log.getLogId();
    }

    /**
     * 新增：库存调整 (盘点/报损/修正)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustStock(InventoryAdjustRequest request) {
        // 1. 查询药品
        Medicine medicine = medicineMapper.selectById(request.getMedicineId());
        if (medicine == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "药品不存在");
        }

        // 2. 计算新库存
        int oldStock = medicine.getStockLevel();
        int changeQty = request.getQuantity(); // 这个值可能是负数
        int newStock = oldStock + changeQty;

        // 3. 校验库存是否足够 (如果是减少库存)
        if (newStock < 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "库存不足，无法减少 " + Math.abs(changeQty) + " (当前库存: " + oldStock + ")");
        }

        // 4. 更新药品库存
        medicine.setStockLevel(newStock);
        medicineMapper.updateById(medicine);

        // 5. 记录流水
        InventoryLog log = new InventoryLog();
        log.setMedicineId(medicine.getMedicineId());
        log.setChangeQuantity(changeQty);
        log.setReason(request.getReason());
        log.setCreateTime(LocalDateTime.now());

        inventoryLogMapper.insert(log);
    }

    @Override
    public Page<InventoryLogVO> getInventoryLogPage(InventoryLogQueryRequest request) {
        Page<InventoryLog> page = new Page<>(request.getPage(), request.getPageSize());

        LambdaQueryWrapper<InventoryLog> query = new LambdaQueryWrapper<>();

        // 按药品ID筛选
        if (request.getMedicineId() != null) {
            query.eq(InventoryLog::getMedicineId, request.getMedicineId());
        }

        // 按原因筛选
        if (StringUtils.hasText(request.getReason())) {
            query.eq(InventoryLog::getReason, request.getReason());
        }

        // 按日期范围筛选
        if (StringUtils.hasText(request.getStartDate())) {
            query.ge(InventoryLog::getCreateTime, request.getStartDate());
        }
        if (StringUtils.hasText(request.getEndDate())) {
            query.le(InventoryLog::getCreateTime, request.getEndDate() + " 23:59:59");
        }

        // 按时间倒序
        query.orderByDesc(InventoryLog::getCreateTime);

        Page<InventoryLog> logPage = inventoryLogMapper.selectPage(page, query);

        // 转换为VO
        Page<InventoryLogVO> voPage = new Page<>(logPage.getCurrent(), logPage.getSize(), logPage.getTotal());
        voPage.setRecords(
                logPage.getRecords().stream()
                        .map(log -> {
                            InventoryLogVO vo = new InventoryLogVO();
                            BeanUtils.copyProperties(log, vo);

                            // 获取药品名称
                            Medicine medicine = medicineMapper.selectById(log.getMedicineId());
                            if (medicine != null) {
                                vo.setMedicineName(medicine.getMedicineName());
                            }

                            return vo;
                        })
                        .collect(java.util.stream.Collectors.toList())
        );

        return voPage;
    }
}
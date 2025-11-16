package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.ResultCode;
import com.template.dto.BillQueryRequest;
import com.template.entity.Bill;
import com.template.entity.BillItem;
import com.template.entity.Patient;
import com.template.common.BusinessException;
import com.template.mapper.BillItemMapper;
import com.template.mapper.BillMapper;
import com.template.mapper.PatientMapper;
import com.template.service.BillService;
import com.template.vo.BillDetailVO;
import com.template.vo.BillVO;
import com.template.vo.RevenueStatisticsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 收费单服务实现类
 *
 * @author template
 */
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillItemMapper billItemMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public Page<BillVO> getBillPage(BillQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<Bill> query = new LambdaQueryWrapper<>();
        
        if (request.getPatientId() != null) {
            query.eq(Bill::getPatientId, request.getPatientId());
        }
        if (StringUtils.hasText(request.getStatus())) {
            query.eq(Bill::getStatus, request.getStatus());
        }
        if (StringUtils.hasText(request.getStartDate())) {
            query.ge(Bill::getCreateTime, request.getStartDate());
        }
        if (StringUtils.hasText(request.getEndDate())) {
            query.le(Bill::getCreateTime, request.getEndDate() + " 23:59:59");
        }
        
        query.orderByDesc(Bill::getCreateTime);

        // 分页查询
        Page<Bill> page = new Page<>(request.getPage(), request.getPageSize());
        Page<Bill> billPage = billMapper.selectPage(page, query);

        // 转换为VO并填充患者信息
        Page<BillVO> voPage = new Page<>(billPage.getCurrent(), billPage.getSize(), billPage.getTotal());
        List<BillVO> voList = billPage.getRecords().stream()
            .map(bill -> {
                BillVO vo = new BillVO();
                vo.setBillId(bill.getBillId());
                vo.setPatientId(bill.getPatientId());
                vo.setTotalAmount(bill.getTotalAmount());
                vo.setStatus(bill.getStatus());
                vo.setCreateTime(bill.getCreateTime());
                vo.setPayTime(bill.getPayTime());

                // 查询患者信息
                Patient patient = patientMapper.selectById(bill.getPatientId());
                if (patient != null) {
                    vo.setPatientName(patient.getPatientName());
                    
                    // 如果有患者姓名筛选条件，在这里过滤
                    if (StringUtils.hasText(request.getPatientName()) && 
                        !patient.getPatientName().contains(request.getPatientName())) {
                        return null;
                    }
                }

                return vo;
            })
            .filter(vo -> vo != null)
            .collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public BillDetailVO getBillDetail(Integer billId) {
        Bill bill = billMapper.selectById(billId);
        if (bill == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "收费单不存在");
        }

        BillDetailVO vo = new BillDetailVO();
        BeanUtils.copyProperties(bill, vo);

        // 获取患者信息
        Patient patient = patientMapper.selectById(bill.getPatientId());
        if (patient != null) {
            vo.setPatientName(patient.getPatientName());
            vo.setPatientPhone(patient.getPhoneNumber());
        }

        // 获取收费明细
        LambdaQueryWrapper<BillItem> itemQuery = new LambdaQueryWrapper<>();
        itemQuery.eq(BillItem::getBillId, billId);
        List<BillItem> items = billItemMapper.selectList(itemQuery);

        List<BillDetailVO.BillItemVO> itemVOList = items.stream()
            .map(item -> {
                BillDetailVO.BillItemVO itemVO = new BillDetailVO.BillItemVO();
                BeanUtils.copyProperties(item, itemVO);
                return itemVO;
            })
            .collect(Collectors.toList());

        vo.setItems(itemVOList);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payBill(Integer billId) {
        Bill bill = billMapper.selectById(billId);
        if (bill == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "收费单不存在");
        }

        if ("已支付".equals(bill.getStatus())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该收费单已支付");
        }

        bill.setStatus("已支付");
        bill.setPayTime(LocalDateTime.now());
        billMapper.updateById(bill);
    }

    @Override
    public RevenueStatisticsVO getRevenueStatistics() {
        RevenueStatisticsVO vo = new RevenueStatisticsVO();

        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(today, LocalTime.MAX);

        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDateTime weekStartTime = LocalDateTime.of(weekStart, LocalTime.MIN);

        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime monthStartTime = LocalDateTime.of(monthStart, LocalTime.MIN);

        // 今日收入和订单数
        QueryWrapper<Bill> todayQuery = new QueryWrapper<>();
        todayQuery.select("COALESCE(SUM(TotalAmount), 0) as revenue", "COUNT(*) as orderCount");
        todayQuery.eq("Status", "已支付");
        todayQuery.ge("PayTime", todayStart);
        todayQuery.le("PayTime", todayEnd);
        Map<String, Object> todayStats = billMapper.selectMaps(todayQuery).stream().findFirst().orElse(null);
        if (todayStats != null) {
            vo.setTodayRevenue((BigDecimal) todayStats.getOrDefault("revenue", BigDecimal.ZERO));
            vo.setTodayOrderCount(((Number) todayStats.getOrDefault("orderCount", 0)).intValue());
        }

        // 本周收入和订单数
        QueryWrapper<Bill> weekQuery = new QueryWrapper<>();
        weekQuery.select("COALESCE(SUM(TotalAmount), 0) as revenue", "COUNT(*) as orderCount");
        weekQuery.eq("Status", "已支付");
        weekQuery.ge("PayTime", weekStartTime);
        Map<String, Object> weekStats = billMapper.selectMaps(weekQuery).stream().findFirst().orElse(null);
        if (weekStats != null) {
            vo.setWeekRevenue((BigDecimal) weekStats.getOrDefault("revenue", BigDecimal.ZERO));
            vo.setWeekOrderCount(((Number) weekStats.getOrDefault("orderCount", 0)).intValue());
        }

        // 本月收入和订单数
        QueryWrapper<Bill> monthQuery = new QueryWrapper<>();
        monthQuery.select("COALESCE(SUM(TotalAmount), 0) as revenue", "COUNT(*) as orderCount");
        monthQuery.eq("Status", "已支付");
        monthQuery.ge("PayTime", monthStartTime);
        Map<String, Object> monthStats = billMapper.selectMaps(monthQuery).stream().findFirst().orElse(null);
        if (monthStats != null) {
            vo.setMonthRevenue((BigDecimal) monthStats.getOrDefault("revenue", BigDecimal.ZERO));
            vo.setMonthOrderCount(((Number) monthStats.getOrDefault("orderCount", 0)).intValue());
        }

        // 按类型统计收入（本月）
        QueryWrapper<BillItem> itemQuery = new QueryWrapper<>();
        itemQuery.select("ItemType", "COALESCE(SUM(Amount), 0) as revenue");
        itemQuery.inSql("BillID", "SELECT BillID FROM T_Bill WHERE Status = '已支付' AND PayTime >= '" + monthStartTime + "'");
        itemQuery.groupBy("ItemType");
        List<Map<String, Object>> itemStats = billItemMapper.selectMaps(itemQuery);

        for (Map<String, Object> stat : itemStats) {
            String itemType = (String) stat.get("ItemType");
            BigDecimal revenue = (BigDecimal) stat.get("revenue");
            if ("挂号".equals(itemType)) {
                vo.setRegistrationRevenue(revenue);
            } else if ("药品".equals(itemType)) {
                vo.setMedicineRevenue(revenue);
            }
        }

        // 设置默认值
        if (vo.getTodayRevenue() == null) vo.setTodayRevenue(BigDecimal.ZERO);
        if (vo.getWeekRevenue() == null) vo.setWeekRevenue(BigDecimal.ZERO);
        if (vo.getMonthRevenue() == null) vo.setMonthRevenue(BigDecimal.ZERO);
        if (vo.getTodayOrderCount() == null) vo.setTodayOrderCount(0);
        if (vo.getWeekOrderCount() == null) vo.setWeekOrderCount(0);
        if (vo.getMonthOrderCount() == null) vo.setMonthOrderCount(0);
        if (vo.getRegistrationRevenue() == null) vo.setRegistrationRevenue(BigDecimal.ZERO);
        if (vo.getMedicineRevenue() == null) vo.setMedicineRevenue(BigDecimal.ZERO);

        return vo;
    }
}


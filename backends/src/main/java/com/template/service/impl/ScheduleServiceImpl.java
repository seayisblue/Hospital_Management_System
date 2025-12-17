package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.BusinessException;
import com.template.common.ResultCode;
import com.template.dto.ScheduleCreateRequest;
import com.template.dto.ScheduleQueryRequest;
import com.template.dto.ScheduleUpdateRequest;
import com.template.entity.Department;
import com.template.entity.DoctorSchedule;
import com.template.entity.Staff;
import com.template.mapper.DepartmentMapper;
import com.template.mapper.DoctorScheduleMapper;
import com.template.mapper.StaffMapper;
import com.template.service.ScheduleService;
import com.template.vo.ScheduleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 排班服务实现类
 *
 * @author template
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private DoctorScheduleMapper scheduleMapper;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 核心权限校验逻辑
     * @param operatorId 操作者ID
     * @param targetStaffId 被排班的医生ID
     */
    private void checkSchedulePermission(Integer operatorId, Integer targetStaffId) {
        Staff operator = staffMapper.selectById(operatorId);
        if (operator == null) {
            throw new BusinessException(ResultCode. PERMISSION_NO_ACCESS, "操作用户不存在");
        }

        // 管理员有权限
        if ("管理员".equals(operator. getRole())) {
            return;
        }

        // 获取操作者所在部门
        Department dept = departmentMapper.selectById(operator.getDeptId());

        // 只要是科室主任就能操作，不再要求职称必须是"主任医师"
        boolean isDeptManager = dept != null && operatorId.equals(dept. getManagerId());
        if (!isDeptManager) {
            throw new BusinessException(ResultCode. PERMISSION_NO_ACCESS, "只有科室主任或管理员才有权操作排班");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createSchedule(ScheduleCreateRequest request, Integer operatorId) { // 增加 operatorId
        // --- 权限校验 ---
        checkSchedulePermission(operatorId, request.getStaffId());
        // ---------------

        // 验证医生是否存在
        Staff staff = staffMapper.selectById(request.getStaffId());
        if (staff == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "医生不存在");
        }
        if (!"医生".equals(staff.getRole())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该职工不是医生");
        }

        // 验证排班是否已存在
        LambdaQueryWrapper<DoctorSchedule> query = new LambdaQueryWrapper<>();
        query.eq(DoctorSchedule::getStaffId, request.getStaffId())
                .eq(DoctorSchedule::getScheduleDate, request.getScheduleDate())
                .eq(DoctorSchedule::getTimeSlot, request.getTimeSlot());

        if (scheduleMapper.selectCount(query) > 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该医生在此时间段已有排班");
        }

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setStaffId(request.getStaffId());
        schedule.setScheduleDate(request.getScheduleDate());
        schedule.setTimeSlot(request.getTimeSlot());
        schedule.setTotalSlots(request.getTotalSlots());
        schedule.setBookedSlots(0);

        scheduleMapper.insert(schedule);
        return schedule.getScheduleId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSchedule(ScheduleUpdateRequest request, Integer operatorId) { // 增加 operatorId
        DoctorSchedule schedule = scheduleMapper.selectById(request.getScheduleId());
        if (schedule == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "排班不存在");
        }

        // 获取目标医生ID (如果是修改医生ID，则校验新的；否则校验旧的)
        Integer targetStaffId = (request.getStaffId() != null) ? request.getStaffId() : schedule.getStaffId();

        // --- 权限校验 ---
        checkSchedulePermission(operatorId, targetStaffId);
        // ---------------

        if (request.getTotalSlots() != null) {
            if (request.getTotalSlots() < schedule.getBookedSlots()) {
                throw new BusinessException(ResultCode.BUSINESS_ERROR,
                        "总号数不能小于已预约数：" + schedule.getBookedSlots());
            }
            schedule.setTotalSlots(request.getTotalSlots());
        }

        if (request.getStaffId() != null) {
            Staff staff = staffMapper.selectById(request.getStaffId());
            if (staff == null || !"医生".equals(staff.getRole())) {
                throw new BusinessException(ResultCode.BUSINESS_ERROR, "医生不存在或角色错误");
            }
            schedule.setStaffId(request.getStaffId());
        }

        if (request.getScheduleDate() != null) schedule.setScheduleDate(request.getScheduleDate());
        if (request.getTimeSlot() != null) schedule.setTimeSlot(request.getTimeSlot());

        scheduleMapper.updateById(schedule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSchedule(Integer scheduleId, Integer operatorId) { // 增加 operatorId
        DoctorSchedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "排班不存在");
        }

        // --- 权限校验 ---
        checkSchedulePermission(operatorId, schedule.getStaffId());
        // ---------------

        if (schedule.getBookedSlots() > 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR,
                    "该排班已有" + schedule.getBookedSlots() + "个预约，不允许删除");
        }

        scheduleMapper.deleteById(scheduleId);
    }

    @Override
    public Page<ScheduleVO> getSchedulePage(ScheduleQueryRequest request) {
        // 构建查询条件
        LambdaQueryWrapper<DoctorSchedule> query = new LambdaQueryWrapper<>();
        
        // 如果指定了科室，先查询该科室下的医生
        if (request.getDeptId() != null) {
            LambdaQueryWrapper<Staff> staffQuery = new LambdaQueryWrapper<>();
            staffQuery.eq(Staff::getDeptId, request.getDeptId());

            List<Staff> doctors = staffMapper.selectList(staffQuery);
            
            if (doctors.isEmpty()) {
                // 该科室没有医生，返回空结果
                return new Page<>(request.getCurrent(), request.getPageSize());
            }
            
            List<Integer> doctorIds = new ArrayList<>();
            for (Staff doctor : doctors) {
                doctorIds.add(doctor.getStaffId());
            }
            query.in(DoctorSchedule::getStaffId, doctorIds);
        }
        
        if (request.getStaffId() != null) {
            query.eq(DoctorSchedule::getStaffId, request.getStaffId());
        }
        
        // 将字符串日期转换为 LocalDate 进行查询
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            LocalDate startDate = LocalDate.parse(request.getStartDate());
            query.ge(DoctorSchedule::getScheduleDate, startDate);
        }
        
        if (request.getEndDate() != null && !request.getEndDate().isEmpty()) {
            LocalDate endDate = LocalDate.parse(request.getEndDate());
            query.le(DoctorSchedule::getScheduleDate, endDate);
        }
        
        if (request.getTimeSlot() != null && !request.getTimeSlot().isEmpty()) {
            query.eq(DoctorSchedule::getTimeSlot, request.getTimeSlot());
        }
        
        query.orderByAsc(DoctorSchedule::getScheduleDate)
                .orderByAsc(DoctorSchedule::getTimeSlot);

        // 分页查询
        Page<DoctorSchedule> page = new Page<>(request.getCurrent(), request.getPageSize());
        page = scheduleMapper.selectPage(page, query);

        // 转换为VO
        Page<ScheduleVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<ScheduleVO> voList = new ArrayList<>();
        
        for (DoctorSchedule schedule : page.getRecords()) {
            ScheduleVO vo = buildScheduleVO(schedule);
            voList.add(vo);
        }
        
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public ScheduleVO getScheduleDetail(Integer scheduleId) {
        DoctorSchedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "排班不存在");
        }
        return buildScheduleVO(schedule);
    }

    /**
     * 构建排班VO对象
     */
    private ScheduleVO buildScheduleVO(DoctorSchedule schedule) {
        ScheduleVO vo = new ScheduleVO();
        BeanUtils.copyProperties(schedule, vo);
        
        // 计算剩余号数
        vo.setAvailableSlots(schedule.getTotalSlots() - schedule.getBookedSlots());
        
        // 查询医生信息
        Staff staff = staffMapper.selectById(schedule.getStaffId());
        if (staff != null) {
            vo.setStaffName(staff.getStaffName());
            vo.setDeptId(staff.getDeptId());
            
            // 查询科室信息
            if (staff.getDeptId() != null) {
                Department dept = departmentMapper.selectById(staff.getDeptId());
                if (dept != null) {
                    vo.setDeptName(dept.getDeptName());
                }
            }
        }
        
        return vo;
    }
}


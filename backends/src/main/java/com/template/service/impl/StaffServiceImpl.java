package com.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.common.ResultCode;
import com.template.dto.StaffCreateRequest;
import com.template.dto.StaffLoginRequest;
import com.template.dto.StaffLoginResponse;
import com.template.dto.StaffQueryRequest;
import com.template.dto.StaffUpdateRequest;
import com.template.entity.Department;
import com.template.entity.Staff;
import com.template.common.BusinessException;
import com.template.mapper.DepartmentMapper;
import com.template.mapper.StaffMapper;
import com.template.service.StaffService;
import com.template.util.JwtUtil;
import com.template.util.PasswordUtil;
import com.template.vo.StaffVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

// ✅ 1. 新增了这两个必须的包
import java.util.HashMap;
import java.util.Map;

/**
 * 职工服务实现类
 *
 * @author template
 */
@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public StaffLoginResponse login(StaffLoginRequest request) {
        // 查询职工账号
        LambdaQueryWrapper<Staff> query = new LambdaQueryWrapper<>();
        query.eq(Staff::getLoginName, request.getLoginName());
        Staff staff = staffMapper.selectOne(query);

        if (staff == null) {
            System.out.println("❌ 登录失败：账号不存在 - " + request.getLoginName());
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "账号或密码错误");
        }

        // 调试日志
        System.out.println("========== 登录调试信息 ==========");
        System.out.println("登录账号: " + request.getLoginName());
        System.out.println("输入密码: " + request.getPassword());
        System.out.println("数据库哈希: " + staff.getPasswordHash());
        System.out.println("哈希长度: " + (staff.getPasswordHash() != null ? staff.getPasswordHash().length() : 0));

        // 验证密码
        boolean matches = PasswordUtil.matches(request.getPassword(), staff.getPasswordHash());
        System.out.println("密码验证结果: " + (matches ? "✓ 成功" : "✗ 失败"));
        System.out.println("================================");

        if (!matches) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "账号或密码错误");
        }

        // ✅ 2. 修复了这里：改成了 Map 传参
        // 原报错代码: String token = JwtUtil.createToken(staff.getStaffId().longValue(), staff.getLoginName());

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", staff.getStaffId());
        claims.put("username", staff.getLoginName());

        String token = JwtUtil.generateToken(staff.getStaffId().longValue(), staff.getLoginName());

        // 构建返回信息
        StaffLoginResponse response = new StaffLoginResponse();
        response.setStaffId(staff.getStaffId());
        response.setStaffName(staff.getStaffName());
        response.setRole(staff.getRole());
        response.setTitle(staff.getTitle());
        response.setToken(token);
        response.setDeptId(staff.getDeptId()); // 设置科室ID

        // === 新增：查询并设置科室名称 ===
        if (staff.getDeptId() != null) {
            Department dept = departmentMapper.selectById(staff.getDeptId());
            if (dept != null) {
                response.setDeptName(dept.getDeptName());

                // 🔥 核心判断逻辑 🔥
                // 如果科室表里的 managerId 等于当前登录人的 staffId，他就是主任
                if (dept.getManagerId() != null && dept.getManagerId().equals(staff.getStaffId())) {
                    response.setIsDeptManager(true);
                } else {
                    response.setIsDeptManager(false);
                }
            }
        }

        return response;
    }

    @Override
    public Integer createStaff(StaffCreateRequest request) {
        // 检查登录账号是否已存在
        LambdaQueryWrapper<Staff> query = new LambdaQueryWrapper<>();
        query.eq(Staff::getLoginName, request.getLoginName());
        if (staffMapper.selectCount(query) > 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "登录账号已存在");
        }

        // 如果指定了科室，检查科室是否存在
        if (request.getDeptId() != null) {
            Department dept = departmentMapper.selectById(request.getDeptId());
            if (dept == null) {
                throw new BusinessException(ResultCode.BUSINESS_ERROR, "科室不存在");
            }
        }

        // 创建职工
        Staff staff = new Staff();
        BeanUtils.copyProperties(request, staff);
        // 加密密码
        staff.setPasswordHash(PasswordUtil.encode(request.getPassword()));

        staffMapper.insert(staff);
        return staff.getStaffId();
    }

    @Override
    public void updateStaff(Integer staffId, StaffUpdateRequest request) {
        Staff staff = staffMapper.selectById(staffId);
        if (staff == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "职工不存在");
        }

        // 如果指定了科室，检查科室是否存在
        if (request.getDeptId() != null) {
            Department dept = departmentMapper.selectById(request.getDeptId());
            if (dept == null) {
                throw new BusinessException(ResultCode.BUSINESS_ERROR, "科室不存在");
            }
        }

        // 更新职工信息
        Staff updateStaff = new Staff();
        updateStaff.setStaffId(staffId);
        BeanUtils.copyProperties(request, updateStaff);

        // 如果要修改密码
        if (StringUtils.hasText(request.getPassword())) {
            updateStaff.setPasswordHash(PasswordUtil.encode(request.getPassword()));
        }

        staffMapper.updateById(updateStaff);
    }

    @Override
    public void deleteStaff(Integer staffId) {
        Staff staff = staffMapper.selectById(staffId);
        if (staff == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "职工不存在");
        }


        staffMapper.deleteById(staffId);
    }

    @Override
    public StaffVO getStaffDetail(Integer staffId) {
        Staff staff = staffMapper.selectById(staffId);
        if (staff == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "职工不存在");
        }

        return convertToVO(staff);
    }

    @Override
    public Page<StaffVO> getStaffPage(StaffQueryRequest request) {
        Page<Staff> page = new Page<>(request.getPage(), request.getPageSize());

        LambdaQueryWrapper<Staff> query = new LambdaQueryWrapper<>();

        // 按姓名模糊查询
        if (StringUtils.hasText(request.getStaffName())) {
            query.like(Staff::getStaffName, request.getStaffName());
        }

        // 按登录账号模糊查询
        if (StringUtils.hasText(request.getLoginName())) {
            query.like(Staff::getLoginName, request.getLoginName());
        }

        // 按科室筛选
        if (request.getDeptId() != null) {
            query.eq(Staff::getDeptId, request.getDeptId());
        }

        // 按角色筛选
        if (StringUtils.hasText(request.getRole())) {
            query.eq(Staff::getRole, request.getRole());
        }

        Page<Staff> staffPage = staffMapper.selectPage(page, query);

        // 转换为VO
        Page<StaffVO> voPage = new Page<>(staffPage.getCurrent(), staffPage.getSize(), staffPage.getTotal());
        voPage.setRecords(
                staffPage.getRecords().stream()
                        .map(this::convertToVO)
                        .collect(java.util.stream.Collectors.toList())
        );

        return voPage;
    }

    /**
     * 转换为VO
     */
    private StaffVO convertToVO(Staff staff) {
        StaffVO vo = new StaffVO();
        BeanUtils.copyProperties(staff, vo);

        // 获取科室名称
        if (staff.getDeptId() != null) {
            Department dept = departmentMapper.selectById(staff.getDeptId());
            if (dept != null) {
                vo.setDeptName(dept.getDeptName());
            }
        }

        return vo;
    }
}
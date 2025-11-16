package com.template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.dto.StaffCreateRequest;
import com.template.dto.StaffLoginRequest;
import com.template.dto.StaffLoginResponse;
import com.template.dto.StaffQueryRequest;
import com.template.dto.StaffUpdateRequest;
import com.template.vo.StaffVO;

/**
 * 职工服务接口
 *
 * @author template
 */
public interface StaffService {

    /**
     * 职工登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    StaffLoginResponse login(StaffLoginRequest request);

    /**
     * 创建职工
     *
     * @param request 创建请求
     * @return 职工ID
     */
    Integer createStaff(StaffCreateRequest request);

    /**
     * 更新职工
     *
     * @param staffId 职工ID
     * @param request 更新请求
     */
    void updateStaff(Integer staffId, StaffUpdateRequest request);

    /**
     * 删除职工
     *
     * @param staffId 职工ID
     */
    void deleteStaff(Integer staffId);

    /**
     * 获取职工详情
     *
     * @param staffId 职工ID
     * @return 职工信息
     */
    StaffVO getStaffDetail(Integer staffId);

    /**
     * 分页查询职工列表
     *
     * @param request 查询请求
     * @return 职工列表
     */
    Page<StaffVO> getStaffPage(StaffQueryRequest request);
}


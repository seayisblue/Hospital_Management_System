package com.template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.dto.ScheduleCreateRequest;
import com.template.dto.ScheduleQueryRequest;
import com.template.dto.ScheduleUpdateRequest;
import com.template.vo.ScheduleVO;

/**
 * 排班服务接口
 *
 * @author template
 */
public interface ScheduleService {

    /**
     * 创建排班
     *
     * @param request 创建请求
     * @return 排班ID
     */
    Integer createSchedule(ScheduleCreateRequest request);

    /**
     * 更新排班
     *
     * @param request 更新请求
     */
    void updateSchedule(ScheduleUpdateRequest request);

    /**
     * 删除排班
     *
     * @param scheduleId 排班ID
     */
    void deleteSchedule(Integer scheduleId);

    /**
     * 分页查询排班
     *
     * @param request 查询请求
     * @return 排班分页数据
     */
    Page<ScheduleVO> getSchedulePage(ScheduleQueryRequest request);

    /**
     * 获取排班详情
     *
     * @param scheduleId 排班ID
     * @return 排班详情
     */
    ScheduleVO getScheduleDetail(Integer scheduleId);
}


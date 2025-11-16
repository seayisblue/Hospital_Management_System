package com.template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.template.dto.AppointmentQueryRequest;
import com.template.dto.OnSiteAppointmentRequest;
import com.template.vo.AppointmentDetailVO;

/**
 * 管理员挂号服务接口
 *
 * @author template
 */
public interface AdminAppointmentService {

    /**
     * 现场挂号
     */
    Integer createOnSiteAppointment(OnSiteAppointmentRequest request);

    /**
     * 分页查询挂号记录
     */
    Page<AppointmentDetailVO> getAppointmentPage(AppointmentQueryRequest request);

    /**
     * 取消挂号（管理员操作）
     */
    void cancelAppointment(Integer appointmentId);

    /**
     * 获取挂号详情
     */
    AppointmentDetailVO getAppointmentDetail(Integer appointmentId);
}


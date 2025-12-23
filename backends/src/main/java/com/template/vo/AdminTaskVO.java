package com.template.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员待处理任务视图对象
 */
@Data
public class AdminTaskVO {

    /**
     * 类型标签
     */
    private String type;

    /**
     * 描述
     */
    private String description;

    /**
     * 提交/更新时间
     */
    private LocalDateTime submitTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 前端跳转链接
     */
    private String actionUrl;
}

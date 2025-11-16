package com.template.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充配置
 * 用于自动填充创建时间、更新时间等字段
 *
 * 使用方法：在实体类字段上添加注解
 * @TableField(fill = FieldFill.INSERT)
 * private LocalDateTime createTime;
 *
 * @TableField(fill = FieldFill.INSERT_UPDATE)
 * private LocalDateTime updateTime;
 *
 * @author template
 */
@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        // 自动填充更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        // 自动填充创建人（如果需要，可以从上下文获取当前用户）
        // this.strictInsertFill(metaObject, "createBy", String.class, getCurrentUser());
        // 自动填充逻辑删除标志
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        // 自动填充更新人（如果需要，可以从上下文获取当前用户）
        // this.strictUpdateFill(metaObject, "updateBy", String.class, getCurrentUser());
    }

    /**
     * 获取当前用户（示例）
     * 实际项目中需要从 SecurityContext 或 ThreadLocal 中获取
     */
    // private String getCurrentUser() {
    //     // 从 Spring Security 获取
    //     // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     // return authentication.getName();
    //     return "system";
    // }
}


package com.template.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建处方请求
 *
 * @author template
 */
@Data
public class PrescriptionCreateRequest {

    /**
     * 病历ID
     */
    @NotNull(message = "病历ID不能为空")
    private Integer recordId;

    /**
     * 处方明细列表
     */
    @NotEmpty(message = "处方明细不能为空")
    private List<PrescriptionItemRequest> items;

    @Data
    public static class PrescriptionItemRequest {
        /**
         * 药品ID
         */
        @NotNull(message = "药品ID不能为空")
        private Integer medicineId;

        /**
         * 数量
         */
        @NotNull(message = "数量不能为空")
        private Integer quantity;

        /**
         * 用法
         */
        private String usage;

        /**
         * 频次
         */
        private String frequency;

        /**
         * 天数
         */
        private Integer days;
    }
}


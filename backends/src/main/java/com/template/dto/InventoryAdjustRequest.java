package com.template.dto;

import lombok.Data; // 如果你用Lombok，保留这个
import javax.validation.constraints.NotNull;

// 确保加上 @Data 注解
@Data
public class InventoryAdjustRequest {
    @NotNull(message = "药品ID不能为空")
    private Integer medicineId;

    @NotNull(message = "调整数量不能为空")
    private Integer quantity;

    private String reason;

    // ====================================================
    // 如果你的 Lombok 不生效（报错找不到符号），请手动加上下面的方法：
    // ====================================================

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
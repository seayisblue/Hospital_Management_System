package com.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.template.entity.Medicine;
import org.apache.ibatis.annotations.Mapper;

/**
 * 药品Mapper
 *
 * @author template
 */
@Mapper
public interface MedicineMapper extends BaseMapper<Medicine> {
}


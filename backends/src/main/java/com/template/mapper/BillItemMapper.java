package com.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.template.entity.BillItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收费明细Mapper
 *
 * @author template
 */
@Mapper
public interface BillItemMapper extends BaseMapper<BillItem> {
}


package com.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.template.entity.Bill;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收费单Mapper
 *
 * @author template
 */
@Mapper
public interface BillMapper extends BaseMapper<Bill> {
}


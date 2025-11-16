package com.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.template.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 科室Mapper接口
 *
 * @author template
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}


package com.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.template.entity.Patient;
import org.apache.ibatis.annotations.Mapper;

/**
 * 患者Mapper接口
 *
 * @author template
 */
@Mapper
public interface PatientMapper extends BaseMapper<Patient> {
}


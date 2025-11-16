package com.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.template.entity.Prescription;
import com.template.vo.PrescriptionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 处方Mapper
 *
 * @author template
 */
@Mapper
public interface PrescriptionMapper extends BaseMapper<Prescription> {

    /**
     * 查询患者的处方列表
     *
     * @param patientId 患者ID
     * @return 处方列表
     */
    @Select("SELECT p.PrescriptionID as prescriptionId, p.RecordID as recordId, " +
            "p.PatientID as patientId, pt.PatientName as patientName, " +
            "p.StaffID as staffId, s.StaffName as staffName, " +
            "p.PrescriptionDate as prescriptionDate, p.Status as status " +
            "FROM T_Prescription p " +
            "LEFT JOIN T_Patient pt ON p.PatientID = pt.PatientID " +
            "LEFT JOIN T_Staff s ON p.StaffID = s.StaffID " +
            "WHERE p.PatientID = #{patientId} " +
            "ORDER BY p.PrescriptionDate DESC")
    List<PrescriptionVO> selectByPatientId(@Param("patientId") Integer patientId);
}

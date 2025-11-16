package com.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.template.entity.MedicalRecord;
import com.template.vo.MedicalRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 病历Mapper接口
 *
 * @author template
 */
@Mapper
public interface MedicalRecordMapper extends BaseMapper<MedicalRecord> {

    /**
     * 查询患者的病历列表
     *
     * @param patientId 患者ID
     * @return 病历列表
     */
    @Select("SELECT mr.RecordID as recordId, mr.AppointmentID as appointmentId, " +
            "mr.PatientID as patientId, p.PatientName as patientName, " +
            "mr.StaffID as staffId, s.StaffName as staffName, " +
            "d.DeptName as deptName, " +
            "mr.Subjective as subjective, mr.Objective as objective, " +
            "mr.Assessment as assessment, mr.Plan as plan, " +
            "mr.CreateTime as createTime " +
            "FROM T_Medical_Record mr " +
            "LEFT JOIN T_Patient p ON mr.PatientID = p.PatientID " +
            "LEFT JOIN T_Staff s ON mr.StaffID = s.StaffID " +
            "LEFT JOIN T_Department d ON s.DeptID = d.DeptID " +
            "WHERE mr.PatientID = #{patientId} " +
            "ORDER BY mr.CreateTime DESC")
    List<MedicalRecordVO> selectByPatientId(@Param("patientId") Integer patientId);
}


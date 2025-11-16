package com.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.template.entity.Appointment;
import com.template.vo.AppointmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 挂号Mapper接口
 *
 * @author template
 */
@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {

    /**
     * 查询患者的挂号列表
     *
     * @param patientId 患者ID
     * @return 挂号列表
     */
    @Select("SELECT a.AppointmentID as appointmentId, a.ScheduleID as scheduleId, " +
            "a.PatientID as patientId, p.PatientName as patientName, p.PhoneNumber as patientPhone, " +
            "a.StaffID as staffId, s.StaffName as staffName, " +
            "a.DeptID as deptId, d.DeptName as deptName, " +
            "ds.ScheduleDate as scheduleDate, ds.TimeSlot as timeSlot, " +
            "a.AppointmentTime as appointmentTime, a.AppointmentTime as createTime, " +
            "a.Status as status, a.Fee as fee " +
            "FROM t_appointment a " +
            "LEFT JOIN t_patient p ON a.PatientID = p.PatientID " +
            "LEFT JOIN t_staff s ON a.StaffID = s.StaffID " +
            "LEFT JOIN t_department d ON a.DeptID = d.DeptID " +
            "LEFT JOIN t_doctor_schedule ds ON a.ScheduleID = ds.ScheduleID " +
            "WHERE a.PatientID = #{patientId} " +
            "ORDER BY a.AppointmentTime DESC")
    List<AppointmentVO> selectByPatientId(@Param("patientId") Integer patientId);

    /**
     * 查询挂号详情
     *
     * @param appointmentId 挂号ID
     * @return 挂号详情
     */
    @Select("SELECT a.AppointmentID as appointmentId, a.ScheduleID as scheduleId, " +
            "a.PatientID as patientId, p.PatientName as patientName, p.PhoneNumber as patientPhone, " +
            "a.StaffID as staffId, s.StaffName as staffName, " +
            "a.DeptID as deptId, d.DeptName as deptName, " +
            "ds.ScheduleDate as scheduleDate, ds.TimeSlot as timeSlot, " +
            "a.AppointmentTime as appointmentTime, a.AppointmentTime as createTime, " +
            "a.Status as status, a.Fee as fee " +
            "FROM t_appointment a " +
            "LEFT JOIN t_patient p ON a.PatientID = p.PatientID " +
            "LEFT JOIN t_staff s ON a.StaffID = s.StaffID " +
            "LEFT JOIN t_department d ON a.DeptID = d.DeptID " +
            "LEFT JOIN t_doctor_schedule ds ON a.ScheduleID = ds.ScheduleID " +
            "WHERE a.AppointmentID = #{appointmentId}")
    AppointmentVO selectDetailById(@Param("appointmentId") Integer appointmentId);
}


package com.template.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.template.entity.DoctorSchedule;
import com.template.vo.DoctorScheduleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 医生排班Mapper接口
 *
 * @author template
 */
@Mapper
public interface DoctorScheduleMapper extends BaseMapper<DoctorSchedule> {

    /**
     * 查询可预约的排班列表
     *
     * @param deptId       科室ID
     * @param staffId      医生ID
     * @param scheduleDate 排班日期
     * @param timeSlot     时段
     * @return 排班列表
     */
    @Select("<script>" +
            "SELECT ds.ScheduleID as scheduleId, ds.StaffID as staffId, s.StaffName as staffName, " +
            "s.DeptID as deptId, d.DeptName as deptName, s.Title as title, " +
            "ds.ScheduleDate as scheduleDate, ds.TimeSlot as timeSlot, " +
            "ds.TotalSlots as totalSlots, ds.BookedSlots as bookedSlots, " +
            "(ds.TotalSlots - ds.BookedSlots) as availableSlots " +
            "FROM T_Doctor_Schedule ds " +
            "LEFT JOIN T_Staff s ON ds.StaffID = s.StaffID " +
            "LEFT JOIN T_Department d ON s.DeptID = d.DeptID " +
            "WHERE 1=1 " +
            "<if test='deptId != null'> AND s.DeptID = #{deptId} </if>" +
            "<if test='staffId != null'> AND ds.StaffID = #{staffId} </if>" +
            "<if test='scheduleDate != null'> AND ds.ScheduleDate = #{scheduleDate} </if>" +
            "<if test='timeSlot != null and timeSlot != \"\"'> AND ds.TimeSlot = #{timeSlot} </if>" +
            "AND ds.TotalSlots > ds.BookedSlots " +
            "ORDER BY ds.ScheduleDate, ds.TimeSlot" +
            "</script>")
    List<DoctorScheduleVO> selectAvailableSchedules(@Param("deptId") Integer deptId,
                                                     @Param("staffId") Integer staffId,
                                                     @Param("scheduleDate") LocalDate scheduleDate,
                                                     @Param("timeSlot") String timeSlot);
}


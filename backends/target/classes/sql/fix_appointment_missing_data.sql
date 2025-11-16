-- 修复挂号记录缺失的StaffID和DeptID

-- 方法1: 通过ScheduleID关联更新
UPDATE T_Appointment a
INNER JOIN T_Doctor_Schedule ds ON a.ScheduleID = ds.ScheduleID
INNER JOIN T_Staff s ON ds.StaffID = s.StaffID
SET 
    a.StaffID = ds.StaffID,
    a.DeptID = s.DeptID
WHERE a.StaffID IS NULL OR a.DeptID IS NULL;

-- 检查修复结果
SELECT 
    a.AppointmentID AS '挂号ID',
    p.PatientName AS '患者',
    p.PhoneNumber AS '电话',
    s.StaffName AS '医生',
    d.DeptName AS '科室',
    ds.ScheduleDate AS '就诊日期',
    ds.TimeSlot AS '时段',
    a.Status AS '状态'
FROM T_Appointment a
LEFT JOIN T_Patient p ON a.PatientID = p.PatientID
LEFT JOIN T_Staff s ON a.StaffID = s.StaffID
LEFT JOIN T_Department d ON a.DeptID = d.DeptID
LEFT JOIN T_Doctor_Schedule ds ON a.ScheduleID = ds.ScheduleID
ORDER BY a.AppointmentID DESC
LIMIT 20;


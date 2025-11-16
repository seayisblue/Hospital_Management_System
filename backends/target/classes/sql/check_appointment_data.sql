-- 检查挂号数据完整性

-- 1. 查看挂号记录的基本信息
SELECT '挂号记录基本信息:' AS '';
SELECT 
    AppointmentID AS '挂号ID',
    PatientID AS '患者ID',
    ScheduleID AS '排班ID',
    StaffID AS '医生ID',
    DeptID AS '科室ID',
    Status AS '状态',
    AppointmentTime AS '挂号时间'
FROM T_Appointment 
ORDER BY AppointmentID DESC 
LIMIT 10;

-- 2. 检查有多少记录缺少关键字段
SELECT '数据完整性统计:' AS '';
SELECT 
    COUNT(*) AS '总记录数',
    SUM(CASE WHEN ScheduleID IS NULL THEN 1 ELSE 0 END) AS '缺少ScheduleID',
    SUM(CASE WHEN StaffID IS NULL THEN 1 ELSE 0 END) AS '缺少StaffID',
    SUM(CASE WHEN DeptID IS NULL THEN 1 ELSE 0 END) AS '缺少DeptID'
FROM T_Appointment;

-- 3. 检查关联查询结果
SELECT '关联查询结果:' AS '';
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
LIMIT 10;

-- 4. 检查排班数据
SELECT '排班数据:' AS '';
SELECT 
    ds.ScheduleID,
    ds.StaffID,
    s.StaffName AS '医生',
    d.DeptName AS '科室',
    ds.ScheduleDate AS '日期',
    ds.TimeSlot AS '时段',
    ds.TotalSlots AS '总数',
    ds.BookedSlots AS '已约'
FROM T_Doctor_Schedule ds
LEFT JOIN T_Staff s ON ds.StaffID = s.StaffID
LEFT JOIN T_Department d ON s.DeptID = d.DeptID
WHERE ds.ScheduleDate >= CURDATE()
LIMIT 10;


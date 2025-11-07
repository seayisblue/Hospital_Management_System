-- 根据实际数据库结构生成测试数据（用于药房工作站测试）
-- 基于 hospital_system.sql 结构

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ====================================
-- 1. 科室数据
-- ====================================
INSERT INTO t_department (DeptID, DeptName, Description)
VALUES 
(1, '内科', '内科诊疗'),
(2, '外科', '外科诊疗'),
(3, '儿科', '儿科诊疗')
ON DUPLICATE KEY UPDATE DeptName = VALUES(DeptName);

-- ====================================
-- 2. 职工数据（医生、药剂师、管理员）
-- ====================================
INSERT INTO t_staff (StaffID, StaffName, DeptID, Role, Title, LoginName, PasswordHash)
VALUES 
-- 管理员
(1, '管理员', NULL, '管理员', NULL, 'admin', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.'),
-- 医生
(2, '李医生', 1, '医生', '主治医师', 'doctor1', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.'),
(3, '张医生', 2, '医生', '主任医师', 'doctor2', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.'),
-- 药剂师
(4, '王药师', NULL, '药剂师', '主管药师', 'pharmacist1', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.')
ON DUPLICATE KEY UPDATE StaffName = VALUES(StaffName);

-- ====================================
-- 3. 患者数据
-- ====================================
INSERT INTO t_patient (PatientID, PatientName, Gender, DateOfBirth, IDCardNumber, PhoneNumber, PasswordHash, Address, CreateTime)
VALUES 
(1, '张三', '男', '1990-01-01', '110101199001011234', '13800138001', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.', '北京市朝阳区', NOW()),
(2, '李四', '女', '1985-05-15', '110101198505151234', '13800138002', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.', '北京市海淀区', NOW()),
(3, '王五', '男', '1995-08-20', '110101199508201234', '13800138003', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.', '北京市西城区', NOW()),
(4, '赵六', '女', '1988-12-10', '110101198812101234', '13800138004', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.', '北京市东城区', NOW())
ON DUPLICATE KEY UPDATE PatientName = VALUES(PatientName);

-- ====================================
-- 4. 药品数据
-- ====================================
INSERT INTO t_medicine (MedicineID, MedicineName, Specification, Manufacturer, UnitPrice, StockLevel)
VALUES 
(1, '感冒灵颗粒', '10g*10袋/盒', '某某制药有限公司', 15.50, 500),
(2, '阿莫西林胶囊', '0.25g*24粒/盒', '某某制药有限公司', 12.00, 800),
(3, '布洛芬缓释胶囊', '0.3g*20粒/盒', '某某制药有限公司', 18.00, 300),
(4, '降压片', '5mg*30片/盒', '某某制药有限公司', 25.00, 600),
(5, '奥美拉唑肠溶胶囊', '20mg*14粒/盒', '某某制药有限公司', 22.50, 400),
(6, '阿司匹林肠溶片', '100mg*30片/盒', '某某制药有限公司', 8.50, 1000),
(7, '氨溴索口服液', '30ml*6支/盒', '某某制药有限公司', 28.00, 200),
(8, '维生素C片', '100mg*100片/瓶', '某某制药有限公司', 6.50, 1500)
ON DUPLICATE KEY UPDATE MedicineName = VALUES(MedicineName);

-- ====================================
-- 5. 医生排班数据
-- ====================================
INSERT INTO t_doctor_schedule (ScheduleID, StaffID, ScheduleDate, TimeSlot, TotalSlots, BookedSlots)
VALUES 
-- 今天的排班
(100, 2, CURDATE(), '上午', 30, 3),
(101, 2, CURDATE(), '下午', 30, 2),
(102, 3, CURDATE(), '上午', 25, 1),
-- 明天的排班
(103, 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '上午', 30, 0),
(104, 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '下午', 25, 0)
ON DUPLICATE KEY UPDATE BookedSlots = VALUES(BookedSlots);

-- ====================================
-- 6. 挂号数据
-- ====================================
INSERT INTO t_appointment (AppointmentID, ScheduleID, PatientID, StaffID, DeptID, AppointmentTime, Status, Fee)
VALUES 
(100, 100, 1, 2, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR), '已就诊', 10.00),
(101, 100, 2, 2, 1, DATE_SUB(NOW(), INTERVAL 1 HOUR), '已就诊', 10.00),
(102, 101, 3, 2, 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE), '已就诊', 10.00)
ON DUPLICATE KEY UPDATE Status = VALUES(Status);

-- ====================================
-- 7. 病历数据（SOAP格式）
-- ====================================
INSERT INTO t_medical_record (RecordID, AppointmentID, PatientID, StaffID, Subjective, Objective, Assessment, Plan, CreateTime)
VALUES 
(100, 100, 1, 2, 
    '患者主诉：发热3天，伴有咳嗽、流涕，体温最高39℃',
    '查体：体温38.5℃，咽部充血，双肺呼吸音清，心律齐',
    '急性上呼吸道感染（感冒）',
    '1. 多休息，多饮水\n2. 口服抗感冒药物\n3. 如症状加重请及时复诊',
    DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(101, 101, 2, 2,
    '患者主诉：头晕、头痛1周，血压控制不佳',
    '查体：血压160/95mmHg，心率80次/分，心律齐',
    '高血压2级',
    '1. 规律服用降压药\n2. 低盐饮食\n3. 定期监测血压\n4. 两周后复诊',
    DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(102, 102, 3, 2,
    '患者主诉：胃痛、胃胀2天，进食后加重',
    '查体：上腹部轻压痛，无反跳痛，肠鸣音正常',
    '慢性胃炎',
    '1. 规律饮食，避免辛辣刺激\n2. 口服胃黏膜保护剂\n3. 症状持续请胃镜检查',
    DATE_SUB(NOW(), INTERVAL 30 MINUTE))
ON DUPLICATE KEY UPDATE Assessment = VALUES(Assessment);

-- ====================================
-- 8. 处方数据（未发药状态）
-- ====================================
INSERT INTO t_prescription (PrescriptionID, RecordID, StaffID, PatientID, PrescriptionDate, Status)
VALUES 
(100, 100, 2, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR), '未发药'),
(101, 101, 2, 2, DATE_SUB(NOW(), INTERVAL 1 HOUR), '未发药'),
(102, 102, 2, 3, DATE_SUB(NOW(), INTERVAL 30 MINUTE), '未发药')
ON DUPLICATE KEY UPDATE Status = '未发药';

-- ====================================
-- 9. 处方明细数据
-- ====================================
DELETE FROM t_prescription_item WHERE PrescriptionID IN (100, 101, 102);
INSERT INTO t_prescription_item (ItemID, PrescriptionID, MedicineID, Quantity, UnitPrice, Subtotal)
VALUES 
-- 处方100：感冒药（张三）
(1000, 100, 1, 2, 15.50, 31.00),  -- 感冒灵颗粒 x2盒
(1001, 100, 2, 1, 12.00, 12.00),  -- 阿莫西林胶囊 x1盒
(1002, 100, 3, 1, 18.00, 18.00),  -- 布洛芬缓释胶囊 x1盒
(1003, 100, 8, 1, 6.50, 6.50),    -- 维生素C片 x1瓶
-- 处方101：降压药（李四）
(1004, 101, 4, 2, 25.00, 50.00),  -- 降压片 x2盒
(1005, 101, 6, 1, 8.50, 8.50),    -- 阿司匹林肠溶片 x1盒
-- 处方102：胃药（王五）
(1006, 102, 5, 2, 22.50, 45.00);  -- 奥美拉唑肠溶胶囊 x2盒

-- ====================================
-- 10. 收费单数据
-- ====================================
INSERT INTO t_bill (BillID, PatientID, TotalAmount, Status, CreateTime, PayTime)
VALUES 
(100, 1, 77.50, '未支付', DATE_SUB(NOW(), INTERVAL 2 HOUR), NULL),
(101, 2, 68.50, '未支付', DATE_SUB(NOW(), INTERVAL 1 HOUR), NULL),
(102, 3, 55.00, '未支付', DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL)
ON DUPLICATE KEY UPDATE Status = '未支付';

-- ====================================
-- 11. 收费明细数据
-- ====================================
DELETE FROM t_bill_items WHERE BillID IN (100, 101, 102);
INSERT INTO t_bill_items (ItemID, BillID, ItemType, ItemName, Amount, ReferenceID)
VALUES 
-- 收费单100（张三）
(1000, 100, '挂号', '内科挂号费', 10.00, 100),
(1001, 100, '药品', '感冒灵颗粒 x2盒 + 阿莫西林胶囊 x1盒 + 布洛芬缓释胶囊 x1盒 + 维生素C片 x1瓶', 67.50, 100),
-- 收费单101（李四）
(1002, 101, '挂号', '内科挂号费', 10.00, 101),
(1003, 101, '药品', '降压片 x2盒 + 阿司匹林肠溶片 x1盒', 58.50, 101),
-- 收费单102（王五）
(1004, 102, '挂号', '内科挂号费', 10.00, 102),
(1005, 102, '药品', '奥美拉唑肠溶胶囊 x2盒', 45.00, 102);

SET FOREIGN_KEY_CHECKS = 1;

-- ====================================
-- 数据验证查询
-- ====================================
SELECT '✅ 测试数据创建成功！' AS '状态';

SELECT '========== 待发药处方列表 ==========' AS '';
SELECT 
    p.PrescriptionID AS '处方ID',
    pat.PatientName AS '患者',
    pat.Gender AS '性别',
    TIMESTAMPDIFF(YEAR, pat.DateOfBirth, CURDATE()) AS '年龄',
    s.StaffName AS '医生',
    d.DeptName AS '科室',
    p.PrescriptionDate AS '开具时间',
    p.Status AS '状态',
    COUNT(pi.ItemID) AS '药品数',
    MAX(b.TotalAmount) AS '费用'
FROM t_prescription p
LEFT JOIN t_patient pat ON p.PatientID = pat.PatientID
LEFT JOIN t_staff s ON p.StaffID = s.StaffID
LEFT JOIN t_medical_record mr ON p.RecordID = mr.RecordID
LEFT JOIN t_appointment apt ON mr.AppointmentID = apt.AppointmentID
LEFT JOIN t_department d ON apt.DeptID = d.DeptID
LEFT JOIN t_prescription_item pi ON p.PrescriptionID = pi.PrescriptionID
LEFT JOIN t_bill_items bi ON bi.ReferenceID = p.PrescriptionID AND bi.ItemType = '药品'
LEFT JOIN t_bill b ON bi.BillID = b.BillID
WHERE p.Status = '未发药'
GROUP BY p.PrescriptionID, pat.PatientName, pat.Gender, pat.DateOfBirth, s.StaffName, d.DeptName, p.PrescriptionDate, p.Status
ORDER BY p.PrescriptionDate DESC;

SELECT '========== 处方明细（处方100） ==========' AS '';
SELECT 
    pi.ItemID AS '明细ID',
    m.MedicineName AS '药品名称',
    m.Specification AS '规格',
    pi.Quantity AS '数量',
    pi.UnitPrice AS '单价',
    pi.Subtotal AS '小计'
FROM t_prescription_item pi
LEFT JOIN t_medicine m ON pi.MedicineID = m.MedicineID
WHERE pi.PrescriptionID = 100;

SELECT '========== 账号信息 ==========' AS '';
SELECT 
    StaffID AS 'ID',
    StaffName AS '姓名',
    Role AS '角色',
    LoginName AS '登录账号',
    '123456' AS '密码'
FROM t_staff
WHERE StaffID IN (1, 2, 4)
ORDER BY StaffID;


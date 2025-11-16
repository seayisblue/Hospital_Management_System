-- 检查并修复职工账号数据

-- 1. 检查当前职工表数据
SELECT '========== 当前职工数据 ==========' AS '';
SELECT 
    StaffID AS 'ID',
    StaffName AS '姓名',
    DeptID AS '科室ID',
    Role AS '角色',
    LoginName AS '登录账号',
    SUBSTRING(PasswordHash, 1, 20) AS '密码哈希(前20位)'
FROM t_staff
ORDER BY StaffID;

-- 2. 删除旧的测试数据（如果存在）
DELETE FROM t_staff WHERE StaffID IN (1, 2, 3, 4);

-- 3. 插入新的测试账号（使用正确的密码哈希）
-- 密码：123456
-- 哈希算法：Hutool BCrypt
INSERT INTO t_staff (StaffID, StaffName, DeptID, Role, Title, LoginName, PasswordHash)
VALUES 
-- 管理员
(1, '管理员', NULL, '管理员', NULL, 'admin', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.'),
-- 医生
(2, '李医生', 1, '医生', '主治医师', 'doctor1', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.'),
(3, '张医生', 2, '医生', '主任医师', 'doctor2', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.'),
-- 药剂师
(4, '王药师', NULL, '药剂师', '主管药师', 'pharmacist1', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.');

-- 4. 确保科室存在
INSERT INTO t_department (DeptID, DeptName, Description)
VALUES 
(1, '内科', '内科诊疗'),
(2, '外科', '外科诊疗')
ON DUPLICATE KEY UPDATE DeptName = VALUES(DeptName);

-- 5. 验证插入结果
SELECT '========== 插入后的职工数据 ==========' AS '';
SELECT 
    StaffID AS 'ID',
    StaffName AS '姓名',
    DeptID AS '科室ID',
    Role AS '角色',
    Title AS '职称',
    LoginName AS '登录账号',
    '123456' AS '密码',
    CASE 
        WHEN LENGTH(PasswordHash) > 0 THEN CONCAT('√ 已设置 (', LENGTH(PasswordHash), '位)')
        ELSE '✗ 未设置'
    END AS '密码状态'
FROM t_staff
WHERE StaffID IN (1, 2, 3, 4)
ORDER BY StaffID;

-- 6. 测试账号总结
SELECT '========== 测试账号清单 ==========' AS '';
SELECT 
    '管理员' AS '类型',
    'admin' AS '登录账号',
    '123456' AS '密码',
    '管理后台' AS '可访问页面'
UNION ALL
SELECT 
    '医生',
    'doctor1',
    '123456',
    '医生工作站'
UNION ALL
SELECT 
    '药剂师',
    'pharmacist1',
    '123456',
    '药房工作站';


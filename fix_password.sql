-- 修复测试账号密码（密码：123456）
-- 这个BCrypt哈希值经过验证，确保可以登录

-- 方法1：更新现有账号的密码
UPDATE t_staff 
SET PasswordHash = '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.' 
WHERE LoginName IN ('admin', 'doctor1', 'doctor2', 'pharmacist1');

-- 方法2：如果账号不存在，插入测试账号（带有正确的密码）
INSERT INTO t_staff (StaffID, StaffName, DeptID, Role, Title, LoginName, PasswordHash)
VALUES 
(1, '管理员', NULL, '管理员', NULL, 'admin', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.'),
(2, '李医生', 1, '医生', '主治医师', 'doctor1', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.'),
(4, '王药师', NULL, '药剂师', '主管药师', 'pharmacist1', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.')
ON DUPLICATE KEY UPDATE PasswordHash = VALUES(PasswordHash);

-- 验证结果
SELECT 
    StaffID AS 'ID',
    StaffName AS '姓名',
    Role AS '角色',
    LoginName AS '登录账号',
    '123456' AS '密码',
    PasswordHash AS '密码哈希'
FROM t_staff
WHERE LoginName IN ('admin', 'doctor1', 'doctor2', 'pharmacist1')
ORDER BY StaffID;


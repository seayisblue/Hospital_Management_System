/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : hospital_system

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 07/11/2025 13:21:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_appointment
-- ----------------------------
DROP TABLE IF EXISTS `t_appointment`;
CREATE TABLE `t_appointment`  (
  `AppointmentID` int(11) NOT NULL AUTO_INCREMENT COMMENT '挂号ID',
  `ScheduleID` int(11) NULL DEFAULT NULL COMMENT '关联排班ID（关联 T_Doctor_Schedule）',
  `PatientID` int(11) NOT NULL COMMENT '患者ID',
  `StaffID` int(11) NOT NULL COMMENT '挂号的医生ID(可冗余,也可通过ScheduleID查询)',
  `DeptID` int(11) NOT NULL COMMENT '挂号的科室ID(可冗余,也可通过ScheduleID查询)',
  `AppointmentTime` datetime NOT NULL COMMENT '挂号时间',
  `Status` enum('待就诊','已就诊','已取消') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '待就诊' COMMENT '状态(待就诊,已就诊,已取消)',
  `Fee` decimal(10, 2) NOT NULL COMMENT '挂号费',
  PRIMARY KEY (`AppointmentID`) USING BTREE,
  INDEX `idx_patient_id`(`PatientID`) USING BTREE,
  INDEX `idx_staff_id`(`StaffID`) USING BTREE,
  INDEX `idx_dept_id`(`DeptID`) USING BTREE,
  INDEX `idx_appointment_time`(`AppointmentTime`) USING BTREE,
  INDEX `idx_status`(`Status`) USING BTREE,
  INDEX `FK_Appointment_Schedule`(`ScheduleID`) USING BTREE,
  CONSTRAINT `FK_Appointment_Schedule` FOREIGN KEY (`ScheduleID`) REFERENCES `t_doctor_schedule` (`ScheduleID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_appointment_dept` FOREIGN KEY (`DeptID`) REFERENCES `t_department` (`DeptID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_appointment_patient` FOREIGN KEY (`PatientID`) REFERENCES `t_patient` (`PatientID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_appointment_staff` FOREIGN KEY (`StaffID`) REFERENCES `t_staff` (`StaffID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '挂号表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_appointment
-- ----------------------------
INSERT INTO `t_appointment` VALUES (100, 100, 1, 2, 1, '2025-11-07 10:40:38', '已就诊', 10.00);
INSERT INTO `t_appointment` VALUES (101, 100, 2, 2, 1, '2025-11-07 11:40:38', '已就诊', 10.00);
INSERT INTO `t_appointment` VALUES (102, 101, 3, 2, 1, '2025-11-07 12:10:38', '已就诊', 10.00);
INSERT INTO `t_appointment` VALUES (103, 102, 1, 3, 2, '2025-11-07 13:17:35', '待就诊', 15.00);

-- ----------------------------
-- Table structure for t_bill
-- ----------------------------
DROP TABLE IF EXISTS `t_bill`;
CREATE TABLE `t_bill`  (
  `BillID` int(11) NOT NULL AUTO_INCREMENT COMMENT '收费单ID',
  `PatientID` int(11) NOT NULL COMMENT '患者ID',
  `TotalAmount` decimal(10, 2) NOT NULL COMMENT '总金额',
  `Status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '未支付' COMMENT '状态(未支付,已支付,已退款)',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `PayTime` datetime NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`BillID`) USING BTREE,
  INDEX `idx_patient_id`(`PatientID`) USING BTREE,
  INDEX `idx_status`(`Status`) USING BTREE,
  CONSTRAINT `fk_bill_patient` FOREIGN KEY (`PatientID`) REFERENCES `t_patient` (`PatientID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收费单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_bill
-- ----------------------------
INSERT INTO `t_bill` VALUES (100, 1, 77.50, '未支付', '2025-11-07 10:40:38', NULL);
INSERT INTO `t_bill` VALUES (101, 2, 68.50, '未支付', '2025-11-07 11:40:38', NULL);
INSERT INTO `t_bill` VALUES (102, 3, 55.00, '未支付', '2025-11-07 12:10:38', NULL);

-- ----------------------------
-- Table structure for t_bill_items
-- ----------------------------
DROP TABLE IF EXISTS `t_bill_items`;
CREATE TABLE `t_bill_items`  (
  `ItemID` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `BillID` int(11) NOT NULL COMMENT '关联的收费单ID',
  `ItemType` enum('挂号','药品') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '费用类型(挂号,药品)',
  `ItemName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目名称',
  `Amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `ReferenceID` int(11) NULL DEFAULT NULL COMMENT '关联的业务ID(如AppointmentID)',
  PRIMARY KEY (`ItemID`) USING BTREE,
  INDEX `idx_bill_id`(`BillID`) USING BTREE,
  INDEX `idx_reference_id`(`ReferenceID`) USING BTREE,
  CONSTRAINT `fk_bill_item_bill` FOREIGN KEY (`BillID`) REFERENCES `t_bill` (`BillID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1006 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收费明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_bill_items
-- ----------------------------
INSERT INTO `t_bill_items` VALUES (1000, 100, '挂号', '内科挂号费', 10.00, 100);
INSERT INTO `t_bill_items` VALUES (1001, 100, '药品', '感冒灵颗粒 x2盒 + 阿莫西林胶囊 x1盒 + 布洛芬缓释胶囊 x1盒 + 维生素C片 x1瓶', 67.50, 100);
INSERT INTO `t_bill_items` VALUES (1002, 101, '挂号', '内科挂号费', 10.00, 101);
INSERT INTO `t_bill_items` VALUES (1003, 101, '药品', '降压片 x2盒 + 阿司匹林肠溶片 x1盒', 58.50, 101);
INSERT INTO `t_bill_items` VALUES (1004, 102, '挂号', '内科挂号费', 10.00, 102);
INSERT INTO `t_bill_items` VALUES (1005, 102, '药品', '奥美拉唑肠溶胶囊 x2盒', 45.00, 102);

-- ----------------------------
-- Table structure for t_department
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department`  (
  `DeptID` int(11) NOT NULL AUTO_INCREMENT COMMENT '科室ID',
  `DeptName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '科室名称',
  `Description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '科室描述',
  PRIMARY KEY (`DeptID`) USING BTREE,
  UNIQUE INDEX `uk_dept_name`(`DeptName`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '科室表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_department
-- ----------------------------
INSERT INTO `t_department` VALUES (1, '内科', '内科诊疗');
INSERT INTO `t_department` VALUES (2, '外科', '外科诊疗');
INSERT INTO `t_department` VALUES (3, '儿科', '儿科诊疗');

-- ----------------------------
-- Table structure for t_doctor_schedule
-- ----------------------------
DROP TABLE IF EXISTS `t_doctor_schedule`;
CREATE TABLE `t_doctor_schedule`  (
  `ScheduleID` int(11) NOT NULL AUTO_INCREMENT COMMENT '排班ID（主键）',
  `StaffID` int(11) NOT NULL COMMENT '医生ID（关联 T_Staff）',
  `ScheduleDate` date NOT NULL COMMENT '排班日期（如 2025-11-20）',
  `TimeSlot` enum('上午','下午','晚上') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '时间段（上午、下午、晚上）',
  `TotalSlots` int(11) NOT NULL COMMENT '总号数（例 30）',
  `BookedSlots` int(11) NOT NULL DEFAULT 0 COMMENT '已预约号数',
  PRIMARY KEY (`ScheduleID`) USING BTREE,
  INDEX `FK_Schedule_Staff`(`StaffID`) USING BTREE,
  CONSTRAINT `FK_Schedule_Staff` FOREIGN KEY (`StaffID`) REFERENCES `t_staff` (`StaffID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生排班表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_doctor_schedule
-- ----------------------------
INSERT INTO `t_doctor_schedule` VALUES (100, 2, '2025-11-07', '上午', 30, 3);
INSERT INTO `t_doctor_schedule` VALUES (101, 2, '2025-11-07', '下午', 30, 2);
INSERT INTO `t_doctor_schedule` VALUES (102, 3, '2025-11-07', '上午', 25, 2);
INSERT INTO `t_doctor_schedule` VALUES (103, 2, '2025-11-08', '上午', 30, 0);
INSERT INTO `t_doctor_schedule` VALUES (104, 3, '2025-11-08', '下午', 25, 0);

-- ----------------------------
-- Table structure for t_inventory_logs
-- ----------------------------
DROP TABLE IF EXISTS `t_inventory_logs`;
CREATE TABLE `t_inventory_logs`  (
  `LogID` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `MedicineID` int(11) NOT NULL COMMENT '药品ID',
  `ChangeQuantity` int(11) NOT NULL COMMENT '变动数量(正数为入库,负数为出库)',
  `Reason` enum('采购入库','处方消耗','盘点调整','过期报废') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '变动原因(采购入库,处方消耗,盘点调整,过期报废)',
  `RelatedID` int(11) NULL DEFAULT NULL COMMENT '关联的业务ID(如PrescriptionID)',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`LogID`) USING BTREE,
  INDEX `idx_medicine_id`(`MedicineID`) USING BTREE,
  INDEX `idx_create_time`(`CreateTime`) USING BTREE,
  INDEX `idx_reason`(`Reason`) USING BTREE,
  CONSTRAINT `fk_inventory_medicine` FOREIGN KEY (`MedicineID`) REFERENCES `t_medicine` (`MedicineID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '库存流水表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_inventory_logs
-- ----------------------------
INSERT INTO `t_inventory_logs` VALUES (19, 1, 1, '采购入库', NULL, '2025-11-07 13:18:52');

-- ----------------------------
-- Table structure for t_medical_record
-- ----------------------------
DROP TABLE IF EXISTS `t_medical_record`;
CREATE TABLE `t_medical_record`  (
  `RecordID` int(11) NOT NULL AUTO_INCREMENT COMMENT '病历ID',
  `AppointmentID` int(11) NOT NULL COMMENT '关联的挂号ID(一对一)',
  `PatientID` int(11) NOT NULL COMMENT '患者ID',
  `StaffID` int(11) NOT NULL COMMENT '主治医生ID',
  `Subjective` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '主诉(患者自述)',
  `Objective` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '查体(医生检查)',
  `Assessment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '初步诊断',
  `Plan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '处理意见/治疗方案',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '诊疗时间',
  PRIMARY KEY (`RecordID`) USING BTREE,
  UNIQUE INDEX `uk_appointment_id`(`AppointmentID`) USING BTREE,
  INDEX `idx_patient_id`(`PatientID`) USING BTREE,
  INDEX `idx_staff_id`(`StaffID`) USING BTREE,
  CONSTRAINT `fk_record_appointment` FOREIGN KEY (`AppointmentID`) REFERENCES `t_appointment` (`AppointmentID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_record_patient` FOREIGN KEY (`PatientID`) REFERENCES `t_patient` (`PatientID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_record_staff` FOREIGN KEY (`StaffID`) REFERENCES `t_staff` (`StaffID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '病历表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_medical_record
-- ----------------------------
INSERT INTO `t_medical_record` VALUES (100, 100, 1, 2, '患者主诉：发热3天，伴有咳嗽、流涕，体温最高39℃', '查体：体温38.5℃，咽部充血，双肺呼吸音清，心律齐', '急性上呼吸道感染（感冒）', '1. 多休息，多饮水\n2. 口服抗感冒药物\n3. 如症状加重请及时复诊', '2025-11-07 10:40:38');
INSERT INTO `t_medical_record` VALUES (101, 101, 2, 2, '患者主诉：头晕、头痛1周，血压控制不佳', '查体：血压160/95mmHg，心率80次/分，心律齐', '高血压2级', '1. 规律服用降压药\n2. 低盐饮食\n3. 定期监测血压\n4. 两周后复诊', '2025-11-07 11:40:38');
INSERT INTO `t_medical_record` VALUES (102, 102, 3, 2, '患者主诉：胃痛、胃胀2天，进食后加重', '查体：上腹部轻压痛，无反跳痛，肠鸣音正常', '慢性胃炎', '1. 规律饮食，避免辛辣刺激\n2. 口服胃黏膜保护剂\n3. 症状持续请胃镜检查', '2025-11-07 12:10:38');

-- ----------------------------
-- Table structure for t_medicine
-- ----------------------------
DROP TABLE IF EXISTS `t_medicine`;
CREATE TABLE `t_medicine`  (
  `MedicineID` int(11) NOT NULL AUTO_INCREMENT COMMENT '药品ID',
  `MedicineName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '药品名称',
  `Specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规格',
  `Manufacturer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生产厂家',
  `UnitPrice` decimal(10, 2) NOT NULL COMMENT '单价',
  `StockLevel` int(11) NOT NULL DEFAULT 0 COMMENT '库存数量(由T_Inventory_Logs计算)',
  PRIMARY KEY (`MedicineID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '药品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_medicine
-- ----------------------------
INSERT INTO `t_medicine` VALUES (1, '感冒灵颗粒', '10g*10袋/盒', '某某制药有限公司', 15.50, 501);
INSERT INTO `t_medicine` VALUES (2, '阿莫西林胶囊', '0.25g*24粒/盒', '某某制药有限公司', 12.00, 800);
INSERT INTO `t_medicine` VALUES (3, '布洛芬缓释胶囊', '0.3g*20粒/盒', '某某制药有限公司', 18.00, 300);
INSERT INTO `t_medicine` VALUES (4, '降压片', '5mg*30片/盒', '某某制药有限公司', 25.00, 600);
INSERT INTO `t_medicine` VALUES (5, '奥美拉唑肠溶胶囊', '20mg*14粒/盒', '某某制药有限公司', 22.50, 400);
INSERT INTO `t_medicine` VALUES (6, '阿司匹林肠溶片', '100mg*30片/盒', '某某制药有限公司', 8.50, 1000);
INSERT INTO `t_medicine` VALUES (7, '氨溴索口服液', '30ml*6支/盒', '某某制药有限公司', 28.00, 200);
INSERT INTO `t_medicine` VALUES (8, '维生素C片', '100mg*100片/瓶', '某某制药有限公司', 6.50, 1500);

-- ----------------------------
-- Table structure for t_patient
-- ----------------------------
DROP TABLE IF EXISTS `t_patient`;
CREATE TABLE `t_patient`  (
  `PatientID` int(11) NOT NULL AUTO_INCREMENT COMMENT '患者ID',
  `PatientName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `Gender` enum('男','女') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `DateOfBirth` date NULL DEFAULT NULL COMMENT '出生日期',
  `IDCardNumber` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证号',
  `PhoneNumber` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `PasswordHash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录密码(哈希值)',
  `Address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '住址',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建档时间',
  PRIMARY KEY (`PatientID`) USING BTREE,
  UNIQUE INDEX `uk_id_card`(`IDCardNumber`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`PhoneNumber`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '患者表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_patient
-- ----------------------------
INSERT INTO `t_patient` VALUES (1, '张三', '男', '1990-01-01', '110101199001011234', '13800138001', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '北京市朝阳区', '2025-11-07 12:40:38');
INSERT INTO `t_patient` VALUES (2, '李四', '女', '1985-05-15', '110101198505151234', '13800138002', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.', '北京市海淀区', '2025-11-07 12:40:38');
INSERT INTO `t_patient` VALUES (3, '王五', '男', '1995-08-20', '110101199508201234', '13800138003', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.', '北京市西城区', '2025-11-07 12:40:38');
INSERT INTO `t_patient` VALUES (4, '赵六', '女', '1988-12-10', '110101198812101234', '13800138004', '$2a$10$N.zmdr9k7uOEW3Xcj7YWU.hQTjBz6EHjLDXIxxzL8kQZl8fLCkfH.', '北京市东城区', '2025-11-07 12:40:38');

-- ----------------------------
-- Table structure for t_prescription
-- ----------------------------
DROP TABLE IF EXISTS `t_prescription`;
CREATE TABLE `t_prescription`  (
  `PrescriptionID` int(11) NOT NULL AUTO_INCREMENT COMMENT '处方ID',
  `RecordID` int(11) NOT NULL COMMENT '关联的病历ID',
  `StaffID` int(11) NOT NULL COMMENT '开处方的医生ID',
  `PatientID` int(11) NOT NULL COMMENT '患者ID',
  `PrescriptionDate` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开具日期',
  `Status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '未发药' COMMENT '处方状态(未发药,已发药,已作废)',
  PRIMARY KEY (`PrescriptionID`) USING BTREE,
  INDEX `idx_record_id`(`RecordID`) USING BTREE,
  INDEX `idx_staff_id`(`StaffID`) USING BTREE,
  INDEX `idx_patient_id`(`PatientID`) USING BTREE,
  INDEX `idx_status`(`Status`) USING BTREE,
  CONSTRAINT `fk_prescription_patient` FOREIGN KEY (`PatientID`) REFERENCES `t_patient` (`PatientID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_prescription_record` FOREIGN KEY (`RecordID`) REFERENCES `t_medical_record` (`RecordID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_prescription_staff` FOREIGN KEY (`StaffID`) REFERENCES `t_staff` (`StaffID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '处方表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_prescription
-- ----------------------------
INSERT INTO `t_prescription` VALUES (100, 100, 2, 1, '2025-11-07 10:40:38', '未发药');
INSERT INTO `t_prescription` VALUES (101, 101, 2, 2, '2025-11-07 11:40:38', '未发药');
INSERT INTO `t_prescription` VALUES (102, 102, 2, 3, '2025-11-07 12:10:38', '未发药');

-- ----------------------------
-- Table structure for t_prescription_item
-- ----------------------------
DROP TABLE IF EXISTS `t_prescription_item`;
CREATE TABLE `t_prescription_item`  (
  `ItemID` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `PrescriptionID` int(11) NOT NULL COMMENT '处方ID',
  `MedicineID` int(11) NOT NULL COMMENT '药品ID',
  `Quantity` int(11) NOT NULL COMMENT '数量',
  `UnitPrice` decimal(10, 2) NOT NULL COMMENT '单价',
  `Subtotal` decimal(10, 2) NOT NULL COMMENT '小计',
  PRIMARY KEY (`ItemID`) USING BTREE,
  INDEX `idx_prescription_id`(`PrescriptionID`) USING BTREE,
  INDEX `idx_medicine_id`(`MedicineID`) USING BTREE,
  CONSTRAINT `fk_item_medicine` FOREIGN KEY (`MedicineID`) REFERENCES `t_medicine` (`MedicineID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_item_prescription` FOREIGN KEY (`PrescriptionID`) REFERENCES `t_prescription` (`PrescriptionID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1007 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '处方明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_prescription_item
-- ----------------------------
INSERT INTO `t_prescription_item` VALUES (1000, 100, 1, 2, 15.50, 31.00);
INSERT INTO `t_prescription_item` VALUES (1001, 100, 2, 1, 12.00, 12.00);
INSERT INTO `t_prescription_item` VALUES (1002, 100, 3, 1, 18.00, 18.00);
INSERT INTO `t_prescription_item` VALUES (1003, 100, 8, 1, 6.50, 6.50);
INSERT INTO `t_prescription_item` VALUES (1004, 101, 4, 2, 25.00, 50.00);
INSERT INTO `t_prescription_item` VALUES (1005, 101, 6, 1, 8.50, 8.50);
INSERT INTO `t_prescription_item` VALUES (1006, 102, 5, 2, 22.50, 45.00);

-- ----------------------------
-- Table structure for t_staff
-- ----------------------------
DROP TABLE IF EXISTS `t_staff`;
CREATE TABLE `t_staff`  (
  `StaffID` int(11) NOT NULL AUTO_INCREMENT COMMENT '职工ID(主键)',
  `StaffName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '职工姓名',
  `DeptID` int(11) NULL DEFAULT NULL COMMENT '所属科室ID',
  `Role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色',
  `Title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职称',
  `LoginName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录账号',
  `PasswordHash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录密码(哈希值)',
  PRIMARY KEY (`StaffID`) USING BTREE,
  UNIQUE INDEX `uk_login_name`(`LoginName`) USING BTREE,
  INDEX `idx_dept_id`(`DeptID`) USING BTREE,
  CONSTRAINT `fk_staff_dept` FOREIGN KEY (`DeptID`) REFERENCES `t_department` (`DeptID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '职工表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_staff
-- ----------------------------
INSERT INTO `t_staff` VALUES (1, '管理员', NULL, '管理员', '管理员', 'admin', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (2, '李医生', 1, '医生', '主治医师', 'doctor1', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (3, '张医生', 2, '医生', '主任医师', 'doctor2', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (4, '王药师', NULL, '药剂师', '主管药师', 'pharmacist1', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');

SET FOREIGN_KEY_CHECKS = 1;

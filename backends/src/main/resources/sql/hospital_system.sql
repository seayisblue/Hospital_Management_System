/*
 Navicat Premium Data Transfer

 Source Server         : Local_mysql
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : localhost:3306
 Source Schema         : hospital_system

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 09/12/2025 17:49:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_appointment
-- ----------------------------
DROP TABLE IF EXISTS `t_appointment`;
CREATE TABLE `t_appointment`  (
  `AppointmentID` int NOT NULL AUTO_INCREMENT COMMENT '挂号ID',
  `ScheduleID` int NULL DEFAULT NULL COMMENT '关联排班ID（关联 T_Doctor_Schedule）',
  `PatientID` int NOT NULL COMMENT '患者ID',
  `StaffID` int NOT NULL COMMENT '挂号的医生ID(可冗余,也可通过ScheduleID查询)',
  `DeptID` int NOT NULL COMMENT '挂号的科室ID(可冗余,也可通过ScheduleID查询)',
  `AppointmentTime` datetime NOT NULL COMMENT '挂号时间',
  `Status` enum('待就诊','已就诊','已取消') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '待就诊' COMMENT '状态(待就诊,已就诊,已取消)',
  `Fee` decimal(10, 2) NOT NULL COMMENT '挂号费',
  PRIMARY KEY (`AppointmentID`) USING BTREE,
  INDEX `idx_patient_id`(`PatientID` ASC) USING BTREE,
  INDEX `idx_staff_id`(`StaffID` ASC) USING BTREE,
  INDEX `idx_dept_id`(`DeptID` ASC) USING BTREE,
  INDEX `idx_appointment_time`(`AppointmentTime` ASC) USING BTREE,
  INDEX `idx_status`(`Status` ASC) USING BTREE,
  INDEX `FK_Appointment_Schedule`(`ScheduleID` ASC) USING BTREE,
  CONSTRAINT `fk_appointment_dept` FOREIGN KEY (`DeptID`) REFERENCES `t_department` (`DeptID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_appointment_patient` FOREIGN KEY (`PatientID`) REFERENCES `t_patient` (`PatientID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Appointment_Schedule` FOREIGN KEY (`ScheduleID`) REFERENCES `t_doctor_schedule` (`ScheduleID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_appointment_staff` FOREIGN KEY (`StaffID`) REFERENCES `t_staff` (`StaffID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '挂号表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_appointment
-- ----------------------------
INSERT INTO `t_appointment` VALUES (1, 1, 1, 10, 3, '2025-12-02 23:07:21', '已就诊', 15.00);

-- ----------------------------
-- Table structure for t_bill
-- ----------------------------
DROP TABLE IF EXISTS `t_bill`;
CREATE TABLE `t_bill`  (
  `BillID` int NOT NULL AUTO_INCREMENT COMMENT '收费单ID',
  `PatientID` int NOT NULL COMMENT '患者ID',
  `TotalAmount` decimal(10, 2) NOT NULL COMMENT '总金额',
  `Status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '未支付' COMMENT '状态(未支付,已支付,已退款)',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `PayTime` datetime NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`BillID`) USING BTREE,
  INDEX `idx_patient_id`(`PatientID` ASC) USING BTREE,
  INDEX `idx_status`(`Status` ASC) USING BTREE,
  CONSTRAINT `fk_bill_patient` FOREIGN KEY (`PatientID`) REFERENCES `t_patient` (`PatientID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收费单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_bill
-- ----------------------------
INSERT INTO `t_bill` VALUES (1, 1, 69.00, '未支付', '2025-12-02 23:07:21', NULL);

-- ----------------------------
-- Table structure for t_bill_items
-- ----------------------------
DROP TABLE IF EXISTS `t_bill_items`;
CREATE TABLE `t_bill_items`  (
  `ItemID` int NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `BillID` int NOT NULL COMMENT '关联的收费单ID',
  `ItemType` enum('挂号','药品') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '费用类型(挂号,药品)',
  `ItemName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目名称',
  `Amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `ReferenceID` int NULL DEFAULT NULL COMMENT '关联的业务ID(如AppointmentID)',
  PRIMARY KEY (`ItemID`) USING BTREE,
  INDEX `idx_bill_id`(`BillID` ASC) USING BTREE,
  INDEX `idx_reference_id`(`ReferenceID` ASC) USING BTREE,
  CONSTRAINT `fk_bill_item_bill` FOREIGN KEY (`BillID`) REFERENCES `t_bill` (`BillID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收费明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_bill_items
-- ----------------------------
INSERT INTO `t_bill_items` VALUES (1, 1, '挂号', '挂号费', 15.00, 1);
INSERT INTO `t_bill_items` VALUES (2, 1, '药品', '999感冒灵颗粒 x4', 54.00, 1);

-- ----------------------------
-- Table structure for t_department
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department`  (
  `DeptID` int NOT NULL AUTO_INCREMENT COMMENT '科室ID',
  `DeptName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '科室名称',
  `Description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '科室描述',
  `ManagerID` int NULL DEFAULT NULL COMMENT '科室主任ID',
  `WorkHours` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '门诊时间',
  PRIMARY KEY (`DeptID`) USING BTREE,
  UNIQUE INDEX `uk_dept_name`(`DeptName` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '科室表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_department
-- ----------------------------
INSERT INTO `t_department` VALUES (1, '心血管内科', '国家级重点学科，主要诊治高血压、冠心病、心律失常、心力衰竭等疾病。拥有先进的导管室和CCU病房。', 4, '周一至周日 08:00-17:00');
INSERT INTO `t_department` VALUES (2, '呼吸与危重症医学科', '专注于呼吸系统常见病、多发病及疑难危重症的诊治。擅长慢阻肺、哮喘、肺部感染及呼吸衰竭的救治。', 7, '周一至周日 08:00-17:00');
INSERT INTO `t_department` VALUES (3, '消化内科', '集医疗、教学、科研为一体，擅长胃肠道、肝胆胰腺疾病的内镜下微创治疗及综合诊治。', 9, '周一至周六 08:00-17:30');
INSERT INTO `t_department` VALUES (4, '普外科', '开展胃肠、甲状腺、乳腺、疝气等各类常规及微创手术。技术力量雄厚，并发症少。', 11, '周一至周日 08:00-17:00');
INSERT INTO `t_department` VALUES (5, '骨科', '设有脊柱、关节、创伤三个亚专业。常规开展关节置换、脊柱微创手术及复杂骨折内固定术。', 13, '周一至周五 08:00-17:00');
INSERT INTO `t_department` VALUES (6, '儿科', '环境温馨，提供0-14岁儿童常见病诊治、生长发育评估及儿童保健服务。设有新生儿重症监护室。', 15, '周一至周日 08:00-20:00 (含急诊)');
INSERT INTO `t_department` VALUES (7, '皮肤科', '拥有激光美容、光疗、过敏原检测等先进设备。擅长痤疮、湿疹、银屑病及面部损容性皮肤病的治疗。', 18, '周一至周日 09:00-18:00');
INSERT INTO `t_department` VALUES (8, '中医科', '秉承传统医学精髓，采用中药内服外敷、针灸、推拿、拔罐等综合疗法，调理各类慢性疾病及亚健康状态。', 20, '周一至周五 08:30-17:00');

-- ----------------------------
-- Table structure for t_doctor_schedule
-- ----------------------------
DROP TABLE IF EXISTS `t_doctor_schedule`;
CREATE TABLE `t_doctor_schedule`  (
  `ScheduleID` int NOT NULL AUTO_INCREMENT COMMENT '排班ID（主键）',
  `StaffID` int NOT NULL COMMENT '医生ID（关联 T_Staff）',
  `ScheduleDate` date NOT NULL COMMENT '排班日期（如 2025-11-20）',
  `TimeSlot` enum('上午','下午','晚上') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '时间段（上午、下午、晚上）',
  `TotalSlots` int NOT NULL COMMENT '总号数（例 30）',
  `BookedSlots` int NOT NULL DEFAULT 0 COMMENT '已预约号数',
  PRIMARY KEY (`ScheduleID`) USING BTREE,
  INDEX `FK_Schedule_Staff`(`StaffID` ASC) USING BTREE,
  CONSTRAINT `FK_Schedule_Staff` FOREIGN KEY (`StaffID`) REFERENCES `t_staff` (`StaffID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生排班表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_doctor_schedule
-- ----------------------------
INSERT INTO `t_doctor_schedule` VALUES (1, 10, '2025-12-04', '下午', 30, 1);

-- ----------------------------
-- Table structure for t_inventory_logs
-- ----------------------------
DROP TABLE IF EXISTS `t_inventory_logs`;
CREATE TABLE `t_inventory_logs`  (
  `LogID` int NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `MedicineID` int NOT NULL COMMENT '药品ID',
  `ChangeQuantity` int NOT NULL COMMENT '变动数量(正数为入库,负数为出库)',
  `Reason` enum('采购入库','处方消耗','盘点调整','过期报废') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '变动原因(采购入库,处方消耗,盘点调整,过期报废)',
  `RelatedID` int NULL DEFAULT NULL COMMENT '关联的业务ID(如PrescriptionID)',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`LogID`) USING BTREE,
  INDEX `idx_medicine_id`(`MedicineID` ASC) USING BTREE,
  INDEX `idx_create_time`(`CreateTime` ASC) USING BTREE,
  INDEX `idx_reason`(`Reason` ASC) USING BTREE,
  CONSTRAINT `fk_inventory_medicine` FOREIGN KEY (`MedicineID`) REFERENCES `t_medicine` (`MedicineID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '库存流水表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_inventory_logs
-- ----------------------------
INSERT INTO `t_inventory_logs` VALUES (1, 1, -4, '处方消耗', 1, '2025-12-02 23:11:40');

-- ----------------------------
-- Table structure for t_medical_record
-- ----------------------------
DROP TABLE IF EXISTS `t_medical_record`;
CREATE TABLE `t_medical_record`  (
  `RecordID` int NOT NULL AUTO_INCREMENT COMMENT '病历ID',
  `AppointmentID` int NOT NULL COMMENT '关联的挂号ID(一对一)',
  `PatientID` int NOT NULL COMMENT '患者ID',
  `StaffID` int NOT NULL COMMENT '主治医生ID',
  `Subjective` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '主诉(患者自述)',
  `Objective` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '查体(医生检查)',
  `Assessment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '初步诊断',
  `Plan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '处理意见/治疗方案',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '诊疗时间',
  PRIMARY KEY (`RecordID`) USING BTREE,
  UNIQUE INDEX `uk_appointment_id`(`AppointmentID` ASC) USING BTREE,
  INDEX `idx_patient_id`(`PatientID` ASC) USING BTREE,
  INDEX `idx_staff_id`(`StaffID` ASC) USING BTREE,
  CONSTRAINT `fk_record_appointment` FOREIGN KEY (`AppointmentID`) REFERENCES `t_appointment` (`AppointmentID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_record_patient` FOREIGN KEY (`PatientID`) REFERENCES `t_patient` (`PatientID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_record_staff` FOREIGN KEY (`StaffID`) REFERENCES `t_staff` (`StaffID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '病历表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_medical_record
-- ----------------------------
INSERT INTO `t_medical_record` VALUES (1, 1, 1, 10, '发热、咳嗽3天，伴有咽痛', 'T: 38.5℃，咽部充血，扁桃体I度肿大，双肺呼吸音粗，未闻及干湿性啰音', '急性上呼吸道感染', '1. 抗感染治疗\n2. 对症支持治疗\n3. 多饮水，注意休息\n4. 如有加重，及时复诊', '2025-12-02 23:09:06');

-- ----------------------------
-- Table structure for t_medicine
-- ----------------------------
DROP TABLE IF EXISTS `t_medicine`;
CREATE TABLE `t_medicine`  (
  `MedicineID` int NOT NULL AUTO_INCREMENT COMMENT '药品ID',
  `MedicineName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '药品名称',
  `Specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规格',
  `Manufacturer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生产厂家',
  `UnitPrice` decimal(10, 2) NOT NULL COMMENT '单价',
  `StockLevel` int NOT NULL DEFAULT 0 COMMENT '库存数量(由T_Inventory_Logs计算)',
  `MinStock` int NULL DEFAULT 10 COMMENT '安全库存',
  PRIMARY KEY (`MedicineID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '药品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_medicine
-- ----------------------------
INSERT INTO `t_medicine` VALUES (1, '999感冒灵颗粒', '10g*9袋/盒', '华润三九医药股份有限公司', 13.50, 496, 10);
INSERT INTO `t_medicine` VALUES (2, '复方氨酚烷胺片(感康)', '12片/盒', '吉林省吴太感康药业有限公司', 14.80, 800, 10);
INSERT INTO `t_medicine` VALUES (3, '连花清瘟胶囊', '0.35g*24粒/盒', '石家庄以岭药业股份有限公司', 28.50, 300, 10);
INSERT INTO `t_medicine` VALUES (4, '布洛芬缓释胶囊(芬必得)', '0.3g*20粒/盒', '中美天津史克制药有限公司', 22.00, 450, 10);
INSERT INTO `t_medicine` VALUES (5, '对乙酰氨基酚片', '0.5g*12片/盒', '东北制药集团沈阳第一制药有限公司', 3.50, 1000, 10);
INSERT INTO `t_medicine` VALUES (6, '小儿氨酚黄那敏颗粒', '6g*10袋/盒', '葵花药业集团(重庆)有限公司', 15.00, 200, 10);
INSERT INTO `t_medicine` VALUES (7, '复方板蓝根颗粒', '15g*20袋/包', '广州白云山和记黄埔中药有限公司', 10.50, 600, 10);
INSERT INTO `t_medicine` VALUES (8, '阿莫西林胶囊', '0.25g*20粒/盒', '珠海联邦制药股份有限公司', 18.00, 400, 10);
INSERT INTO `t_medicine` VALUES (9, '头孢拉定胶囊', '0.25g*24粒/盒', '山东鲁抗医药股份有限公司', 6.50, 500, 10);
INSERT INTO `t_medicine` VALUES (10, '罗红霉素胶囊', '150mg*12粒/盒', '扬子江药业集团有限公司', 12.00, 300, 10);
INSERT INTO `t_medicine` VALUES (11, '阿奇霉素分散片', '0.25g*6片/盒', '石药集团欧意药业有限公司', 16.50, 250, 10);
INSERT INTO `t_medicine` VALUES (12, '人工牛黄甲硝唑胶囊', '20粒/盒', '修正药业集团股份有限公司', 8.50, 400, 10);
INSERT INTO `t_medicine` VALUES (13, '红霉素软膏', '10g/支', '北京双吉制药有限公司', 2.50, 1000, 10);
INSERT INTO `t_medicine` VALUES (14, '硝苯地平控释片(拜新同)', '30mg*7片/盒', '拜耳医药保健有限公司', 32.00, 300, 10);
INSERT INTO `t_medicine` VALUES (15, '苯磺酸氨氯地平片(络活喜)', '5mg*7片/盒', '辉瑞制药有限公司', 28.00, 300, 10);
INSERT INTO `t_medicine` VALUES (16, '奥美拉唑肠溶胶囊', '20mg*14粒/盒', '修正药业集团股份有限公司', 22.50, 400, 10);
INSERT INTO `t_medicine` VALUES (17, '健胃消食片', '0.8g*32片/盒', '江中药业股份有限公司', 9.00, 1200, 10);
INSERT INTO `t_medicine` VALUES (18, '蒙脱石散(思密达)', '3g*10袋/盒', '博福-益普生(天津)制药有限公司', 22.80, 300, 10);
INSERT INTO `t_medicine` VALUES (19, '云南白药创可贴', '100片/盒', '云南白药集团股份有限公司', 15.00, 800, 10);
INSERT INTO `t_medicine` VALUES (20, '维生素C泡腾片(力度伸)', '1g*10片/支', '拜耳医药保健有限公司', 35.00, 400, 10);

-- ----------------------------
-- Table structure for t_patient
-- ----------------------------
DROP TABLE IF EXISTS `t_patient`;
CREATE TABLE `t_patient`  (
  `PatientID` int NOT NULL AUTO_INCREMENT COMMENT '患者ID',
  `PatientName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `Gender` enum('男','女') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `DateOfBirth` date NULL DEFAULT NULL COMMENT '出生日期',
  `IDCardNumber` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证号',
  `PhoneNumber` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `PasswordHash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录密码(哈希值)',
  `Address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '住址',
  `CreateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建档时间',
  PRIMARY KEY (`PatientID`) USING BTREE,
  UNIQUE INDEX `uk_id_card`(`IDCardNumber` ASC) USING BTREE,
  UNIQUE INDEX `uk_phone`(`PhoneNumber` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '患者表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_patient
-- ----------------------------
INSERT INTO `t_patient` VALUES (1, '张伟', '男', '1985-03-12', '110101198503121234', '13800138001', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '北京市朝阳区朝阳北路101号', '2025-12-02 22:25:02');
INSERT INTO `t_patient` VALUES (2, '王芳', '女', '1990-07-25', '110102199007255678', '13900139002', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '上海市浦东新区世纪大道88号', '2025-12-02 22:25:02');
INSERT INTO `t_patient` VALUES (3, '李强', '男', '1978-11-05', '310104197811059012', '13700137003', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '广州市天河区珠江新城5号', '2025-12-02 22:25:02');
INSERT INTO `t_patient` VALUES (4, '刘洋', '女', '1995-01-18', '320102199501183456', '13600136004', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '深圳市南山区科技园南区', '2025-12-02 22:25:02');
INSERT INTO `t_patient` VALUES (5, '陈杰', '男', '1988-09-30', '440106198809307890', '13500135005', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '成都市武侯区人民南路三段', '2025-12-02 22:25:02');
INSERT INTO `t_patient` VALUES (6, '赵敏', '女', '2000-05-14', '510107200005141234', '13300133006', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '杭州市西湖区文三路30号', '2025-12-02 22:25:02');
INSERT INTO `t_patient` VALUES (7, '孙悟空', '男', '2015-06-01', '120101201506016666', '18000180007', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '花果山水帘洞小区1号楼 (监护人: 唐僧)', '2025-12-02 22:25:02');
INSERT INTO `t_patient` VALUES (8, '林黛玉', '女', '1998-04-12', '330106199804128888', '18100181008', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '苏州市姑苏区平江路潇湘馆', '2025-12-02 22:25:02');
INSERT INTO `t_patient` VALUES (9, '马云飞', '男', '1965-12-20', '420103196512209999', '18900189009', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '武汉市江汉区汉口火车站旁', '2025-12-02 22:25:02');
INSERT INTO `t_patient` VALUES (10, '周星驰', '男', '1970-06-22', '810000197006220000', '19900199010', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq', '香港特别行政区九龙塘', '2025-12-02 22:25:02');

-- ----------------------------
-- Table structure for t_prescription
-- ----------------------------
DROP TABLE IF EXISTS `t_prescription`;
CREATE TABLE `t_prescription`  (
  `PrescriptionID` int NOT NULL AUTO_INCREMENT COMMENT '处方ID',
  `RecordID` int NOT NULL COMMENT '关联的病历ID',
  `StaffID` int NOT NULL COMMENT '开处方的医生ID',
  `PatientID` int NOT NULL COMMENT '患者ID',
  `PrescriptionDate` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开具日期',
  `Status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '未发药' COMMENT '处方状态(未发药,已发药,已作废)',
  PRIMARY KEY (`PrescriptionID`) USING BTREE,
  INDEX `idx_record_id`(`RecordID` ASC) USING BTREE,
  INDEX `idx_staff_id`(`StaffID` ASC) USING BTREE,
  INDEX `idx_patient_id`(`PatientID` ASC) USING BTREE,
  INDEX `idx_status`(`Status` ASC) USING BTREE,
  CONSTRAINT `fk_prescription_patient` FOREIGN KEY (`PatientID`) REFERENCES `t_patient` (`PatientID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_prescription_record` FOREIGN KEY (`RecordID`) REFERENCES `t_medical_record` (`RecordID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_prescription_staff` FOREIGN KEY (`StaffID`) REFERENCES `t_staff` (`StaffID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '处方表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_prescription
-- ----------------------------
INSERT INTO `t_prescription` VALUES (1, 1, 10, 1, '2025-12-02 23:09:35', '已发药');

-- ----------------------------
-- Table structure for t_prescription_item
-- ----------------------------
DROP TABLE IF EXISTS `t_prescription_item`;
CREATE TABLE `t_prescription_item`  (
  `ItemID` int NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `PrescriptionID` int NOT NULL COMMENT '处方ID',
  `MedicineID` int NOT NULL COMMENT '药品ID',
  `Quantity` int NOT NULL COMMENT '数量',
  `UnitPrice` decimal(10, 2) NOT NULL COMMENT '单价',
  `Subtotal` decimal(10, 2) NOT NULL COMMENT '小计',
  PRIMARY KEY (`ItemID`) USING BTREE,
  INDEX `idx_prescription_id`(`PrescriptionID` ASC) USING BTREE,
  INDEX `idx_medicine_id`(`MedicineID` ASC) USING BTREE,
  CONSTRAINT `fk_item_medicine` FOREIGN KEY (`MedicineID`) REFERENCES `t_medicine` (`MedicineID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_item_prescription` FOREIGN KEY (`PrescriptionID`) REFERENCES `t_prescription` (`PrescriptionID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '处方明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_prescription_item
-- ----------------------------
INSERT INTO `t_prescription_item` VALUES (1, 1, 1, 4, 13.50, 54.00);

-- ----------------------------
-- Table structure for t_staff
-- ----------------------------
DROP TABLE IF EXISTS `t_staff`;
CREATE TABLE `t_staff`  (
  `StaffID` int NOT NULL AUTO_INCREMENT COMMENT '职工ID(主键)',
  `StaffName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '职工姓名',
  `DeptID` int NULL DEFAULT NULL COMMENT '所属科室ID',
  `Role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色',
  `Title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职称',
  `LoginName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录账号',
  `PasswordHash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录密码(哈希值)',
  PRIMARY KEY (`StaffID`) USING BTREE,
  UNIQUE INDEX `uk_login_name`(`LoginName` ASC) USING BTREE,
  INDEX `idx_dept_id`(`DeptID` ASC) USING BTREE,
  CONSTRAINT `fk_staff_dept` FOREIGN KEY (`DeptID`) REFERENCES `t_department` (`DeptID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '职工表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_staff
-- ----------------------------
INSERT INTO `t_staff` VALUES (1, '系统管理员', NULL, '管理员', '高级工程师', 'admin', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (2, '王钰', NULL, '药剂师', '主管药师', 'pharm1', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (3, '陈星', NULL, '药剂师', '药师', 'pharm2', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (4, '张立强', 1, '医生', '主任医师', 'doc101', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (5, '李慧', 1, '医生', '主治医师', 'doc102', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (6, '赵志刚', 1, '医生', '副主任医师', 'doc103', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (7, '王建国', 2, '医生', '主任医师', 'doc201', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (8, '陈静', 2, '医生', '医师', 'doc202', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (9, '刘波', 3, '医生', '副主任医师', 'doc301', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (10, '吴海燕', 3, '医生', '主治医师', 'doc302', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (11, '孙伟', 4, '医生', '主任医师', 'doc401', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (12, '周杰', 4, '医生', '主治医师', 'doc402', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (13, '郑强', 5, '医生', '副主任医师', 'doc501', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (14, '冯涛', 5, '医生', '医师', 'doc502', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (15, '褚敏', 6, '医生', '主任医师', 'doc601', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (16, '卫红', 6, '医生', '主治医师', 'doc602', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (17, '蒋小龙', 6, '医生', '医师', 'doc603', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (18, '沈丽', 7, '医生', '副主任医师', 'doc701', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (19, '韩梅梅', 7, '医生', '医师', 'doc702', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (20, '杨仁和', 8, '医生', '主任医师', 'doc801', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');
INSERT INTO `t_staff` VALUES (21, '朱丹', 8, '医生', '主治医师', 'doc802', '$2a$10$FpAwGF2h6My7PUO1/Ng6d.nkLtlMDFlMkL/eJx2uoFSg2W3/j30gq');

SET FOREIGN_KEY_CHECKS = 1;

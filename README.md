# Hospital Management System

项目名称：Hospital Management System  

## 项目简介
Hospital Management System（HMS）是一个用于医院管理的综合性示例/应用，包含后端服务和前端界面，旨在管理病人信息、预约、医护人员、病历与统计等功能。本仓库将后端和前端分离，便于独立开发与部署。


## 主要特性
- 病人信息管理（新增 / 编辑 / 查询 / 删除）
- 医护人员与科室管理
- 预约与排班系统
- 病历与检查结果记录
- 权限与角色管理（管理员 / 医生 / 护士 / 患者）
- 医院药品入库出库管理

## 快速开始

1. 克隆仓库
```bash
git clone https://github.com/seayisblue/Hospital_Management_System.git
cd Hospital_Management_System
```

2. 启动后端
```bash
cd backends
# 使用 Maven
mvn clean package
java -jar target/your-backend.jar
# 或 使用 Gradle
./gradlew bootRun
```

3. 启动前端
```bash
cd frontends
npm install
npm run dev     
```

## 贡献
欢迎贡献！请提交 Issue 或 Pull Request。建议：
- 提交前建立 feature 分支
- 详述变更目的与回归测试策略
- 为新功能添加测试


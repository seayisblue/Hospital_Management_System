# PowerShell脚本：将所有Java文件中的javax导入更新为jakarta
# 使用方法：在PowerShell中运行此脚本

$projectRoot = "d:\桌面\database\hospital\Hospital_Management_System\backends\src\main\java\com\template"

# 获取所有Java文件
$javaFiles = Get-ChildItem -Path $projectRoot -Filter "*.java" -Recurse

Write-Host "开始更新javax导入为jakarta..." -ForegroundColor Green
Write-Host "共找到 $($javaFiles.Count) 个Java文件"
Write-Host ""

$updateCount = 0

foreach ($file in $javaFiles) {
    $content = Get-Content -Path $file.FullName -Raw
    $originalContent = $content
    
    # 替换各种javax导入
    $content = $content -replace 'import javax\.validation\.', 'import jakarta.validation.'
    $content = $content -replace 'import javax\.servlet\.', 'import jakarta.servlet.'
    $content = $content -replace 'import javax\.persistence\.', 'import jakarta.persistence.'
    $content = $content -replace 'import javax\.crypto\.', 'import javax.crypto.' # 保持javax.crypto不变
    
    # 如果内容有改变，则保存
    if ($content -ne $originalContent) {
        Set-Content -Path $file.FullName -Value $content
        $updateCount++
        Write-Host "✓ 已更新: $($file.Name)" -ForegroundColor Cyan
    }
}

Write-Host ""
Write-Host "完成！共更新 $updateCount 个文件" -ForegroundColor Green
Write-Host ""
Write-Host "验证：查找是否还有javax.validation或javax.servlet的导入..."
$remaining = Get-ChildItem -Path $projectRoot -Filter "*.java" -Recurse | 
    Select-String -Pattern 'import javax\.(validation|servlet|persistence)' 

if ($remaining.Count -gt 0) {
    Write-Host "警告：仍然找到以下需要更新的导入:" -ForegroundColor Yellow
    $remaining | ForEach-Object { Write-Host "  $($_.FileName): $($_.Line)" }
} else {
    Write-Host "验证成功：所有javax.validation/servlet/persistence导入已更新为jakarta" -ForegroundColor Green
}

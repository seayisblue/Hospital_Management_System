// 通用工具函数

/**
 * 显示消息提示
 * @param {string} message 消息内容
 * @param {string} type 类型: success, error, warning, info
 */
function showMessage(message, type = 'info') {
    // 移除现有的消息框
    const existingAlert = document.querySelector('.alert-message');
    if (existingAlert) {
        existingAlert.remove();
    }

    // 创建新的消息框
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show alert-message`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // 添加到页面顶部
    document.body.insertBefore(alertDiv, document.body.firstChild);
    
    // 3秒后自动消失
    setTimeout(() => {
        if (alertDiv.parentElement) {
            alertDiv.remove();
        }
    }, 3000);
}

/**
 * 格式化日期
 * @param {Date} date 日期对象
 * @returns {string} 格式化后的日期字符串
 */
function formatDate(date) {
    return new Date(date).toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
}

/**
 * 验证手机号格式
 * @param {string} phone 手机号
 * @returns {boolean} 是否有效
 */
function validatePhone(phone) {
    return /^1[3-9]\d{9}$/.test(phone);
}

/**
 * 验证身份证号格式
 * @param {string} idCard 身份证号
 * @returns {boolean} 是否有效
 */
function validateIdCard(idCard) {
    return /^\d{17}[\dXx]$/.test(idCard);
}

/**
 * 加载导航栏
 * @param {string} containerId 容器ID
 */
function loadNavbar(containerId = 'navbar-container') {
    const container = document.getElementById(containerId);
    if (container) {
        fetch('../../components/navbar.html')
            .then(response => response.text())
            .then(html => {
                container.innerHTML = html;
                console.log('导航栏加载成功');
            })
            .catch(error => {
                console.error('加载导航栏失败:', error);
                // 如果加载失败，显示一个简单的导航栏
                container.innerHTML = `
                    <nav class="navbar navbar-expand-lg navbar-dark">
                        <div class="container">
                            <a class="navbar-brand" href="#">
                                🏥 医院门诊管理系统
                                <small class="ms-2 opacity-75">[系统]</small>
                            </a>
                            <div class="navbar-nav">
                                <a class="nav-link" href="../patient/login.html">登录</a>
                            </div>
                        </div>
                    </nav>
                `;
            });
    }
}
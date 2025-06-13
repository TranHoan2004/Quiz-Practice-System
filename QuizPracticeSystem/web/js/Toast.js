function createToast(message, type = 'info', duration = 4000) {
    // Tạo container nếu chưa có
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        Object.assign(container.style, {
            position: 'fixed',
            top: '1rem',
            right: '1rem',
            zIndex: 9999,
            display: 'flex',
            flexDirection: 'column',
            gap: '0.5rem',
            maxWidth: '320px',
        });
        document.body.appendChild(container);
    }

    // Icon theo type
    const icons = {
        success: '<i class="fas fa-check-circle"></i>',
        error: '<i class="fas fa-times-circle"></i>',
        warning: '<i class="fas fa-exclamation-triangle"></i>',
        info: '<i class="fas fa-info-circle"></i>'
    };


    // Màu nền theo type
    const bgColors = {
        success: '#28a745',
        error: '#dc3545',
        warning: '#ffc107',
        info: '#17a2b8'
    };

    // Tạo toast element
    const toast = document.createElement('div');
    Object.assign(toast.style, {
        backgroundColor: bgColors[type] || bgColors.info,
        color: '#fff',
        padding: '0.75rem 1rem',
        borderRadius: '4px',
        display: 'flex',
        alignItems: 'center',
        boxShadow: '0 2px 8px rgba(0,0,0,0.15)',
        opacity: '0',
        transition: 'opacity 0.3s ease',
        fontFamily: 'Arial, sans-serif',
        cursor: 'pointer',
    });

    toast.innerHTML = `
        <span style="margin-right: 0.75rem; font-size: 1.2rem;">${icons[type] || icons.info}</span>
        <div style="flex-grow: 1;">${message}</div>
        <button aria-label="Close" style="
            background: none;
            border: none;
            color: white;
            font-weight: bold;
            font-size: 1.2rem;
            margin-left: 0.75rem;
            cursor: pointer;
            line-height: 1;
        ">&times;</button>
    `;

    // Đóng toast khi nhấn nút close
    toast.querySelector('button').addEventListener('click', () => {
        fadeOutAndRemove(toast);
    });

    // Thêm toast vào container
    container.appendChild(toast);

    // Hiện toast (fade in)
    setTimeout(() => {
        toast.style.opacity = '1';
    }, 50);

    // Tự động ẩn toast sau duration
    setTimeout(() => {
        fadeOutAndRemove(toast);
    }, duration);

    // Hàm ẩn và xóa toast
    function fadeOutAndRemove(el) {
        el.style.opacity = '0';
        setTimeout(() => {
            if (el.parentElement) {
                el.parentElement.removeChild(el);
            }
            // Nếu container rỗng thì xóa container luôn
            if (container.children.length === 0) {
                container.remove();
            }
        }, 300);
    }
}

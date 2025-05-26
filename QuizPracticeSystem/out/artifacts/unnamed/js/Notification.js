function showNotification(message, type = 'success') {
    const container = document.getElementById('notification-container');

    const popup = document.createElement('div');
    popup.className = `toast-notification d-flex shadow align-items-center ${type === 'success' ? 'bg-success' : 'bg-danger'}`;
    popup.setAttribute('role', 'alert');
    popup.setAttribute('aria-live', 'assertive');
    popup.setAttribute('aria-atomic', 'true');

    popup.innerHTML = `
        <div class="toast-icon">
            <i class="bi ${type === 'success' ? 'bi-check-circle-fill' : 'bi-exclamation-triangle-fill'}"></i>
        </div>
        <div class="toast-body">${message}</div>
        <button type="button" class="btn-close btn-close-white ms-2" aria-label="Close"></button>
    `;

    popup.querySelector('.btn-close').addEventListener('click', () => {
        popup.classList.remove('show');
        setTimeout(() => {
            popup.remove();
        }, 500);
    });

    container.appendChild(popup);
    setTimeout(() => {
        popup.classList.add('show');
    }, 10); 

    setTimeout(() => {
        popup.classList.remove('show');
        setTimeout(() => {
            popup.remove();
        }, 500);
    }, 4000);
}
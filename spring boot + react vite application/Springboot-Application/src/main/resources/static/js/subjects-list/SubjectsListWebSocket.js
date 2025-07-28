/**
 * Khởi tạo kết nối WebSocket với server và thiết lập các sự kiện cần xử lý.
 */
function initWebSocket() {
    socket = new WebSocket(path);
    console.log('Initializing WebSocket:', path);

    socket.onopen = function () {
        console.log('WebSocket connected');
        socket.send('currentEmail');
    };

    socket.onmessage = function (event) {
        answer = event.data;
        console.log('Server connection acknowledged');
    };

    socket.onerror = function (event) {
        console.error('WebSocket error:', event);
    };

    socket.onclose = function (event) {
        console.warn('WebSocket closed:', event);
//        setTimeout(initWebSocket, 3000);
    };
}

initWebSocket();
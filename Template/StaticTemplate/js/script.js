document.getElementById("loginForm").addEventListener("submit", function (e) {
    e.preventDefault(); // Ngăn form submit nếu có lỗi

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    if (email === "" || password === "") {
        alert("Vui lòng điền đầy đủ Email và Password.");
        return;
    }

    // Nếu cần xử lý tiếp, bạn có thể gửi dữ liệu đến server ở đây
    alert("Đăng nhập thành công!");
});
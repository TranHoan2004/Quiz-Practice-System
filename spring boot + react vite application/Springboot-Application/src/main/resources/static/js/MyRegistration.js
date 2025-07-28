let href = `${window.contextPath}/user/registration`;

/**
 * Lắng nghe sự kiện thay đổi bộ lọc môn học.
 * Khi thay đổi, cập nhật URL với tham số filter và chuyển hướng đến trang mới.
 */
document.getElementById('subjectFilter').addEventListener('change', function () {
    const value = this.value;
    let href = `${window.contextPath}/user/registration`;
    if (value !== 'all') {
        href += '?filter=' + value;
    }
    window.location.href = href;
});

/**
 * Gắn sự kiện click vào từng dòng (record).
 * Nếu click không nằm trên button hoặc link, thực hiện chuyển hướng theo `data-id`.
 */
document.querySelectorAll('.record').forEach(function (row) {
    row.addEventListener('click', function (e) {
        if (e.target.closest('button') || e.target.closest('a')) {
            return;
        }
        const value = this.dataset.id;
        if (value !== 'all') {
            href += '?org=' + value;
        }
        window.location.href = href;
    });
});

/**
 * Gửi yêu cầu hủy đăng ký khóa học theo ID đến server.
 * @param {string} id - Mã ID của khóa học cần hủy đăng ký.
 */
window.updateCourse = (id) => {
    fetch(`${window.contextPath}/user/registration`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({id: id})
    })
        .then(response => {
            if (!response.ok)
                throw new Error("Request failed");
            return response.body;
        })
        .then(data => {
            showNotification("Cancel register successfully", "success");
            setTimeout(() => {
                location.href = href;
            }, 4500);
        })
        .catch(error => {
            console.log(error);
        });
}

/**
 * Gửi yêu cầu HTTP GET để lấy tiêu đề từ `href` và gán tiêu đề đầu tiên vào giao diện.
 *
 * @async
 * @function getHeaderTitle
 * @throws {Error} Nếu xảy ra lỗi trong quá trình fetch hoặc parse JSON.
 *
 * @author HoanTX
 */
const getHeaderTitle = async () => {
    try {
        const response = await fetch(`${href}`, {
            method: 'GET',
            headers: {
                'X-Source': 'getTitle'
            }
        });
        const content = await response.json();
        assignTitle(content[0]);
    } catch (e) {
        console.error(e);
    }
};

await getHeaderTitle();

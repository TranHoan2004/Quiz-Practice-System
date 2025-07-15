let href = `${window.contextPath}/user/practice`; 

/**
 * Chuyển hướng đến trang /practice khi thay đổi bộ lọc môn học.
 */
document.getElementById('subjectFilter').addEventListener('change', function () {
    const value = this.value;
    if (value !== 'all') {
        href += '?filter=' + value;
    }
    window.location.href = href;
});

/**
 * Lưu ID của bản ghi được chọn để xóa khi click vào nút xóa trong bảng.
 * ID được lấy từ thuộc tính `data-section` của phần tử cha <tr>.
 */
let recordId = '';
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", function () {
            let recordRow = this.closest("tr");
            recordId = recordRow.getAttribute("data-section");

            if (recordId) {
                console.log("ID của bản ghi cần xóa:", recordId);
            }
        });
    });
});

let lastDeleted = null;
let lastDeletedParent = null;
let lastDeletedNextSibling = null;
let lastDeletedPracticeId = null;
let undoTimeout = null;
let rowToDelete = null;

/**
 * Gắn sự kiện xóa cho tất cả nút `.delete-btn`. Đảm bảo không bị gắn trùng nhiều lần.
 */
function attachDeleteEvents() {
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.removeEventListener('click', deleteHandler);
        btn.addEventListener('click', deleteHandler);
    });
}

/**
 * Xử lý khi người dùng click vào nút xóa: hiển thị modal xác nhận.
 * @param {MouseEvent} e - Sự kiện click.
 */
function deleteHandler(e) {
    rowToDelete = e.target.closest('tr');
    const modalEl = document.getElementById('deleteModal');
    if (modalEl) {
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
    }
}

attachDeleteEvents();

/**
 * Khi người dùng xác nhận xóa:
 * - Xóa hàng đang chọn
 * - Nếu là dòng cuối của section, xóa cả phần footer
 * - Lưu trạng thái để có thể undo
 */
const confirmBtn = document.getElementById('confirmDeleteBtn');
if (confirmBtn) {
    confirmBtn.addEventListener('click', () => {
        if (rowToDelete) {
            // Lấy id thực tế của practice, ví dụ từ data-practice-id (nếu có)
            lastDeletedPracticeId = rowToDelete.getAttribute('data-practice-id') || rowToDelete.children[0]?.textContent?.trim() || null;

            const section = rowToDelete.getAttribute('data-section');
            const allRowsInSection = document.querySelectorAll(`tr[data-section='` + section + `']`);
            const dataRows = Array.from(allRowsInSection).filter(tr => !tr.classList.contains('section-footer'));
            const footerRow = Array.from(allRowsInSection).find(tr => tr.classList.contains('section-footer'));

            if (dataRows.length === 1) {
                lastDeleted = document.createDocumentFragment();
                lastDeletedParent = rowToDelete.parentNode;
                lastDeletedNextSibling = footerRow?.nextSibling || null;

                dataRows.concat(footerRow).forEach(tr => {
                    lastDeleted.appendChild(tr.cloneNode(true));
                    tr.remove();
                });
            } else {
                lastDeleted = rowToDelete.cloneNode(true);
                lastDeletedParent = rowToDelete.parentNode;
                lastDeletedNextSibling = rowToDelete.nextSibling;
                rowToDelete.remove();
            }

            rowToDelete = null;
        }

        const modalEl = document.getElementById('deleteModal');
        const modal = bootstrap.Modal.getInstance(modalEl);
        if (modal)
            modal.hide();
    });
}

/**
 * Xử lý form thêm mới thực hành.
 * Khi submit thành công sẽ ẩn modal và reset form.
 */
document.addEventListener('DOMContentLoaded', function () {
    const addPracticeForm = document.getElementById('addPracticeForm');
    if (addPracticeForm) {
        addPracticeForm.addEventListener('submit', function (e) {
            e.preventDefault();
            showNotification('Practice added successfully!', 'success');
            const addModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('addPracticeModal'));
            addModal.hide();
            addPracticeForm.reset();
        });
    }

    /**
     * Xử lý khi nhấn xác nhận xóa — hiển thị toast + cho phép undo trong 3 giây.
     * Nếu không undo, gọi API để xóa bản ghi khỏi DB.
     */
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', function () {
            const undoHtml = `
                    <div class="d-flex align-items-center">
                        <div class="flex-grow-1">
                            <span>Item deleted.</span>
                        </div>
                        <button type="button" class="btn btn-outline-light btn-sm ms-3 px-3 rounded-pill fw-bold" id="undoBtn" style="box-shadow:none;">
                            <i class="bi bi-arrow-counterclockwise me-1"></i>Undo
                        </button>
                    </div>
                `;
            showNotification(undoHtml, "success");

            setTimeout(() => {
                const container = document.getElementById('notification-container');
                const toasts = container.querySelectorAll('.toast-notification');
                const toast = toasts[toasts.length - 1];
                if (!toast)
                    return;
                const undoBtn = toast.querySelector('#undoBtn');
                let undone = false;

                /**
                 * Gửi yêu cầu DELETE thực tế đến server nếu không undo.
                 */
                function doDeleteAPI() {
                    if (!undone && lastDeletedPracticeId) {
                        fetch(href, {
                            method: 'DELETE',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({id: recordId})
                        })
                                .then(response => response.text())
                                .then(() => {
                                    location.href = href;
                                })
                                .catch(err => console.log(err));
                        lastDeletedPracticeId = null;
                    }
                }

                // Tự động ẩn toast sau 3s và gọi API nếu không undo
                undoTimeout = setTimeout(() => {
                    toast.classList.remove('show');
                    setTimeout(() => {
                        toast.remove();
                        doDeleteAPI();
                    }, 500);
                }, 3000);

                if (undoBtn) {
                    undoBtn.addEventListener('click', function (e) {
                        e.preventDefault();
                        undone = true;
                        if (undoTimeout)
                            clearTimeout(undoTimeout);
                        if (lastDeleted && lastDeletedParent) {
                            if (lastDeletedNextSibling) {
                                lastDeletedParent.insertBefore(lastDeleted, lastDeletedNextSibling);
                            } else {
                                lastDeletedParent.appendChild(lastDeleted);
                            }

                            attachDeleteEvents();

                            lastDeleted = null;
                            lastDeletedParent = null;
                            lastDeletedNextSibling = null;
                            toast.classList.remove('show');
                            setTimeout(() => {
                                toast.remove();
                            }, 500);
                            showNotification("Restored successfully!", "success");
                        }
                        lastDeletedPracticeId = null;
                    });
                }
            }, 20);
        });
    }
});

/**
 * Mở modal thêm thực hành khi nhấn vào nút "Add New".
 */
document.querySelector('.btn-success').addEventListener('click', function (e) {
    if (this.innerText.includes('Add New')) {
        e.preventDefault();
        let modal = new bootstrap.Modal(document.getElementById('addPracticeModal'));
        modal.show();
    }
});

const getHeaderTitle = async () => {
    try {
        const response = await fetch(href, {
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
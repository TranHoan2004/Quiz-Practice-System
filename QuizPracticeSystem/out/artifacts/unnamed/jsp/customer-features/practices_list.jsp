<%-- 
    Document   : practices_list
    Created on : May 19, 2025, 4:26:56 PM
    Author     : TranHoan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>Practices List</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">
    <link href="img/favicon.ico" rel="icon">
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
    <link href="${pageContext.request.contextPath}/css/lib/css2.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/fontawesome/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/practicelist.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
</head>

<body>
<jsp:include page="../../component/spinner.html"/>
<jsp:include page="../../component/header.jsp"/>

<!-- Courses Start -->
<div class="container-xxl py-5">
    <div class="container">
        <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
            <h6 class="section-title bg-white text-center text-primary px-3">Practice</h6>
            <h1 class="mb-3">Practices List</h1>
        </div>

        <div class="row wow fadeInUp" data-wow-delay="0.2s">
            <!-- Sidebar bên trái -->
            <div class="col-md-2 mb-3">
                <div class="d-flex flex-column gap-3">
                    <!-- Search -->
                    <form action="${pageContext.request.contextPath}/user/practice" method="get"
                          class="service-item p-3 rounded shadow mt-2 bg-white">
                        <label for="practiceSearcher" class="form-label mb-2">
                                    <span class="fw-semibold text-primary">
                                        <i class="bi bi-search me-1"></i>Search
                                    </span>
                        </label>
                        <div class="input-group input-group-sm">
                            <input type="text" id="practiceSearcher" name="keyword" class="form-control"
                                   placeholder="Search...">
                            <button class="btn btn-primary" type="submit" id="searchBtn">
                                <i class="bi bi-search"></i>
                            </button>
                        </div>
                    </form>

                    <!-- Subject Filter -->
                    <div class="service-item p-3 rounded shadow mt-2 bg-white">
                        <label for="subjectFilter" class="form-label mb-2">
                                    <span class="fw-semibold text-primary">
                                        <i class="bi bi-filter me-1"></i>Subject
                                    </span>
                        </label>
                        <select class="form-select form-select-sm" id="subjectFilter">
                            <option value="" disabled selected hidden>-- Select Subject --</option>
                            <option value="all" default>All Subjects</option>
                            <c:if test="${subjects ne null}">
                                <c:forEach items="${subjects}" var="s">
                                    <option value="${s.getName().toLowerCase()}">${s.getName()}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>

                    <!-- Buttons -->
                    <div class="service-item p-3 rounded shadow mt-2 bg-white d-flex flex-column gap-2">
                        <button type="button" class="btn btn-success btn-sm w-100">
                            <i class="bi bi-plus-circle me-1 text-white"></i> Add New
                        </button>
                        <button type="button" class="btn btn-info btn-sm w-100">
                            <i class="bi bi-lightning-charge-fill me-1 text-white"></i> Simulation Exam
                        </button>
                    </div>
                </div>
            </div>

            <!-- Bảng dữ liệu bên phải -->
            <div class="col-md-10">
                <div class="table-responsive mb-0">
                    <table class="table table-bordered align-middle text-center mb-0" id="practiceTable">
                        <thead class="table-primary">
                        <tr>
                            <th>#</th>
                            <th>Subject Name</th>
                            <th>Exam Name</th>
                            <th>Date Taken</th>
                            <th>Questions</th>
                            <th>Rate Of Correct Questions</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${exams ne null}">
                            <c:forEach items="${exams}" var="i" varStatus="loop">
                                <c:set var="correct" value="${i.getNumberOfCorrectQuestions()}"/>
                                <c:set var="total" value="${i.getNumberOfQuestions()}"/>
                                <tr data-section="${i.getId()}">
                                    <td>${loop.count}</td>
                                    <td>${i.getSubjectName()}</td>
                                    <td>${i.getExamName()}</td>
                                    <td>${i.getDateTaken()}</td>
                                    <td style="text-align: center; vertical-align: middle; padding: 10px;">
                                        <div>
                                            <div>${correct} correct</div>
                                            <div>${total} questions</div>
                                        </div>
                                    </td>
                                    <td>${correct / total * 100}% Correct</td>
                                    <td>
                                        <button class="btn btn-info btn-sm me-1">
                                            <i class="bi bi-eye"></i>
                                            View
                                        </button>
                                        <button class="btn btn-danger btn-sm delete-btn">
                                            <i class="bi bi-trash"></i>
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                                <tr data-section="${i.getId()}" class="section-footer">
                                    <td colspan="7">
                                        <div class="d-flex">
                                            <div>${i.getMoreInformation()}</div>
                                            <div class="ms-auto">Duration: ${i.getDuration()}</div>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <c:if test="${exams == null}">
                            <tr class="text-danger">
                                <td colspan="7">No results</td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>

                <div class="card-footer d-flex justify-content-between align-items-center">
                    <c:if test="${exams ne null}">
                        <c:set value="${exams.size()}" var="n"/>
                        <c:set value="${totalElements}" var="a"/>
                        <span>Showing 1 to ${n} of ${a} entries</span>
                        <nav aria-label="Practice pagination">
                            <ul class="pagination mb-0" id="pagination"
                                style="font-size: 1rem; --bs-pagination-active-bg: #0d6efd; --bs-pagination-active-border-color: #0d6efd;">

                                <li class="page-item ${currentIndex == 1 ? "disabled" : ''}">
                                    <a class="page-link rounded-pill px-3"
                                       href="${pageContext.request.contextPath}/user/practice?page=${currentIndex - 1}">
                                        <i class="bi bi-chevron-left"></i> Prev
                                    </a>
                                </li>
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${i == currentIndex ? "active" : ''}">
                                        <a class="page-link rounded-pill px-3"
                                           href="${pageContext.request.contextPath}/user/practice?page=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${(currentIndex == totalPages || totalPages == 0.0) ? "disabled" : ''}">
                                    <a class="page-link rounded-pill px-3"
                                       href="${pageContext.request.contextPath}/user/practice?page=${currentIndex + 1}">
                                        Next <i class="bi bi-chevron-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Add New Practice Modal -->
<div class="modal fade" id="addPracticeModal" tabindex="-1" aria-labelledby="addPracticeModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title text-white" id="addPracticeModalLabel">
                    <i class="bi bi-plus-circle me-2"></i>
                    Add New Practice
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <form id="addPracticeForm"></form>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="practiceSubject" class="form-label">Subject</label>
                    <select class="form-select" id="practiceSubject" required>
                        <option value="" disabled selected hidden>-- Select Subject --</option>
                        <option value="all" default>All Subjects</option>
                        <c:if test="${subjects ne null}">
                            <c:forEach items="${subjects}" var="s">
                                <option value="${s.getName().toLowerCase()}">${s.getName()}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="numQuestions" class="form-label">Number of Questions</label>
                    <input type="number" class="form-control" id="numQuestions" min="1" required>
                </div>
                <div class="mb-3">
                    <label for="topicSelect" class="form-label">Select Subject by Topic</label>
                    <select class="form-select" id="topicSelect" required>
                        <option value="" disabled selected hidden>-- Select --</option>
                        <option value="topic">By subject topic</option>
                        <option value="dimension">By dimension</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="questionGroup" class="form-label">Question Group</label>
                    <select class="form-select" id="questionGroup" required>
                        <option value="" disabled selected hidden>-- Select --</option>
                        <option value="all">All</option>
                        <option value="topics">All topics</option>
                        <option value="dimensions">All dimensions</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer justify-content-center">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                    <i class="bi bi-x-circle me-1"></i>Cancel
                </button>
                <button type="submit" class="btn btn-success">
                    <i class="bi bi-check-circle me-1"></i>Practice
                </button>
            </div>
            </form>
        </div>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title text-white" id="deleteModalLabel">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    Confirm Delete
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body text-center">
                <i class="bi bi-trash display-4 text-danger mb-3"></i>
                <p class="mb-0">Are you sure you want to delete this practice?</p>
            </div>
            <div class="modal-footer justify-content-center">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                    <i class="bi bi-x-circle me-1"></i>Cancel
                </button>
                <button type="button" class="btn btn-danger" id="confirmDeleteBtn">
                    <i class="bi bi-check-circle me-1"></i>OK
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../component/notification.html"/>
<jsp:include page="../../component/footer.html"/>
<jsp:include page="../../component/back_to_top.html"/>

<script>
    document.getElementById('subjectFilter').addEventListener('change', function () {
        const value = this.value;
        let href = `${pageContext.request.contextPath}/user/practice`
        if (value !== 'all') {
            href += '?filter=' + value;
        }
        window.location.href = href;
    })

    let recordId = ''
    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll(".delete-btn").forEach(button => {
            button.addEventListener("click", function () {
                // Lấy ID của bản ghi bằng cách tìm phần tử cha chứa ID
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

    function attachDeleteEvents() {
        document.querySelectorAll('.delete-btn').forEach(btn => {
            btn.removeEventListener('click', deleteHandler);
            btn.addEventListener('click', deleteHandler);
        });
    }

    function deleteHandler(e) {
        rowToDelete = e.target.closest('tr');
        const modalEl = document.getElementById('deleteModal');
        if (modalEl) {
            const modal = new bootstrap.Modal(modalEl);
            modal.show();
        }
    }

    attachDeleteEvents();

    // Mở modal xác nhận xóa, đồng thời lấy tất cả các dòng trong bảng có chung data-section
    // Nếu nhấn nút xóa thì dòng đó sẽ bị loại bỏ
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
            if (modal) modal.hide();
        });
    }

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

        // Mở thông báo đã xóa thành công, có yêu cầu redo nếu cần
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
                    if (!toast) return;
                    const undoBtn = toast.querySelector('#undoBtn');
                    let undone = false;

                    function doDeleteAPI() {
                        if (!undone && lastDeletedPracticeId) {
                            fetch(`${pageContext.request.contextPath}/user/practice`, {
                                method: 'DELETE',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify({id: recordId})
                            })
                                .then(response => response.text())
                                .then(() => {
                                    location.href = `${pageContext.request.contextPath}/user/practice`
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
                            if (undoTimeout) clearTimeout(undoTimeout);
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

    document.querySelector('.btn-success').addEventListener('click', function (e) {
        if (this.innerText.includes('Add New')) {
            e.preventDefault();
            let modal = new bootstrap.Modal(document.getElementById('addPracticeModal'));
            modal.show();
        }
    });
</script>
<script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="${pageContext.request.contextPath}/js/Notification.js"></script>
</body>

</html>

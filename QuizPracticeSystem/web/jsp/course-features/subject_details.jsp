<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Subject Details</title>
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
        <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
    </head>

    <body>
        <jsp:include page="../../component/component.subject/header.jsp"/>
        <jsp:include page="../../component/component.subject/header.html"/>

        <!-- Courses Start -->
        <div class="container-xxl py-5">
            <div class="container">
                <div class="row">
                    <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                        <h6 class="section-title bg-white text-center text-primary px-3">Subjects</h6>
                        <h1 class="mb-3">Subjects Details</h1>
                    </div>
                    <div class="col-12 wow fadeInUp">
                        <ul class="nav nav-tabs mb-4" id="subjectDetailsTabs" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active" id="overview-tab" data-bs-toggle="tab"
                                        data-bs-target="#overview" type="button" role="tab" aria-controls="overview"
                                        aria-selected="true">
                                    Overview
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="dimension-tab" data-bs-toggle="tab" data-bs-target="#dimension"
                                        type="button" role="tab" aria-controls="dimension" aria-selected="false">
                                    Dimension
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="price-tab" data-bs-toggle="tab" data-bs-target="#price"
                                        type="button" role="tab" aria-controls="price" aria-selected="false">
                                    Price Package
                                </button>
                            </li>
                        </ul>
                        <div class="tab-content" id="subjectDetailsTabsContent">
                            <!-- Overview Tab -->
                            <div class="tab-pane fade show active" id="overview" role="tabpanel"
                                 aria-labelledby="overview-tab">
                                <div class="card mb-4">
                                    <div class="card-header bg-primary text-white">
                                        Subject General Information
                                    </div>
                                    <div class="card-body">
                                        <div class="row">
                                            <!-- Left: Info -->
                                            <div class="col-md-8">
                                                <form>
                                                    <!-- Subject Name -->
                                                    <div class="row mb-3">
                                                        <label class="col-sm-4 col-form-label">Subject Name</label>
                                                        <div class="col-sm-8">
                                                            <input type="text" class="form-control" value="${subjectDetail.name}">
                                                        </div>
                                                    </div>

                                                    <!-- Category -->
                                                    <div class="row mb-3">
                                                        <label class="col-sm-4 col-form-label">Category</label>
                                                        <div class="col-sm-8">
                                                            <select class="form-select" title="Category" >
                                                                <option selected>${subjectDetail.category}</option>
                                                            </select>
                                                        </div>
                                                    </div>

                                                    <!-- Featured Subject + Status -->
                                                    <div class="row mb-3 align-items-center">
                                                        <label class="col-sm-4 col-form-label">Featured Subject</label>
                                                        <div class="col-sm-2">
                                                            <input class="form-check-input" type="checkbox" id="featured" 
                                                                   ${subjectDetail.featured ? "checked" : ""}>
                                                        </div>
                                                        <label class="col-sm-2 col-form-label text-end">Status</label>
                                                        <div class="col-sm-4">
                                                            <select class="form-select" id="subject-status" >
                                                                <option ${subjectDetail.published ? "selected" : ""}>Published</option>
                                                                <option ${!subjectDetail.published ? "selected" : ""}>Unpublished</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>

                                            <!-- Right: Subject Image -->
                                            <div class="col-md-4 d-flex align-items-start justify-content-center">
                                                <img src="${subjectDetail.thumbnailUrl}" alt="Subject Image" class="img-fluid rounded shadow-sm"
                                                     style="max-width: 250px;">
                                            </div>
                                        </div>

                                        <!-- Description -->
                                        <div class="row mt-4">
                                            <div class="col-12">
                                                <label class="form-label fw-bold">Description</label>
                                                <textarea class="form-control" rows="4" >${subjectDetail.description}</textarea>
                                            </div>
                                        </div>

                                        <!-- Button -->
                                        <div class="row mt-3">
                                            <div class="col-12 text-end">
                                                <button type="submit" class="btn btn-success me-2">Submit</button>
                                                <button type="button" class="btn btn-secondary" onclick="history.back()">Back</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Dimension Tab -->
                            <div class="tab-pane fade" id="dimension" role="tabpanel" aria-labelledby="dimension-tab">
                                <div class="card mb-4">
                                    <div class="card-header bg-primary text-white">
                                        Subject Dimensions
                                    </div>
                                    <div class="card-body">
                                        <table class="table table-bordered align-middle">
                                            <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Type</th>
                                                    <th>Dimension</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody id="dimension-table-body"></tbody>
                                            <tbody>
                                                <c:forEach var="d" items="${subjectDetail.dimensions}" varStatus="loop">
                                                    <tr>
                                                        <td>${loop.index + 1}</td>
                                                        <td>${d.name}</td> 
                                                        <td>${d.description}</td>
                                                        <td>
                                                            <button type="button"
                                                                    class="btn btn-sm btn-outline-danger open-delete-modal"
                                                                    data-id="${d.id}"
                                                                    data-type="dimension"
                                                                    data-subject="${subjectDetail.id}"
                                                                    data-bs-toggle="modal"
                                                                    data-bs-target="#confirmDeleteModal">
                                                                <i class="fas fa-trash"></i> Delete
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                        <button class="btn btn-success" data-bs-toggle="modal"
                                                data-bs-target="#addDimensionModal">
                                            <i class="fas fa-plus me-1"></i>Add Dimension
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <!-- Price Package Tab -->
                            <div class="tab-pane fade" id="price" role="tabpanel" aria-labelledby="price-tab">
                                <div class="card mb-4">
                                    <div class="card-header bg-primary text-white">
                                        Price Packages
                                    </div>
                                    <div class="card-body">
                                        <table class="table table-bordered align-middle">
                                            <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Package Name</th>
                                                    <th>Duration</th>
                                                    <th>List Price</th>
                                                    <th>Sale Price</th>
                                                    <th>Status</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody id="price-table-body"></tbody>
                                            <tbody>
                                                <c:forEach var="p" items="${subjectDetail.pricePackages}" varStatus="loop">
                                                    <tr>
                                                        <td>${loop.index + 1}</td>
                                                        <td>
                                                            <a href="${pageContext.request.contextPath}/user/subject_lesson?id=${p.courseId}" 
                                                               class="text-decoration-none text-primary d-block w-100 h-100">
                                                                ${p.title}
                                                            </a>
                                                        </td>
                                                        <td>${p.accessDuration} Month(s)</td>
                                                        <td>$${p.price}</td>
                                                        <td>$${p.salePrice}</td>
                                                        <td>
                                                            <span class="badge ${p.status ? 'bg-success' : 'bg-secondary'}">
                                                                ${p.status ? 'Active' : 'Inactive'}
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <button type="button"
                                                                    class="btn btn-sm btn-outline-primary open-edit-price"
                                                                    data-id="${p.id}"
                                                                    data-title="${p.title}"
                                                                    data-duration="${p.accessDuration}"
                                                                    data-price="${p.price}"
                                                                    data-sale="${p.salePrice}"
                                                                    data-status="${p.status}"
                                                                    data-course="${p.courseId}"
                                                                    data-bs-toggle="modal"
                                                                    data-bs-target="#editPricePackageModal">
                                                                <i class="fas fa-edit"></i> Edit
                                                            </button>
                                                            <button type="button"
                                                                    class="btn btn-sm btn-outline-danger open-delete-modal"
                                                                    data-id="${p.id}"
                                                                    data-type="pricePackage"
                                                                    data-subject="${subjectDetail.id}"
                                                                    data-bs-toggle="modal"
                                                                    data-bs-target="#confirmDeleteModal">
                                                                <i class="fas fa-trash"></i> Delete
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                        <button class="btn btn-success" data-bs-toggle="modal"
                                                data-bs-target="#addPricePackageModal">
                                            <i class="fas fa-plus me-1"></i>Add Price Package
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="../../component/footer.html"/>
        <jsp:include page="../../component/back_to_top.html"/>
        <jsp:include page="../../component/notification.html"/>

        <!-- Confirm Delete Modal -->
        <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <form method="post" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmDeleteModalLabel">Confirm Delete</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this item?
                    </div>
                    <div class="modal-footer">
                        <input type="hidden" name="deleteId" id="modalDeleteId">
                        <input type="hidden" name="deleteType" id="modalDeleteType">
                        <input type="hidden" name="subjectId" id="modalSubjectId">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Edit Price Package -->
        <div class="modal fade" id="editPricePackageModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <form class="modal-content" method="post" action="${pageContext.request.contextPath}/user/subject_detail">
                    <input type="hidden" name="action" value="editPricePackage">
                    <input type="hidden" name="packageId" id="editPackageId">
                    <input type="hidden" name="courseId" id="editCourseId">
                    <input type="hidden" name="id" value="${subjectDetail.id}">

                    <div class="modal-header">
                        <h5 class="modal-title">Edit Price Package</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label>Package Name</label>
                            <input type="text" class="form-control" name="packageName" id="editPackageName" readonly>
                        </div>
                        <div class="mb-3">
                            <label>Duration</label>
                            <input type="number" class="form-control" name="packageDuration" id="editPackageDuration" required>
                        </div>
                        <div class="mb-3">
                            <label>List Price</label>
                            <input type="number" class="form-control" name="packagePrice" id="editPackagePrice" required>
                        </div>
                        <div class="mb-3">
                            <label>Sale Price</label>
                            <input type="number" class="form-control" name="packageSalePrice" id="editPackageSale" required>
                        </div>
                        <div class="mb-3">
                            <label>Status</label>
                            <select name="packageStatus" class="form-select" id="editPackageStatus" required>
                                <option value="true">Active</option>
                                <option value="false">Inactive</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                    </div>
                </form>
            </div>
        </div>


        <!-- Add Dimension Modal -->
        <div class="modal fade" id="addDimensionModal" tabindex="-1" aria-labelledby="addDimensionModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <form class="modal-content" method="post" action="${pageContext.request.contextPath}/user/subject_detail">
                    <!-- Xác định action để servlet biết xử lý gì -->

                    <input type="hidden" name="action" value="addDimension">

                    <!-- ID của subject (đã encode) -->
                    <input type="hidden" name="subjectId" value="${subjectDetail.id}">

                    <div class="modal-header">
                        <h5 class="modal-title" id="addDimensionModalLabel">Add Dimension</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body">
                        <!-- Dimension Name -->
                        <div class="mb-3">
                            <label for="dimensionName" class="form-label">Dimension Name</label>
                            <input type="text" class="form-control" id="dimensionName" name="dimensionName" required>
                        </div>

                        <!-- Description -->
                        <div class="mb-3">
                            <label for="dimensionDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="dimensionDescription" name="dimensionDescription" rows="3" required></textarea>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Add</button>
                    </div>
                </form>
            </div>
        </div>



        <!-- Add Price  --> 
        <div class="modal fade" id="addPricePackageModal" tabindex="-1" aria-labelledby="addPricePackageModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <form class="modal-content" method="post" action="${pageContext.request.contextPath}/user/subject_detail">
                    <input type="hidden" name="action" value="addPricePackage">
                    <input type="hidden" name="courseId" value="${subjectDetail.courseId}">
                    <input type="hidden" name="id" value="${subjectDetail.id}">

                    <div class="modal-header">
                        <h5 class="modal-title" id="addPricePackageModalLabel">Add Price Package</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label">Package Name</label>
                            <input type="text" class="form-control" name="packageName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Duration (months)</label>
                            <input type="number" class="form-control" name="packageDuration" min="1" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">List Price</label>
                            <input type="number" class="form-control" name="packagePrice" min="0" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Sale Price</label>
                            <input type="number" class="form-control" name="packageSalePrice" min="0" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Status</label>
                            <select class="form-select" name="packageStatus" required>
                                <option value="true">Active</option>
                                <option value="false">Inactive</option>
                            </select>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Add</button>
                    </div>
                </form>
            </div>
        </div>

        

        <script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
        <script src="${pageContext.request.contextPath}/js/Notification.js"></script>
        <script>
                                                    document.addEventListener('DOMContentLoaded', function () {
                                                        let rowToDelete = null;
                                                        let lastDeleted = null;
                                                        let lastDeletedParent = null;
                                                        let lastDeletedNextSibling = null;

                                                        const confirmDeleteModalEl = document.getElementById('confirmDeleteModal');
                                                        const confirmDeleteModal = new bootstrap.Modal(confirmDeleteModalEl);
                                                        const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

                                                        document.body.addEventListener('click', function (e) {
                                                            if (e.target.closest('.btn-delete-row')) {
                                                                rowToDelete = e.target.closest('tr');
                                                                confirmDeleteModal.show();
                                                            }
                                                        });

                                                        confirmDeleteBtn.addEventListener('click', function () {
                                                            if (rowToDelete) {
                                                                lastDeleted = rowToDelete.cloneNode(true);
                                                                lastDeletedParent = rowToDelete.parentNode;
                                                                lastDeletedNextSibling = rowToDelete.nextSibling;

                                                                rowToDelete.remove();
                                                                confirmDeleteModal.hide();
                                                                showUndoNotification();
                                                                rowToDelete = null;
                                                            }
                                                        });

                                                        function showUndoNotification() {
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
                                                            showNotification(undoHtml, "danger");

                                                            setTimeout(() => {
                                                                const container = document.getElementById('notification-container');
                                                                const toasts = container.querySelectorAll('.toast-notification');
                                                                const toast = toasts[toasts.length - 1];
                                                                if (!toast)
                                                                    return;
                                                                const undoBtn = toast.querySelector('#undoBtn');
                                                                if (undoBtn) {
                                                                    undoBtn.addEventListener('click', function (e) {
                                                                        e.preventDefault();
                                                                        if (lastDeleted && lastDeletedParent) {
                                                                            if (lastDeletedNextSibling) {
                                                                                lastDeletedParent.insertBefore(lastDeleted, lastDeletedNextSibling);
                                                                            } else {
                                                                                lastDeletedParent.appendChild(lastDeleted);
                                                                            }
                                                                            lastDeleted = null;
                                                                            lastDeletedParent = null;
                                                                            lastDeletedNextSibling = null;
                                                                            toast.classList.remove('show');
                                                                            setTimeout(() => {
                                                                                toast.remove();
                                                                            }, 500);
                                                                            showNotification("Restored successfully!", "success");
                                                                        }
                                                                    });
                                                                }
                                                            }, 20);
                                                        }
                                                    });

                                                    document.addEventListener('DOMContentLoaded', function () {
                                                        const deleteButtons = document.querySelectorAll('.open-delete-modal');
                                                        deleteButtons.forEach(btn => {
                                                            btn.addEventListener('click', function () {
                                                                document.getElementById('modalDeleteId').value = this.dataset.id;
                                                                document.getElementById('modalDeleteType').value = this.dataset.type;
                                                                document.getElementById('modalSubjectId').value = this.dataset.subject;
                                                            });
                                                        });
                                                    });

                                                    document.querySelectorAll('.open-edit-price').forEach(btn => {
                                                        btn.addEventListener('click', function () {
                                                            document.getElementById('editPackageId').value = this.dataset.id;
                                                            document.getElementById('editCourseId').value = this.dataset.course;
                                                            document.getElementById('editPackageName').value = this.dataset.title;
                                                            document.getElementById('editPackageDuration').value = this.dataset.duration;
                                                            document.getElementById('editPackagePrice').value = this.dataset.price;
                                                            document.getElementById('editPackageSale').value = this.dataset.sale;
                                                            document.getElementById('editPackageStatus').value = this.dataset.status;
                                                        });
                                                    });

        </script>
    </body>

</html>
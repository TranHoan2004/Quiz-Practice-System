<%--
    Document   : subjects_list
    Created on : May 19, 2025, 4:38:34 PM
    Author     : TranHoan
--%>

<%--<%@page import="java.util.List"%>--%>
<%--<%@page import="model.Topic"%>--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>Sliders List</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">
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
    <style>
        .card-text {
            font-size: 0.95rem;
            display: inline-block;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    </style>
</head>

<body>
<jsp:include page="../../component/spinner.html"/>
<jsp:include page="../../component/navbar.jsp"/>
<jsp:include page="../../component/header.html"/>

<!-- Courses Start -->
<div class="container-xxl py-5">
    <div class="container">
        <div class="row">
            <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                <h6 class="section-title bg-white text-center text-primary px-3">Marketing</h6>
                <h1 class="mb-3">Sliders List</h1>
            </div>

            <div class="col-12">
                <div class="d-flex justify-content-end mb-3">
                    <button class="btn btn-success" onclick="addSlider()">
                        <i class="fas fa-plus me-1"></i> Add new
                    </button>
                </div>
            </div>

            <!-- Slider List Start -->
            <div class="col-12">
                <div class="row">
                    <!-- Sidebar Start -->
                    <aside class="col-lg-3 mb-4 mb-lg-0">
                        <div class="card mb-4 shadow-sm p-2" style="border-radius: 12px;">
                            <div class="card-body">
                                <!-- Filter -->
                                <div class="mb-4">
                                    <label for="statusFilterSelect"
                                           class="form-label fw-semibold text-primary-emphasis">
                                        <i class="bi bi-filter me-1"></i>
                                        <span style="color: #1a237e;">Filter by Status</span>
                                    </label>
                                    <select class="form-select" id="statusFilterSelect">
                                        <option value="all">All Status</option>
                                        <option value="active">Active</option>
                                        <option value="inactive">Inactive</option>
                                    </select>
                                </div>

                                <!-- Search -->
                                <form class="mb-4" id="sliderSearchForm">
                                    <label for="sliderSearch" class="form-label fw-semibold text-primary-emphasis">
                                        <i class="bi bi-search me-1"></i>
                                        <span style="color: #1a237e;">Search</span>
                                    </label>
                                    <div class="input-group input-group-sm">
                                        <input type="text" id="sliderSearch" class="form-control"
                                               placeholder="Search..." style="color: #212529;">
                                        <button class="btn btn-primary" type="submit"><i
                                                class="bi bi-search"></i></button>
                                    </div>
                                </form>

                                <!-- Setting -->
                                <div class="mb-4">
                                    <label class="form-label fw-semibold text-primary-emphasis">
                                        <i class="bi bi-gear me-1"></i>
                                        <span style="color: #1a237e;">Setting</span>
                                    </label>
                                    <div class="mb-2">
                                        <input type="number" value="" class="w-50"
                                               id="settingOption1" min="2">
                                        <label class="form-check-label" for="settingOption1">
                                            Chỉnh cỡ bảng
                                        </label>
                                    </div>
                                    <div class="form-check mb-2 form-switch">
                                        <input class="form-check-input" type="checkbox" id="settingOption2">
                                        <label class="form-check-label" for="settingOption2">
                                            Ẩn ảnh
                                        </label>
                                    </div>
                                    <div class="form-check mb-2 form-switch">
                                        <input class="form-check-input" type="checkbox" id="settingOption4">
                                        <label class="form-check-label" for="settingOption4">
                                            Ẩn trạng thái
                                        </label>
                                    </div>
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" id="settingOption3">
                                        <label class="form-check-label" for="settingOption3">
                                            Hiện người tạo
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </aside>

                    <div class="col-lg-9">
                        <div class="row g-4" id="sliderList"></div>

                        <!-- No Results Message -->
                        <div id="noResultsMessage" class="text-center text-muted py-5"
                             style="display:none; font-size:1.2rem;">
                            Không có kết quả
                        </div>

                        <!-- Pagination Start -->
                        <div id="pag">
                            <div class="card-footer d-flex justify-content-between align-items-center">
                                <span id="message">Showing 1 to 10 entries</span>
                                <nav aria-label="Course pagination">
                                    <ul class="pagination mb-0" id="pagination"
                                        style="font-size: 1rem; --bs-pagination-active-bg: #0d6efd; --bs-pagination-active-border-color: #0d6efd;"></ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Theater-style Image Preview Modal -->
<div class="modal fade" id="sliderImageModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-xl">
        <div class="modal-content bg-dark border-0" style="background-color: rgba(0, 0, 0, 0.95);">
            <div class="modal-body p-0 position-relative">
                <!-- Close Button -->
                <button type="button" class="btn-close btn-close-white position-absolute top-0 end-0 m-3"
                        data-bs-dismiss="modal" aria-label="Close"></button>

                <!-- Image -->
                <img id="theatreImage" src="" alt="Slider Image"
                     class="w-100" style="max-height: 90vh; object-fit: contain; border-radius: 8px;">
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../component/notification.html"/>
<jsp:include page="../../component/footer.html"/>
<jsp:include page="../../component/back_to_top.html"/>
<script>
    window.contextPath = `${pageContext.request.contextPath}/slider`;
    let sliders = [];
    let displayFilterSliders = [];
    let itemsPerPage = 8;
    let currentPage = 1;
    let filteredSliders;
    let imgHidden = false;
    let authorHidden = true;
</script>
<script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="${pageContext.request.contextPath}/js/Notification.js"></script>
<script src="${pageContext.request.contextPath}/js/SubjectsList.js" type="module"></script>
</body>

</html>
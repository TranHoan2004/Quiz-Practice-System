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
    <title>Subjects List</title>
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
</head>

<body>
<jsp:include page="../../component/spinner.html"/>
<jsp:include page="../../component/navbar.html"/>
<jsp:include page="../../component/header.jsp"/>

<!-- Courses Start -->
<div class="container-xxl py-5">
    <div class="container">
        <div class="row">
            <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                <h6 class="section-title bg-white text-center text-primary px-3">Subjects</h6>
                <h1 class="mb-3">Subjects List</h1>
            </div>

            <!-- Sidebar Start -->
            <aside class="col-lg-3 mb-4 mb-lg-0">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <form class="mb-3" id="subjectSearchForm"></form>
                        <label for="subjectSearch" class="form-label fw-semibold text-primary-emphasis">
                            <i class="bi bi-search me-1"></i>
                            <span style="color: #1a237e;">Tìm kiếm chủ đề</span>
                        </label>
                        <div class="input-group input-group-sm">
                            <input type="text" id="subjectSearch" class="form-control"
                                   placeholder="Nhập tên chủ đề..." style="color: #212529;">
                            <button class="btn btn-primary" type="submit"><i class="bi bi-search"></i></button>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-semibold text-primary-emphasis">
                                <i class="bi bi-filter me-1"></i>
                                <span style="color: #1a237e;">Danh mục chủ đề</span>
                            </label>
                            <ul class="list-group list-group-flush" id="categoryFilter">
                                <li class="list-group-item"><a href="#" class="text-decoration-none category-link"
                                                               data-category="all" style="color: #333;">Tất cả</a></li>
                                <li class="list-group-item"><a href="#" class="text-decoration-none category-link"
                                                               data-category="Lập trình Web" style="color: #333;">Lập
                                    trình Web</a></li>
                                <li class="list-group-item"><a href="#" class="text-decoration-none category-link"
                                                               data-category="Thiết kế" style="color: #333;">Thiết
                                    kế</a></li>
                                <li class="list-group-item"><a href="#" class="text-decoration-none category-link"
                                                               data-category="Marketing"
                                                               style="color: #333;">Marketing</a></li>
                                <li class="list-group-item"><a href="#" class="text-decoration-none category-link"
                                                               data-category="Kinh doanh" style="color: #333;">Kinh
                                    doanh</a></li>
                                <li class="list-group-item"><a href="#" class="text-decoration-none category-link"
                                                               data-category="Khác" style="color: #333;">Khác</a></li>
                            </ul>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-semibold text-primary-emphasis">
                                <i class="bi bi-star-fill me-1"></i>
                                <span style="color: #1a237e;">Chủ đề tiêu biểu</span>
                            </label>
                            <ul class="list-unstyled">
                                <li class="mb-2">
                                    <a href="subject-detail.html"
                                       class="d-flex align-items-center text-decoration-none"
                                       style="color: #212529;">
                                        <img src="../../img/course-1.jpg" alt="Featured 1" class="rounded me-2"
                                             width="40"
                                             height="40">
                                        <span>Lập trình Python cơ bản</span>
                                    </a>
                                </li>
                                <li class="mb-2">
                                    <a href="subject-detail.html"
                                       class="d-flex align-items-center text-decoration-none"
                                       style="color: #212529;">
                                        <img src="../../img/course-2.jpg" alt="Featured 2" class="rounded me-2"
                                             width="40"
                                             height="40">
                                        <span>Thiết kế UI/UX</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="subject-detail.html"
                                       class="d-flex align-items-center text-decoration-none"
                                       style="color: #212529;">
                                        <img src="../../img/course-3.jpg" alt="Featured 3" class="rounded me-2"
                                             width="40"
                                             height="40">
                                        <span>Digital Marketing</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </aside>

            <!-- Subjects List Start -->
            <section class="col-lg-8">
                <div class="row g-4" id="subjectList">
                        <%
//                                List<model.Topic> topics = (List<model.Topic>) request.getAttribute("topics");
//                                if (topics.isEmpty()) {
                            %>
                    <%--                            <h3 class="text-center">No results</h3>--%>
                        <%
//                            } else {
//                                for (model.Topic t : topics) {
                            %>
                    <div class="col-md-6 wow fadeInUp subject-card" data-wow-delay="0.1s"
                    <%--                                 data-type="<%=t.getType()%>" data-dimension="<%=t.getDimension()%>"--%>
                    >
                        <div class="card h-100 shadow-sm">
                            <a href="subject-detail.html">
                                <img src="../../img/cat-1.jpg" class="card-img-top" alt="Subject 1" width="100%"
                                     height="200px">
                            </a>
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title">
                                    <a href="subject-detail.html" class="text-decoration-none"
                                       style="color: #1a237e;">
                                        <%--                                                <%=t.getType()%>--%>
                                    </a>
                                </h5>
                                <p class="card-text flex-grow-1" style="color: #333;">
                                    <%--                                            <%=t.getDimension()%>--%>
                                </p>
                                <div class="mt-2">
                                            <span class="text-muted text-decoration-line-through me-2"
                                                  style="color: #888 !important;">1.200.000đ</span>
                                    <span class="fw-bold" style="color: #1976d2;">900.000đ</span>
                                </div>
                                <a href="#" class="btn btn-primary mt-3 w-100">Đăng ký</a>
                            </div>
                        </div>
                    </div>
                        <%
//                                    }
//                                }
                            %>

                    <!-- Pagination Start -->
                    <nav class="mt-4 d-flex justify-content-center">
                        <ul class="pagination" id="pagination"></ul>
                    </nav>
            </section>
        </div>
    </div>
</div>

<!-- Subject Registration Modal -->
<div class="modal fade" id="subjectRegisterModal" tabindex="-1" aria-labelledby="subjectRegisterModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <form class="modal-content" id="subjectRegisterForm">
            <div class="modal-header">
                <h5 class="modal-title" id="subjectRegisterModalLabel">Đăng ký chủ đề</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label fw-semibold">Chọn gói giá</label>
                    <select class="form-select" name="package" required>
                        <option value="">-- Chọn gói --</option>
                        <option value="basic">Cơ bản - 500.000đ</option>
                        <option value="advanced">Nâng cao - 900.000đ</option>
                        <option value="premium">Chuyên sâu - 1.200.000đ</option>
                    </select>
                </div>
                <div id="userInfoFields">
                    <div class="mb-3">
                        <label class="form-label">Họ và tên</label>
                        <input type="text" class="form-control" name="fullname" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" class="form-control" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Số điện thoại</label>
                        <input type="tel" class="form-control" name="phone" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Giới tính</label>
                        <select class="form-select" name="gender" required>
                            <option value="">-- Chọn giới tính --</option>
                            <option value="male">Nam</option>
                            <option value="female">Nữ</option>
                            <option value="other">Khác</option>
                        </select>
                    </div>
                </div>
                <div id="userInfoNotice" class="alert alert-info d-none" role="alert">
                    Thông tin liên hệ của bạn đã được tự động điền.
                </div>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-primary w-100">Xác nhận đăng ký</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../../component/notification.html"/>
<jsp:include page="../../component/footer.html"/>
<jsp:include page="../../component/back_to_top.html"/>

<script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="${pageContext.request.contextPath}/js/Notification.js"></script>
<script src="${pageContext.request.contextPath}/js/SubjectsList.js"></script>
</body>

</html>
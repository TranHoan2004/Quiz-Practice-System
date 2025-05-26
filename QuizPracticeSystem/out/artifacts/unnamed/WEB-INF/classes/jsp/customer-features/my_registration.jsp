<%--
    Document   : my_registration
    Created on : May 19, 2025, 4:54:19 PM
    Author     : TranHoan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>My Registration</title>
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
<jsp:include page="../../component/navbar.html"/>
<jsp:include page="../../component/header.html"/>

<!-- Courses Start -->
<div class="container-xxl py-5">
    <div class="container">
        <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
            <h6 class="section-title bg-white text-center text-primary px-3">Courses</h6>
            <h1 class="mb-3">My Registration</h1>
        </div>
        <div class="row g-4">
            <div class="col-sm-2 d-flex flex-column">
                <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.1s">
                    <form action="${pageContext.request.contextPath}/user/registration" method="get"
                          class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                        <label for="practiceSearcher" class="form-label mb-2">
                            <span class="fw-semibold text-primary"><i class="bi bi-search me-1"></i>Search</span>
                        </label>
                        <div class="input-group input-group-sm">
                            <input type="text" name="keyword" id="practiceSearcher" class="form-control"
                                   placeholder="Search...">
                            <button class="btn btn-primary" type="submit" id="searchBtn"><i
                                    class="bi bi-search"></i></button>
                        </div>
                    </form>
                </div>
                <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.2s">
                    <div
                            class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                        <label for="subjectFilter" class="form-label mb-2">
                            <span class="fw-semibold text-primary"><i class="bi bi-filter me-1"></i>Subject</span>
                        </label>
                        <select class="form-select form-select-sm" id="subjectFilter" name="filter">
                            <option value="" disabled selected hidden>-- Select Subject --</option>
                            <option value="all">All Subjects</option>
                            <c:if test="${subjects ne null}">
                                <c:forEach items="${subjects}" var="s">
                                    <option value="${s.getName().toLowerCase()}">${s.getName()}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>
                </div>
                <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.2s">
                    <div
                            class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                        <label for="subjectFilter" class="form-label mb-2">
                            <span class="fw-semibold text-primary">Contact</span>
                        </label>
                        <select class="form-select form-select-sm">
                            <option value="" default>-- Select organizations --</option>
                            <option value="web-development">Web Development</option>
                            <option value="design">Design</option>
                            <option value="marketing">Marketing</option>
                        </select>
                        <%--                        <div class="d-flex flex-column mt-2">--%>
                        <%--                            <section>--%>
                        <%--                                <i class="bi bi-telephone-fill"></i>&nbsp;+84111111--%>
                        <%--                            </section>--%>
                        <%--                            <section>--%>
                        <%--                                <i class="bi bi-link-45deg"></i>&nbsp;https://canvas.com--%>
                        <%--                            </section>--%>
                        <%--                            <section>--%>
                        <%--                                <i class="bi bi-globe"></i>&nbsp;https://canvas.com--%>
                        <%--                            </section>--%>
                        <%--                            <section>--%>
                        <%--                                <i class="bi bi-geo-alt-fill"></i>&nbsp;https://canvas.com--%>
                        <%--                            </section>--%>
                        <%--                        </div>--%>
                    </div>
                </div>
            </div>

            <div class="col-lg-10">
                <!-- Registered Courses List Start -->
                <div class="card mb-4 wow fadeInUp">
                    <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                        <span><i class="bi bi-journal-check me-2"></i>Registered Courses</span>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                            <tr>
                                <th>#</th>
                                <th>Subject</th>
                                <th>Registration Time</th>
                                <th>Package</th>
                                <th>Total Cost</th>
                                <th>Status</th>
                                <th>Valid From</th>
                                <th>Valid To</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody id="courseTableBody">
                            <c:set var="size" value="${courses.size()}"/>
                            <c:if test="${empty courses}">
                                <tr>
                                    <td colspan="9" class="text-danger">Không có dữ liệu khóa học nào</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty courses}">
                                <c:set var="size" value="${couses.size()}"/>
                                <c:forEach items="${courses}" var="c" varStatus="loop">
                                    <tr>
                                        <td>${loop.count}</td>
                                        <td>${c.getSubject()}</td>
                                        <td>${c.getRegistrationTime()}</td>
                                        <td>${c.getPackageName()}</td>
                                        <td>${c.getTotalCost()}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${c.status eq 'paid'}">
                                                    <span class="badge bg-success text-light">${c.getStatus()}</span>
                                                </c:when>
                                                <c:when test="${c.status eq 'sent'}">
                                                    <span class="badge bg-warning text-dark">${c.getStatus()}</span>
                                                </c:when>
                                                <c:when test="${c.status eq null}">
                                                    <span class="badge text-dark">-</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-info text-light">${c.getStatus()}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${c.getValidFrom()}</td>
                                        <td>${c.getValidTo()}</td>
                                        <td class="text-center">
                                            <c:if test="${c.status eq 'sent'}">
                                                <button class="btn btn-sm btn-danger me-1">
                                                    <i class="bi bi-x-circle"></i>
                                                    Hủy
                                                </button>
                                                <a href="${pageContext.request.contextPath}/user/registration?edit=1001"
                                                   class="btn btn-sm btn-secondary">
                                                    <i class="bi bi-pencil-square"></i> Chỉnh sửa
                                                </a>
                                            </c:if>
                                            <c:if test="${c.status ne 'sent'}">
                                                -
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </div>

                    <div class="card-footer d-flex justify-content-between align-items-center">
                        <c:set value="${courses.size()}" var="n"/>
                        <c:set value="${totalElements}" var="a"/>
                        <span>Showing 1 to ${n} of ${a} entries</span>
                        <nav aria-label="Course pagination">
                            <ul class="pagination mb-0" id="pagination"
                                style="font-size: 1rem; --bs-pagination-active-bg: #0d6efd; --bs-pagination-active-border-color: #0d6efd;">

                                <li class="page-item ${currentIndex == 1 ? "disabled" : ''}">
                                    <a class="page-link rounded-pill px-3"
                                       href="${pageContext.request.contextPath}/user/registration?page=${currentIndex - 1}">
                                        <i class="bi bi-chevron-left"></i> Prev
                                    </a>
                                </li>
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${i == currentIndex ? "active" : ''}">
                                        <a class="page-link rounded-pill px-3" href="#">${i}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${(currentIndex == totalPages || totalPages == 0) ? "disabled" : ''}">
                                    <a class="page-link rounded-pill px-3"
                                       href="${pageContext.request.contextPath}/user/registration?page=${currentIndex + 1}">
                                        Next <i class="bi bi-chevron-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../component/footer.html"/>
<jsp:include page="../../component/back_to_top.html"/>

<script>
    document.getElementById('subjectFilter').addEventListener('change', function () {
        const value = this.value;
        let href = `${pageContext.request.contextPath}/user/registration`
        if (value !== 'all') {
            href += '?filter=' + value;
        }
        window.location.href = href;
    })
</script>
<script src="../../js/lib/jquery-3.4.1.min.js"></script>
<script src="../../js/lib/bootstrap.bundle.min.js"></script>
<script src="../../lib/wow/wow.min.js"></script>
<script src="../../lib/easing/easing.min.js"></script>
<script src="../../lib/waypoints/waypoints.min.js"></script>
<script src="../../lib/owlcarousel/owl.carousel.min.js"></script>
<script src="../../js/main.js"></script>
</body>

</html>
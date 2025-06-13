<%--
    Document   : subject_lists
    Created on : May 30, 2025, 1:26:19 PM
    Author     : Lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Subject List</title>
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
        <link href="${pageContext.request.contextPath}/css/practicelist.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
    </head>

    <body>
        <jsp:include page="../../component/component.subject/header.jsp"/>
        <jsp:include page="../../component/component.subject/header.html"/>

        <!-- Courses Start -->
        <div class="container-xxl py-5">
            <div class="container">
                <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                    <h6 class="section-title bg-white text-center text-primary px-3">Courses</h6>
                    <h1 class="mb-3">My Course</h1>
                </div>
                <div class="row g-4">
                    <div class="col-sm-2 d-flex flex-column">
                        <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.1s">
                            <form action="${pageContext.request.contextPath}/user/subject_list" method="get"
                                  class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                                <label for="practiceSearcher" class="form-label mb-2">
                                    <span class="fw-semibold text-primary"><i class="bi bi-search me-1"></i>Search</span>
                                </label>
                                <div class="input-group input-group-sm">
                                    <input type="text" name="keyword" id="practiceSearcher" class="form-control"
                                           placeholder="Search by name">
                                    <button class="btn btn-primary" type="submit" id="searchBtn"><i class="bi bi-search"></i>
                                    </button>
                                </div>
                            </form>
                        </div>
                        <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.2s">
                            <div
                                class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                                <label for="categoryFilter" class="form-label mb-2">
                                    <span class="fw-semibold text-primary"><i class="bi bi-filter me-1"></i>Category</span>
                                </label>
                                <select class="form-select form-select-sm" id="categoryFilter" name="category">
                                    <option value="all">All Categories</option>
                                    <c:forEach items="${categories}" var="c">
                                        <option value="${c}" ${param.category == c ? 'selected' : ''}>${c}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.2s">
                            <div
                                class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                                <label for="statusFilter" class="form-label mb-2">
                                    <span class="fw-semibold text-primary">
                                        <i class="bi bi-filter me-1"></i>Status
                                    </span>
                                </label>
                                <select class="form-select form-select-sm" id="statusFilter" name="status">
                                    <option value="all">All Status</option>
                                    <option value="published">Published</option>
                                    <option value="unpublished">Unpublished</option>
                                </select>
                            </div>
                        </div>
                        <c:if test="${contact ne null}">
                            <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.2s">
                                <div
                                    class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                                    <label for="subjectFilter" class="form-label mb-2 overflow-hidden" style="max-width: 100%;">
                                        <span class="fw-semibold text-primary">Contact</span>
                                    </label>
                                    <div class="d-flex flex-column mt-2 w-100" style="word-break: break-word;">
                                        <p class="text-center fw-bold">${contact.getName()}</p>
                                        <section class="mb-1 d-flex align-items-center">
                                            <i class="bi bi-telephone-fill"></i>&nbsp;
                                            ${contact.getPhone()}
                                        </section>
                                        <c:forEach items="${contact.getLink()}" var="l">
                                            <section class="mb-1 d-flex align-items-center">
                                                <a href="${l.value}" target="_blank"
                                                   style="max-width: 140px; display: inline-block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                                    <c:choose>
                                                        <c:when test="${l.key eq 'social media'}">
                                                            <i class="bi bi-globe"></i>&nbsp;
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="bi bi-link-45deg"></i>&nbsp;
                                                        </c:otherwise>
                                                    </c:choose>
                                                    ${l.value}
                                                </a>
                                            </section>
                                        </c:forEach>
                                        <section class="d-flex align-items-start">
                                            <i class="bi bi-geo-alt-fill mt-1"></i>&nbsp;
                                            <span style="display: inline-block; max-width: 140px; word-break: break-word;">
                                                ${contact.getAddress()}
                                            </span>
                                        </section>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>

                    <!-- Registered Courses List Start -->
                    <div class="col-lg-10">
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                                <span><i class="bi bi-journal-check me-2"></i>Subject List</span>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th>#</th>
                                            <th>Name</th>
                                            <th>Category</th>
                                            <th>Number of Lessons</th>
                                            <th>Owner</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${courses}" var="c" varStatus="loop">
                                            <tr>
                                                <td>${loop.index + 1}</td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/user/subject_detail?id=${c.subjectId}" 
                                                       class="text-decoration-none text-primary">
                                                        ${c.title}
                                                    </a>
                                                </td>
                                                <td>${c.category}</td>
                                                <td>${c.numberOfLessons}</td>
                                                <td>${c.owner}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${c.published == true}">
                                                            <span class="badge bg-success">Published</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">Unpublished</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty courses}">
                                            <tr>
                                                <td colspan="6" class="text-center text-danger">No subjects available</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                            <div class="card-footer d-flex justify-content-between align-items-center">
                                <span>Showing 1 to ${courses.size()} of ${totalElements} entries</span>
                                <nav>
                                    <ul class="pagination mb-0">
                                        <li class="page-item ${currentIndex == 1 ? 'disabled' : ''}">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/user/subject_list?page=${currentIndex - 1}">Prev</a>
                                        </li>
                                        <c:forEach var="i" begin="1" end="${totalPages}">
                                            <li class="page-item ${i == currentIndex ? 'active' : ''}">
                                                <a class="page-link"
                                                   href="${pageContext.request.contextPath}/user/subject_list?page=${i}">${i}</a>
                                            </li>
                                        </c:forEach>
                                        <li class="page-item ${currentIndex == totalPages ? 'disabled' : ''}">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/user/subject_list?page=${currentIndex + 1}">Next</a>
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
        <jsp:include page="../../component/notification.html"/>

        <script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
        <script src="${pageContext.request.contextPath}/js/Notification.js"></script>
        <script>
            const baseHref = `${pageContext.request.contextPath}/user/subject_list`;

            document.getElementById('categoryFilter').addEventListener('change', function () {
                const value = this.value;
                const url = new URL(baseHref, window.location.origin);
                if (value !== 'all') {
                    url.searchParams.set('category', value);
                }
                window.location.href = url.toString();
            });

            document.getElementById('statusFilter').addEventListener('change', function () {
                const value = this.value;
                const url = new URL(baseHref, window.location.origin);
                if (value !== 'all') {
                    url.searchParams.set('status', value);
                }
                window.location.href = url.toString();
            });
        </script>

    </body>

</html>
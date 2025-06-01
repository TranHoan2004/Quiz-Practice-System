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
                                    <tr class="record" data-id="${c.getCourseId()}">
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
                                                <button class="btn btn-sm btn-danger me-1" type="button"
                                                        onclick="updateCourse('${c.getCourseId()}')">
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
                                        <a class="page-link rounded-pill px-3"
                                           href="${pageContext.request.contextPath}/user/registration?page=${i}">${i}</a>
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
    let href = `${pageContext.request.contextPath}/user/registration`
    document.getElementById('subjectFilter').addEventListener('change', function () {
        const value = this.value;
        let href = `${pageContext.request.contextPath}/user/registration`
        if (value !== 'all') {
            href += '?filter=' + value;
        }
        window.location.href = href;
    })

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

    function updateCourse(id) {
        fetch(`${pageContext.request.contextPath}/user/course`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({id: id})
        })
            .then(response => {
                if (!response.ok) throw new Error("Request failed");
                return response.body;
            })
            .then(data => {
                console.log(data)
                showNotification("Cancel register successfully", "success")
                setTimeout(() => {
                    location.href = href;
                }, 4500);
            })
            .catch(error => {
                console.log(error)
            })
    }
</script>

</body>

</html>
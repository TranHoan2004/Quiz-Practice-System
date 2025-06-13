<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Lesson List</title>
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

        <!-- Lessons Start -->
        <div class="container-xxl py-5">
            <div class="container">
                <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                    <h6 class="section-title bg-white text-center text-primary px-3">Lesson</h6>
                    <h1 class="mb-3">My Lessons</h1>
                </div>
                <div class="row g-4">
                    <div class="col-sm-2 d-flex flex-column">
                        <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.1s">
                            <form action="${pageContext.request.contextPath}/user/subject_lesson" method="get"
                                  class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                                <input type="hidden" name="id" value="${param.id}" />
                                <label for="lessonSearcher" class="form-label mb-2">
                                    <span class="fw-semibold text-primary"><i class="bi bi-search me-1"></i>Search</span>
                                </label>
                                <div class="input-group input-group-sm">
                                    <input type="text" name="keyword" id="lessonSearcher" class="form-control"
                                           placeholder="Search lesson by name">
                                    <button class="btn btn-primary" type="submit" id="searchBtn"><i class="bi bi-search"></i>
                                    </button>
                                </div>
                            </form>
                        </div>
                        <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.2s">
                            <div class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                                <label for="lessonTypeFilter" class="form-label mb-2">
                                    <span class="fw-semibold text-primary"><i class="bi bi-filter me-1"></i>Lesson Type</span>
                                </label>
                                <select name="lessonType" id="lessonTypeFilter" class="form-select form-select-sm">
                                    <option value="all">All Lesson Types</option>
                                    <c:forEach items="${lessonTypes}" var="c">
                                        <option value="${c}" ${param.lessonType == c ? 'selected' : ''}>${c}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.2s">
                            <div class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                                <label for="statusFilter" class="form-label mb-2">
                                    <span class="fw-semibold text-primary">
                                        <i class="bi bi-filter me-1"></i>Status
                                    </span>
                                </label>
                                <select class="form-select form-select-sm" id="statusFilter" name="status">
                                    <option value="all">All Status</option>
                                    <option value="active" ${param.status == 'active' ? 'selected' : ''}>Active</option>
                                    <option value="inactive" ${param.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                                </select>
                            </div>
                        </div>
                        <div class="wow fadeInUp shadow mt-2" data-wow-delay="0.2s">
                            <div class="p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                                <label for="entriesInput" class="form-label mb-2">
                                    <span class="fw-semibold text-primary">
                                        <i class="bi bi-list-ol me-1"></i>Entries per page
                                    </span>
                                </label>
                                <input type="number" id="entriesInput" min="1" step="1"
                                       class="form-control form-control-sm w-75 text-center"
                                       placeholder="e.g. 10" onchange="changePageSizeManual(this)">
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-10">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <div></div>
                            <div>
                                <a href="#" class="btn btn-sm btn-primary">
                                    <i class="bi bi-plus-lg"></i> Add New Lesson
                                </a>
                                <div style="position: relative; display: inline-block;">
                                    <button id="columnToggleBtn" class="btn btn-sm btn-outline-secondary ms-2" onclick="toggleColumnSelector(event)">
                                        <i class="bi bi-layout-three-columns"></i> Column
                                    </button>
                                    <div id="columnSelector" class="bg-white border p-2 shadow"
                                         style="display: none; position: absolute; top: 0; left: 105%; white-space: nowrap; z-index: 1000;">
                                        <div><input type="checkbox" checked onchange="toggleColumn(0)"> ID</div>
                                        <div><input type="checkbox" checked onchange="toggleColumn(1)"> Lesson</div>
                                        <div><input type="checkbox" checked onchange="toggleColumn(2)"> Order</div>
                                        <div><input type="checkbox" checked onchange="toggleColumn(3)"> Type</div>
                                        <div><input type="checkbox" checked onchange="toggleColumn(4)"> Status</div>
                                        <div><input type="checkbox" checked onchange="toggleColumn(5)"> Action</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                                <span><i class="bi bi-journal-check me-2"></i>Lesson List</span>
                            </div>

                            <div class="table-responsive">

                                <table class="table table-hover align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th>#</th>
                                            <th>Lesson</th>
                                            <th>Order</th>
                                            <th>Type</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${lessons}" var="l" varStatus="loop">
                                            <tr>
                                                <td>${loop.index + 1}</td>
                                                <td>${l.title}</td>
                                                <td>${loop.index + 1}</td>
                                                <td>${l.type}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${l.active == true}">
                                                            <span class="badge bg-success">Active</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">Inactive</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <!-- Nút Edit -->
                                                    <form method="get" action="${pageContext.request.contextPath}/user/lesson/detail" class="d-inline">
                                                        <input type="hidden" name="id" value="${l.id}" />
                                                        <input type="hidden" name="courseId" value="${param.id}" />
                                                        <button type="submit" class="btn btn-sm p-0 border-0 bg-transparent" title="Edit Lesson">
                                                            <i class="bi bi-pencil-square text-primary" style="font-size: 1.5rem;"></i>
                                                        </button>
                                                    </form>

                                                    <!-- Nút Toggle Trạng thái -->
                                                    <form method="post" action="${pageContext.request.contextPath}/user/subject_lesson/status-toggle" class="d-inline">
                                                        <input type="hidden" name="lessonId" value="${l.id}" />
                                                        <input type="hidden" name="courseId" value="${not empty courseId ? courseId : param.id}" />
                                                        <button type="submit" class="btn btn-sm p-0 border-0 bg-transparent" title="Toggle Status">
                                                            <i class="bi ${l.active ? 'bi-toggle-on text-success' : 'bi-toggle-off text-secondary'}" style="font-size: 1.5rem;"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty lessons}">
                                            <tr>
                                                <td colspan="6" class="text-center text-danger">No lessons available</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                            <div class="card-footer d-flex justify-content-between align-items-center">
                                <nav>
                                    <ul class="pagination mb-0">
                                        <li class="page-item ${currentIndex == 1 ? 'disabled' : ''}">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/user/subject_lesson?page=${currentIndex - 1}&id=${not empty courseId ? courseId : param.id}">
                                                Prev
                                            </a>
                                        </li>

                                        <c:forEach var="i" begin="1" end="${totalPages}">
                                            <li class="page-item ${i == currentIndex ? 'active' : ''}">
                                                <a class="page-link"
                                                   href="${pageContext.request.contextPath}/user/subject_lesson?page=${i}&id=${not empty courseId ? courseId : param.id}">
                                                    ${i}
                                                </a>
                                            </li>
                                        </c:forEach>

                                        <li class="page-item ${currentIndex == totalPages ? 'disabled' : ''}">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/user/subject_lesson?page=${currentIndex + 1}&id=${not empty courseId ? courseId : param.id}">
                                                Next
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
                                            const baseHref = `${pageContext.request.contextPath}/user/subject_lesson`;

                                            document.getElementById('lessonTypeFilter').addEventListener('change', function () {
                                                const value = this.value;
                                                const url = new URL(baseHref, window.location.origin);
                                                url.searchParams.set('id', '${param.id}');
                                                if (value !== 'all') {
                                                    url.searchParams.set('lessonType', value); // ✅ sửa ở đây
                                                }
                                                window.location.href = url.toString();
                                            });

                                            document.getElementById('statusFilter').addEventListener('change', function () {
                                                const value = this.value;
                                                const url = new URL(baseHref, window.location.origin);
                                                url.searchParams.set('id', '${not empty courseId ? courseId : param.id}');
                                                if (value !== 'all') {
                                                    url.searchParams.set('status', value);
                                                }
                                                window.location.href = url.toString();
                                            });

                                            function changePageSizeManual(inputElement) {
                                                const value = parseInt(inputElement.value);
                                                if (!isNaN(value) && value > 0) {
                                                    const url = new URL(window.location.href);
                                                    url.searchParams.set('pageSize', value);
                                                    url.searchParams.set('id', '${param.id}');
                                                    window.location.href = url.toString();
                                                } else {
                                                    alert("Please enter a valid number greater than 0.");
                                                }
                                            }

                                            function toggleColumnSelector(event) {
                                                const selector = document.getElementById("columnSelector");
                                                selector.style.display = selector.style.display === "block" ? "none" : "block";
                                                event.stopPropagation();
                                            }

                                            document.addEventListener("click", function () {
                                                document.getElementById("columnSelector").style.display = "none";
                                            });

                                            document.getElementById("columnSelector").addEventListener("click", function (e) {
                                                e.stopPropagation();
                                            });

                                            function toggleColumn(index) {
                                                const table = document.querySelector("table");
                                                const rows = table.querySelectorAll("tr");

                                                rows.forEach(row => {
                                                    if (row.cells[index]) {
                                                        row.cells[index].style.display =
                                                                row.cells[index].style.display === "none" ? "" : "none";
                                                    }
                                                });
                                            }
        </script>
    </body>

</html>
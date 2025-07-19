<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Marketing Dashboard</title>
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
              integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
              crossorigin="anonymous" referrerpolicy="no-referrer"/>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

        <link href="${pageContext.request.contextPath}/css/marketer-dashboard.css" rel="stylesheet">
    </head>

    <body>
        <div class="d-none">
            <p id="message" class="d-none">${message}</p>
            <p id="type" class="d-none">${type}</p>
        </div>

        <div id="dashboard-data"
             data-revenue-map='${fn:escapeXml(revenueByCategory)}'
             data-orders-all='${fn:escapeXml(ordersCountTrendAll)}'
             data-orders-success='${fn:escapeXml(ordersCountTrendSuccess)}'
             data-orders-date='${fn:escapeXml(ordersCountTrendDate)}'
             data-api-url='${pageContext.request.contextPath}/stats'
             data-new-subjects='${numberOfNewSubjects}'
             data-all-subjects='${numberOfAllSubjects}'
             data-courses-success='${numberOfCoursesSuccess}'
             data-courses-cancel='${numberOfCoursesCancel}'
             data-courses-summited='${numberOfCoursesSummited}'
             data-new-accounts='${numberOfNewAccount}'
             data-new-bought='${numberOfNewBought}'
             data-total-revenue='${totalRevenue}'
             data-change-revenue='${changePercentRevenue}'>
        </div>

        <nav class="sidebar" id="sidebarNav">
            <a class="navbar-brand px-3 mb-4 fw-bold d-flex align-items-center gap-2" href="#">
                <i class="bi bi-bar-chart-fill"></i> Marketing
            </a>
            <ul class="nav flex-column gap-2">
                <li class="nav-item"><a class="nav-link active d-flex align-items-center gap-2" href="#"><i class="bi bi-clipboard-data-fill"></i> Dashboard</a>
                </li>
                <li class="nav-item"><a class="nav-link d-flex align-items-center gap-2"
                                        href="${pageContext.request.contextPath}/home"><i
                    <i
                            class="bi bi-house"></i> Home</a>
                </li>
                
                <c:if test="${sessionScope.userRole == 'Admin' || sessionScope.userRole == 'Marketer'}">
                    <li class="nav-item">
                        <a class="nav-link d-flex align-items-center gap-2"
                           href="${pageContext.request.contextPath}/marketer/post-details">
                            <i class="bi bi-postcard-heart"></i>Post Details
                        </a>
                    </li>
                </c:if>
                
                <c:if test="${sessionScope.userRole == 'Admin' || sessionScope.userRole == 'Marketer'}">
                    <li class="nav-item"><a class="nav-link d-flex align-items-center gap-2"
                                            href="${pageContext.request.contextPath}/jsp/marketing-features/sliders_list.jsp">
                        <i class="bi bi-file-earmark-slides-fill"></i> Slider</a>
                    </li>
                </c:if>

                <c:if test="${sessionScope.userRole == 'Admin'}">
                    <li class="nav-item"><a class="nav-link d-flex align-items-center gap-2"
                                            href="${pageContext.request.contextPath}/user/subject-list">
                        <i class="bi bi-stack-overflow"></i> Subject List</a>
                    </li>
                </c:if>

                <c:if test="${sessionScope.userRole == 'Admin' || sessionScope.userRole == 'Sale'}">
                    <li class="nav-item">
                        <a class="nav-link d-flex align-items-center gap-2"
                           href="${pageContext.request.contextPath}/registration-list">
                            <i class="bi bi-graph-up"></i>Registration List
                        </a>
                    </li>
                </c:if>
            </ul>
        </nav>

        <div class="main-content">
            <!-- Header with toggle and user info -->
            <div class="d-flex justify-content-between align-items-center bg-white shadow-sm px-4 py-3 mb-4 sticky-top"
                 style="z-index:1020; min-height:64px;">
                <div class="d-flex align-items-center gap-3">
                    <button class="btn btn-outline-secondary d-lg-none" id="sidebarToggle" aria-label="Toggle sidebar"><i
                            class="bi bi-list" style="font-size:1.5rem;"></i></button>
                    <h3 class="fw-bold mb-0">Marketing Dashboard</h3>
                </div>
                <div class="d-flex align-items-center gap-2">
                    <!-- Nếu đã đăng nhập -->
                    <c:if test="${not empty sessionScope.currentUser}">
                        <div class="logged-in">
                            <div class="dropdown">
                                <a href="#" class="d-flex align-items-center text-decoration-none dropdown-toggle"
                                   id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                    <c:choose>
                                        <c:when test="${empty sessionScope.currentUser.imageUrl}">
                                            <img src="${pageContext.request.contextPath}/img/default-avatar.png"
                                                 alt="User Avatar"
                                                 class="rounded-circle me-2" width="32" height="32">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/${sessionScope.currentUser.imageUrl}"
                                                 alt="User Avatar"
                                                 class="rounded-circle me-2" width="32" height="32">
                                        </c:otherwise>
                                    </c:choose>

                                    <span class="d-none d-md-inline">${sessionScope.currentUser.fullName}</span>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                    <li><a class="dropdown-item" href="profile.jsp"><i class="fas fa-user me-2"></i>Profile</a>
                                    </li>
                                    <li><a class="dropdown-item" href="my-courses.jsp"><i
                                                class="fas fa-graduation-cap me-2"></i>My Courses</a></li>
                                    <li>
                                        <hr class="dropdown-divider">
                                    </li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/auth/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
            <!-- Date Filter -->
            <div>
                <form id="dateForm"
                      action="${pageContext.request.contextPath}/marketer/dashboard"
                      class="d-flex gap-2 mb-3 justify-content-end align-items-center"
                      method="GET"
                      >
                    <input type="date" class="form-control w-auto" id="startDate" name="startDate" onchange="submitForm()">
                    <input type="date" class="form-control w-auto" id="endDate" name="endDate" onchange="submitForm()">
                </form>
            </div>
            <!-- Grouped Statistics Cards + Order Trend Chart -->
            <div class="row g-2 mb-3">
                <div class="col-12 col-md-5">
                    <div class="row">
                        <!-- Subjects Group -->
                        <div class="col-6 col-sm-4 col-md-4">
                            <div class="mb-2 fw-semibold text-primary d-flex align-items-center gap-2">
                                <i class="bi bi-journal-text"></i> Subjects
                            </div>
                            <div class="row g-2">
                                <div class="col-12">
                                    <div class="card card-box text-center"
                                         style="background:rgba(6,187,204,0.10);padding:14px 8px;">
                                        <div class="card-body p-2">
                                            <div class="card-icon" style="font-size:1.6rem;"><i class="bi bi-bookmark-plus"></i>
                                            </div>
                                            <div class="text-muted small">New Subjects</div>
                                            <div class="fw-bold fs-5">${numberOfNewSubjects}</div>
                                            <div id="changePercentNewSubjectsContainer"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="card card-box text-center"
                                         style="background:rgba(24,29,56,0.08);padding:14px 8px;">
                                        <div class="card-body p-2">
                                            <div class="card-icon" style="font-size:1.6rem;"><i class="bi bi-journal-text"></i>
                                            </div>
                                            <div class="text-muted small">All Subjects</div>
                                            <div class="fw-bold fs-5">${numberOfAllSubjects}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Registrations Group -->
                        <div class="col-6 col-sm-4 col-md-4">
                            <div class="mb-2 fw-semibold text-success d-flex align-items-center gap-2"><i
                                    class="bi bi-person-lines-fill"></i> Registrations
                            </div>
                            <div class="row g-2">
                                <div class="col-12">
                                    <div class="card card-box text-center"
                                         style="background:rgba(25,135,84,0.10);padding:14px 8px;">
                                        <div class="card-body p-2">
                                            <div class="card-icon" style="font-size:1.6rem;"><i
                                                    class="bi bi-person-check"></i></div>
                                            <div class="text-muted small">Success</div>
                                            <div class="fw-bold fs-5">${numberOfCoursesSuccess}</div>
                                            <div id="changePercentCoursesSuccessContainer"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="card card-box text-center"
                                         style="background:rgba(220,53,69,0.10);padding:14px 8px;">
                                        <div class="card-body p-2">
                                            <div class="card-icon" style="font-size:1.6rem;"><i class="bi bi-person-x"></i>
                                            </div>
                                            <div class="text-muted small">Cancelled</div>
                                            <div class="fw-bold fs-5">${numberOfCoursesCancel}</div>
                                            <div id="changePercentCoursesCancelContainer"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="card card-box text-center"
                                         style="background:rgba(255,193,7,0.10);padding:14px 8px;">
                                        <div class="card-body p-2">
                                            <div class="card-icon" style="font-size:1.6rem;"><i
                                                    class="bi bi-person-lines-fill"></i></div>
                                            <div class="text-muted small">Submitted</div>
                                            <div class="fw-bold fs-5">${numberOfCoursesSummited}</div>
                                            <div id="changePercentCoursesSummitedContainer"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Customers Group -->
                        <div class="col-6 col-sm-4 col-md-4">
                            <div class="mb-2 fw-semibold text-purple d-flex align-items-center gap-2"
                                 style="color:#6f42c1!important;"><i class="bi bi-people"></i> Customers
                            </div>
                            <div class="row g-2">
                                <div class="col-12">
                                    <div class="card card-box text-center"
                                         style="background:rgba(111,66,193,0.10);padding:14px 8px;">
                                        <div class="card-body p-2">
                                            <div class="card-icon" style="font-size:1.6rem;"><i
                                                    class="bi bi-person-plus"></i></div>
                                            <div class="text-muted small">Newly Registered</div>
                                            <div class="fw-bold fs-5">${numberOfNewAccount}</div>
                                            <div id="changePercentNewAccountsContainer"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="card card-box text-center"
                                         style="background:rgba(13,202,240,0.10);padding:14px 8px;">
                                        <div class="card-body p-2">
                                            <div class="card-icon" style="font-size:1.6rem;"><i class="bi bi-bag-check"></i>
                                            </div>
                                            <div class="text-muted small">Newly Bought</div>
                                            <div class="fw-bold fs-5">${numberOfNewBought}</div>
                                            <div id="changePercentNewBoughtContainer"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Order Trend Chart -->
                <div class="col-12 col-md-7">
                    <div class="mb-2 fw-semibold text-warning d-flex align-items-center gap-2 mx-3"><i
                            class="bi bi-bar-chart-line"></i> Orders
                    </div>
                    <div class="card card-box h-100 mx-3" style="padding:14px 8px;">
                        <div class="card-header d-flex justify-content-between align-items-center p-2">
                            <span class="fw-semibold">Order Trend</span>
                            <select id="orderType" class="form-select form-select-sm w-auto">
                                <option value="all">All</option>
                                <option value="success">Success Only</option>
                            </select>
                        </div>
                        <div class="card-body p-2">
                            <canvas id="orderTrendChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Revenue by Category Pie Chart + Legend + Total Revenue -->
            <div class="row g-3 mb-4 mt-5 align-items-stretch">
                <!-- Pie Chart -->
                <div class="col-12 col-md-6 col-lg-6">
                    <div class="card card-box h-100 p-3">
                        <div class="card-header d-flex align-items-center gap-2">
                            <i class="bi bi-pie-chart"></i> Revenue by Subject Category
                        </div>
                        <div class="card-body p-2 d-flex justify-content-center">
                            <div style="width:280px;height:280px;">
                                <canvas id="revenueCategoryChart" style="max-width:100%;max-height:100%;"></canvas>
                            </div>
                        </div>
                    </div>

                </div>

                <!-- Legend -->
                <div class="col-12 col-md-3 col-lg-3">
                    <div class="card card-box h-100 p-3 rounded shadow-sm">
                        <div class="card-header fw-semibold mb-2">Legend</div>
                        <div class="card-body p-2">
                            <ul class="list-unstyled mb-0 small" id="revenueCategoryLegend"
                                style="max-height: 220px; overflow-y: auto;"></ul>
                        </div>
                    </div>
                </div>

                <!-- Total Revenue -->
                <div class="col-12 col-md-3 col-lg-3">
                    <div class="card h-100 text-center border-0 shadow-sm"
                         style="background: #f8f9fa; border-radius: 1rem;">
                        <div class="card-body d-flex flex-column justify-content-center align-items-center p-3">
                            <div class="card-icon mb-3" style="font-size:2.5rem; color: #28a745;">
                                <i class="bi bi-cash-coin"></i>
                            </div>
                            <h3 class="fw-bold text-success mb-1" style="font-size:1.5rem;">
                                <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"
                                                  groupingUsed="true"/>
                            </h3>
                            <div class="text-muted small">Total in selected period</div>
                            <div class="fs-6 fw-bold" id="changePercentRevenueContainer"></div>
                        </div>
                    </div>
                </div>

                <div id="samplePrompts" class="p-2">
                    <div class="mb-2">💡 Bạn muốn hỏi gì?</div>
                    <button class="btn btn-sm btn-outline-primary d-block mb-1" onclick="handleSuggestionClick('marketer')">
                        Môn học nào đang đóng góp doanh thu tốt nhất, và nên dùng chiến lược nào để tăng doanh thu, bán được nhiều khoá học hơn?
                    </button>
                    <button class="btn btn-sm btn-outline-primary d-block mb-1" onclick="handleSuggestionClick('re  venue')">
                        Xu hướng đơn hàng qua thời gian đang biến động như thế nào và cần làm gì để duy trì tăng trưởng?
                    </button>
                    <button class="btn btn-sm btn-outline-primary d-block mb-1" onclick="handleSuggestionClick('course')">
                        Người dùng mới có đang thật sự mang lại giá trị doanh thu không?
                    </button>
                </div>  

            </div>
        </div>

        <!-- Nút icon chat -->
        <div class="chat-icon" onclick="toggleChat()">
            <img src="https://cdn-icons-png.flaticon.com/512/2462/2462719.png" alt="Chat"/>
        </div>

        <!-- Popup chat -->
        <div class="chat-popup" id="chatPopup">
            <div class="chat-header">💬 Miss - Your Assistant</div>
            <div class="chat-box" id="chatBox"></div>
            <form class="chat-input" id="chatForm">
                <input type="text" id="messageInput" placeholder="Enter your question..." required/>
                <button type="submit">Send</button>
            </form>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels"></script>

        <script src="../js/Toast.js"></script>

        <script>
            const submitForm = () => {
                document.getElementById("dateForm").submit();
            }
            window.addEventListener('DOMContentLoaded', function () {
                const startDateInput = document.getElementById('startDate');
                const endDateInput = document.getElementById('endDate');

                const formatDate = (date) => date.toISOString().split('T')[0];

                const serverStartDate = '<%= request.getParameter("startDate") != null ? request.getParameter("startDate") : "" %>';
                const serverEndDate = '<%= request.getParameter("endDate") != null ? request.getParameter("endDate") : "" %>';

                if (serverStartDate && serverEndDate) {
                    startDateInput.value = serverStartDate;
                    endDateInput.value = serverEndDate;
                } else {
                    const today = new Date();
                    const priorDate = new Date();
                    priorDate.setDate(today.getDate() - 7);

                    startDateInput.value = formatDate(priorDate);
                    endDateInput.value = formatDate(today);
                }
            });

            const message = document.getElementById("message").innerHTML;
            const type = document.getElementById("type").innerHTML;
            if (message && type) {
                createToast(message, type, 5000);
            }

        </script>


        <script>
            document.addEventListener("DOMContentLoaded", function () {
                renderChangePercent("changePercentNewSubjectsContainer", "${changePercentNewSubjects}");
                renderChangePercent("changePercentCoursesSuccessContainer", "${changePercentCoursesSuccess}");
                renderChangePercent("changePercentCoursesCancelContainer", "${changePercentCoursesCancel}");
                renderChangePercent("changePercentCoursesSummitedContainer", "${changePercentCoursesSummited}");
                renderChangePercent("changePercentNewAccountsContainer", "${changePercentNewAccounts}");
                renderChangePercent("changePercentNewBoughtContainer", "${changePercentNewBought}");
                renderChangePercent("changePercentRevenueContainer", "${changePercentRevenue}");
            });
        </script>

        <script src="../js/MarketingDashboard.js"></script>
    </body>

</html>
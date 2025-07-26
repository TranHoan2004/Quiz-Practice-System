<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Navbar Start -->
<nav class="navbar navbar-expand-lg bg-white navbar-light shadow sticky-top p-0">
    <div class="container-fluid">
        <a href="/qps/home" class="navbar-brand d-flex align-items-center px-4 px-lg-5">
            <h2 class="m-0 text-primary"><i class="fa fa-book me-3"></i>Quezee</h2>
        </a>
        <button type="button" class="navbar-toggler me-4" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
            <div class="navbar-nav ms-auto p-4 p-lg-0">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-item nav-link active">Dashboard</a>
            </div>
            <!-- User Authentication Section -->
            <div class="d-flex align-items-center px-4 px-lg-5">
                <!-- If login -->
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
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user/profile"><i class="fas fa-user me-2"></i>Profile</a>
                                </li>
                                <li><a class="dropdown-item" href="#"><i
                                            class="fas fa-graduation-cap me-2"></i>Setting</a></li>
                                <li>
                                    <hr class="dropdown-divider">
                                </li>
                                <c:if test="${sessionScope.userRole == 'Admin'
                                 || sessionScope.userRole == 'Marketer'
                                  || sessionScope.userRole == 'Sale'}">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/dashboard"><i
                                            class="fas fa-graduation-cap me-2"></i>Dashboard</a></li>
                                </c:if>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/auth/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</nav>
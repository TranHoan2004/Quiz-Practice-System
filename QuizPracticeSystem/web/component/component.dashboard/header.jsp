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
				<a href="/qps/home" class="nav-item nav-link active">DashBoard</a>
				<a href="courses.html" class="nav-item nav-link">New Subject</a>
				<div class="nav-item dropdown">
					<a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">Subject Details</a>
					<div class="dropdown-menu fade-down m-0">
						<a href="about.html" class="dropdown-item">Subject Lesson</a>
						<a href="team.html" class="dropdown-item">Subject Dimension</a>
						<a href="testimonial.html" class="dropdown-item">Price Package</a>
					</div>
				</div>
			</div>
			<!-- User Authentication Section -->
			<div class="d-flex align-items-center px-4 px-lg-5">
				<!-- Nếu chưa đăng nhập -->
				<c:if test="${empty sessionScope.currentUser}">
					<div class="not-logged-in d-flex align-items-center">
						<a href="/qps/user/login"
						   class="btn btn-outline-primary me-2 d-flex align-items-center justify-content-center"
						   style="height: 40px;">Login</a>
						<a href="/qps/user/register" class="btn btn-primary d-flex align-items-center justify-content-center"
						   style="height: 40px;">Register</a>
					</div>
				</c:if>
				
				<!-- Nếu đã đăng nhập -->
				<c:if test="${not empty sessionScope.currentUser}">
					<div class="logged-in">
						<div class="dropdown">
							<a href="#" class="d-flex align-items-center text-decoration-none dropdown-toggle"
							   id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
								<c:choose>
									<c:when test="${empty sessionScope.currentUser.imageUrl}">
										<img src="${pageContext.request.contextPath}/img/default-avatar.png" alt="User Avatar"
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
								<li><a class="dropdown-item" href="profile.jsp"><i class="fas fa-user me-2"></i>Profile</a></li>
								<li><a class="dropdown-item" href="my-courses.jsp"><i class="fas fa-graduation-cap me-2"></i>My Courses</a></li>
								<li><hr class="dropdown-divider"></li>
								<li><a class="dropdown-item" href="logout.jsp"><i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
							</ul>
						</div>
					</div>
				</c:if>
			</div>
		</div>
	</div>
</nav>
<!-- Navbar End -->

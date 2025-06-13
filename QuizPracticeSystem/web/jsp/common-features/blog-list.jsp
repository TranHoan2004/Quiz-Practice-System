<%--
  Created by IntelliJ IDEA.
  User: Huong
  Date: 6/2/2025
  Time: 9:51 AM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
	<meta charset="UTF-8">
	
	<title>Quezee</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport">
	<meta content="" name="keywords">
	<meta content="" name="description">
	
	<!-- Google Web Fonts -->
	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<!-- Ví dụ: Sử dụng font Inter hoặc Roboto từ Google Fonts -->
	<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
	
	
	<!-- Icon Font Stylesheet -->
	<link rel="stylesheet" href="fontawesome/css/all.min.css">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
	
	<!-- Libraries Stylesheet -->
	<link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
	
	<!-- Customized Bootstrap Stylesheet -->
	<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
	
	<link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/blog-list.css" rel="stylesheet">

</head>
<body>

<jsp:include page="../../component/header.jsp"/>

<div class="container py-4">
	<div class="row">
		<!-- Main Content - Blog Posts List -->
		<div class="col-lg-9 col-md-8">
			<!-- Breadcrumb -->
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Homepage</a></li>
					<li class="breadcrumb-item active" aria-current="page">Blog</li>
				</ol>
			</nav>
			
			<h3 class="mb-4">📰 Danh sách bài viết</h3>
			
			<%-- Blog List --%>
			<c:choose>
				<c:when test="${not empty blogs}">
					<c:forEach var="blog" items="${blogs}">
						<div class="blog-card p-3 mb-4 border rounded shadow-sm">
							<!-- Avatar + Author -->
							<div class="d-flex align-items-center mb-3">
								<img src="${pageContext.request.contextPath}/${blog.avatarUrl}"
									 class="rounded-circle me-2 blog-card-avatar" alt="Avatar">
								<strong>${blog.accountName}</strong>
							</div>
							
							<!-- Content Wrapper -->
							<div class="d-flex justify-content-between align-items-center">
								<div class="me-3 flex-grow-1">
									<h5 class="mb-1">
										<a href="${pageContext.request.contextPath}/blog-details?id=${blog.id}" class="text-dark text-decoration-none fw-bold">${blog.title}</a>
									</h5>
									<p class="text-muted text-truncate-2 mb-2">${blog.briefInfo}</p>
									<div class="d-flex align-items-center flex-wrap gap-2">
										<span class="badge bg-secondary">${blog.category}</span>
										<small class="text-muted">${blog.createdDate}</small>
										<small class="text-muted">• ${blog.views} views</small>
									</div>
								</div>
								
								<!-- Thumbnail -->
								<div class="d-flex align-items-center">
									<img src="${pageContext.request.contextPath}/${blog.thumbnailUrl}"
										 class="blog-card-img" alt="Thumbnail">
								</div>
							</div>
						</div>
					</c:forEach>
				</c:when>
				
				<c:otherwise>
					<div class="alert alert-warning text-center">
						Không tìm thấy bài viết nào.
					</div>
				</c:otherwise>
			</c:choose>
			
			<!-- Pagination -->
			<c:set var="maxPagesToShow" value="10" />
			<c:set var="halfPagesToShow" value="${maxPagesToShow / 2}" />
			
			<c:set var="startPage" value="${currentIndex - halfPagesToShow}" />
			<c:if test="${startPage < 1}">
				<c:set var="startPage" value="1" />
			</c:if>
			
			<c:set var="endPage" value="${startPage + maxPagesToShow - 1}" />
			<c:if test="${endPage > totalPages}">
				<c:set var="endPage" value="${totalPages}" />
			</c:if>
			
			<c:if test="${endPage - startPage + 1 < maxPagesToShow}">
				<c:set var="startPage" value="${endPage - maxPagesToShow + 1}" />
				<c:if test="${startPage < 1}">
					<c:set var="startPage" value="1" />
				</c:if>
			</c:if>
			
			<nav>
				<ul class="pagination justify-content-center mt-4">
					<li class="page-item ${currentIndex == 1 ? 'disabled' : ''}">
						<a class="page-link" href="?page=${currentIndex - 1}">Trước</a>
					</li>
					
					<c:forEach var="i" begin="${startPage}" end="${endPage}">
						<li class="page-item ${i == currentIndex ? 'active' : ''}">
							<a class="page-link" href="?page=${i}">${i}</a>
						</li>
					</c:forEach>
					
					<li class="page-item ${currentIndex == totalPages ? 'disabled' : ''}">
						<a class="page-link" href="?page=${currentIndex + 1}">Sau</a>
					</li>
				</ul>
			</nav>
		
		</div>
		
		<!-- Sidebar -->
		<div class="col-lg-3 col-md-4 mb-4 sidebar">
			<form action="${pageContext.request.contextPath}/blog-list" method="get">
				<h5 class="section-title"><label for="search">🔍 Tìm kiếm</label></h5>
				<input type="text" name="keyword" class="form-control mb-3" id="search" placeholder="Tìm bài viết...">
				<button type="submit" class="btn btn-primary">Tìm kiếm</button>
			</form>
			
			
			<h5 class="section-title">📂 Danh mục</h5>
			<ul class="list-group mb-3 sidebar-categories" id="category-list">
				<c:forEach items="${categories}" var="category">
					<a href="?category=${category.categoryId}" class="list-group-item">${category.category}</a>
				</c:forEach>
			</ul>
			
			<h5 class="section-title">🕒 Bài viết mới</h5>
			<ul class="list-group mb-3 gap-3">
				<c:forEach var="blog" items="${latestBlogs}">
					<li class="list-group-item p-3 latest-blog-item">
						<div class="d-flex justify-content-between align-items-center mb-2">
							<div class="d-flex align-items-center">
								<img src="${pageContext.request.contextPath}/${blog.avatarUrl}"
									 class="rounded-circle me-2 avatar" alt="Avatar">
								<strong class="author-name mb-0">${blog.accountName}</strong>
							</div>
							<small class="text-muted">${blog.createdDate}</small>
						</div>
						
						<h6 class="fw-bold mb-2 text-truncate-2">
							<a href="${pageContext.request.contextPath}/blog-details?id=${blog.id}"
							   class="text-decoration-none text-dark">${blog.title}</a>
						</h6>
						
						<div class="d-flex gap-3">
							<img src="${pageContext.request.contextPath}/${blog.thumbnailUrl}"
								 class="blog-thumbnail" alt="Thumbnail">
							<div class="text-truncate-3 small text-muted">${blog.briefInfo}</div>
						</div>
					</li>
				</c:forEach>
			</ul>
			
			<h5 class="section-title">📌 Liên hệ</h5>
			<ul class="list-group">
				<li class="list-group-item">📞 1900 0000</li>
				<li class="list-group-item">✉ support@abc.com</li>
				<li class="list-group-item">🌐 facebook.com/example</li>
			</ul>
		</div>
	</div>
</div>

<jsp:include page="../../component/footer.html"/>

<!-- JavaScript Libraries -->
<script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
</body>
</html>


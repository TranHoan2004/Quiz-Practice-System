<%--
  Created by IntelliJ IDEA.
  User: Huong
  Date: 6/2/2025
  Time: 9:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <title>${blog.title}</title>
    <link href="img/favicon.ico" rel="icon">
    
    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="css/lib/css2.css">
    
    <!-- Icon Font Stylesheet -->
    <link rel="stylesheet" href="fontawesome/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
    
    <!-- Libraries Stylesheet -->
    <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    
    <!-- Customized Bootstrap Stylesheet -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    
    <link href="${pageContext.request.contextPath}/css/blog-details.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../../component/header.jsp"/>

<div class="container py-4">
    <div class="row">
        <!-- Main content -->
        <main class="col-lg-9 col-md-8 mb-4">
            <!-- Breadcrumb -->
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Homepage</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/blog-list">Blog</a></li>
                    <li class="breadcrumb-item active" aria-current="page">${blogDetails.title}</li>
                </ol>
            </nav>
            
            <a href="${pageContext.request.contextPath}/blog-list" class="btn btn-outline-secondary mb-3">
                ← Back to blog list
            </a>
            
            <article class="p-4 shadow-sm post-container">
                <!-- Post title -->
                <h1 class="post-title mb-3 text-center">${blogDetails.title}</h1>
                
                <!-- Post meta -->
                <div class="d-flex flex-wrap justify-content-between text-muted mb-3 fs-6">
                    <div>
                        <img src="${blogDetails.avatarUrl}"
                             alt="Avatar" class="rounded-circle me-2" width="36" height="36" />
                        <strong>${blogDetails.accountName}</strong>
                    </div>
                    <div class="mt-2">
                        <i class="fa fa-eye me-1"></i>
                        <span>${blogDetails.views} views</span> •
                        <i class="fa-solid fa-calendar-days"></i>
                        <span>${blogDetails.createdDate}</span> •
                        <span class="badge bg-secondary">${blogDetails.category}</span>
                    </div>
                
                </div>
                
                <!-- Post content -->
                <div class="post-media mt-4">
                    <div class="post-content text-dark">
                        <!-- Brief Info -->
                        <div class="mb-3">
                            ${blogDetails.briefInfo}
                        </div>
                        
                        <!-- Thumbnail -->
                        <div class="d-flex justify-content-center mb-3">
                            <div class="post-media mt-4">
                                <img src="${pageContext.request.contextPath}/${blogDetails.thumbnailUrl}"
                                     alt="Thumbnail" class="rounded shadow" />
                                <c:forEach var="media" items="${blogDetails.blogMediaList}">
                                    <div class="text-center mb-4">
                                        <c:choose>
                                            <c:when test="${fn:endsWith(media.mediaUrl, '.mp4')
                                                                || fn:endsWith(media.mediaUrl, '.mov')
                                                                || fn:endsWith(media.mediaUrl, '.avi')
                                                                || fn:endsWith(media.mediaUrl, '.mkv')}">
                                                <video style="width: 80%;" controls class="rounded shadow">
                                                    <source src="${pageContext.request.contextPath}/${media.mediaUrl}" />
                                                    Trình duyệt của bạn không hỗ trợ video.
                                                </video>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/${media.mediaUrl}"
                                                     alt="${media.caption}" width="400" class="rounded shadow" />
                                            </c:otherwise>
                                        </c:choose>
                                        
                                        <p class="mt-2 text-muted fst-italic">${media.caption}</p>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        
                        <!-- Full Content -->
                        <div>
                            <c:out value="${blogDetails.content}" escapeXml="false" />
                        </div>
                    </div>
                
                </div>
            </article>
        </main>
        
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
                    <a href="${pageContext.request.contextPath}/blog-list?category=${category.categoryId}" class="list-group-item">${category.category}</a>
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
<script src="js/lib/jquery-3.4.1.min.js"></script>
<script src="js/lib/bootstrap.bundle.min.js"></script>
<script src="lib/wow/wow.min.js"></script>
<script src="lib/easing/easing.min.js"></script>
<script src="lib/waypoints/waypoints.min.js"></script>
<script src="lib/owlcarousel/owl.carousel.min.js"></script>
</body>
</html>


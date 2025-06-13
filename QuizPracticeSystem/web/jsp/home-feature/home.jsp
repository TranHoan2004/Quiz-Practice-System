<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Quezee</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">

        <!-- Favicon -->
        <link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lib/css2.css">

        <!-- Icon Font Stylesheet -->
        <link rel="stylesheet" href="fontawesome/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Libraries Stylesheet -->
        <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">

        <!-- Customized Bootstrap Stylesheet -->
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">

        <link href="${pageContext.request.contextPath}/css/homepage.css" rel="stylesheet">
    </head>

    <body>
        <jsp:include page="../../component/spinner.html" />
        <jsp:include page="../../component/header.jsp" />


        <!-- Carousel Start -->
        <div class="container-fluid p-0 mb-5">
            <div class="owl-carousel header-carousel position-relative">
                <c:forEach items="${sliderActive}" var="slider">
                    <div class="owl-carousel-item position-relative">
                        <img class="img-fluid" src="${pageContext.request.contextPath}/${slider.imageUrl}" alt="" style="height: 600px; object-fit: cover;">
                        <div class="position-absolute top-0 start-0 w-100 h-100 d-flex align-items-center"
                             style="background: rgba(24, 29, 56, .7);">
                            <div class="container">
                                <div class="row justify-content-center">
                                    <div class="col-sm-10 col-lg-8 text-center">
                                        <h5 class="text-primary text-uppercase mb-3 animated slideInDown">Best Online
                                            Courses
                                        </h5>
                                        <h1 class="display-3 text-white animated slideInDown">
                                            ${slider.title}
                                        </h1>
                                        <p class="fs-5 text-white mb-4 pb-2">Vero elitr justo clita lorem. Ipsum dolor
                                            at sed
                                            stet sit diam no. Kasd rebum ipsum et diam justo clita et kasd rebum sea
                                            sanctus
                                            eirmod elitr.</p>
                                        <a href="${pageContext.request.contextPath}/${slider.backlinkUrl}" class="btn btn-primary py-md-3 px-md-5 me-3 animated slideInLeft">Read
                                            More</a>
                                        <a href="${pageContext.request.contextPath}/${slider.backlinkUrl}" class="btn btn-light py-md-3 px-md-5 animated slideInRight">Join
                                            Now</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <!-- Carousel End -->

        <%-- Body --%>
        <div class="container-xxl">
            <div class="row">
                <!-- Content Left -->
                <div class="col-9 mb-5">
                    <!-- Courses -->
                    <section class="">
                        <div class="container">
                            <div class="text-center wow fadeInUp" data-wow-delay="0.1s">
                                <h6 class="section-title bg-white text-center text-primary px-3">Course</h6>
                                <h1 class="mb-5">Feature Courses</h1>
                            </div>
                            
                            <div class="d-flex justify-content-between align-items-center mb-4 px-2 py-2 rounded bg-light shadow-sm">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-bookmark-star-fill text-primary me-2 fs-4"></i>
                                    <h4 class="mb-0 fw-semibold text-primary">Common Courses</h4>
                                </div>
                                <div>
                                    <button id="scrollLeft" class="btn btn-outline-primary me-2">
                                        <i class="bi bi-chevron-left"></i>
                                    </button>
                                    <button id="scrollRight" class="btn btn-outline-primary">
                                        <i class="bi bi-chevron-right"></i>
                                    </button>
                                </div>
                            </div>
                            
                            <div class="owl-carousel course-carousel">
                                <c:forEach items="${courses}" var="course">
                                    <div class="item">
                                        <a href="${pageContext.request.contextPath}/course-detail?id=${course.id}" class="course-link">
                                            <div class="card h-100 shadow-sm course-card">
                                                <img src="${pageContext.request.contextPath}/${course.image}" class="card-img-top course-img" alt="Scratch Nâng Cao">
                                                <div class="card-body">
                                                    <!-- HOT label -->
                                                    <div class="position-absolute top-0 start-0 bg-danger px-3 py-1 rounded-bottom-end text-white fw-semibold" style="font-size: 0.8rem; z-index: 10;">
                                                        🔥 HOT
                                                    </div>
                                                    <h3 class="course-title h5 mb-2">${course.title}</h3>
                                                    <p class="text-muted mb-2" style="font-size: 0.9rem;">
                                                            ${course.description}
                                                    </p>
                                                    <div class="d-flex align-items-center text-warning mb-2"
                                                         style="font-size: 1rem;">
                                                    </div>
                                                    <!-- Price -->
                                                    <div class="d-flex align-items-center justify-content-between mt-auto">
                                                        <div>
                                                            <span class="text-danger fw-bold fs-5">
                                                                ${course.salePrice} đ
                                                            </span>
                                                            <span class="text-muted text-decoration-line-through ms-2 small">
                                                                ${course.price} đ
                                                            </span>
                                                        </div>
                                                        <span class="badge bg-success fs-6">Sale</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </a>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </section>

                    <!-- Subject Start -->
                    <div class="container mt-5">
                        <div class="text-center wow fadeInUp" data-wow-delay="0.1s">
                            <h6 class="section-title bg-white text-center text-primary px-3">Subject</h6>
                            <h1 class="mb-5">Feature Subject</h1>
                        </div>
                        <div class="row g-3">
                            <!-- First 3 subjects -->
                            <div class="col-lg-7 col-md-6">
                                <div class="row g-3">
                                    <div class="col-lg-12 col-md-12 wow zoomIn" data-wow-delay="0.1s">
                                        <a class="position-relative d-block overflow-hidden" href="">
                                            <img class="img-fluid w-100 h-100" src="${pageContext.request.contextPath}/${featureSubject[0].thumbnailUrl}" alt="">
                                            <div class="bg-white text-center position-absolute bottom-0 end-0 py-2 px-3" style="margin: 1px;">
                                                <h5 class="m-0">${featureSubject[0].subjectName}</h5>
                                                <small class="text-primary">${featureSubject[0].tagline}</small>
                                            </div>
                                        </a>
                                    </div>
                                    <div class="col-lg-6 col-md-12 wow zoomIn" data-wow-delay="0.3s">
                                        <a class="position-relative d-block overflow-hidden" href="">
                                            <img class="img-fluid w-100 h-100" src="${pageContext.request.contextPath}/${featureSubject[1].thumbnailUrl}" alt="">
                                            <div class="bg-white text-center position-absolute bottom-0 end-0 py-2 px-3" style="margin: 1px;">
                                                <h5 class="m-0">Graphic Design</h5>
                                                <small class="text-primary">${featureSubject[1].tagline}</small>
                                            </div>
                                        </a>
                                    </div>
                                    <div class="col-lg-6 col-md-12 wow zoomIn" data-wow-delay="0.5s">
                                        <a class="position-relative d-block overflow-hidden" href="">
                                            <img class="img-fluid w-100 h-100" src="${pageContext.request.contextPath}/${featureSubject[2].thumbnailUrl}" alt="">
                                            <div class="bg-white text-center position-absolute bottom-0 end-0 py-2 px-3" style="margin: 1px;">
                                                <h5 class="m-0">Video Editing</h5>
                                                <small class="text-primary">${featureSubject[2].tagline}</small>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <!-- Right-side subject -->
                            <div class="col-lg-5 col-md-6 wow zoomIn" data-wow-delay="0.7s" style="min-height: 350px;">
                                <a class="position-relative d-block h-100 overflow-hidden" href="">
                                    <img class="img-fluid position-absolute w-100 h-100" src="${pageContext.request.contextPath}/${featureSubject[3].thumbnailUrl}" alt="Subject">
                                    <div class="bg-white text-center position-absolute bottom-0 end-0 py-2 px-3" style="margin:  1px;">
                                        <h5 class="m-0">Online Marketing</h5>
                                        <small class="text-primary">${featureSubject[3].tagline}</small>
                                    </div>
                                </a>
                            </div>
                        </div>
                    </div>
                    <!-- Subject End -->
                </div>

                <!-- Sidebar Right -->
                <div class="col-3">
                    <div class="container">
                        <div class="text-center wow fadeInUp" data-wow-delay="0.1s">
                            <h6 class="section-title bg-white text-center text-primary px-3">Blog</h6>
                            <h1 class="mb-5">New Posts</h1>
                        </div>
                        <div class="row justify-content-center align-items-center">
                            <div class="new-posts-wrap d-flex flex-column gap-4">
                                <c:forEach items="${latestBlogs}" var="blog">
                                    <div class="new-posts--item bg-light h-100 d-flex justify-content-center align-items-center">
                                        <div class="p-2 d-flex flex-column">
                                            <a href="blog-detail.html" class="new-posts--heading text-dark fs-6 mb-0 link-primary">
                                                ${blog.title}
                                            </a>
                                            <div class="d-flex align-items-center gap-2 mb-2">
                                                <img src="${pageContext.request.contextPath}/${blog.avatarUrl}" class="rounded-circle" alt="">
                                                <div class="user-info">
                                                    <a href="#" class="fw-bold latest-posts-poster">${blog.accountName}</a>
                                                    <div class="text-muted">
                                                        <i class="fa fa-calendar-alt"></i>
                                                        ${blog.createdDate}
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="d-flex gap-2">
                                                <img src="${pageContext.request.contextPath}/${blog.thumbnailUrl}" alt="Thumbnail"
                                                     style="width: 80px; height: 60px; object-fit: cover;">
                                                <p class="new-posts--item-content mb-0">${blog.content}</p>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>

                                <%--						<!-- Contact -->--%>
                                <%--						<div class="new-posts--item bg-light h-100 d-flex justify-content-center align-items-center">--%>
                                <%--							<div class="p-3 w-100 h-100 d-flex flex-column">--%>
                                <%--								<h4 class="mb-3 text-center">Contact</h4>--%>
                                <%--								<p class="mb-2"><i class="fa fa-map-marker-alt me-3"></i>123 Street, New York, USA</p>--%>
                                <%--								<p class="mb-2"><i class="fa fa-phone-alt me-3"></i>+012 345 67890</p>--%>
                                <%--								<p class="mb-2"><i class="fa fa-envelope me-3"></i>info@example.com</p>--%>
                                <%--							</div>--%>
                                <%--						</div>--%>
                                <%--						<!-- Contact End -->--%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Blog Start -->
            <div class="mb-5 mt-5">
                <div class="text-center wow fadeInUp" data-wow-delay="0.1s">
                    <h6 class="section-title bg-white text-center text-primary px-3">Blog</h6>
                    <h1 class="mb-5">Hot Posts</h1>
                </div>
                <div class="row justify-content-center align-items-center">
                    <!-- Button left -->
                    <div class="col-auto">
                        <button class="btn btn-primary btn-lg-square rounded-circle blog-carousel-nav-btn left"
                                style="border:2px solid #fff;">
                            <i class="bi bi-chevron-left text-white"></i>
                        </button>
                    </div>

                    <!-- Hot Posts -->
                    <div class="col-lg-10">
                        <div class="owl-carousel blog-carousel">
                            <c:forEach items="${hottestBlogs}" var="blog">
                                <div class="blog-item bg-light d-flex align-items-center">
                                    <div class="p-3 w-100 h-100 d-flex flex-column">
                                        <a href="blog-detail.html"
                                           class="h5 d-block mb-2 text-dark text-decoration-none text-center link-primary">
                                            ${blog.title}</a>
                                        <div class="d-flex align-items-center gap-2 mb-2">
                                            <img src="${pageContext.request.contextPath}/${blog.avatarUrl}"
                                                 class="rounded-circle" alt="">
                                            <div class="div">
                                                <a href="#" class="fw-bold">${blog.accountName}</a>
                                                <div class="text-muted">
                                                    <i class="fa fa-calendar-alt me-2"></i>
                                                    ${blog.createdDate}
                                                </div>
                                            </div>
                                        </div>

                                        <div class="w-100 h-100 d-flex align-content-center justify-content-between gap-4">
                                            <img src="${pageContext.request.contextPath}/${blog.thumbnailUrl}" alt="Thumnail"
                                                 class="img-fluid w-50 h-50">
                                            <p class="blog-item-content mb-0">
                                                ${blog.content}
                                            </p>
                                        </div>

                                        <a href="blog-detail?id=${blog.id}" class="d-flex flex-row-reverse text-decoration-underline">Click here to see more</a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <!-- Button right -->
                    <div class="col-auto">
                        <button class="btn btn-primary btn-lg-square rounded-circle blog-carousel-nav-btn right"
                                style="border:2px solid #fff;">
                            <i class="bi bi-chevron-right text-white"></i>
                        </button>
                    </div>
                </div>
            </div>
            <!-- Blog End -->                
        </div>

        <!-- About Start -->
        <div class="container mt-5">
            <div class="row g-5">
                <div class="col-lg-6 wow fadeInUp" data-wow-delay="0.1s" style="min-height: 400px;">
                    <div class="position-relative h-100">
                        <img class="img-fluid position-absolute w-100 h-100" src="img/about.jpg" alt=""
                             style="object-fit: cover;">
                    </div>
                </div>
                <div class="col-lg-6 wow fadeInUp" data-wow-delay="0.3s">
                    <h6 class="section-title bg-white text-start text-primary pe-3">About Us</h6>
                    <h1 class="mb-4">Welcome to QUEZEE</h1>
                    <p class="mb-4">Welcome to Quiz Learning — your friendly companion on the journey of knowledge
                        and skill mastery!
                        At Quiz Learning, we believe that learning should be fun, engaging, and effective.
                    </p>
                    <p class="mb-4">Our platform offers a wide range of quizzes across various subjects,
                        designed to help learners of all ages test their knowledge, reinforce what they've learned,
                        and challenge themselves to reach new heights.
                    </p>
                    <div class="row gy-2 gx-4 mb-4">
                        <div class="col-sm-6">
                            <p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>Skilled
                                Instructors</p>
                        </div>
                        <div class="col-sm-6">
                            <p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>Online
                                Classes
                            </p>
                        </div>
                        <div class="col-sm-6">
                            <p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>International
                                Certificate
                            </p>
                        </div>
                        <div class="col-sm-6">
                            <p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>Skilled
                                Instructors</p>
                        </div>
                        <div class="col-sm-6">
                            <p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>Online
                                Classes
                            </p>
                        </div>
                        <div class="col-sm-6">
                            <p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>International
                                Certificate
                            </p>
                        </div>
                    </div>
                    <a class="btn btn-primary py-3 px-5 mt-2" href="">Read More</a>
                </div>
            </div>
        </div>
        <!-- About End -->

        <jsp:include page="../../component/footer.html" />


        <!-- Back to Top -->
        <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>


        <!-- JavaScript Libraries -->
        <script src="js/lib/jquery-3.4.1.min.js"></script>
        <script src="js/lib/bootstrap.bundle.min.js"></script>
        <script src="lib/wow/wow.min.js"></script>
        <script src="lib/easing/easing.min.js"></script>
        <script src="lib/waypoints/waypoints.min.js"></script>
        <script src="lib/owlcarousel/owl.carousel.min.js"></script>

        <!-- Template Javascript -->
        <script src="js/HomePage.js"></script>
    </body>

</html>
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
	<link href="img/favicon.ico" rel="icon">
	
	<!-- Google Web Fonts -->
	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link rel="stylesheet" href="css/lib/css2.css">
	
	<!-- Icon Font Stylesheet -->
	<link rel="stylesheet" href="fontawesome/css/all.min.css">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
	
	<!-- Libraries Stylesheet -->
	<link href="lib/animate/animate.min.css" rel="stylesheet">
	<link href="lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
	
	<!-- Customized Bootstrap Stylesheet -->
	<link href="css/bootstrap.min.css" rel="stylesheet">
	
	<!-- Template Stylesheet -->
	<link href="css/homepage.css" rel="stylesheet">
</head>

<body>
<jsp:include page="../../component/spinner.html"></jsp:include>

<!-- Navbar Start -->
<nav class="navbar navbar-expand-lg bg-white navbar-light shadow sticky-top p-0">
	<a href="index.html" class="navbar-brand d-flex align-items-center px-4 px-lg-5">
		<h2 class="m-0 text-primary"><i class="fa fa-book me-3"></i>Quezee</h2>
	</a>
	<button type="button" class="navbar-toggler me-4" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarCollapse">
		<div class="navbar-nav ms-auto p-4 p-lg-0">
			<a href="index.html" class="nav-item nav-link active">Home</a>
			<a href="courses.html" class="nav-item nav-link">Courses</a>
			<a href="contact.html" class="nav-item nav-link">Contact</a>
			<div class="nav-item dropdown">
				<a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">More</a>
				<div class="dropdown-menu fade-down m-0">
					<a href="about.html" class="dropdown-item">About Us</a>
					<a href="team.html" class="dropdown-item">Our Team</a>
					<a href="testimonial.html" class="dropdown-item">Testimonials</a>
				</div>
			</div>
		</div>
		<!-- User Authentication Section -->
		<div class="d-flex align-items-center px-4 px-lg-5">
			<!-- Not Logged In -->
			<div class="not-logged-in d-flex align-items-center">
				<a href="login.html"
				   class="btn btn-outline-primary me-2 d-flex align-items-center justify-content-center"
				   style="height: 40px;">Login</a>
				<a href="register.html" class="btn btn-primary d-flex align-items-center justify-content-center"
				   style="height: 40px;">Register</a>
			</div>
			<!-- Logged In -->
			<div class="logged-in d-none">
				<div class="dropdown">
					<a href="#" class="d-flex align-items-center text-decoration-none dropdown-toggle"
					   id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
						<img src="img/user-avatar.jpg" alt="User Avatar" class="rounded-circle me-2" width="32"
							 height="32">
						<span class="d-none d-md-inline">John Doe</span>
					</a>
					<ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
						<li><a class="dropdown-item" href="profile.html"><i class="fas fa-user me-2"></i>Profile</a>
						</li>
						<li><a class="dropdown-item" href="my-courses.html"><i
								class="fas fa-graduation-cap me-2"></i>My Courses</a></li>
						<li>
							<hr class="dropdown-divider">
						</li>
						<li><a class="dropdown-item" href="logout.html"><i
								class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</nav>
<!-- Navbar End -->

<!-- Carousel Start -->
<div class="container-fluid p-0 mb-5">
	<div class="owl-carousel header-carousel position-relative">
		<div class="owl-carousel-item position-relative">
			<img class="img-fluid" src="img/carousel-1.jpg" alt="" style="height: 600px; object-fit: cover;">
			<div class="position-absolute top-0 start-0 w-100 h-100 d-flex align-items-center"
				 style="background: rgba(24, 29, 56, .7);">
				<div class="container">
					<div class="row justify-content-center">
						<div class="col-sm-10 col-lg-8 text-center">
							<h5 class="text-primary text-uppercase mb-3 animated slideInDown">Best Online
								Courses
							</h5>
							<h1 class="display-3 text-white animated slideInDown">The Best Online Learning
								Platform
							</h1>
							<p class="fs-5 text-white mb-4 pb-2">Vero elitr justo clita lorem. Ipsum dolor
								at sed
								stet sit diam no. Kasd rebum ipsum et diam justo clita et kasd rebum sea
								sanctus
								eirmod elitr.</p>
							<a href="" class="btn btn-primary py-md-3 px-md-5 me-3 animated slideInLeft">Read
								More</a>
							<a href="" class="btn btn-light py-md-3 px-md-5 animated slideInRight">Join
								Now</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="owl-carousel-item position-relative">
			<img class="img-fluid" src="img/carousel-2.jpg" alt="" style="height: 600px; object-fit: cover;">
			<div class="position-absolute top-0 start-0 w-100 h-100 d-flex align-items-center"
				 style="background: rgba(24, 29, 56, .7);">
				<div class="container">
					<div class="row justify-content-center">
						<div class="col-sm-10 col-lg-8 text-center">
							<h5 class="text-primary text-uppercase mb-3 animated slideInDown">Best Online
								Courses
							</h5>
							<h1 class="display-3 text-white animated slideInDown">Get Educated Online From
								Your Home
							</h1>
							<p class="fs-5 text-white mb-4 pb-2">Vero elitr justo clita lorem. Ipsum dolor
								at sed
								stet sit diam no. Kasd rebum ipsum et diam justo clita et kasd rebum sea
								sanctus
								eirmod elitr.</p>
							<a href="" class="btn btn-primary py-md-3 px-md-5 me-3 animated slideInLeft">Read
								More</a>
							<a href="" class="btn btn-light py-md-3 px-md-5 animated slideInRight">Join
								Now</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- Carousel End -->

<!-- Hot Posts -->
<div class="container">
	<div class="row">
		<div class="col-8 mb-5">
			<!-- Blog Start -->
			<div class="mb-5">
				<div class="text-center wow fadeInUp" data-wow-delay="0.1s">
					<h6 class="section-title bg-white text-center text-primary px-3">Blog</h6>
					<h1 class="mb-5">Hot Posts</h1>
				</div>
				<div class="row justify-content-center align-items-center">
					<!-- Button trái -->
					<div class="col-auto">
						<button class="btn btn-primary btn-lg-square rounded-circle blog-carousel-nav-btn left"
								style="border:2px solid #fff;">
							<i class="bi bi-chevron-left text-white"></i>
						</button>
					</div>
					
					<!-- Carousel -->
					<div class="col-lg-10">
						<div class="owl-carousel blog-carousel">
							<c:forEach items="${hottestBlogs}" var="blog">
								<div class="blog-item bg-light d-flex align-items-center">
									<div class="p-3 w-100 h-100 d-flex flex-column">
										<a href="blog-detail.html"
										   class="h5 d-block mb-2 text-dark text-decoration-none text-center">
											${blog.title}</a>
										<div class="d-flex align-items-center gap-2 mb-2">
											<img src="${blog.avatarUrl}"
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
											<img src="${blog.thumbnailUrl}" alt="Thumnail"
												 class="img-fluid w-75 h-75"
											>
											<p class="blog-item-content mb-0">
												${blog.content}
											</p>
										</div>
										
										<a href="blog-detail?id=${blog.id}" class="d-flex flex-row-reverse text-decoration-underline">Click here to see more</a>
									</div>
								</div>
							</c:forEach>
							
							<!-- Item 2 -->
							<div class="blog-item bg-light d-flex align-items-center">
								<div class="p-3 w-100 h-100 d-flex flex-column">
									<a href="blog-detail.html"
									   class="h5 d-block mb-2 text-dark text-decoration-none text-center">
										🌟 Bí quyết giữ năng lượng tích cực mỗi ngày</a>
									<div class="d-flex align-items-center gap-2 mb-2">
										<img src="https://sm.ign.com/ign_nordic/cover/a/avatar-gen/avatar-generations_prsz.jpg"
											 class="rounded-circle" alt="">
										<div class="div">
											<a href="#" class="fw-bold">Nguyễn Văn A</a>
											<div class="text-muted"><i
													class="fa fa-calendar-alt me-2"></i>12/06/2024
											</div>
										</div>
									</div>
									
									
									<div class="w-100 h-100 d-flex align-content-center justify-content-between gap-4">
										<img src="https://iso.500px.com/wp-content/uploads/2014/06/W4A2827-1.jpg" alt="Thumnail"
											class="img-fluid w-75 h-75"
										>
										<p class="blog-item-content mb-0">
											Trong cuộc sống hiện đại, việc duy trì năng lượng tích cực là rất
											quan
											trọng.
											Hãy cùng khám phá những bí quyết đơn giản nhưng hiệu quả để giữ cho
											tâm
											trạng
											luôn lạc quan và tràn đầy sức sống. Trong cuộc sống hiện đại, việc
											Trong cuộc sống hiện đại, việc duy trì năng lượng tích cực là rất
											quan
											trọng.
											Hãy cùng khám phá những bí quyết đơn giản nhưng hiệu quả để giữ cho
											tâm
											trạng
											luôn lạc quan và tràn đầy sức sống. Trong cuộc sống hiện đại, việc
										</p>
									</div>
									
									<a href="#" class="d-flex flex-row-reverse text-decoration-underline">Click here to see more</a>
								</div>
							</div>
							
							<!-- Item 3 -->
							<div class="blog-item bg-light d-flex align-items-center">
								<div class="p-3 w-100 h-100 d-flex flex-column">
									<a href="blog-detail.html"
									   class="h5 d-block mb-2 text-dark text-decoration-none text-center">
										🌟 Bí quyết giữ năng lượng tích cực mỗi ngày</a>
									<div class="d-flex align-items-center gap-2 mb-2">
										<img src="https://sm.ign.com/ign_nordic/cover/a/avatar-gen/avatar-generations_prsz.jpg"
											 class="rounded-circle" alt="">
										<div class="div">
											<a href="#" class="fw-bold">Nguyễn Văn A</a>
											<div class="text-muted"><i
													class="fa fa-calendar-alt me-2"></i>12/06/2024
											</div>
										</div>
									</div>
									
									
									<div class="w-100 h-100 d-flex align-content-center justify-content-between gap-4">
										<img src="https://iso.500px.com/wp-content/uploads/2014/06/W4A2827-1.jpg" alt="Thumnail"
											 class="img-fluid w-75 h-75"
										>
										<p class="blog-item-content mb-0">
											Trong cuộc sống hiện đại, việc duy trì năng lượng tích cực là rất
											quan
											trọng.
											Hãy cùng khám phá những bí quyết đơn giản nhưng hiệu quả để giữ cho
											tâm
											trạng
											luôn lạc quan và tràn đầy sức sống. Trong cuộc sống hiện đại, việc
											Trong cuộc sống hiện đại, việc duy trì năng lượng tích cực là rất
											quan
											trọng.
											Hãy cùng khám phá những bí quyết đơn giản nhưng hiệu quả để giữ cho
											tâm
											trạng
											luôn lạc quan và tràn đầy sức sống. Trong cuộc sống hiện đại, việc
										</p>
									</div>
									
									<a href="#" class="d-flex flex-row-reverse text-decoration-underline">Click here to see more</a>
								</div>
							</div>
						</div>
					</div>
					
					<!-- Button phải -->
					<div class="col-auto">
						<button class="btn btn-primary btn-lg-square rounded-circle blog-carousel-nav-btn right"
								style="border:2px solid #fff;">
							<i class="bi bi-chevron-right text-white"></i>
						</button>
					</div>
				</div>
			</div>
			<!-- Blog End -->
			
			<!-- Categories Start -->
			<div class="container mt-5">
				<div class="text-center wow fadeInUp" data-wow-delay="0.1s">
					<h6 class="section-title bg-white text-center text-primary px-3">Categories</h6>
					<h1 class="mb-5">Courses Categories</h1>
				</div>
				<div class="row g-3">
					<div class="col-lg-7 col-md-6">
						<div class="row g-3">
							<div class="col-lg-12 col-md-12 wow zoomIn" data-wow-delay="0.1s">
								<a class="position-relative d-block overflow-hidden" href="">
									<img class="img-fluid" src="img/cat-1.jpg" alt="">
									<div class="bg-white text-center position-absolute bottom-0 end-0 py-2 px-3"
										 style="margin: 1px;">
										<h5 class="m-0">Web Design</h5>
										<small class="text-primary">49 Courses</small>
									</div>
								</a>
							</div>
							<div class="col-lg-6 col-md-12 wow zoomIn" data-wow-delay="0.3s">
								<a class="position-relative d-block overflow-hidden" href="">
									<img class="img-fluid" src="img/cat-2.jpg" alt="">
									<div class="bg-white text-center position-absolute bottom-0 end-0 py-2 px-3"
										 style="margin: 1px;">
										<h5 class="m-0">Graphic Design</h5>
										<small class="text-primary">49 Courses</small>
									</div>
								</a>
							</div>
							<div class="col-lg-6 col-md-12 wow zoomIn" data-wow-delay="0.5s">
								<a class="position-relative d-block overflow-hidden" href="">
									<img class="img-fluid" src="img/cat-3.jpg" alt="">
									<div class="bg-white text-center position-absolute bottom-0 end-0 py-2 px-3"
										 style="margin: 1px;">
										<h5 class="m-0">Video Editing</h5>
										<small class="text-primary">49 Courses</small>
									</div>
								</a>
							</div>
						</div>
					</div>
					<div class="col-lg-5 col-md-6 wow zoomIn" data-wow-delay="0.7s" style="min-height: 350px;">
						<a class="position-relative d-block h-100 overflow-hidden" href="">
							<img class="img-fluid position-absolute w-100 h-100" src="img/cat-4.jpg" alt=""
								 style="object-fit: cover;">
							<div class="bg-white text-center position-absolute bottom-0 end-0 py-2 px-3"
								 style="margin:  1px;">
								<h5 class="m-0">Online Marketing</h5>
								<small class="text-primary">49 Courses</small>
							</div>
						</a>
					</div>
				</div>
			</div>
			<!-- Categories Start -->
		</div>
		
		<div class="col-4">
			<!-- Blog Start -->
			<div class="container">
				<div class="text-center wow fadeInUp" data-wow-delay="0.1s">
					<h6 class="section-title bg-white text-center text-primary px-3">Blog</h6>
					<h1 class="mb-5">New Posts</h1>
				</div>
				<div class="row justify-content-center align-items-center">
					<!-- Carousel -->
					<div class="new-posts-wrap d-flex flex-column gap-4">
						<!-- Item 1 -->
						<div
								class="new-posts--item bg-light h-100 d-flex justify-content-center align-items-center">
							<div class="p-3 w-100 h-100 d-flex flex-column">
								<a href="blog-detail.html" class="new-posts--heading text-dark fs-6 mb-0">
									🌟 Bí quyết giữ năng lượng tích cực mỗi ngày
								</a>
								<div class="d-flex align-items-center gap-2 mb-2">
									<img src="https://sm.ign.com/ign_nordic/cover/a/avatar-gen/avatar-generations_prsz.jpg"
										 class="rounded-circle" alt="">
									<div class="user-info">
										<a href="#" class="fw-bold latest-posts-poster">Nguyễn Văn A</a>
										<div class="text-muted">
											<i class="fa fa-calendar-alt"></i>
											12/06/2024
										</div>
									</div>
								</div>
								
								
								<div class="w-100 h-100 d-flex align-content-center justify-content-between gap-2">
									<img src="https://iso.500px.com/wp-content/uploads/2014/06/W4A2827-1.jpg" alt="Thumnail"
										 class="img-fluid w-50 h-50"
									>
									<p class="new-posts--item-content">
										Trong cuộc sống hiện đại, việc duy trì năng lượng tích cực là rất
										quan
										trọng.
										Hãy cùng khám phá những bí quyết đơn giản nhưng hiệu quả để giữ cho
										tâm
										trạng
									</p>
								</div>
							</div>
						</div>
						
						<!-- Item 1 -->
						<div
								class="new-posts--item bg-light h-100 d-flex justify-content-center align-items-center">
							<div class="p-3 w-100 h-100 d-flex flex-column">
								<a href="blog-detail.html" class="new-posts--heading text-dark fs-6 mb-0">
									🌟 Bí quyết giữ năng lượng tích cực mỗi ngày
								</a>
								<div class="d-flex align-items-center gap-2 mb-2">
									<img src="https://sm.ign.com/ign_nordic/cover/a/avatar-gen/avatar-generations_prsz.jpg"
										 class="rounded-circle" alt="">
									<div class="user-info">
										<a href="#" class="fw-bold latest-posts-poster">Nguyễn Văn A</a>
										<div class="text-muted">
											<i class="fa fa-calendar-alt"></i>
											12/06/2024
										</div>
									</div>
								</div>
								
								
								<div class="w-100 h-100 d-flex align-content-center justify-content-between gap-2">
									<img src="https://iso.500px.com/wp-content/uploads/2014/06/W4A2827-1.jpg" alt="Thumnail"
										 class="img-fluid w-50 h-50"
									>
									<p class="new-posts--item-content">
										Trong cuộc sống hiện đại, việc duy trì năng lượng tích cực là rất
										quan
										trọng.
										Hãy cùng khám phá những bí quyết đơn giản nhưng hiệu quả để giữ cho
										tâm
										trạng
									</p>
								</div>
							</div>
						</div>
						
						<!-- Item 1 -->
						<div
								class="new-posts--item bg-light h-100 d-flex justify-content-center align-items-center">
							<div class="p-3 w-100 h-100 d-flex flex-column">
								<a href="blog-detail.html" class="new-posts--heading text-dark fs-6 mb-0">
									🌟 Bí quyết giữ năng lượng tích cực mỗi ngày
								</a>
								<div class="d-flex align-items-center gap-2 mb-2">
									<img src="https://sm.ign.com/ign_nordic/cover/a/avatar-gen/avatar-generations_prsz.jpg"
										 class="rounded-circle" alt="">
									<div class="user-info">
										<a href="#" class="fw-bold latest-posts-poster">Nguyễn Văn A</a>
										<div class="text-muted">
											<i class="fa fa-calendar-alt"></i>
											12/06/2024
										</div>
									</div>
								</div>
								
								
								<div class="w-100 h-100 d-flex align-content-center justify-content-between gap-2">
									<img src="https://iso.500px.com/wp-content/uploads/2014/06/W4A2827-1.jpg" alt="Thumnail"
										 class="img-fluid w-50 h-50"
									>
									<p class="new-posts--item-content">
										Trong cuộc sống hiện đại, việc duy trì năng lượng tích cực là rất
										quan
										trọng.
										Hãy cùng khám phá những bí quyết đơn giản nhưng hiệu quả để giữ cho
										tâm
										trạng
									</p>
								</div>
							</div>
						</div>
						
						
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
					<h1 class="mb-4">Welcome to eLEARNING</h1>
					<p class="mb-4">Tempor erat elitr rebum at clita. Diam dolor diam ipsum sit. Aliqu diam
						amet
						diam et
						eos. Clita erat ipsum et lorem et sit.</p>
					<p class="mb-4">Tempor erat elitr rebum at clita. Diam dolor diam ipsum sit. Aliqu diam
						amet
						diam et
						eos. Clita erat ipsum et lorem et sit, sed stet lorem sit clita duo justo magna
						dolore
						erat amet
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
	</div>
</div>
<!-- Hot Posts End -->

<jsp:include page="../../component/footer.html"></jsp:include>


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
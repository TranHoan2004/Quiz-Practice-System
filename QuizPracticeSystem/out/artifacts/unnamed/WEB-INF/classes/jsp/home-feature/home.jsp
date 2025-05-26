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
	<link href="css/style.css" rel="stylesheet">
</head>

<body>
<jsp:include page="../../component/spinner.html"></jsp:include>
<jsp:include page="../../component/navbar.html"></jsp:include>
<jsp:include page="../../component/header.html"></jsp:include>


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
							<h5 class="text-primary text-uppercase mb-3 animated slideInDown">Best Online Courses
							</h5>
							<h1 class="display-3 text-white animated slideInDown">The Best Online Learning Platform
							</h1>
							<p class="fs-5 text-white mb-4 pb-2">Vero elitr justo clita lorem. Ipsum dolor at sed
								stet sit diam no. Kasd rebum ipsum et diam justo clita et kasd rebum sea sanctus
								eirmod elitr.</p>
							<a href="" class="btn btn-primary py-md-3 px-md-5 me-3 animated slideInLeft">Read
								More</a>
							<a href="" class="btn btn-light py-md-3 px-md-5 animated slideInRight">Join Now</a>
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
							<h5 class="text-primary text-uppercase mb-3 animated slideInDown">Best Online Courses
							</h5>
							<h1 class="display-3 text-white animated slideInDown">Get Educated Online From Your Home
							</h1>
							<p class="fs-5 text-white mb-4 pb-2">Vero elitr justo clita lorem. Ipsum dolor at sed
								stet sit diam no. Kasd rebum ipsum et diam justo clita et kasd rebum sea sanctus
								eirmod elitr.</p>
							<a href="" class="btn btn-primary py-md-3 px-md-5 me-3 animated slideInLeft">Read
								More</a>
							<a href="" class="btn btn-light py-md-3 px-md-5 animated slideInRight">Join Now</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- Carousel End -->





<!-- About Start -->
<div class="container-xxl py-5">
	<div class="container">
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
				<p class="mb-4">Tempor erat elitr rebum at clita. Diam dolor diam ipsum sit. Aliqu diam amet diam et
					eos. Clita erat ipsum et lorem et sit.</p>
				<p class="mb-4">Tempor erat elitr rebum at clita. Diam dolor diam ipsum sit. Aliqu diam amet diam et
					eos. Clita erat ipsum et lorem et sit, sed stet lorem sit clita duo justo magna dolore erat amet
				</p>
				<div class="row gy-2 gx-4 mb-4">
					<div class="col-sm-6">
						<p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>Skilled Instructors</p>
					</div>
					<div class="col-sm-6">
						<p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>Online Classes</p>
					</div>
					<div class="col-sm-6">
						<p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>International Certificate
						</p>
					</div>
					<div class="col-sm-6">
						<p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>Skilled Instructors</p>
					</div>
					<div class="col-sm-6">
						<p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>Online Classes</p>
					</div>
					<div class="col-sm-6">
						<p class="mb-0"><i class="fa fa-arrow-right text-primary me-2"></i>International Certificate
						</p>
					</div>
				</div>
				<a class="btn btn-primary py-3 px-5 mt-2" href="">Read More</a>
			</div>
		</div>
	</div>
</div>
<!-- About End -->


<!-- Categories Start -->
<div class="container-xxl py-5 category">
	<div class="container">
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
</div>
<!-- Categories Start -->


<!-- Team Start -->
<div class="container-xxl py-5">
	<div class="container">
		<div class="text-center wow fadeInUp" data-wow-delay="0.1s">
			<h6 class="section-title bg-white text-center text-primary px-3">Blog</h6>
			<h1 class="mb-5">Bài viết nổi bật</h1>
		</div>
		<div class="row justify-content-center align-items-center">
			<div class="col-lg-1 d-flex justify-content-center">
				<button class="btn btn-primary btn-lg-square rounded-circle blog-prev"
						style="border:2px solid #fff;"><i class="bi bi-chevron-left text-white"></i></button>
			</div>
			<div class="col-lg-10">
				<div class="owl-carousel blog-carousel">
					<div class="blog-item bg-light h-100">
						<a href="blog-detail.html" class="d-block overflow-hidden rounded-top">
							<img class="img-fluid w-100" src="img/blog-1.jpg" alt="">
						</a>
						<div class="p-4">
							<a href="blog-detail.html" class="h5 d-block mb-2 text-dark text-decoration-none">🌟 Tựa
								đề: Bí quyết giữ năng lượng tích cực mỗi ngày</a>
							<div class="mb-2 text-muted"><i class="fa fa-calendar-alt me-2"></i>12/06/2024</div>
							<div class="d-flex align-items-center mb-2">
								<img src="img/user-avatar.jpg" alt="avatar" class="rounded-circle me-2" width="32"
									 height="32">
								<span class="fw-bold">Nguyễn Văn A</span>
							</div>
							<div class="blog-content text-muted">Cuộc sống hiện đại khiến chúng ta dễ rơi vào căng
								thẳng và mệt mỏi. Tuy nhiên, việc duy trì năng lượng tích cực không hề khó nếu bạn
								biết cách chăm sóc bản thân từ những điều nhỏ nhặt mỗi ngày.
								
								Một ly nước ấm buổi sáng, 5 phút hít thở sâu, hay thói quen ghi lại 3 điều khiến bạn
								biết ơn trước khi ngủ... đều là những việc làm đơn giản nhưng có tác động mạnh mẽ
								đến tinh thần. Ngoài ra, hãy bao quanh mình với những người tích cực, biết lắng nghe
								và chia sẻ. Cảm xúc cũng "lây lan" – vì vậy, hãy chọn điều tốt để lan tỏa.
								
								Hãy nhớ: năng lượng bạn mang theo trong ngày hôm nay sẽ ảnh hưởng đến chính bạn và
								cả những người xung quanh. Bắt đầu từ hôm nay, hãy chọn sống tích cực!
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-1 d-flex justify-content-center">
				<button class="btn btn-primary btn-lg-square rounded-circle blog-next"
						style="border:2px solid #fff;"><i class="bi bi-chevron-right text-white"></i></button>
			</div>
		</div>
	</div>
</div>
<!-- Blog End -->


<!-- Testimonial Start -->
<div class="container-xxl py-5 wow fadeInUp" data-wow-delay="0.1s">
	<div class="container">
		<div class="text-center">
			<h6 class="section-title bg-white text-center text-primary px-3">Testimonial</h6>
			<h1 class="mb-5">Our Students Say!</h1>
		</div>
		<div class="owl-carousel testimonial-carousel position-relative">
			<div class="testimonial-item text-center">
				<img class="border rounded-circle p-2 mx-auto mb-3" src="img/testimonial-1.jpg"
					 style="width: 80px; height: 80px;">
				<h5 class="mb-0">Client Name</h5>
				<p>Profession</p>
				<div class="testimonial-text bg-light text-center p-4">
					<p class="mb-0">Tempor erat elitr rebum at clita. Diam dolor diam ipsum sit diam amet diam et
						eos. Clita erat ipsum et lorem et sit.</p>
				</div>
			</div>
			<div class="testimonial-item text-center">
				<img class="border rounded-circle p-2 mx-auto mb-3" src="img/testimonial-2.jpg"
					 style="width: 80px; height: 80px;">
				<h5 class="mb-0">Client Name</h5>
				<p>Profession</p>
				<div class="testimonial-text bg-light text-center p-4">
					<p class="mb-0">Tempor erat elitr rebum at clita. Diam dolor diam ipsum sit diam amet diam et
						eos. Clita erat ipsum et lorem et sit.</p>
				</div>
			</div>
			<div class="testimonial-item text-center">
				<img class="border rounded-circle p-2 mx-auto mb-3" src="img/testimonial-3.jpg"
					 style="width: 80px; height: 80px;">
				<h5 class="mb-0">Client Name</h5>
				<p>Profession</p>
				<div class="testimonial-text bg-light text-center p-4">
					<p class="mb-0">Tempor erat elitr rebum at clita. Diam dolor diam ipsum sit diam amet diam et
						eos. Clita erat ipsum et lorem et sit.</p>
				</div>
			</div>
			<div class="testimonial-item text-center">
				<img class="border rounded-circle p-2 mx-auto mb-3" src="img/testimonial-4.jpg"
					 style="width: 80px; height: 80px;">
				<h5 class="mb-0">Client Name</h5>
				<p>Profession</p>
				<div class="testimonial-text bg-light text-center p-4">
					<p class="mb-0">Tempor erat elitr rebum at clita. Diam dolor diam ipsum sit diam amet diam et
						eos. Clita erat ipsum et lorem et sit.</p>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- Testimonial End -->


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
<script src="js/main.js"></script>
</body>

</html>
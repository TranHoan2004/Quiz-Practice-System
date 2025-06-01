<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <%--	<link rel="stylesheet" href="https://unpkg.com/bootstrap@5.3.3/dist/css/bootstrap.min.css">--%>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <%--	<link rel="stylesheet" href="https://unpkg.com/bs-brain@2.0.4/components/logins/login-5/assets/css/login-5.css">--%>
    <link href="${pageContext.request.contextPath}/css/lib/login-5.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/fontawesome/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/practicelist.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>

<body>
<jsp:include page="../../component/header.jsp"/>

<!-- Register - Bootstrap Brain Component -->
<section class="p-3 p-md-4 p-xl-5">
    <div class="container">
        <div class="card border-light-subtle shadow-sm">
            <div class="row g-0">
                <div class="col-12 col-md-6 text-bg-primary">
                    <div class="d-flex align-items-center justify-content-center h-100">
                        <div class="col-10 col-xl-8 py-3">
                            <img class="img-fluid rounded mb-4 w-100" loading="lazy"
                                 src="https://ocd.vn/wp-content/uploads/2020/06/hoc-truc-tuyen-ocd-elearning.jpg"
                                 width="245" height="80" alt="BootstrapBrain Logo">
                            <hr class="border-primary-subtle mb-4">
                            <h2 class="h1 mb-4">Join us and start your journey today.</h2>
                            <p class="lead m-0">Create your account to access exclusive features and content.</p>
                        </div>
                    </div>
                </div>
                <div class="col-12 col-md-6">
                    <div class="card-body p-3 p-md-4 p-xl-5">
                        <div class="row">
                            <div class="col-12">
                                <div class="mb-5">
                                    <h3>Register</h3>
                                </div>
                            </div>
                        </div>
                        <div>
                            <span style="color: red">${message}</span>
                        </div>
                        <form action="${pageContext.request.contextPath}/user/register" method="post" class="row g-3">
                            <div class="row gy-3 gy-md-4 overflow-hidden">
                                <div class="col-12">
                                    <label for="fullName" class="form-label">Full Name<span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="fullName" id="fullName"
                                           placeholder="Full Name" required>
                                </div>

                                <div class="col-6">
                                    <label class="form-label d-block">Gender <span class="text-danger">*</span></label>
                                    <div class="d-flex">
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="gender" id="gender-male"
                                                   value="0" required>
                                            <label class="form-check-label" for="gender-male">Male</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="gender"
                                                   id="gender-female" value="1">
                                            <label class="form-check-label" for="gender-female">Female</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="gender" id="gender-other"
                                                   value="2">
                                            <label class="form-check-label" for="gender-other">Other</label>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-6">
                                    <label for="phoneNumber" class="form-label">Phone number<span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="phoneNumber" id="phoneNumber"
                                           placeholder="Phone Number" required maxlength="10" minlength="10">
                                </div>

                                <div class="col-12">
                                    <label for="email" class="form-label">Email <span
                                            class="text-danger">*</span></label>
                                    <input type="email" class="form-control" name="email" id="email"
                                           placeholder="name@example.com" required>
                                </div>

                                <div class="col-12">
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" value="" name="terms"
                                               id="terms" required>
                                        <label class="form-check-label text-secondary" for="terms">
                                            I agree to the
                                            <a href="#!" class="link-primary text-decoration-none">
                                                terms and conditions
                                            </a>
                                        </label>
                                    </div>
                                </div>

                                <div class="col-12">
                                    <div class="d-grid">
                                        <button class="btn bsb-btn-xl btn-primary" type="submit">
                                            Register now
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </form>
                        <div class="row">
                            <div class="col-12">
                                <hr class="mt-5 mb-4 border-secondary-subtle">
                                <div class="d-flex gap-2 gap-md-4 flex-column flex-md-row justify-content-md-end">
                                    <a href="${pageContext.request.contextPath}/qps/user/login"
                                       class="link-secondary text-decoration-none">
                                        Already have an account?
                                        <span style="color: blue">Log in</span>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="../../component/footer.html"/>
</body>

</html>
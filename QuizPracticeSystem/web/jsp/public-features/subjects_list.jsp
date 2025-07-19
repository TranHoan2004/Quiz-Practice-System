<%-- Document : subjects_list Created on : May 19, 2025, 4:38:34 PM Author : TranHoan --%>
<%@ page import="model.Account" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>Subjects List</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
    <link href="${pageContext.request.contextPath}/css/lib/css2.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/fontawesome/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css"
          rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/subjects_list.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/onboarding-topics.css" rel="stylesheet">
</head>

<body>
<jsp:include page="../../component/spinner.html"/>
<jsp:include page="../../component/navbar.jsp"/>
<jsp:include page="../../component/header.html"/>

<div id="onboarding-steps" class="d-flex flex-column align-items-center justify-content-center py-5">
    <div class="onboarding-container" id="onboarding-container">
        <div id="stepper">
            <div class="step active" data-step="1">1</div>
            <div class="step" data-step="2">2</div>
            <div class="step" data-step="3">3</div>
            <div class="step" data-step="4">4</div>
        </div>
        <div id="onboardingForm">
            <!--Step 1-->
            <div class="onboarding-step d-flex flex-column align-items-center justify-content-center" data-step="1">
                <h2 class="mb-3">Mục tiêu học tập của bạn là gì?</h2>
                <div class="form-group row justify-content-center">
                    <div class="col-md-8">
                        <div class="card p-3 mb-3 border-0 shadow-sm rounded-3 checkbox-option">
                            <label class="form-check-label">
                                <input type="checkbox" name="goal" value="working" class="form-check-input me-2">
                                <strong>Working</strong><br>
                                <small class="text-muted">Tập trung vào kỹ năng phục vụ công việc hiện tại hoặc tương
                                    lai.</small>
                            </label>
                        </div>
                        <div class="card p-3 mb-3 border-0 shadow-sm rounded-3 checkbox-option">
                            <label class="form-check-label">
                                <input type="checkbox" name="goal" value="improve my knowledge"
                                       class="form-check-input me-2">
                                <strong>Improve my knowledge</strong><br>
                                <small class="text-muted">Bồi dưỡng và nâng cao hiểu biết trong lĩnh vực đã học.</small>
                            </label>
                        </div>
                        <div class="card p-3 mb-3 border-0 shadow-sm rounded-3 checkbox-option">
                            <label class="form-check-label">
                                <input type="checkbox" name="goal" value="explore new knowledge"
                                       class="form-check-input me-2">
                                <strong>Explore new knowledge</strong><br>
                                <small class="text-muted">Khám phá những kiến thức hoàn toàn mới với bạn.</small>
                            </label>
                        </div>
                    </div>
                </div>
                <button type="button"
                        class="btn btn-primary next-step mt-3"
                        onclick="getLearningTarget()">
                    Next
                </button>
            </div>

            <!--Step 2-->
            <div class="onboarding-step d-none flex-column align-items-center justify-content-center" data-step="2">
                <h2 class="mb-3 text-center">Who are you?</h2>
                <div class="form-group row justify-content-center">
                    <div class="col-md-8">
                        <div class="card p-3 mb-3 border-0 shadow-sm rounded-3">
                            <label class="form-check-label">
                                <input type="radio" name="occupation" value="pupil" class="form-check-input me-2">
                                <strong>Pupil</strong><br>
                                <small class="text-muted">Học sinh cấp 1, 2 hoặc 3.</small>
                            </label>
                        </div>
                        <div class="card p-3 mb-3 border-0 shadow-sm rounded-3">
                            <label class="form-check-label">
                                <input type="radio" name="occupation" value="student" class="form-check-input me-2">
                                <strong>Student</strong><br>
                                <small class="text-muted">Sinh viên đang theo học tại đại học hoặc cao đẳng.</small>
                            </label>
                        </div>
                        <div class="card p-3 mb-3 border-0 shadow-sm rounded-3">
                            <label class="form-check-label">
                                <input type="radio" name="occupation" value="working professional"
                                       class="form-check-input me-2">
                                <strong>Working professional</strong><br>
                                <small class="text-muted">Đã đi làm và đang phát triển sự nghiệp.</small>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="text-center mt-4">
                    <button type="button" class="btn btn-secondary prev-step mt-3">Back</button>
                    <button
                            type="button"
                            class="btn btn-primary next-step mt-3"
                            onclick="getIdentified()">
                        Continue
                    </button>
                </div>
            </div>

            <!--Step 3-->
            <div class="onboarding-step d-none flex-column align-items-center justify-content-center" data-step="3">
                <h2 class="mb-3 text-center">What is your education level?</h2>
                <div class="form-group row justify-content-center">
                    <div class="col-md-8">
                        <div class="card p-3 mb-3 border-0 shadow-sm rounded-3">
                            <label class="form-check-label">
                                <input type="radio" name="education" value="secondary education or below"
                                       class="form-check-input me-2">
                                <strong>Secondary education or below</strong><br>
                                <small class="text-muted">THPT hoặc thấp hơn.</small>
                            </label>
                        </div>
                        <div class="card p-3 mb-3 border-0 shadow-sm rounded-3">
                            <label class="form-check-label">
                                <input type="radio" name="education" value="undergraduate level"
                                       class="form-check-input me-2">
                                <strong>Undergraduate level</strong><br>
                                <small class="text-muted">Đang học hoặc đã tốt nghiệp đại học.</small>
                            </label>
                        </div>
                        <div class="card p-3 mb-3 border-0 shadow-sm rounded-3">
                            <label class="form-check-label">
                                <input type="radio" name="education" value="postgraduate level"
                                       class="form-check-input me-2">
                                <strong>Postgraduate level</strong><br>
                                <small class="text-muted">Cao học, thạc sĩ hoặc tiến sĩ.</small>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="text-center mt-4">
                    <button type="button" class="btn btn-secondary prev-step mt-3">Back</button>
                    <button
                            type="button"
                            class="btn btn-primary next-step mt-3"
                            onclick="getEducationLevel()"
                    >Continue
                    </button>
                </div>
            </div>

            <!--Step 4-->
            <div class="onboarding-step d-none" data-step="4">
                <h2 class="mb-3">Chủ đề bạn muốn tìm hiểu?</h2>
                <div class="mb-3">
                    <div class="role-search-box mb-3">
                        <input type="text" class="form-control" id="roleSearchInput" placeholder="Tìm kiếm chủ đề...">
                    </div>
                    <div id="topic-card-list" class="row gx-3 gy-3"></div>
                    <div class="invalid-feedback d-block" id="topic-error">
                        Vui lòng chọn ít nhất một chủ đề.
                    </div>
                    <div class="mt-2">
                        <a href="#" id="viewMoreRoles">
                            <span>+</span> Xem thêm chủ đề
                        </a>
                    </div>
                </div>
                <div class="text-center mt-4">
                    <button type="button" class="btn btn-secondary prev-step mt-3">Back</button>
                    <button type="button" class="btn btn-success mt-3" onclick="submitForm()">
                        Bắt đầu trải nghiệm
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="course-section" style="display:none;">
    <!-- Courses Start -->
    <div class="container-xxl py-5">
        <div class="container">
            <div class="row">
                <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                    <h6 class="section-title bg-white text-center text-primary px-3">Marketing</h6>
                    <h1 class="mb-3">Sliders List</h1>
                    <div class="gemini-container" id="ai-course-chat">
                        <form id="aiPromptFormCourse" class="gemini-form">
                            <div class="prompt-container">
                                <input type="text" id="aiPromptInputCourse"
                                       placeholder="Nhập yêu cầu tại đây..." required autocomplete="off">
                                <button type="submit" title="Gửi"><i class="fas fa-arrow-up"></i></button>
                            </div>
                        </form>
                        <div id="aiResponseCourse" class="gemini-response mt-4 d-none"></div>
                    </div>
                </div>

                <!-- Slider List Start -->
                <div class="col-12">
                    <div class="row">
                        <!-- Sidebar Start -->
                        <aside class="col-lg-3 mb-4 mb-lg-0">
                            <div class="card mb-4 shadow-sm p-2" id="card">
                                <div class="card-body">
                                    <!-- Subject Search -->
                                    <form class="mb-4" id="subjectSearchForm">
                                        <label for="subjectSearch"
                                               class="form-label fw-semibold text-primary-emphasis">
                                            <i class="bi bi-search me-1"></i>
                                            <span>Search Subjects</span>
                                        </label>
                                        <div class="input-group input-group-sm">
                                            <input type="text" id="subjectSearch" class="form-control"
                                                   placeholder="Search...">
                                            <button class="btn btn-primary" type="submit">
                                                <i class="bi bi-search"></i>
                                            </button>
                                        </div>
                                    </form>

                                    <!-- Featured Subjects -->
                                    <div class="mb-4">
                                        <label class="form-label fw-semibold text-primary-emphasis">
                                            <i class="bi bi-star me-1"></i>
                                            <span class="featured_subjects">Featured Subjects</span>
                                        </label>
                                        <ul class="list-group" id="featuredSubjects"></ul>
                                    </div>

                                    <button type="button"
                                            class="mb-4 btn btn-outline-primary d-flex align-items-center justify-content-center gap-2"
                                            onclick="reopenOnboarding()"
                                    >
                                        <i class="bi bi-pencil-square"></i>
                                        <span>Edit your goal</span>
                                    </button>

                                    <div id="cardBody"></div>
                                </div>
                            </div>
                        </aside>

                        <!-- Subjects List Start -->
                        <div class="col-lg-9">
                            <div class="row g-4" id="subjectList"></div>

                            <!-- No Results Message -->
                            <div id="noSubjectResultsMessage" class="text-center text-muted py-5">
                                No results
                            </div>
                            <!-- Pagination Start -->
                            <nav class="mt-4 d-flex justify-content-center">
                                <ul class="pagination" id="subjectPagination"></ul>
                            </nav>
                            <div class="d-flex justify-content-center align-items-center mt-3"
                                 id="showing-item">
                                <p class="me-3">Showing <span id="subjectStartEntry">1</span> to <span
                                        id="subjectEndEntry">10</span>
                                    of
                                    <span id="subjectTotalEntries">0</span> entries
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Subject Registration Modal (Brand Color Style) -->
<div class="modal fade" id="subjectRegisterModal" tabindex="-1" aria-labelledby="subjectRegisterModalLabel"
     aria-hidden="true">
    <%
        Account user = (Account) session.getAttribute("currentUser");
    %>
    <div class="modal-dialog modal-dialog-centered">
        <form class="modal-content border-0 shadow-lg" id="subjectRegisterForm">
            <div class="modal-header border-0">
                <h5 class="modal-title text-white fw-bold d-flex align-items-center gap-2"
                    id="subjectRegisterModalLabel">
                    <i class="fa fa-user-plus"></i> Subjects Register
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Đóng"></button>
            </div>
            <div class="modal-body py-4 px-4">
                <div class="mb-3">
                    <label class="form-label fw-semibold" id="selectPackage" for="registerPackageSelect">
                        <i class="fa fa-gift me-1"></i> Select price package
                    </label>
                    <select class="form-select rounded-pill px-3 py-2" name="package"
                            id="registerPackageSelect" required>
                        <option value="">-- Chọn gói --</option>
                    </select>
                </div>
                <div id="userInfoFields">
                    <div class="mb-3">
                        <label class="form-label" for="fullname">
                            <i class="fa fa-user me-1"></i> Full Name</label>
                        <input type="text" class="form-control rounded-pill px-3 py-2" name="fullname"
                               placeholder="<%=user != null ? user.getFullName() : "Nhập họ tên của bạn"%>" required
                            <%=user != null ? "disabled" : ""%>
                               id="fullname">
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="email">
                            <i class="fa fa-envelope me-1"></i> Email</label>
                        <input type="email" class="form-control rounded-pill px-3 py-2" name="email"
                               placeholder="<%=user != null ? user.getEmail() : "example@email.com"%>" required
                            <%=user != null ? "disabled" : ""%>
                               id="email">
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="phone">
                            <i class="fa fa-phone me-1"></i> Phone number</label>
                        <input type="tel" class="form-control rounded-pill px-3 py-2" name="phone"
                               placeholder="<%=user != null ? user.getPhoneNumber() : "09xxxxxxxx"%>" required
                            <%=user != null ? "disabled" : ""%>
                               id="phone">
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="gender">
                            <i class="fa fa-venus-mars me-1"></i> Gender</label>
                        <select class="form-select rounded-pill px-3 py-2" name="gender" required
                                id="gender"
                                <%
                                    if (user != null) {
                                %>
                                disabled>
                            <option value="" selected>
                                <%
                                    String gender;
                                    switch (user.getGender()) {
                                        case 0:
                                            gender = "Male";
                                            break;
                                        case 1:
                                            gender = "Female";
                                            break;
                                        default:
                                            gender = "Others";
                                            break;
                                    }
                                %>
                                <%=gender%>
                            </option>
                            <%
                            } else {
                            %>
                            >
                            <option value="">-- Select your gender --</option>
                            <option value="0">Male</option>
                            <option value="1">Female</option>
                            <option value="2">Others</option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                </div>
                <div id="userInfoNotice" class="alert alert-info d-none mt-3" role="alert">
                    <i class="fa fa-check-circle me-2"></i>
                    Thông tin liên hệ của bạn đã được tự động điền.
                </div>
            </div>
            <div class="modal-footer border-0 d-flex flex-column gap-2">
                <button type="submit" class="btn btn-gradient w-100 py-2 fw-bold rounded-pill">
                    <i class="fa fa-paper-plane me-2"></i> Confirm registered
                </button>
                <div class="text-center w-100">
                    <i class="fa fa-lock me-1"></i> Thông tin của bạn được bảo mật
                </div>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../../component/notification.html"/>
<jsp:include page="../../component/footer.html"/>
<jsp:include page="../../component/back_to_top.html"/>

<script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="${pageContext.request.contextPath}/js/Notification.js"></script>
<script>
    window.contextPath = '${pageContext.request.contextPath}';
    let subjects = [];
    let currentPage = 1;
    let numberItemsPerPage = 6;
    let filteredSubjects = [];
    let numberOfPages = 0;
    let numberOfItems = 0;
    let featuredSubjects = [];
    let socket;
    const path = `ws://localhost:8080/qps/answer`;
    let answer;
</script>
<script src="${pageContext.request.contextPath}/js/subjects-list/AssistantHandler.js" type="module"></script>
<script src="${pageContext.request.contextPath}/js/subjects-list/SubjectsPagination.js" type="module"></script>
<script src="${pageContext.request.contextPath}/js/subjects-list/SubjectsListWebSocket.js"></script>
<script src="${pageContext.request.contextPath}/js/subjects-list/OnboardingTopics.js" type="module"></script>
</body>

</html>
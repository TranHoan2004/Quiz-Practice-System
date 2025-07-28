<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Quezee</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">
        <link href="https://fonts.googleapis.com" rel="preconnect">
        <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
        <link href="${pageContext.request.contextPath}/css/lib/css2.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/fontawesome/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    </head>

    <body>
        <jsp:include page="../../component/spinner.html"/>
        <jsp:include page="../../component/navbar.jsp"/>

        <!-- Courses Start -->

        <div class="container-xxl py-5">
            <div class="container">
                <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                    <h6 class="section-title bg-white text-center text-primary px-3">Simulation Exams</h6>
                    <h1 class="mb-3">Available Simulation Exams</h1>
                    <div class="d-flex justify-content-center align-items-center gap-3 flex-wrap mb-4 mt-5">
                        <!-- Tìm kiếm -->
                        <div class="input-group" style="min-width: 300px; max-width: 400px; width: 100%;">
                            <input type="text" id="searchInput" value="${param.search}" class="form-control rounded-start" placeholder="Search by exam name..." style="border-radius: 0.5rem 0 0 0.5rem;">
                            <button id="searchBtn" class="btn btn-primary rounded-end" style="border-radius: 0 0.5rem 0.5rem 0;">
                                <i class="bi bi-search"></i>
                            </button>
                        </div>

                        <!-- Bộ lọc môn học -->
                        <div class="form-group d-flex align-items-center">
                            <label for="subjectExamFilter" class="me-2 mb-0 fw-semibold">Subject:</label>
                            <select class="form-select" id="subjectExamFilter" style="min-width: 180px; border-radius: 0.5rem;">
                                <option value="">All Subjects</option>
                                <c:forEach var="subject" items="${subjects}">
                                    <option value="${subject.id}" ${param.subject == subject.id ? 'selected' : ''}>${subject.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>

                <!-- Simulation Exams List Start -->
                <div class="card mb-4 wow fadeInUp">
                    <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                        <span><i class="bi bi-journal-check me-2"></i>Simulation Exams You Can Access</span>
                    </div>

                    <div class="table-responsive">
                        <table class="table table-striped table-bordered align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Subject</th>
                                    <th>Exam Title</th>
                                    <th>Level</th>
                                    <th>Num of Questions</th>
                                    <th>Duration</th>
                                    <th>Pass Rate</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody id="quiz-table-body">
                                <!-- JS sẽ tự động render vào đây -->
                            </tbody>
                        </table>
                    </div>

                    <!-- Pagination -->
                    <div id="pagination"></div>
                </div>
            </div>
        </div>

        <!-- Quiz Detail Modal -->
        <div class="modal fade" id="quizDetailModal" tabindex="-1" aria-labelledby="quizDetailModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content shadow-sm">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title" id="quizDetailModalLabel"><i class="bi bi-journal-text me-2"></i>Quiz Detail</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body p-4 d-flex flex-column gap-3">
                        <div class="row mb-2">
                            <div class="col-md-6"><strong>Title:</strong> <span id="quizTitle"></span></div>
                            <div class="col-md-6"><strong>Subject:</strong> <span id="quizSubject"></span></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6"><strong>Level:</strong> <span id="quizLevel"></span></div>
                            <div class="col-md-6"><strong>Type:</strong> <span id="quizType"></span></div>
                        </div>
                        <div class="row mb-2">
                            <div class="col-md-6">
                                <strong>Duration:</strong>
                                <span id="quizDuration" class="text-danger">

                                </span> <span class="text-danger">mins</span>
                            </div>
                            <div class="col-md-6"><strong>Pass Rate:</strong> <span id="quizPassRate"></span></div>
                        </div>

                        <div class="row mb-2">
                            <div class="col-md-6">
                                <strong>Number of Question: </strong>
                                <span id="numOfQuestion" class="text-danger"></span>
                            </div>
                            <div class="col-md-6">
                                <strong>Updated At:</strong> <span id="updatedDate" class="mt-1 text-muted"></span>
                            </div>
                        </div>

                        <div class="col-md-12">
                            <strong>Description:</strong> <p id="quizDescription" class="mt-1 text-muted"></p>
                        </div>

                        <div class="text-end">
                            <a href="#" id="practiceBtn" class="btn btn-outline-success"><i class="bi bi-pencil"></i> Practice</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>



        <jsp:include page="../../component/footer.html"/>
        <jsp:include page="../../component/back_to_top.html"/>

        <script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/main.js"></script>

        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/SimulationExam.js"></script>
    </body>
</html>
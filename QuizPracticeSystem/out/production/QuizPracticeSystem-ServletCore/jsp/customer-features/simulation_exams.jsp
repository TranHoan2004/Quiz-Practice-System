<%-- 
    Document   : simulation_exams
    Created on : May 19, 2025, 4:51:25 PM
    Author     : TranHoan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
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
<jsp:include page="../../component/navbar.html"/>
<jsp:include page="../../component/header.jsp"/>

<!-- Courses Start -->
<div class="container-xxl py-5">
    <div class="container">
        <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
            <h6 class="section-title bg-white text-center text-primary px-3">Simulation Exams</h6>
            <h1 class="mb-3">Available Simulation Exams</h1>
            <div class="row g-3 mb-4 justify-content-center">
                <div class="col-lg-3 col-md-4 col-sm-6 wow fadeInUp" data-wow-delay="0.1s">
                    <div
                            class="service-item p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                        <label for="examSearcher" class="form-label mb-2">
                                        <span class="fw-semibold text-primary"><i class="bi bi-search me-1"></i>Search
                                            Exam</span>
                        </label>
                        <div class="input-group input-group-sm">
                            <input type="text" id="examSearcher" class="form-control"
                                   placeholder="Search by exam name...">
                            <button class="btn btn-primary" type="button"><i class="bi bi-search"></i></button>
                        </div>
                    </div>
                </div>
                <div class="col-lg-3 col-md-4 col-sm-6 wow fadeInUp" data-wow-delay="0.2s">
                    <div
                            class="service-item p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                        <label for="subjectExamFilter" class="form-label mb-2">
                            <span class="fw-semibold text-primary"><i class="bi bi-filter me-1"></i>Subject</span>
                        </label>
                        <select class="form-select form-select-sm" id="subjectExamFilter">
                            <option value="">All Subjects</option>
                            <option value="web-development">Web Development</option>
                            <option value="design">Design</option>
                            <option value="marketing">Marketing</option>
                            <option value="data-science">Data Science</option>
                            <option value="python">Python Programming</option>
                            <!-- Add more subjects as needed -->
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <!-- Simulation Exams List Start -->
        <div class="card mb-4 wow fadeInUp">
            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                <span><i class="bi bi-journal-check me-2"></i>Simulation Exams You Can Access</span>
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0" id="simulationExamTable">
                    <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Chủ đề</th>
                        <th>Kỳ thi mô phỏng</th>
                        <th>Cấp độ</th>
                        <th>Số câu hỏi</th>
                        <th>Thời gian</th>
                        <th>Tỷ lệ đỗ</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>EX001</td>
                        <td>Lập trình Web</td>
                        <td>HTML & CSS Cơ bản</td>
                        <td>Cơ bản</td>
                        <td>30</td>
                        <td>45 phút</td>
                        <td>70%</td>
                        <td>
                            <a href="exam-detail.html?id=EX001" class="btn btn-sm btn-primary">
                                <i class="bi bi-eye"></i> Xem chi tiết
                            </a>
                            <a href="quiz-manager.html?exam=EX001" class="btn btn-sm btn-success ms-1">
                                <i class="bi bi-play-circle"></i> Luyện tập
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td>EX002</td>
                        <td>Thiết kế</td>
                        <td>UI/UX Căn bản</td>
                        <td>Trung bình</td>
                        <td>40</td>
                        <td>60 phút</td>
                        <td>75%</td>
                        <td>
                            <a href="exam-detail.html?id=EX002" class="btn btn-sm btn-primary">
                                <i class="bi bi-eye"></i> Xem chi tiết
                            </a>
                            <a href="quiz-manager.html?exam=EX002" class="btn btn-sm btn-success ms-1">
                                <i class="bi bi-play-circle"></i> Luyện tập
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td>EX003</td>
                        <td>Marketing</td>
                        <td>Marketing số cơ bản</td>
                        <td>Cơ bản</td>
                        <td>25</td>
                        <td>30 phút</td>
                        <td>65%</td>
                        <td>
                            <a href="exam-detail.html?id=EX003" class="btn btn-sm btn-primary">
                                <i class="bi bi-eye"></i> Xem chi tiết
                            </a>
                            <a href="quiz-manager.html?exam=EX003" class="btn btn-sm btn-success ms-1">
                                <i class="bi bi-play-circle"></i> Luyện tập
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td>EX004</td>
                        <td>Khoa học dữ liệu</td>
                        <td>Phân tích dữ liệu với Python</td>
                        <td>Nâng cao</td>
                        <td>50</td>
                        <td>90 phút</td>
                        <td>80%</td>
                        <td>
                            <a href="exam-detail.html?id=EX004" class="btn btn-sm btn-primary">
                                <i class="bi bi-eye"></i> Xem chi tiết
                            </a>
                            <a href="quiz-manager.html?exam=EX004" class="btn btn-sm btn-success ms-1">
                                <i class="bi bi-play-circle"></i> Luyện tập
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td>EX005</td>
                        <td>Lập trình Python</td>
                        <td>Khái niệm OOP trong Python</td>
                        <td>Trung bình</td>
                        <td>35</td>
                        <td>50 phút</td>
                        <td>70%</td>
                        <td>
                            <a href="exam-detail.html?id=EX005" class="btn btn-sm btn-primary">
                                <i class="bi bi-eye"></i> Xem chi tiết
                            </a>
                            <a href="quiz-manager.html?exam=EX005" class="btn btn-sm btn-success ms-1">
                                <i class="bi bi-play-circle"></i> Luyện tập
                            </a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="card-footer d-flex justify-content-between align-items-center">
                <span>Showing 1 to 5 of 10 entries</span>
                <nav aria-label="Course pagination">
                    <ul class="pagination mb-0" id="pagination"
                        style="font-size: 1rem; --bs-pagination-active-bg: #0d6efd; --bs-pagination-active-border-color: #0d6efd;">
                    </ul>
                </nav>
            </div>
        </div>

        <script>
            // Simple client-side filter for demo purposes
            document.getElementById('examSearcher').addEventListener('input', function () {
                filterExams();
            });
            document.getElementById('subjectExamFilter').addEventListener('change', function () {
                filterExams();
            });

            function filterExams() {
                const search = document.getElementById('examSearcher').value.toLowerCase();
                const subject = document.getElementById('subjectExamFilter').value;
                const table = document.getElementById('simulationExamTable');
                const rows = table.querySelectorAll('tbody tr');
                let visible = 0;
                rows.forEach(row => {
                    const examName = row.cells[2].textContent.toLowerCase();
                    const subjectName = row.cells[1].textContent.toLowerCase().replace(/\s+/g, '-');
                    const matchSearch = !search || examName.includes(search);
                    const matchSubject = !subject || subjectName === subject;
                    if (matchSearch && matchSubject) {
                        row.style.display = '';
                        visible++;
                    } else {
                        row.style.display = 'none';
                    }
                });
                // Update footer count
                document.querySelector('.card-footer span').textContent = `Showing 1 to ${visible} of ${rows.length} entries`;
            }
        </script>
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
<script src="${pageContext.request.contextPath}/js/TableRender.js"></script>
</body>

</html>
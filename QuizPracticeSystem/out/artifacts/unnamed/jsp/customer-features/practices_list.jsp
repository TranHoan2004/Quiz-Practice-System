<%-- 
    Document   : practices_list
    Created on : May 19, 2025, 4:26:56 PM
    Author     : TranHoan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Practices List</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">
        <link href="img/favicon.ico" rel="icon">
        <link href="https://fonts.googleapis.com" rel="preconnect">
        <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
        <link href="${pageContext.request.contextPath}/css/lib/css2.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/fontawesome/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/practicelist.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
    </head>

    <body>
<%--        <jsp:include page="../../component/spinner.html"/>--%>
        <jsp:include page="../../component/navbar.html"/>
        <jsp:include page="../../component/header.html"/>

            <!-- Courses Start -->
            <div class="container-xxl py-5">
                <div class="container">
                    <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                        <h6 class="section-title bg-white text-center text-primary px-3">Practice</h6>
                        <h1 class="mb-3">Practices List</h1>
                        <div class="row g-3 mb-4 justify-content-center">
                            <div class="col-lg-3 col-md-4 col-sm-6 wow fadeInUp" data-wow-delay="0.1s">
                                <div
                                    class="service-item p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                                    <label for="practiceSearcher" class="form-label mb-2">
                                        <span class="fw-semibold text-primary"><i class="bi bi-search me-1"></i>Search</span>
                                    </label>
                                    <div class="input-group input-group-sm">
                                        <input type="text" id="practiceSearcher" class="form-control" placeholder="Search...">
                                        <button class="btn btn-primary" type="button"><i class="bi bi-search"></i></button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-4 col-sm-6 wow fadeInUp" data-wow-delay="0.2s">
                                <div
                                    class="service-item p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center">
                                    <label for="subjectFilter" class="form-label mb-2">
                                        <span class="fw-semibold text-primary"><i class="bi bi-filter me-1"></i>Subject</span>
                                    </label>
                                    <select class="form-select form-select-sm" id="subjectFilter">
                                        <option value="" default>All Subjects</option>
                                        <option value="web-development">Web Development</option>
                                        <option value="design">Design</option>
                                        <option value="marketing">Marketing</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-4 col-sm-12 wow fadeInUp" data-wow-delay="0.3s">
                                <div
                                    class="service-item p-3 rounded shadow-sm bg-white h-100 d-flex flex-column justify-content-center align-items-center gap-2">
                                    <button type="button" class="btn btn-success btn-sm w-100 mb-1">
                                        <i class="bi bi-plus-circle me-1"></i> Add New
                                    </button>
                                    <button type="button" class="btn btn-info btn-sm w-100">
                                        <i class="bi bi-lightning-charge-fill me-1"></i> Simulation Exam
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row wow fadeInUp" data-wow-delay="0.2s">
                        <div class="col-12">
                            <div class="table-responsive">
                                <table class="table table-bordered align-middle text-center">
                                    <thead class="table-primary">
                                        <tr>
                                            <th>#</th>
                                            <th>Subject Name</th>
                                            <th>Exam Name</th>
                                            <th>Date Taken</th>
                                            <th>Questions</th>
                                            <th>Rate Of Correct Questions</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr data-section="group1">
                                            <td>1</td>
                                            <td>HTML Basics</td>
                                            <td>Web Development</td>
                                            <td>10/09/2019</td>
                                            <td style="text-align: center; vertical-align: middle; padding: 10px;">
                                                <div>
                                                    <div>10 Correct</div>
                                                    <div>20 Questions</div>
                                                </div>
                                            </td>
                                            <td>50% Correct</td>
                                            <td>
                                                <button class="btn btn-info btn-sm me-1">
                                                    <i class="bi bi-eye"></i>
                                                    View
                                                </button>
                                                <button class="btn btn-danger btn-sm delete-btn">
                                                    <i class="bi bi-trash"></i>
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                        <tr data-section="group1">
                                            <td>2</td>
                                            <td>CSS Basics</td>
                                            <td>Web Development</td>
                                            <td>11/09/2019</td>
                                            <td style="text-align: center; vertical-align: middle; padding: 10px;">
                                                <div>
                                                    <div>15 Correct</div>
                                                    <div>20 Questions</div>
                                                </div>
                                            </td>
                                            <td>75% Correct</td>
                                            <td>
                                                <button class="btn btn-info btn-sm me-1">
                                                    <i class="bi bi-eye"></i>
                                                    View
                                                </button>
                                                <button class="btn btn-danger btn-sm delete-btn">
                                                    <i class="bi bi-trash"></i>
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                        <tr data-section="group1" class="section-footer">
                                            <td colspan="7">Test name</td>
                                        </tr>

                                        <tr data-section="group2">
                                            <td>3</td>
                                            <td>CSS Flexbox</td>
                                            <td>Web Development</td>
                                            <td>10/09/2019</td>
                                            <td style="text-align: center; vertical-align: middle; padding: 10px;">
                                                <div>
                                                    <div>10 Correct</div>
                                                    <div>20 Questions</div>
                                                </div>
                                            </td>
                                            <td>50% Correct</td>
                                            <td>
                                                <button class="btn btn-info btn-sm me-1">
                                                    <i class="bi bi-eye"></i>
                                                    View
                                                </button>
                                                <button class="btn btn-danger btn-sm delete-btn">
                                                    <i class="bi bi-trash"></i>
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                        <tr data-section="group2" class="section-footer">
                                            <td colspan="7">Test name</td>
                                        </tr>

                                        <tr data-section="group3">
                                            <td>3</td>
                                            <td>Marketing 101</td>
                                            <td>Marketing</td>
                                            <td>10/09/2019</td>
                                            <td style="text-align: center; vertical-align: middle; padding: 10px;">
                                                <div>
                                                    <div>10 Correct</div>
                                                    <div>20 Questions</div>
                                                </div>
                                            </td>
                                            <td>50% Correct</td>
                                            <td>
                                                <button class="btn btn-info btn-sm me-1">
                                                    <i class="bi bi-eye"></i>
                                                    View
                                                </button>
                                                <button class="btn btn-danger btn-sm delete-btn">
                                                    <i class="bi bi-trash"></i>
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                        <tr data-section="group3" class="section-footer">
                                            <td colspan="7">Test name</td>
                                        </tr>
                                        <!-- <span class="badge bg-success">Active</span>
                                                <button class="btn btn-info btn-sm me-1"><i class="bi bi-eye"></i> View</button>
                                                <button class="btn btn-warning btn-sm me-1"><i class="bi bi-pencil"></i>
                                                    Edit</button>-->
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
                    </div>
                </div>
            </div>

            <!-- Add New Practice Modal -->
            <div class="modal fade" id="addPracticeModal" tabindex="-1" aria-labelledby="addPracticeModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title text-white" id="addPracticeModalLabel">
                                <i class="bi bi-plus-circle me-2"></i>
                                Add New Practice
                            </h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                        </div>
                        <form id="addPracticeForm"></form>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="practiceSubject" class="form-label">Subject</label>
                                <select class="form-select" id="practiceSubject" required>
                                    <option value="">Select Subject</option>
                                    <option value="web-development">Web Development</option>
                                    <option value="design">Design</option>
                                    <option value="marketing">Marketing</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="numQuestions" class="form-label">Number of Questions</label>
                                <input type="number" class="form-control" id="numQuestions" min="1" required>
                            </div>
                            <div class="mb-3">
                                <label for="topicSelect" class="form-label">Select Subject by Topic</label>
                                <select class="form-select" id="topicSelect" required>
                                    <option value="">Select Topic</option>
                                    <option value="html">HTML</option>
                                    <option value="css">CSS</option>
                                    <option value="seo">SEO</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="questionGroup" class="form-label">Question Group</label>
                                <select class="form-select" id="questionGroup" required>
                                    <option value="">All</option>
                                    <option value="group1">Group 1</option>
                                    <option value="group2">Group 2</option>
                                    <option value="group3">Group 3</option>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer justify-content-center">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                <i class="bi bi-x-circle me-1"></i>Cancel
                            </button>
                            <button type="submit" class="btn btn-success">
                                <i class="bi bi-check-circle me-1"></i>Practice
                            </button>
                        </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Delete Confirmation Modal -->
            <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title text-white" id="deleteModalLabel">
                                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                Confirm Delete
                            </h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                        </div>
                        <div class="modal-body text-center">
                            <i class="bi bi-trash display-4 text-danger mb-3"></i>
                            <p class="mb-0">Are you sure you want to delete this practice?</p>
                        </div>
                        <div class="modal-footer justify-content-center">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                <i class="bi bi-x-circle me-1"></i>Cancel
                            </button>
                            <button type="button" class="btn btn-danger" id="confirmDeleteBtn">
                                <i class="bi bi-check-circle me-1"></i>OK
                            </button>
                        </div>
                    </div>
                </div>
            </div>

        <jsp:include page="../../component/notification.html"></jsp:include>
        <jsp:include page="../../component/footer.html"></jsp:include>
        <jsp:include page="../../component/back_to_top.html"></jsp:include>

        <script src="../../js/lib/jquery-3.4.1.min.js"></script>
        <script src="../../js/lib/bootstrap.bundle.min.js"></script>
        <script src="../../lib/wow/wow.min.js"></script>
        <script src="../../lib/easing/easing.min.js"></script>
        <script src="../../lib/waypoints/waypoints.min.js"></script>
        <script src="../../lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="../../js/main.js"></script>
        <script src="../../js/PracticeList.js"></script>
        <script src="../../js/Notification.js"></script>
        <script src="../../js/TableRender.js"></script>
    </body>

</html>

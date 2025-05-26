<%-- 
    Document   : practice_details
    Created on : May 19, 2025, 4:36:09 PM
    Author     : TranHoan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Practice Details</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">
        <link href="img/favicon.ico" rel="icon">
        <link href="https://fonts.googleapis.com" rel="preconnect">
        <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
        <link href="../../css/lib/css2.css" rel="stylesheet">
        <link href="../../fontawesome/css/all.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
        <link href="../../lib/animate/animate.min.css" rel="stylesheet">
        <link href="../../lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
        <link href="../../css/bootstrap.min.css" rel="stylesheet">
        <link href="../../css/style.css" rel="stylesheet">
        <link href="../../css/practicelist.css" rel="stylesheet">
        <link href="../../css/notification.css" rel="stylesheet">
    </head>

    <body>
        <jsp:include page="../../component/spinner.html"></jsp:include>
        <jsp:include page="../../component/navbar.html"></jsp:include>
        <jsp:include page="../../component/header.html"></jsp:include>

            <!-- Practice Details Start -->
            <div class="container-xxl py-5">
                <div class="container">
                    <div class="text-center mb-4">
                        <h6 class="section-title bg-white text-center text-primary px-3">Practice</h6>
                        <h1 class="mb-3">Practice Details</h1>
                    </div>

                    <!-- Existing Practices Table -->
                    <div class="row">
                        <div class="col-12">
                            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                                <span><i class="bi bi-journal-check me-2"></i>Practice Details</span>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-hover table-bordered align-middle text-center">
                                    <thead class="table-primary">
                                        <tr>
                                            <th>#</th>
                                            <th>Subject Name</th>
                                            <th>Number of Questions</th>
                                            <th>Filter By</th>
                                            <th>Question Group</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <!-- Example rows for demo -->
                                        <tr>
                                            <td>1</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-1.jpg" alt="Web Development" class="rounded me-2"
                                                         width="40" height="40">
                                                    <span>Web Development</span>
                                                </div>
                                            </td>
                                            <td>20</td>
                                            <td>Topic: HTML</td>
                                            <td>Group 1</td>
                                            <td>
                                                <span class="badge bg-warning text-dark px-3 py-2 rounded-pill">Not
                                                    Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-success btn-sm px-3">
                                                    <i class="bi bi-play-circle"></i> Practice
                                                </a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>2</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-2.jpg" alt="Marketing" class="rounded me-2" width="40"
                                                         height="40">
                                                    <span>Marketing</span>
                                                </div>
                                            </td>
                                            <td>15</td>
                                            <td>Domain: SEO</td>
                                            <td>Group 2</td>
                                            <td>
                                                <span class="badge bg-success px-3 py-2 rounded-pill">Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-info btn-sm px-3">
                                                    <i class="bi bi-eye"></i> Review
                                                </a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>3</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-3.jpg" alt="Design" class="rounded me-2" width="40"
                                                         height="40">
                                                    <span>Design</span>
                                                </div>
                                            </td>
                                            <td>10</td>
                                            <td>Topic: UI</td>
                                            <td>Group 3</td>
                                            <td>
                                                <span class="badge bg-warning text-dark px-3 py-2 rounded-pill">Not
                                                    Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-success btn-sm px-3">
                                                    <i class="bi bi-play-circle"></i> Practice
                                                </a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>4</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-1.jpg" alt="Web Development" class="rounded me-2"
                                                         width="40" height="40">
                                                    <span>Web Development</span>
                                                </div>
                                            </td>
                                            <td>25</td>
                                            <td>Domain: CSS</td>
                                            <td>Group 1</td>
                                            <td>
                                                <span class="badge bg-success px-3 py-2 rounded-pill">Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-info btn-sm px-3">
                                                    <i class="bi bi-eye"></i> Review
                                                </a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>5</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-2.jpg" alt="Marketing" class="rounded me-2" width="40"
                                                         height="40">
                                                    <span>Marketing</span>
                                                </div>
                                            </td>
                                            <td>18</td>
                                            <td>Topic: Content</td>
                                            <td>Group 2</td>
                                            <td>
                                                <span class="badge bg-warning text-dark px-3 py-2 rounded-pill">Not
                                                    Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-success btn-sm px-3">
                                                    <i class="bi bi-play-circle"></i> Practice
                                                </a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>6</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-3.jpg" alt="Design" class="rounded me-2" width="40"
                                                         height="40">
                                                    <span>Design</span>
                                                </div>
                                            </td>
                                            <td>12</td>
                                            <td>Domain: UX</td>
                                            <td>Group 3</td>
                                            <td>
                                                <span class="badge bg-success px-3 py-2 rounded-pill">Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-info btn-sm px-3">
                                                    <i class="bi bi-eye"></i> Review
                                                </a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>7</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-1.jpg" alt="Web Development" class="rounded me-2"
                                                         width="40" height="40">
                                                    <span>Web Development</span>
                                                </div>
                                            </td>
                                            <td>22</td>
                                            <td>Topic: JavaScript</td>
                                            <td>Group 1</td>
                                            <td>
                                                <span class="badge bg-warning text-dark px-3 py-2 rounded-pill">Not
                                                    Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-success btn-sm px-3">
                                                    <i class="bi bi-play-circle"></i> Practice
                                                </a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>8</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-2.jpg" alt="Marketing" class="rounded me-2" width="40"
                                                         height="40">
                                                    <span>Marketing</span>
                                                </div>
                                            </td>
                                            <td>16</td>
                                            <td>Domain: SEM</td>
                                            <td>Group 2</td>
                                            <td>
                                                <span class="badge bg-success px-3 py-2 rounded-pill">Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-info btn-sm px-3">
                                                    <i class="bi bi-eye"></i> Review
                                                </a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>9</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-3.jpg" alt="Design" class="rounded me-2" width="40"
                                                         height="40">
                                                    <span>Design</span>
                                                </div>
                                            </td>
                                            <td>14</td>
                                            <td>Topic: Typography</td>
                                            <td>Group 3</td>
                                            <td>
                                                <span class="badge bg-warning text-dark px-3 py-2 rounded-pill">Not
                                                    Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-success btn-sm px-3">
                                                    <i class="bi bi-play-circle"></i> Practice
                                                </a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>10</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-1.jpg" alt="Web Development" class="rounded me-2"
                                                         width="40" height="40">
                                                    <span>Web Development</span>
                                                </div>
                                            </td>
                                            <td>30</td>
                                            <td>Domain: PHP</td>
                                            <td>Group 1</td>
                                            <td>
                                                <span class="badge bg-success px-3 py-2 rounded-pill">Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-info btn-sm px-3">
                                                    <i class="bi bi-eye"></i> Review
                                                </a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>10</td>
                                            <td class="text-start">
                                                <div class="d-flex align-items-center">
                                                    <img src="../../img/course-1.jpg" alt="Web Development" class="rounded me-2"
                                                         width="40" height="40">
                                                    <span>Web Development</span>
                                                </div>
                                            </td>
                                            <td>30</td>
                                            <td>Domain: PHP</td>
                                            <td>Group 1</td>
                                            <td>
                                                <span class="badge bg-success px-3 py-2 rounded-pill">Completed</span>
                                            </td>
                                            <td>
                                                <a href="#" class="btn btn-outline-info btn-sm px-3">
                                                    <i class="bi bi-eye"></i> Review
                                                </a>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>

                            <!-- Pagination -->
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

        <jsp:include page="../../component/footer.html"></jsp:include>
        <jsp:include page="../../component/back_to_top.html"></jsp:include>

        <script src="../../js/lib/jquery-3.4.1.min.js"></script>
        <script src="../../js/lib/bootstrap.bundle.min.js"></script>
        <script src="../../lib/wow/wow.min.js"></script>
        <script src="../../lib/easing/easing.min.js"></script>
        <script src="../../lib/waypoints/waypoints.min.js"></script>
        <script src="../../lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="../../js/main.js"></script>
        <script src="../../js/TableRender.js"></script>
    </body>

</html>
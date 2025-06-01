<%-- 
    Document   : subject_details
    Created on : May 19, 2025, 4:49:13 PM
    Author     : TranHoan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>Subject Details</title>
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
    <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
</head>

<body>
<jsp:include page="../../component/spinner.html"/>
<jsp:include page="../../component/navbar.html"/>
<jsp:include page="../../component/header.html"/>

<!-- Courses Start -->
<div class="container-xxl py-5">
    <div class="container">
        <div class="row">
            <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                <h6 class="section-title bg-white text-center text-primary px-3">Subjects</h6>
                <h1 class="mb-3">Subjects Details</h1>
            </div>
            <div class="col-12 wow fadeInUp">
                <ul class="nav nav-tabs mb-4" id="subjectDetailsTabs" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active" id="overview-tab" data-bs-toggle="tab"
                                data-bs-target="#overview" type="button" role="tab" aria-controls="overview"
                                aria-selected="true">
                            Overview
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="dimension-tab" data-bs-toggle="tab" data-bs-target="#dimension"
                                type="button" role="tab" aria-controls="dimension" aria-selected="false">
                            Dimension
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="price-tab" data-bs-toggle="tab" data-bs-target="#price"
                                type="button" role="tab" aria-controls="price" aria-selected="false">
                            Price Package
                        </button>
                    </li>
                </ul>
                <div class="tab-content" id="subjectDetailsTabsContent">
                    <!-- Overview Tab -->
                    <div class="tab-pane fade show active" id="overview" role="tabpanel"
                         aria-labelledby="overview-tab">
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white">
                                Subject General Information
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <!-- Left: Info except Description -->
                                    <div class="col-md-8">
                                        <form>
                                            <div class="row mb-3">
                                                <label class="col-sm-4 col-form-label">Subject Code</label>
                                                <div class="col-sm-8">
                                                    <input type="text" class="form-control" value="PRJ301" readonly
                                                           placeholder="Subject Code" title="Subject Code">
                                                </div>
                                            </div>
                                            <div class="row mb-3">
                                                <label class="col-sm-4 col-form-label">Subject Name</label>
                                                <div class="col-sm-8">
                                                    <input type="text" class="form-control"
                                                           value="Java Web Application Development" readonly
                                                           placeholder="Subject Name" title="Subject Name">
                                                </div>
                                            </div>
                                            <div class="row mb-3 align-items-center">
                                                <label class="col-sm-4 col-form-label">Status</label>
                                                <div class="col-sm-4">
                                                    <select class="form-select" id="subject-status"
                                                            aria-label="Subject Status">
                                                        <option selected>Published</option>
                                                        <option>Unpublished</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <hr>
                                            <div class="row mb-3 align-items-center">
                                                <label class="col-sm-4 col-form-label">Owner (Expert)</label>
                                                <div class="col-sm-4 d-flex align-items-center">
                                                    <img src="../../img/course-3.jpg" alt="Expert Avatar"
                                                         class="rounded-circle me-2" width="40" height="40">
                                                    <span>Jane Smith</span>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <!-- Right: Subject Image -->
                                    <div
                                            class="col-md-4 d-flex flex-column align-items-center justify-content-start">
                                        <img src="../../img/team-1.jpg" alt="Subject Image"
                                             class="img-fluid rounded mb-3"
                                             style="max-width: 250px;">
                                    </div>
                                </div>
                                <!-- Description below, full width -->
                                <div class="row mt-4">
                                    <div class="col-12">
                                        <label class="form-label fw-bold">Description</label>
                                        <textarea class="form-control" rows="5" readonly title="Subject Description"
                                                  placeholder="Subject Description">
                                                    Learn how to build Java web applications using modern frameworks and tools.
                                                </textarea>
                                    </div>
                                </div>
                                <!-- Button row below description -->
                                <div class="row mt-3">
                                    <div class="col-12 text-end">
                                        <button type="button" class="btn btn-primary"
                                                onclick="notification('Changes saved successfully!')">
                                            Save Changes
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Dimension Tab -->
                    <div class="tab-pane fade" id="dimension" role="tabpanel" aria-labelledby="dimension-tab">
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white">
                                Subject Dimensions
                            </div>
                            <div class="card-body">
                                <table class="table table-bordered align-middle">
                                    <thead>
                                    <tr>
                                        <th>Dimension Name</th>
                                        <th>Description</th>
                                        <th>Type</th>
                                        <th>Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>Knowledge</td>
                                        <td>Core concepts and theory</td>
                                        <td>Core</td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-primary me-1"><i
                                                    class="fas fa-edit"></i> Edit
                                            </button>
                                            <button class="btn btn-sm btn-outline-danger"><i
                                                    class="fas fa-trash"></i> Delete
                                            </button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Skill</td>
                                        <td>Practical coding and project work</td>
                                        <td>Practical</td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-primary me-1"><i
                                                    class="fas fa-edit"></i> Edit
                                            </button>
                                            <button class="btn btn-sm btn-outline-danger"><i
                                                    class="fas fa-trash"></i> Delete
                                            </button>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <button class="btn btn-success" data-bs-toggle="modal"
                                        data-bs-target="#addDimensionModal">
                                    <i class="fas fa-plus me-1"></i>Add Dimension
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Price Package Tab -->
                    <div class="tab-pane fade" id="price" role="tabpanel" aria-labelledby="price-tab">
                        <div class="card mb-4">
                            <div class="card-header bg-primary text-white">
                                Price Packages
                            </div>
                            <div class="card-body">
                                <table class="table table-bordered align-middle">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Package Name</th>
                                        <th>Duration</th>
                                        <th>Prices List</th>
                                        <th>Sale Price</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>1</td>
                                        <td>Basic</td>
                                        <td>1 Month</td>
                                        <td>$50</td>
                                        <td>$50</td>
                                        <td><span class="badge bg-success">Active</span></td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-primary me-1"><i
                                                    class="fas fa-edit"></i> Edit
                                            </button>
                                            <button class="btn btn-sm btn-outline-danger"><i
                                                    class="fas fa-trash"></i> Delete
                                            </button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>2</td>
                                        <td>Premium</td>
                                        <td>6 Months</td>
                                        <td>$250</td>
                                        <td>$250</td>
                                        <td><span class="badge bg-secondary">Inactive</span></td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-primary me-1"><i
                                                    class="fas fa-edit"></i> Edit
                                            </button>
                                            <button class="btn btn-sm btn-outline-danger"><i
                                                    class="fas fa-trash"></i> Delete
                                            </button>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <button class="btn btn-success" data-bs-toggle="modal"
                                        data-bs-target="#addPricePackageModal">
                                    <i class="fas fa-plus me-1"></i>Add Price Package
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../component/notification.html"/>
<jsp:include page="../../component/footer.html"/>
<jsp:include page="../../component/back_to_top.html"/>

<!-- Add Dimension Modal -->
<div class="modal fade" id="addDimensionModal" tabindex="-1" aria-labelledby="addDimensionModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <form class="modal-content" id="addDimensionForm">
            <div class="modal-header">
                <h5 class="modal-title" id="addDimensionModalLabel">Add Dimension</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="dimensionName" class="form-label">Dimension Name</label>
                    <input type="text" class="form-control" id="dimensionName" required>
                </div>
                <div class="mb-3">
                    <label for="dimensionDescription" class="form-label">Description</label>
                    <textarea class="form-control" id="dimensionDescription" rows="3" required></textarea>
                </div>
                <div class="mb-3">
                    <label for="dimensionType" class="form-label">Type</label>
                    <select class="form-select" id="dimensionType" required>
                        <option value="">Select Type</option>
                        <option value="Core">Core</option>
                        <option value="Practical">Practical</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Add</button>
            </div>
        </form>
    </div>
</div>

<!-- Add Price Package Modal -->
<div class="modal fade" id="addPricePackageModal" tabindex="-1" aria-labelledby="addPricePackageModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <form class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addPricePackageModalLabel">Add Price Package</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="packageName" class="form-label">Package Name</label>
                    <input type="text" class="form-control" id="packageName" required>
                </div>
                <div class="mb-3">
                    <label for="packageDuration" class="form-label">Duration</label>
                    <input type="text" class="form-control" id="packageDuration" placeholder="e.g. 1 Month"
                           required>
                </div>
                <div class="mb-3">
                    <label for="packagePrice" class="form-label">Price List</label>
                    <input type="number" class="form-control" id="packagePrice" min="0" required>
                </div>
                <div class="mb-3">
                    <label for="packageSalePrice" class="form-label">Sale Price</label>
                    <input type="number" class="form-control" id="packageSalePrice" min="0" required>
                </div>
                <div class="mb-3">
                    <label for="packageStatus" class="form-label">Status</label>
                    <select class="form-select" id="packageStatus" required>
                        <option value="">Select Status</option>
                        <option value="Active">Active</option>
                        <option value="Inactive">Inactive</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary">Add</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="${pageContext.request.contextPath}/js/Notification.js"></script>
<script src="${pageContext.request.contextPath}/js/SubjectDetails.js"></script>
</body>

</html>
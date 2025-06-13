<%--
  Created by IntelliJ IDEA.
  User: TranHoan
  Date: 6/10/2025
  Time: 5:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
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
    <link href="${pageContext.request.contextPath}/css/marketing.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../../component/spinner.html"/>
<jsp:include page="../../component/navbar.html"/>
<jsp:include page="../../component/header.jsp"/>

<div class="container-xxl py-5">
    <div class="container">
        <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
            <h6 class="section-title bg-white text-center text-primary px-3">Marketing</h6>
            <h1 class="mb-3">Slider Details</h1>
        </div>

        <!-- IMG -->
        <div class="slider-img-box mb-4 image-box">
            <c:if test="${slider ne null}">
                <img class="img-fluid w-50" src="${slider.getImageUrl()}" alt="Slider Image">
            </c:if>
        </div>

        <!-- Main content -->
        <div class="row justify-content-center">
            <!-- Filter -->
            <div class="col-md-3 mb-3">
                <div class="setting-card shadow-sm">
                    <div class="d-flex align-items-center mb-3">
                        <i class="bi bi-sliders2-vertical text-primary me-2"></i>
                        <span class="filter">Filter & Setting</span>
                    </div>
                    <div class="mb-3">
                        <div class="form-check form-switch mb-2">
                            <input class="form-check-input" type="checkbox" id="cb1">
                            <label class="form-check-label hiddenItems" for="cb1" id="cb1_label">Ẩn ảnh</label>
                        </div>
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" id="cb2">
                            <label class="form-check-label hiddenItems" for="cb2" id="cb2_label">Ẩn người tạo</label>
                        </div>
                    </div>
                    <button class="btn w-100 mb-2 editBtn"
                            id="editSliderBtn"
                            data-id="${slider.getId()}"
                            data-title="${slider.getTitle()}"
                            data-link="${slider.getBacklinkUrl()}"
                            data-status="${slider.isStatus()}"
                            data-image="${slider.getImageUrl()}">
                        <i class="fas fa-edit me-1"></i> Edit
                    </button>
                    <button id="reviewSliderBtn"
                            class="btn w-100 reviewBtn"
                            data-image="${slider.getImageUrl()}">
                        <i class="fas fa-eye me-1"></i> Review
                    </button>
                </div>
            </div>

            <!-- Table -->
            <div class="col-md-9">
                <table class="slider-detail-table">
                    <c:if test="${slider ne null}">
                        <tr>
                            <th class="title">ID</th>
                            <td class="content">${slider.getId()}</td>
                        </tr>
                        <tr class="author-row" hidden>
                            <th class="title">Author</th>
                            <td class="content">${slider.getAuthor()}</td>
                        </tr>
                        <tr>
                            <th class="title">Title</th>
                            <td class="content">${slider.getTitle()}</td>
                        </tr>
                        <tr>
                            <th class="title">Link</th>
                            <td class="content">${slider.getBacklinkUrl()}</td>
                        </tr>
                        <tr>
                            <th class="title">Status</th>
                            <td class="content">
                                <span class="statusBtn ${slider.isStatus() ? 'btn-success' : 'btn-danger'}">
                                        ${slider.isStatus() ? 'Active' : 'Inactive'}
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <th class="title">Note</th>
                            <td class="content">${slider.getNote()}</td>
                        </tr>
                    </c:if>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Edit Slider Modal -->
<div class="modal fade" id="editSliderModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <form action="/updateSlider" method="POST" enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Slider</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">
                    <input type="hidden" id="editSliderId" name="id"/>
                    <div class="mb-3">
                        <label for="editSliderTitle" class="form-label">Title</label>
                        <input type="text" class="form-control" id="editSliderTitle" name="title"/>
                    </div>
                    <div class="mb-3">
                        <label for="editSliderLink" class="form-label">Link</label>
                        <input type="text" class="form-control" id="editSliderLink" name="link"/>
                    </div>
                    <div class="mb-3">
                        <label for="editSliderStatus" class="form-label">Status</label>
                        <select class="form-select" id="editSliderStatus" name="status">
                            <option value="true">Active</option>
                            <option value="false">Inactive</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="editSliderImage" class="form-label">Upload Image</label>
                        <input type="file" class="form-control" id="editSliderImage" name="image" accept="image/*"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Preview</label>
                        <img id="previewImage" src="#" class="img-fluid border"
                             style="display:none; max-height:200px;"/>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">Save changes</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Review Slider Modal -->
<div class="modal fade" id="theatreModal" tabindex="-1">
    <div class="modal-dialog modal-fullscreen">
        <div class="modal-content bg-dark bg-opacity-75 d-flex align-items-center justify-content-center border-0">
            <img id="theatreImage" class="img-fluid rounded shadow-lg" style="max-height: 90vh;" />
            <button type="button" class="btn-close btn-close-white position-absolute top-0 end-0 m-4" data-bs-dismiss="modal"></button>
        </div>
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
    // Checkbox: Hiện ảnh
    document.getElementById("cb1").addEventListener("change", function () {
        const imageBox = document.querySelector(".image-box");
        imageBox.hidden = this.checked;
        const label = document.getElementById("cb1_label");
        label.innerHTML = this.checked ? "Hiện ảnh" : "Ẩn ảnh";
    });

    // Checkbox: Hiện người tạo (Author)
    document.getElementById("cb2").addEventListener("change", function () {
        const authorRow = document.querySelector(".author-row");
        if (this.checked) {
            authorRow.removeAttribute("hidden");
        } else {
            authorRow.setAttribute("hidden", true);
        }
        const label = document.getElementById("cb2_label");
        label.innerHTML = this.checked ? "Hiện người tạo" : "Ẩn người tạo";
    });

    document.getElementById('editSliderBtn').addEventListener('click', function () {
        const id = this.getAttribute('data-id');
        const title = this.getAttribute('data-title');
        const link = this.getAttribute('data-link');
        const status = this.getAttribute('data-status');
        const image = this.getAttribute('data-image');

        // Gán vào modal form
        document.getElementById('editSliderId').value = id;
        document.getElementById('editSliderTitle').value = title;
        document.getElementById('editSliderLink').value = link;
        document.getElementById('editSliderStatus').value = status === 'true' ? 'true' : 'false';

        // Gán preview ảnh
        const preview = document.getElementById('previewImage');
        preview.src = image;
        preview.style.display = 'block';

        // Hiển thị modal
        const modal = new bootstrap.Modal(document.getElementById('editSliderModal'));
        modal.show();
    });

    document.getElementById('editSliderImage').addEventListener('change', function (e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (event) {
                document.getElementById('previewImage').src = event.target.result;
                document.getElementById('previewImage').style.display = 'block';
            };
            reader.readAsDataURL(file);
        }
    });

    // Review Slider Image
    document.getElementById('reviewSliderBtn').addEventListener('click', function () {
        const imageUrl = this.getAttribute('data-image');
        const theatreImg = document.getElementById('theatreImage');
        theatreImg.src = imageUrl;

        const modal = new bootstrap.Modal(document.getElementById('theatreModal'));
        modal.show();
    });
</script>
</body>
</html>

<%--
    Document   : subjects_list
    Created on : May 19, 2025, 4:38:34 PM
    Author     : TranHoan
--%>

<%--<%@page import="java.util.List"%>--%>
<%--<%@page import="model.Topic"%>--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
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
    <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
    <style>
        .card-text {
            font-size: 0.95rem;
            display: inline-block;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    </style>
</head>

<body>
<jsp:include page="../../component/spinner.html"/>
<jsp:include page="../../component/navbar.html"/>
<jsp:include page="../../component/header.jsp"/>

<!-- Courses Start -->
<div class="container-xxl py-5">
    <div class="container">
        <div class="row">
            <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                <h6 class="section-title bg-white text-center text-primary px-3">Marketing</h6>
                <h1 class="mb-3">Sliders List</h1>
            </div>

            <div class="col-12">
                <div class="d-flex justify-content-end mb-3">
                    <button class="btn btn-success" onclick="addSlider()">
                        <i class="fas fa-plus me-1"></i> Add new
                    </button>
                </div>
            </div>

            <!-- Slider List Start -->
            <div class="col-12">
                <div class="row">
                    <!-- Sidebar Start -->
                    <aside class="col-lg-3 mb-4 mb-lg-0">
                        <div class="card mb-4 shadow-sm p-2" style="border-radius: 12px;">
                            <div class="card-body">
                                <!-- Filter -->
                                <div class="mb-4">
                                    <label for="statusFilterSelect"
                                           class="form-label fw-semibold text-primary-emphasis">
                                        <i class="bi bi-filter me-1"></i>
                                        <span style="color: #1a237e;">Filter by Status</span>
                                    </label>
                                    <select class="form-select" id="statusFilterSelect">
                                        <option value="all">All Status</option>
                                        <option value="active">Active</option>
                                        <option value="inactive">Inactive</option>
                                    </select>
                                </div>

                                <!-- Search -->
                                <form class="mb-4" id="sliderSearchForm">
                                    <label for="sliderSearch" class="form-label fw-semibold text-primary-emphasis">
                                        <i class="bi bi-search me-1"></i>
                                        <span style="color: #1a237e;">Search</span>
                                    </label>
                                    <div class="input-group input-group-sm">
                                        <input type="text" id="sliderSearch" class="form-control"
                                               placeholder="Search..." style="color: #212529;">
                                        <button class="btn btn-primary" type="submit"><i
                                                class="bi bi-search"></i></button>
                                    </div>
                                </form>

                                <!-- Setting -->
                                <div class="mb-4">
                                    <label class="form-label fw-semibold text-primary-emphasis">
                                        <i class="bi bi-gear me-1"></i>
                                        <span style="color: #1a237e;">Setting</span>
                                    </label>
                                    <div class="mb-2">
                                        <input type="number" value="" class="w-50"
                                               id="settingOption1">
                                        <label class="form-check-label" for="settingOption1">
                                            Chỉnh cỡ bảng
                                        </label>
                                    </div>
                                    <div class="form-check mb-2 form-switch">
                                        <input class="form-check-input" type="checkbox" id="settingOption2">
                                        <label class="form-check-label" for="settingOption2">
                                            Ẩn ảnh
                                        </label>
                                    </div>
                                    <div class="form-check mb-2 form-switch">
                                        <input class="form-check-input" type="checkbox" id="settingOption4">
                                        <label class="form-check-label" for="settingOption4">
                                            Ẩn trạng thái
                                        </label>
                                    </div>
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" id="settingOption3">
                                        <label class="form-check-label" for="settingOption3">
                                            Hiện người tạo
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </aside>

                    <div class="col-lg-9">
                        <div class="row g-4" id="sliderList"></div>

                        <!-- No Results Message -->
                        <div id="noResultsMessage" class="text-center text-muted py-5"
                             style="display:none; font-size:1.2rem;">
                            Không có kết quả
                        </div>

                        <!-- Pagination Start -->
                        <div id="pag">
                            <div class="card-footer d-flex justify-content-between align-items-center">
                                <span id="message">Showing 1 to 10 entries</span>
                                <nav aria-label="Course pagination">
                                    <ul class="pagination mb-0" id="pagination"
                                        style="font-size: 1rem; --bs-pagination-active-bg: #0d6efd; --bs-pagination-active-border-color: #0d6efd;"></ul>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Theater-style Image Preview Modal -->
<div class="modal fade" id="sliderImageModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-xl">
        <div class="modal-content bg-dark border-0" style="background-color: rgba(0, 0, 0, 0.95);">
            <div class="modal-body p-0 position-relative">
                <!-- Close Button -->
                <button type="button" class="btn-close btn-close-white position-absolute top-0 end-0 m-3" data-bs-dismiss="modal" aria-label="Close"></button>

                <!-- Image -->
                <img id="theatreImage" src="" alt="Slider Image"
                     class="w-100" style="max-height: 90vh; object-fit: contain; border-radius: 8px;">
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../component/notification.html"/>
<jsp:include page="../../component/footer.html"/>
<jsp:include page="../../component/back_to_top.html"/>

<script>
    let sliders = [];
    let displayFilterSliders = [];
    let itemsPerPage = 8;
    let currentPage = 1;
    let filteredSliders;
    let imgHidden = false;
    let authorHidden = true;

    async function renderSliders() {
        const response = await fetch(`${pageContext.request.contextPath}/slider`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        if (response.ok) {
            const data = await response.json();
            sliders = data.map(slider => ({
                id: slider.id,
                title: slider.title,
                img: slider.imageUrl,
                link: slider.backlinkUrl,
                status: slider.status,
                author: slider.author
            }));
        }
        filteredSliders = [...sliders];
        renderData(filteredSliders)
    }

    function renderData(filteredSliders) {
        const list = document.getElementById('sliderList');
        list.innerHTML = '';

        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;
        const slidersToRender = filteredSliders.slice(startIndex, endIndex);

        if (slidersToRender.length === 0) {
            document.getElementById('noResultsMessage').style.display = 'block';
            document.getElementById('pag').style.display = 'none';
        } else {
            document.getElementById('noResultsMessage').style.display = 'none';
            document.getElementById('pag').style.display = 'block';
        }

        slidersToRender.forEach(slider => {
            const col = document.createElement('div');
            col.className = `col-md-3 wow fadeInUp slider-card d-flex`;
            col.setAttribute('data-wow-delay', '0.1s');
            const id = slider.id;
            const arr = slider.status === true ?
                ['btn-danger', '<i class="fas fa-eye-slash me-1"></i> Inactive', 'btn-success', 'Active']
                : ['btn-success', '<i class="fas fa-eye me-1"></i> Active', 'btn-danger', 'Inactive'];

            col.innerHTML = `
                    <div class="card h-100 shadow-sm border border-dark" style="position: relative; border-radius: 12px;">
                    <img src="../../` + slider.img + `" class="card-img-top" alt="` + slider.title + `" style="width: 100%; height: 140px; object-fit: cover; border-top-left-radius: 12px; border-top-right-radius: 12px;">
                    <div class="card-body d-flex flex-column p-2">
                        <h5 class="card-title text-center" style="font-size: 1.1rem;">` + slider.title + `</h5>
                        <p class="card-text mb-1">ID: ` + slider.id + `</p>
                        <p class="card-text mb-1" id="author" hidden>Author: ` + slider.author + `</p>
                        <p class="card-text mb-1">Link: <a href=" ` + slider.link + `" target="_blank">` + slider.link + `</a></p>
                        <p class="card-text mb-2">Status: <button class="btn ` + arr[2] + ` rounded-pill w-auto">` + arr[3] + `</button></p>
                        <div class="d-flex justify-content-center gap-1 mt-auto">
                        <button class="btn btn-sm btn-primary" onclick="editSlider('` + id +`')">
                            <i class="fas fa-edit me-1"></i> Edit
                        </button>
                        <button class="btn btn-sm ` + arr[0] + `" onclick="toggleStatus('` + id +`')">
                            ` + arr[1] + `
                        </button>
                        <button class="btn btn-sm btn-info text-white" onclick="viewSlider('` + id +`')">
                            <i class="fas fa-eye me-1"></i> View
                        </button>
                        </div>
                    </div>
                    </div>
                    `;
            list.appendChild(col);
        });

        renderPagination(filteredSliders);
    }

    function renderPagination(slidersToRender) {
        const pagination = document.getElementById('pagination');
        const message = document.getElementById('message')
        const sliders = slidersToRender;
        pagination.innerHTML = `
            <li class="page-item ` + (currentPage === 1 ? 'disabled' : '') + `">
                 <a class="page-link rounded-pill px-3" href="#" onclick="currentPage = ` + (currentPage - 1) + `; renderData(sliders);">
                     <i class="bi bi-chevron-left"></i> Prev
                 </a>
            </li>`

        const totalPages = Math.ceil(slidersToRender.length / itemsPerPage);

        for (let i = 1; i <= totalPages; i++) {
            pagination.innerHTML += `
            <li class="page-item ` + (i === currentPage ? 'active' : '') + `">
                 <a class="page-link rounded-pill px-3" href="#" onclick="currentPage = ` + i + `; renderData(sliders);">`
                + i + `</a>
            </li>`
        }

        pagination.innerHTML += `
            <li class="page-item ` + (currentPage === totalPages ? 'disabled' : '') + `">
                 <a class="page-link rounded-pill px-3" href="#" onclick="currentPage = ` + (currentPage + 1) + `; renderData(sliders);">
                    Next <i class="bi bi-chevron-right"></i>
                 </a>
            </li>`

        message.innerHTML = `Showing ` + ((currentPage - 1) * itemsPerPage + 1) + ` to `
            + Math.min(currentPage * itemsPerPage, slidersToRender.length) + ` entries of ` + slidersToRender.length + ` total`;
    }

    // Initial render
    renderSliders();

    // Handle change table's size
    document.getElementById('settingOption1').addEventListener('change', function () {
        itemsPerPage = parseInt(this.value) || 8;
        renderData(filteredSliders);
    })

    // Handle show/hide images
    document.getElementById('settingOption2').addEventListener('change', function () {
        imgHidden = this.checked;
        const images = document.querySelectorAll('.slider-card img');
        images.forEach(img => {
            img.style.display = imgHidden ? 'none' : 'block';
        });
        const label = document.querySelector('label[for="settingOption2"]');
        label.innerHTML = imgHidden ? 'Hiện ảnh' : 'Ẩn ảnh';
    })

    // Handle show/hide authors
    document.getElementById('settingOption3').addEventListener('change', function () {
        authorHidden = this.checked;
        const authors = document.querySelectorAll('.slider-card #author');
        authors.forEach(author => {
            author.hidden = !authorHidden;
        });
        const label = document.querySelector('label[for="settingOption3"]');
        label.innerHTML = authorHidden ? 'Ẩn người tạo' : 'Hiện người tạo';
    })

    // Handle show/hide status
    document.getElementById('settingOption4').addEventListener('change', function () {
        const statusButtons = document.querySelectorAll('.slider-card .btn-danger, .slider-card .btn-success');
        statusButtons.forEach(button => {
            button.hidden = this.checked;
        });
        const label = document.querySelector('label[for="settingOption4"]');
        label.innerHTML = this.checked ? 'Hiện trạng thái' : 'Ẩn trạng thái';
    })

    function filterSliders(searchTerm = '', status = 'all') {
        let sliders = filteredSliders
        if (!searchTerm.empty || !status.empty) {
            sliders = sliders.filter(slider => {
                const titleMatch = slider.title.toLowerCase().includes(searchTerm.toLowerCase());
                const authorMatch = slider.author.toLowerCase().includes(searchTerm.toLowerCase());
                const statusMatch = status === 'all' || slider.status === status;

                return (titleMatch || authorMatch) && statusMatch;
            });
        }
        currentPage = 1;
        displayFilterSliders = sliders;
        renderData(displayFilterSliders);
    }

    document.getElementById('sliderSearchForm').addEventListener('submit', function (e) {
        e.preventDefault();
        const searchTerm = document.getElementById('sliderSearch').value;
        getSlidersByKeyword(searchTerm);
    });

    // Handle status filter change
    document.getElementById('statusFilterSelect').addEventListener('change', function () {
        const status = this.value;
        getSlidersByFilter(status);
    })

    async function getSlidersByFilter(status) {
        const response = await fetch(`${pageContext.request.contextPath}/slider?status=` + status === 'all' ? '' : status, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Type': 'filter'
            }
        })
        if (response.ok) {
            const data = await response.json();
            sliders = data.map(slider => ({
                id: slider.id,
                title: slider.title,
                img: slider.imageUrl,
                link: slider.backlinkUrl,
                status: slider.status,
                author: slider.author
            }));
        }
        filteredSliders = [...sliders];
        renderData(filteredSliders)
    }

    async function getSlidersByKeyword(keyword) {
        const response = await fetch(`${pageContext.request.contextPath}/slider?keyword=` + keyword, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Type': 'keyword'
            }
        })
        if (response.ok) {
            const data = await response.json();
            sliders = data.map(slider => ({
                id: slider.id,
                title: slider.title,
                img: slider.imageUrl,
                link: slider.backlinkUrl,
                status: slider.status,
                author: slider.author
            }));
        }
        filteredSliders = [...sliders];
        renderData(filteredSliders)
    }

    function editSlider(id) {
        window.location.href = `${pageContext.request.contextPath}/slider?id=` + id;
    }

    function viewSlider(id) {
        const slider = sliders.find(s => s.id === id);
        if (!slider) return;

        const imgEl = document.getElementById('theatreImage');
        imgEl.src = `../../` + slider.img;
        imgEl.alt = slider.title || 'Slider Image';

        const modal = new bootstrap.Modal(document.getElementById('sliderImageModal'));
        modal.show();
    }

</script>

<script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
<script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="${pageContext.request.contextPath}/js/Notification.js"></script>
<script src="${pageContext.request.contextPath}/js/SubjectsList.js"></script>
</body>

</html>
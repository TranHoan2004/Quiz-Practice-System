
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Post Details - Marketing</title>
    
    <!-- Libraries Stylesheet -->
    <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    
    <!-- Customized Bootstrap Stylesheet -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    
    <link href="${pageContext.request.contextPath}/css/post-details.css" rel="stylesheet">
</head>

<body>
<jsp:include page="../../component/header.jsp"/>

<div class="container bg-white p-4 mt-2 ">
    <h2 class="mb-0">Post-Details Management</h2>
    <p class="text-muted mb-3" style="font-size: 0.95rem;">
        Manage your blog posts, search, filter, and control visibility all in one place.
    </p>
    
    <!-- Toolbar -->
    <div class="mb-4 d-flex flex-wrap align-items-center gap-3 justify-content-between">
        
        <!-- Add Button -->
        <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addPostModal">
            <i class="fa-solid fa-plus"></i>
            Add New Post
        </button>
        
        <!-- Right Group -->
        <div class="d-flex flex-wrap align-items-center gap-3 flex-grow-1 justify-content-end">
            
            <!-- Search -->
            <form action="${pageContext.request.contextPath}/post-details" id="searchForm"
                  class="d-flex align-items-center gap-2">
                <div class="search-box position-relative flex-grow-1">
                    <i class="fas fa-search search-icon"></i>
                    <input type="text" id="searchInput" name="keyword"
                           class="form-control form-control-sm"
                           placeholder="Search by title or category..." />
                </div>
                <button type="submit" class="btn btn-primary btn-sm">Search</button>
            </form>
            
            <!-- Page Size Selector -->
            <form action="${pageContext.request.contextPath}/post-details" method="get">
                <div class="d-flex align-items-center gap-2 mx-1 dropdown">
                    <label for="pageSize" class="fw-bold form-label mb-0 text-primary fw-semibold">Show</label>
                    <select id="pageSize" name="pageSize" class="page-size-selector form-select-sm text-primary"
                            onchange="this.form.submit()">
                        <option value="5" ${pageSize == 5 ? 'selected' : ''}>5</option>
                        <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
                        <option value="15" ${pageSize == 15 ? 'selected' : ''}>15</option>
                        <option value="20" ${pageSize == 20 ? 'selected' : ''}>20</option>
                    </select>
                </div>
            </form>
            
            <!-- Filter + Column Display -->
            <div class="d-flex gap-2">
                <form action="${pageContext.request.contextPath}/post-details" method="get">
                    <div class="dropdown">
                        <button class="btn btn-outline-secondary btn-sm dropdown-toggle"
                                type="button" data-bs-toggle="dropdown">Filter</button>
                        <ul class="dropdown-menu">
                            <li><h6 class="dropdown-header">Status</h6></li>
                            <li><a class="dropdown-item" href="?status=true">Active</a></li>
                            <li><a class="dropdown-item" href="?status=false">Inactive</a></li>
                            
                            <li><hr class="dropdown-divider" /></li>
                            
                            <li><h6 class="dropdown-header">Feature</h6></li>
                            <li><a class="dropdown-item" href="?feature=true">Featured</a></li>
                            <li><a class="dropdown-item" href="?feature=flase">Not Featured</a></li>
                            
                            <li><hr class="dropdown-divider" /></li>
                            
                            <li><h6 class="dropdown-header">Sort By</h6></li>
                            <li><a class="dropdown-item" href="?sortBy=newest">Newest First</a></li>
                            <li><a class="dropdown-item" href="?sortBy=oldest">Oldest First</a></li>
                        </ul>
                    </div>
                </form>
                
                <div class="dropdown">
                    <button class="btn btn-outline-secondary btn-sm dropdown-toggle"
                            data-bs-toggle="dropdown">Column Display</button>
                    <div class="dropdown-menu p-3">
                        <div class="form-check"><input class="form-check-input column-toggle" type="checkbox" value="thumbnail" checked /> Thumbnail</div>
                        <div class="form-check"><input class="form-check-input column-toggle" type="checkbox" value="category" checked /> Category</div>
                        <div class="form-check"><input class="form-check-input column-toggle" type="checkbox" value="title" checked /> Title</div>
                        <div class="form-check"><input class="form-check-input column-toggle" type="checkbox" value="brief" checked /> Brief Info</div>
                        <div class="form-check"><input class="form-check-input column-toggle" type="checkbox" value="desc" checked /> Description</div>
                        <div class="form-check"><input class="form-check-input column-toggle" type="checkbox" value="featuring" checked /> Featuring</div>
                        <div class="form-check"><input class="form-check-input column-toggle" type="checkbox" value="status" checked /> Status</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Posts Table -->
    <div class="table-responsive">
        <table class="table table-bordered" id="postsTable">
            <thead>
            <tr>
                <th>#</th>
                <th class="thumbnail">Thumbnail</th>
                <th class="category">Category</th>
                <th class="title">Title</th>
                <th class="brief">Brief Info</th>
                <th class="desc">Description</th>
                <th class="featuring">Featuring</th>
                <th class="status">Status</th>
                <th class="action">Actions</th>
            </tr>
            </thead>
            <tbody id="tableBody">
            <c:choose>
                <c:when test="${not empty blogs}">
                    <c:forEach var="blog" items="${blogs}" varStatus="loop">
                        <tr>
                            <td>${(currentIndex - 1) * pageSize + loop.index + 1}</td>
                            <td class="thumbnail">
                                <c:if test="${not empty blog.blogMediaList}">
                                    <c:set var="firstMedia" value="${blog.blogMediaList[0]}" />
                                    
                                    <c:choose>
                                        <c:when test="${fn:endsWith(firstMedia.mediaUrl, '.mp4')
                                                                    || fn:endsWith(firstMedia.mediaUrl, '.mov')
                                                                    || fn:endsWith(firstMedia.mediaUrl, '.mkv')
                                                                    || fn:endsWith(firstMedia.mediaUrl, '.avi')}">
                                            <video width="200" controls>
                                                <source src="${pageContext.request.contextPath}/${firstMedia.mediaUrl}">
                                            </video>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/${firstMedia.mediaUrl}" width="100" />
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            
                            </td>
                            
                            <td class="category">${blog.category}</td>
                            <td class="title">${blog.title}</td>
                            <td class="brief">${blog.briefInfo}</td>
                            <td class="desc">${blog.content}</td>
                            <td class="feature text-center">${blog.flagFeature ? "ON" : "OFF"}</td>
                            <td class="status text-center">${blog.status ? "Active" : "Inactive"}</td>
                            <td>
                                <button
                                        class="btn btn-sm btn-outline-info btn-view"
                                        data-bs-toggle="modal"
                                        data-bs-target="#viewPostModal"
                                        data-title="${blog.title}"
                                        data-category="${blog.category}"
                                        data-status="${blog.status}"
                                        data-brief="${blog.briefInfo}"
                                        data-content="${blog.content}"
                                        data-featuring="${blog.flagFeature}"
                                        data-date="${blog.createdDate}"
                                        data-blog-media='${fn:escapeXml(blog.blogMediaJson)}'
                                >
                                    View
                                </button>
                                
                                <button class="btn btn-sm btn-outline-primary btn-edit"
                                        data-index="${loop.index}"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editPostModal"
                                        data-id="${blog.id}"
                                        data-title="${blog.title}"
                                        data-category="${blog.categoryId}"
                                        data-status="${blog.status}"
                                        data-brief="${blog.briefInfo}"
                                        data-content="${blog.content}"
                                        data-featuring="${blog.flagFeature}"
                                        data-date="${blog.createdDate}"
                                        data-blog-media='${fn:escapeXml(blog.blogMediaJson)}'
                                >
                                    Edit
                                </button>
                                <form action="${pageContext.request.contextPath}/post-details" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete-post">
                                    <input type="hidden" name="id" value="${blog.id}">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" onclick="return confirm('Do you want to delete this post?');">Del</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="9" style="text-align:center; font-style: italic; color: gray;">
                            No posts found.
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    
    <!-- Pagination -->
    <c:set var="maxPagesToShow" value="10" />
    <c:set var="halfPagesToShow" value="${maxPagesToShow / 2}" />
    
    <c:set var="startPage" value="${currentIndex - halfPagesToShow}" />
    <c:if test="${startPage < 1}">
        <c:set var="startPage" value="1" />
    </c:if>
    
    <c:set var="endPage" value="${startPage + maxPagesToShow - 1}" />
    <c:if test="${endPage > totalPages}">
        <c:set var="endPage" value="${totalPages}" />
    </c:if>
    
    <c:if test="${endPage - startPage + 1 < maxPagesToShow}">
        <c:set var="startPage" value="${endPage - maxPagesToShow + 1}" />
        <c:if test="${startPage < 1}">
            <c:set var="startPage" value="1" />
        </c:if>
    </c:if>
    
    <nav>
        <ul class="pagination justify-content-center mt-4">
            <li class="page-item ${currentIndex == 1 ? 'disabled' : ''}">
                <a class="page-link" href="?page=${currentIndex - 1}">Prev</a>
            </li>
            
            <c:forEach var="i" begin="${startPage}" end="${endPage}">
                <li class="page-item ${i == currentIndex ? 'active' : ''}">
                    <a class="page-link" href="?page=${i}">${i}</a>
                </li>
            </c:forEach>
            
            <li class="page-item ${currentIndex == totalPages ? 'disabled' : ''}">
                <a class="page-link" href="?page=${currentIndex + 1}">Next</a>
            </li>
        </ul>
    </nav>
</div>

<!-- Add New Post Modal -->
<div class="modal fade" id="addPostModal" tabindex="-1" aria-labelledby="addPostModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addPostModalLabel">Add New Post</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/post-details"
                      method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add-post" />
                    <div class="row g-3">
                        <!-- Thumbnail -->
                        <div class="col-md-6">
                            <label class="form-label">Thumbnail</label>
                            <input type="file" id="mediaInput"
                                   class="form-control"
                                   accept="image/*,video/*"
                                   multiple="multiple"
                                   name="media" />
                            <div id="previewArea" class="mt-2"></div>
                        </div>
                        
                        <!-- Category -->
                        <div class="col-md-6">
                            <label class="form-label">Category</label>
                            <select class="form-select" name="category">
                                <c:forEach items="${categories}" var="category">
                                    <option value="${category.categoryId}">${category.category}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <!-- Title -->
                        <div class="col-12">
                            <label class="form-label">Title</label>
                            <input type="text" class="form-control" name="title" required />
                        </div>
                        
                        <!-- Brief Info -->
                        <div class="col-12">
                            <label class="form-label">Brief Info</label>
                            <textarea type="text" class="form-control" name="briefInfo" rows="3"></textarea>
                        </div>
                        
                        <!-- Description -->
                        <div class="col-12">
                            <label class="form-label">Description</label>
                            <textarea class="form-control" name="content" rows="5"></textarea>
                        </div>
                        
                        <!-- Status Switch -->
                        <div class="col-md-6 d-flex align-items-end">
                            <div class="form-check form-switch">
                                <input class="form-check-input" type="checkbox" id="statusSwitch" name="status" value="true">
                                <label class="form-check-label" for="statusSwitch">Active Status</label>
                            </div>
                        </div>
                        
                        <!-- Featuring Switch -->
                        <div class="col-md-6 d-flex align-items-end">
                            <div class="form-check form-switch">
                                <input class="form-check-input" type="checkbox" id="featuring" name="featured" value="true">
                                <label class="form-check-label" for="featuring">Show as Featured Post</label>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Submit -->
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Save Post</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Modal View Post - Improved Design -->
<div class="modal fade" id="viewPostModal" tabindex="-1" aria-labelledby="viewPostModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-xl"> <!-- căn giữa màn hình -->
        <div class="modal-content shadow-lg rounded-4 border-0">
            <div class="modal-header bg-primary text-white rounded-top-4">
                <h5 class="modal-title" id="viewPostModalLabel">
                    <i class="bi bi-card-text me-2"></i>Post Details
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            
            <div class="modal-body">
                <div class="row g-4">
                    
                    <div id="mediaContainer" class="col-md-4 text-center">
                        <div id="viewMediaContainer"
                             class="d-flex flex-column justify-content-center align-items-center">
                        </div>
                    </div>
                    <div class="col-md-8">
                        <h4 id="viewTitle" class="fw-bold mb-3"></h4>
                        <p class="text-muted fst-italic mb-3" id="viewBrief"></p>
                        
                        <dl class="row">
                            <dt class="col-sm-4 fw-semibold text-secondary">Category</dt>
                            <dd class="col-sm-8" id="viewCategory"></dd>
                            
                            <dt class="col-sm-4 fw-semibold text-secondary">Featuring</dt>
                            <dd class="col-sm-8" id="viewFeaturing">
                                <span class="badge bg-success text-uppercase">Feature</span>
                            </dd>
                            
                            <dt class="col-sm-4 fw-semibold text-secondary">Status</dt>
                            <dd class="col-sm-8" id="viewStatus">
                                <span class="badge bg-success text-uppercase"></span>
                            </dd>
                            
                            <dt class="col-sm-4 fw-semibold text-secondary">Created Date</dt>
                            <dd class="col-sm-8" id="viewCreatedDate">
                                <span class="badge text-dark"></span>
                            </dd>
                        </dl>
                        
                        <hr />
                        <h5 class="fw-semibold">Brief Content</h5>
                        <p id="viewBriefInfo" class="text-justify"></p>
                        <hr />
                        <h5 class="fw-semibold">Description</h5>
                        <p id="viewDescription" class="text-justify"></p>
                        <hr />
                    </div>
                </div>
            </div>
            
            <div class="modal-footer border-0">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<%-- Edit Post Modal --%>
<div class="modal fade" id="editPostModal" tabindex="-1" aria-labelledby="editPostModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-centered">
        <div class="modal-content shadow-lg rounded-4 border-0">
            <form action="${pageContext.request.contextPath}/post-details" method="post" id="editPostForm" enctype="multipart/form-data">
                <input type="hidden" name="action" value="update-post" />
                <input type="hidden" name="id" id="editPostId" value=""/>
                <div class="modal-header bg-primary text-white rounded-top-4">
                    <h5 class="modal-title" id="editPostModalLabel">
                        <i class="bi bi-pencil-square me-2"></i>Edit Post
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                </div>
                
                <div class="modal-body">
                    <div class="row g-4">
                        <div class="col-md-4 text-center">
                            <input type="file" id="editMediaInput"
                                   class="form-control"
                                   accept="image/*,video/*"
                                   multiple="multiple"
                                   name="media" />
                            <div id="editPreviewArea" class="mt-2"></div>
                            
                            <div id="editMediaContainer"
                                 class="d-flex flex-column justify-content-center align-items-center">
                            </div>
                        </div>
                        
                        <!-- Right column: form fields -->
                        <div class="col-md-8">
                            <label for="editTitle" class="form-label fw-semibold">Title</label>
                            <input type="text" id="editTitle" name="title" class="form-control mb-3" required />
                            
                            <label for="editBriefInfo" class="form-label fw-semibold">Brief Info</label>
                            <input type="text" id="editBriefInfo" name="briefInfo" class="form-control mb-3" />
                            
                            <!-- Category, Featuring, Status on one row -->
                            <div class="row mb-3 align-items-center">
                                <div class="col-md-5">
                                    <label for="editCategory" class="form-label fw-semibold mb-1">Category</label>
                                    <select id="editCategory" class="form-select" name="category">
                                        <c:forEach items="${categories}" var="category">
                                            <option value="${category.categoryId}">${category.category}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-3 d-flex align-items-center pt-4">
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" id="editFeaturing"
                                               name="featured" />
                                        <label class="form-check-label fw-semibold ms-2"
                                               for="editFeaturing">Featured</label>
                                    </div>
                                </div>
                                <div class="col-md-3 d-flex align-items-center pt-4">
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" id="editStatus"
                                               name="status" />
                                        <label class="form-check-label fw-semibold ms-2"
                                               for="editStatus">Status</label>
                                    </div>
                                </div>
                            </div>
                            
                            <label for="editDescription" class="form-label fw-semibold">Description</label>
                            <textarea id="editDescription" name="content" class="form-control"
                                      rows="7"></textarea>
                        </div>
                    </div>
                </div>
                
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../../component/footer.html"/>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/PostDetails.js"></script>
<script src="js/Toast.js"></script>
<script>
    const message = "${param.message}";
    const type = "${param.type}";
    if (message && type) {
        createToast(message, type, 5000);
    }
    
    function isVideoFile(url) {
        const videoExtensions = ['.mp4', '.mov', '.mkv', '.avi'];
        return videoExtensions.some(ext => url.toLowerCase().endsWith(ext));
    }
    
    document.addEventListener('DOMContentLoaded', function () {
        const viewButtons = document.querySelectorAll('.btn-view');
        
        viewButtons.forEach(button => {
            button.addEventListener('click', function () {
                document.getElementById('viewTitle').textContent = this.dataset.title;
                document.getElementById('viewCategory').textContent = this.dataset.category;
                document.getElementById('viewBriefInfo').textContent = this.dataset.brief;
                document.getElementById('viewDescription').textContent = this.dataset.content;
                
                document.querySelector('#viewFeaturing span').textContent = this.dataset.featuring;
                document.querySelector('#viewStatus span').textContent = this.dataset.status;
                document.querySelector('#viewCreatedDate span').textContent = this.dataset.date;
                
                // --- Media List ---
                const mediaContainer = document.getElementById('viewMediaContainer');
                mediaContainer.innerHTML = '';
                
                try {
                    const mediaList = JSON.parse(this.dataset.blogMedia);
                    
                    const createMediaElement = (tagName, mediaUrl) => {
                        const media = document.createElement(tagName);
                        media.src = `${pageContext.request.contextPath}/` + mediaUrl;
                        media.width = 250;
                        media.classList.add('me-2', 'mb-2');
                        if (tagName === 'video') {
                            media.controls = true;
                            media.preload = 'metadata';
                        }
                        return media;
                    };
                    
                    mediaList.forEach(({ mediaType, mediaUrl, caption }) => {
                        const tag = mediaType === 'image' ? 'img' : 'video';
                        const mediaEl = createMediaElement(tag, mediaUrl);
                        mediaContainer.appendChild(mediaEl);
                        
                        const captionEl = document.createElement('p');
                        captionEl.textContent = caption;
                        captionEl.classList.add('text-center');
                        mediaContainer.appendChild(captionEl);
                    });
                    
                } catch (e) {
                    console.error('Lỗi khi parse JSON blogMedia:', e);
                }
            });
        });
        
        const editButtons = document.querySelectorAll('.btn-edit');
        
        editButtons.forEach(button => {
            button.addEventListener('click', function () {
                document.getElementById('editPostId').value = this.dataset.id;
                document.getElementById('editTitle').value = this.dataset.title;
                document.getElementById('editBriefInfo').value = this.dataset.brief;
                document.getElementById('editDescription').value = this.dataset.content;
                
                const categoryId = this.dataset.category; // ví dụ: "2"
                const selectElement = document.getElementById('editCategory');
                console.log("Setting category:", categoryId);
                Array.from(selectElement.options).forEach(option => {
                    option.selected = option.value === categoryId;
                });
                
                document.getElementById('editStatus').checked = this.dataset.status === 'true';
                
                document.getElementById('editFeaturing').checked = this.dataset.featuring === 'true';
                
                const mediaContainer = document.getElementById('editMediaContainer');
                if (mediaContainer) {
                    mediaContainer.innerHTML = '';
                    
                    try {
                        const mediaList = JSON.parse(this.dataset.blogMedia);
                        
                        mediaList.forEach(({ mediaType, mediaUrl, caption }) => {
                            const fullUrl = `${pageContext.request.contextPath}/` + mediaUrl;
                            
                            let mediaEl;
                            if (mediaType === 'image') {
                                mediaEl = document.createElement('img');
                                mediaEl.src = fullUrl;
                                mediaEl.width = 250;
                            } else if (mediaType === 'video') {
                                mediaEl = document.createElement('video');
                                mediaEl.src = fullUrl;
                                mediaEl.width = 250;
                                mediaEl.controls = true;
                                mediaEl.preload = 'metadata';
                            }
                            
                            if (mediaEl) {
                                mediaEl.classList.add('me-2', 'mb-2');
                                mediaContainer.appendChild(mediaEl);
                                
                                const captionEl = document.createElement('p');
                                captionEl.textContent = caption;
                                captionEl.classList.add('text-center');
                                mediaContainer.appendChild(captionEl);
                            }
                        });
                        
                    } catch (e) {
                        console.error('Lỗi khi parse JSON blogMedia:', e);
                    }
                }
            });
        });
        
    });
</script>
</body>

</html>
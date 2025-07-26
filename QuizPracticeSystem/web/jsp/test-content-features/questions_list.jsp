<%-- Trong file jsp/question-features/questions_list.jsp --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Question Management</title>
    <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>

    <!-- Customized Bootstrap Stylesheet -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/post-details.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/questions-list.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../../component/navbar.jsp"/>

<div class="container">

    <header class="header">
        <h1>Question Management</h1>
        <button class="add-btn" onclick="window.location.href='addquestion'">Add New Question</button>
    </header>

    <form id="filterForm" method="GET" action="questionsList" class="search-form">
        <div class="search-group">
            <label for="subjectId">Subject</label>
            <select id="subjectId" name="subjectId" class="dropdown">
                <option value="">All Subjects</option>
                <c:forEach var="subject" items="${subjectList}">
                    <option value="${subject.id}" ${subject.id == paramSubjectId ? 'selected' : ''}>${subject.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="search-group">
            <label for="lessonId">Lesson</label>
            <select id="lessonId" name="lessonId" class="dropdown">
                <option value="">All Lessons</option>
                <c:forEach var="lesson" items="${lessonList}">
                    <option value="${lesson.id}" ${lesson.id == paramLessonId ? 'selected' : ''}>${lesson.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="search-group">
            <label for="dimensionId">Dimension</label>
            <select id="dimensionId" name="dimensionId" class="dropdown">
                <option value="">All Dimensions</option>
                <c:forEach var="dimension" items="${dimensionList}">
                    <option value="${dimension.id}" ${dimension.id == paramDimensionId ? 'selected' : ''}>${dimension.value}</option>
                </c:forEach>
            </select>
        </div>

        <div class="search-group">
            <label for="level">Level</label>
            <select id="level" name="level" class="dropdown">
                <option value="">All Levels</option>
                <c:forEach var="level" items="${levelList}">
                    <option value="${level.value}" ${level.value == paramLevel ? 'selected' : ''}>${level.value}</option>
                </c:forEach>
            </select>
        </div>

        <div class="search-group">
            <label for="status">Status</label>
            <select id="status" name="status" class="dropdown">
                <option value="all" ${paramStatus == null || paramStatus == 'all' ? 'selected' : ''}>All Status</option>
                <option value="active" ${paramStatus == 'active' ? 'selected' : ''}>Active</option>
                <option value="inactive" ${paramStatus == 'inactive' ? 'selected' : ''}>Inactive</option>
            </select>
        </div>

        <div class="search-group extended-search">
            <label for="content">Search by Content</label>
            <div class="input-group">
                <input type="text" id="content" name="content" placeholder="Search questions..."
                       value="${paramContent != null ? paramContent : ''}" class="form-control search-input">
                <button type="submit" class="btn btn-primary search-btn"><i class="fas fa-search"></i> Search</button>
            </div>
        </div>

    </form>

    <div class="table-container">
        <table class="table table-hover">
            <thead>
            <tr>
                <th>ID</th>
                <th>Content</th>
                <th>Subject</th>
                <th>Dimension</th>
                <th>Lesson</th>
                <th>Level</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="question" items="${questionDtoList}">
                <tr>
                    <td>${question.id}</td>
                    <td>${question.content}</td>
                    <td>${question.subjectName}</td>
                    <td>${question.dimensionName}</td>
                    <td>${question.lessonName}</td>
                    <td><span class="status-badge status-${question.level.toLowerCase()}">${question.level}</span></td>
                    <td>
                                <span class="status-badge status-${question.status ? 'active' : 'inactive'}">
                                        ${question.status ? 'Active' : 'Inactive'}
                                </span>
                    </td>
                    <td class="actions">
                            <%-- Nút Option (Toggle Status) --%>
                        <button type="button" class="action-button option-btn"
                                onclick="toggleQuestionStatus('${question.id}')"
                                title="${question.status ? 'Hide Question (Set Inactive)' : 'Show Question (Set Active)'}">
                            Option
                        </button>
                            <%-- Nút Edit --%>
                        <a href="question-details?id=${question.id}" title="Edit Question"
                           class="action-button edit-btn">
                            Edit
                        </a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty questionDtoList}">
                <tr>
                    <td colspan="8" class="text-center py-4 text-muted">No questions found matching your criteria.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="questionsList?page=${currentPage - 1}&subjectId=${paramSubjectId}&lessonId=${paramLessonId}&dimensionId=${paramDimensionId}&level=${paramLevel}&status=${paramStatus}&content=${paramContent}"
               class="pagination-link">Previous</a>
        </c:if>

        <c:forEach begin="1" end="${endPage}" var="i">
            <a href="questionsList?page=${i}&subjectId=${paramSubjectId}&lessonId=${paramLessonId}&dimensionId=${paramDimensionId}&level=${paramLevel}&status=${paramStatus}&content=${paramContent}"
               class="pagination-link ${currentPage == i ? 'active' : ''}">${i}</a>
        </c:forEach>

        <c:if test="${currentPage < endPage}">
            <a href="questionsList?page=${currentPage + 1}&subjectId=${paramSubjectId}&lessonId=${paramLessonId}&dimensionId=${paramDimensionId}&level=${paramLevel}&status=${paramStatus}&content=${paramContent}"
               class="pagination-link">Next</a>
        </c:if>
    </div>
</div>

<script>
    function toggleQuestionStatus(questionId) {
        if (confirm("Are you sure you want to change the status of this question?")) {
            var form = document.createElement('form');
            form.method = 'POST';
            form.action = 'questionsList';

            // Thêm hidden input cho questionId và action
            var idInput = document.createElement('input');
            idInput.type = 'hidden';
            idInput.name = 'questionId';
            idInput.value = questionId;
            form.appendChild(idInput);

            var actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'toggleStatus';
            form.appendChild(actionInput);

            // Lấy tất cả các tham số từ form lọc/tìm kiếm và thêm vào form ẩn
            var filterForm = document.getElementById('filterForm'); // Lấy form search/filter
            var filterInputs = filterForm.querySelectorAll('input[name], select[name]');

            filterInputs.forEach(function (input) {
                var hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = input.name;
                hiddenInput.value = input.value;
                form.appendChild(hiddenInput);
            });

            // Lấy tham số phân trang hiện tại từ URL (param 'page')
            var urlParams = new URLSearchParams(window.location.search);
            var currentPage = urlParams.get('page');
            if (currentPage) {
                var pageInput = document.createElement('input');
                pageInput.type = 'hidden';
                pageInput.name = 'page';
                pageInput.value = currentPage;
                form.appendChild(pageInput);
            }


            document.body.appendChild(form);
            form.submit();
        }
    }
</script>
<jsp:include page="../../component/footer.html"/>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/PostDetails.js"></script>
<script src="js/Toast.js"></script>
</body>
</html>
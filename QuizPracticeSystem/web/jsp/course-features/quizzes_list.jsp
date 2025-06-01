<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quizzes_list.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>Quiz Management</h1>
            <button class="add-btn" onclick="window.location.href='addquiz'">Add New Quiz</button>
        </header>

        <!-- Search Form -->
        <form method="POST" action="quizzeslist" class="search-form">
            <div class="search-group">
                <label for="subjectId">Subject</label>
                <select id="subjectId" name="subjectId" class="dropdown">
                    <option value="">ALL Subjects</option>
                    <c:forEach var="subject" items="${subjectList}">
                        <option value="${subject.id}" ${subject.id == param.subjectId ? 'selected' : ''}>${subject.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="search-group">
                <label for="type">Quiz Type</label>
                <select id="type" name="type" class="dropdown">
                    <option value="">ALL TYPES</option>
                    <c:forEach var="quizType" items="${quizTypeList}">
                        <option value="${quizType.id}" ${quizType.id == param.type ? 'selected' : ''}>${quizType.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="search-group">
                <label for="title">Search by Title</label>
                <input type="text" id="title" name="title" placeholder="Enter quiz title..." value="${param.title != null ? param.title : ''}" class="search-input extended">
            </div>

            <button type="submit" class="search-btn">Search</button>
        </form>

        <!-- Quiz Table -->
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Subject</th>
                        <th>Description</th>
                        <th>Level</th>
                        <th>Number of Questions</th>
                        <th>Duration (minutes)</th>
                        <th>Pass Rate (%)</th>
                        <th>Quiz Type</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="quiz" items="${quizDtoList}">
                        <tr>
                            <td>${quiz.id}</td>
                            <td>${quiz.title}</td>
                            <td>
                                <c:forEach var="subject" items="${subjectList}">
                                    <c:if test="${subject.id == quiz.subjectId}">${subject.name}</c:if>
                                </c:forEach>
                            </td>
                            <td>${quiz.description}</td>
                            <td>${quiz.level}</td>
                            <td>${quiz.numberOfQuestions}</td>
                            <td>${quiz.duration}</td>
                            <td>${quiz.passRate != 0 ? quiz.passRate : '-'}</td>
                            <td>
                                <c:forEach var="quizType" items="${quizTypeList}">
                                    <c:if test="${quizType.id == quiz.type}">${quizType.name}</c:if>
                                </c:forEach>
                            </td>
                            <td class="actions">
                                <c:if test="${quiz.check}">
                                    <button class="edit-btn">Edit</button>
                                </c:if>
                                <button class="delete-btn" onclick="showDeleteConfirm('${quiz.id}')">Delete</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Pagination -->
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="quizzeslist?page=${currentPage - 1}&subjectId=${param.subjectId}&type=${param.type}&title=${param.title}" class="pagination-link">Previous</a>
            </c:if>

            <c:forEach begin="1" end="${endPage}" var="i">
                <a href="quizzeslist?page=${i}&subjectId=${param.subjectId}&type=${param.type}&title=${param.title}" class="pagination-link ${currentPage == i ? 'active' : ''}">${i}</a>
            </c:forEach>

            <c:if test="${currentPage < endPage}">
                <a href="quizzeslist?page=${currentPage + 1}&subjectId=${param.subjectId}&type=${param.type}&title=${param.title}" class="pagination-link">Next</a>
            </c:if>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div id="deleteConfirmModal" class="modal">
        <div class="modal-content delete-modal">
            <h2>Confirm Delete</h2>
            <p>Are you sure you want to delete this quiz?</p>
            <form method="POST" action="quizzeslist" class="delete-form">
                <input type="hidden" name="quizId" id="quizIdToDelete">
                <button type="button" onclick="hideDeleteConfirm()" class="cancel-btn">Cancel</button>
                <button type="submit" name="action" value="delete" class="delete-btn-modal">Confirm</button>
            </form>
        </div>
    </div>

    <script>
        // Hàm hiển thị modal xác nhận xóa
        function showDeleteConfirm(quizId) {
            document.getElementById('quizIdToDelete').value = quizId;
            document.getElementById('deleteConfirmModal').style.display = 'flex';
        }

        // Hàm ẩn modal xác nhận xóa
        function hideDeleteConfirm() {
            document.getElementById('deleteConfirmModal').style.display = 'none';
        }
    </script>
</body>
</html>
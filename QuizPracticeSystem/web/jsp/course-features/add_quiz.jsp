<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Quiz</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f3f3f3;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 700px;
            margin: 40px auto;
            background-color: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 6px;
        }

        input[type="text"],
        input[type="number"],
        textarea,
        select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
        }

        textarea {
            resize: vertical;
        }

        .form-actions {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: bold;
            text-decoration: none;
        }

        .btn-primary {
            background-color: #007bff;
            color: white;
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-outline-primary {
            display: inline-block;
            margin-right: 8px;
            margin-bottom: 8px;
            padding: 6px 12px;
            border: 1px solid #007bff;
            color: #007bff;
            border-radius: 5px;
            text-decoration: none;
        }

        .btn-outline-primary:hover {
            background-color: #007bff;
            color: white;
        }

        .btn-selected {
            background-color: #007bff !important;
            color: white !important;
            font-weight: bold;
        }

        .error {
            color: red;
            font-size: 13px;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Add New Quiz</h2>
        <form action="addquiz" method="post">
            <!-- SUBJECT SELECTION -->
            <div class="form-group">
                <label>Subject:</label>
                <select name="subject" required>
                    <c:forEach items="${subjectList}" var="s">
                        <option value="${s.id}">${s.name}</option>
                    </c:forEach>
                </select>

            </div>

            <!-- TOPIC DROPDOWN -->
            

            <!-- TITLE -->
            <div class="form-group">
                <label for="title">Title:</label>
                <input type="text" id="title" name="title" value="${title}" required />
                <c:if test="${not empty titleError}">
                    <span class="error">${titleError}</span>
                </c:if>
            </div>

            <!-- DESCRIPTION -->
            <div class="form-group">
                <label for="description">Description:</label>
                <textarea id="description" name="description" rows="4" required>${description}</textarea>
                <c:if test="${not empty descriptionError}">
                    <span class="error">${descriptionError}</span>
                </c:if>
            </div>

            <!-- DURATION -->
            <div class="form-group">
                <label for="duration">Duration (minutes):</label>
                <input type="number" id="duration" name="duration" value="${duration}" required />
                <c:if test="${not empty durationError}">
                    <span class="error">${durationError}</span>
                </c:if>
            </div>

            <!-- PASS RATE -->
            <div class="form-group">
                <label for="passRate">Pass Rate (%):</label>
                <input type="number" id="passRate" name="passRate" value="${passRate}" required />
                <c:if test="${not empty passRateError}">
                    <span class="error">${passRateError}</span>
                </c:if>
            </div>

            <!-- NUMBER OF QUESTIONS -->
            <div class="form-group">
                <label for="numberOfQuestions">Number of Questions:</label>
                <input type="number" id="numberOfQuestions" name="numberOfQuestions" value="${numberOfQuestions}" required />
                <c:if test="${not empty numberOfQuestionsError}">
                    <span class="error">${numberOfQuestionsError}</span>
                </c:if>
            </div>

            <!-- TYPE -->
            <div class="form-group">
                <label for="type">Type:</label>
                <select id="type" name="type" required>
                    <c:forEach var="qt" items="${quizTypeList}">
                        <option value="${qt.id}" <c:if test="${qt.id == type}">selected</c:if>>${qt.name}</option>
                    </c:forEach>
                </select>
                <c:if test="${not empty typeError}">
                    <span class="error">${typeError}</span>
                </c:if>
            </div>

            <!-- LEVEL -->
            <div class="form-group">
                <label for="level">Level:</label>
                <select id="level" name="level" required>
                    <c:forEach var="lvl" items="${levelList}">
                        <option value="${lvl.id}" <c:if test="${lvl.id == level}">selected</c:if>>${lvl.name}</option>
                    </c:forEach>
                </select>
                <c:if test="${not empty levelError}">
                    <span class="error">${levelError}</span>
                </c:if>
            </div>

            <!-- FORM ACTIONS -->
            <div class="form-actions">
                <a href="quizzeslist" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-primary">Add Quiz</button>
            </div>
        </form>
    </div>
</body>
</html>

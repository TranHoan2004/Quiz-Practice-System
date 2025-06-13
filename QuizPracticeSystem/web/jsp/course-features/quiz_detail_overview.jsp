<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quiz Details Overview</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quizdetailoverview.css">
</head>
<body>
    <div class="container">
        <div class="tabs">
            <div class="tab active">Overview</div>
            <div class="tab">
                <a href="setting?quizId=${quiz.id}">Setting</a>
            </div>
        </div>
        
        <form action="overview" method="POST">
            <input type="hidden" name="id" value="${quiz.id}">
            
            <div class="form-group">
                <label>Name</label>
                <input type="text" name="title" value="${param.title != null ? param.title : quiz.title}">
                <c:if test="${not empty errors.title}">
                    <div class="error">${errors.title}</div>
                </c:if>
            </div>
            
            <div class="form-group">
                <label>Subject</label>
                <select name="subjectId">
                    <c:forEach var="subject" items="${subjectList}">
                        <option value="${subject.id}" 
                            ${param.subjectId != null ? (param.subjectId == subject.id ? 'selected' : '') : (quiz.subjectId == subject.id ? 'selected' : '')}>
                            ${subject.name}
                        </option>
                    </c:forEach>
                </select>
                <c:if test="${not empty errors.subjectId}">
                    <div class="error">${errors.subjectId}</div>
                </c:if>
            </div>
            
            <div class="form-group">
                <label>Exam Level</label>
                <select name="level">
                    <c:forEach var="level" items="${quizLevelList}">
                        <option value="${level.id}" 
                            ${param.level != null ? (param.level == level.id ? 'selected' : '') : (quiz.level == level.id ? 'selected' : '')}>
                            ${level.name}
                        </option>
                    </c:forEach>
                </select>
                <c:if test="${not empty errors.level}">
                    <div class="error">${errors.level}</div>
                </c:if>
            </div>
            
            <div class="form-group">
                <label>Duration (minutes)</label>
                <input type="text" name="duration" value="${param.duration != null ? param.duration : quiz.duration}">
                <c:if test="${not empty errors.duration}">
                    <div class="error">${errors.duration}</div>
                </c:if>
            </div>
            
            <div class="form-group">
                <label>Pass Rate (%)</label>
                <input type="text" name="passRate" value="${param.passRate != null ? param.passRate : quiz.passRate}">
                <c:if test="${not empty errors.passRate}">
                    <div class="error">${errors.passRate}</div>
                </c:if>
            </div>
            
            <div class="form-group">
                <label>Quiz Type</label>
                <select name="type">
                    <c:forEach var="type" items="${quizTypeList}">
                        <option value="${type.id}" 
                            ${param.type != null ? (param.type == type.id ? 'selected' : '') : (quiz.type == type.id ? 'selected' : '')}>
                            ${type.name}
                        </option>
                    </c:forEach>
                </select>
                <c:if test="${not empty errors.type}">
                    <div class="error">${errors.type}</div>
                </c:if>
            </div>
            
            <div class="form-group">
                <label>Description</label>
                <textarea name="description">${param.description != null ? param.description : quiz.description}</textarea>
                <c:if test="${not empty errors.description}">
                    <div class="error">${errors.description}</div>
                </c:if>
            </div>
            
            <div class="button-group">
                <button type="submit">Submit</button>
                <button type="button" onclick="window.location.href='quizzeslist'">Back</button>
            </div>
            
            <c:if test="${not empty success}">
                <div class="success">${success}</div>
            </c:if>
        </form>
    </div>
</body>
</html>
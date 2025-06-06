<%-- 
    Document   : loginAccount
    Created on : May 24, 2025, 10:22:28 AM
    Author     : Kim Tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quezee - Login</title>
    <link rel="stylesheet" href="https://unpkg.com/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link href="${pageContext.request.contextPath}/css/lib/login-5.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/fontawesome/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<style>
    .error-message {
        color: red;
        font-size: 14px;
        margin-top: 5px;
        margin-bottom: 10px;
    }
</style>
<body>

<jsp:include page="../../component/header.jsp"/>
<form action="${pageContext.request.contextPath}/user/login" method="POST">
    <div class="background-overlay">
        <div class="login-container">
            <div class="login-box">
                <h1><span class="logo">🎓</span>Quezee</h1>
                <p class="subtitle">Sign in to your account</p>

                <!-- Hiển thị lỗi -->
                <c:if test="${not empty error}">
                    <p class="error-message">${error}</p>
                </c:if>
                <c:if test="${not empty emptyEmail}">
                    <p class="error-message">${emptyEmail}</p>
                </c:if>
                <c:if test="${not empty emptyPassword}">
                    <p class="error-message">${emptyPassword}</p>
                </c:if>

                <!-- Form nội dung -->
                <label for="email">EMAIL</label>
                <input type="email" id="email" name="email" placeholder="hello@fpt.edu.vn">

                <label for="password">PASSWORD</label>
                <input type="password" id="password" name="password" placeholder="******">

                <div class="forgot">
                    <a href="#">Forgot Password?</a>
                </div>

                <button type="submit">LOGIN</button>

                <div class="signup">
                    Don’t have an account? <a href="${pageContext.request.contextPath}/user/register">Sign up</a>
                </div>
            </div>
        </div>
    </div>
</form>

<jsp:include page="../../component/footer.html"/>
</body>
</html>

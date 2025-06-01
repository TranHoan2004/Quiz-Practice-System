<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editProfile.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h1>EDIT PROFILE</h1>
        <a href="javascript:history.back()" class="back-btn">Back</a>
    </div>
    <div class="profile-section">
        <div class="profile-picture">
            <c:set var="accountToEdit"
                   value="${requestScope.account != null ? requestScope.account : sessionScope.currentUser}"/>
            <img src="${pageContext.request.contextPath}/${accountToEdit.imageUrl}" alt="Profile Picture"
                 id="profileImg" class="profile-img">
            <input type="file" id="imageInput" name="imageInput" accept="image/*" onchange="previewImage()"
                   style="display: none;">
            <button onclick="document.getElementById('imageInput').click()" class="upload-btn">Upload new image</button>
            <span class="error" id="imageError">${requestScope.imageError}</span>
        </div>
        <div class="personal-details">
            <h2>PERSONAL DETAIL</h2>
            <form action="${pageContext.request.contextPath}/userProfile" method="POST" enctype="multipart/form-data">
                <table>
                    <tr>
                        <td><label for="fullName">FULL NAME</label></td>
                        <td>
                            <input type="text" id="fullName" name="fullName"
                                   value="<%= request.getParameter("fullName") != null ? request.getParameter("fullName") : (session.getAttribute("currentUser") != null ? ((model.Account) session.getAttribute("currentUser")).getFullName() : "") %>"
                                   required>
                            <span class="error" id="fullNameError">${requestScope.fullNameError}</span>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="gender">GENDER</label></td>
                        <td>
                            <select id="gender" name="gender" required>
                                <option value="Male" <%= "Male".equals(request.getParameter("gender")) || (request.getParameter("gender") == null && session.getAttribute("currentUser") != null && ((model.Account) session.getAttribute("currentUser")).getGender() == 0) ? "selected" : "" %>>
                                    Male
                                </option>
                                <option value="Female" <%= "Female".equals(request.getParameter("gender")) || (request.getParameter("gender") == null && session.getAttribute("currentUser") != null && ((model.Account) session.getAttribute("currentUser")).getGender() == 1) ? "selected" : "" %>>
                                    Female
                                </option>
                                <option value="Other" <%= "Other".equals(request.getParameter("gender")) || (request.getParameter("gender") == null && session.getAttribute("currentUser") != null && ((model.Account) session.getAttribute("currentUser")).getGender() == 2) ? "selected" : "" %>>
                                    Other
                                </option>
                            </select>
                            <span class="error" id="genderError">${requestScope.genderError}</span>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="dob">DATE OF BIRTH</label></td>
                        <td>
                            <input type="date" id="dob" name="dob"
                                   value="<%= request.getParameter("dob") != null ? request.getParameter("dob") : (session.getAttribute("currentUser") != null && ((model.Account) session.getAttribute("currentUser")).getDob() != null ? ((model.Account) session.getAttribute("currentUser")).getDob().toString() : "") %>"
                                   required>
                            <span class="error" id="dobError">${requestScope.dobError}</span>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="phoneNumber">PHONE NUMBER</label></td>
                        <td>
                            <input type="text" id="phoneNumber" name="mobile"
                                   value="<%= request.getParameter("mobile") != null ? request.getParameter("mobile") : (session.getAttribute("currentUser") != null ? ((model.Account) session.getAttribute("currentUser")).getPhoneNumber() : "") %>"
                                   required>
                            <span class="error" id="phoneNumberError">${requestScope.mobileError}</span>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="email">EMAIL</label></td>
                        <td>
                            <input type="email" id="email" name="email"
                                   value="${sessionScope.currentUser.email}" readonly>
                            <span class="error" id="emailError">${requestScope.emailError}</span>
                        </td>
                    </tr>
                </table>
                <button type="submit" class="save-btn">Save changes</button>
                <!-- Loại bỏ hoặc không sử dụng profilePictureInput nếu không cần base64 -->
                <!-- <input type="hidden" name="profilePicture" id="profilePictureInput"> -->
            </form>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/editProfile.js"></script>
</body>
</html>
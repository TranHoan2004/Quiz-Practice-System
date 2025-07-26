<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
    <%-- Giữ nguyên các link CSS của bạn --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editProfile.css">
    <link href="${pageContext.request.contextPath}/img/favicon.ico" rel="icon">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lib/css2.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/homepage.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="../../component/spinner.html"/>
    <jsp:include page="../../component/navbar.jsp"/>

    <%-- BẮT ĐẦU: THÊM THẺ MAIN BAO BỌC --%>
    <main class="edit-profile-main-content">

        <div class="container">
            <div class="header">
                <h1>EDIT PROFILE</h1>
                <a href="javascript:history.back()" class="back-btn">Back</a>
            </div>

            <c:set var="accountToEdit" value="${not empty requestScope.account ? requestScope.account : sessionScope.currentUser}"/>

            <div class="profile-section">
                <div class="profile-picture">
                    <img src="${pageContext.request.contextPath}/${accountToEdit.imageUrl}" alt="Profile Picture" id="profileImg" class="profile-img">
                    <button onclick="document.getElementById('imageInput').click()" class="upload-btn">Upload new image</button>
                    <span class="error" id="imageError">${requestScope.imageError}</span>
                </div>
                
                <div class="personal-details">
                    <h2>PERSONAL DETAIL</h2>
                    <form action="${pageContext.request.contextPath}/userProfile" method="POST" enctype="multipart/form-data">
                        <input type="file" id="imageInput" name="imageInput" accept="image/*" onchange="previewImage()" style="display: none;">
                        
                        <%-- Giữ nguyên table và các input fields của bạn --%>
                        <table>
                            <tr>
                                <td><label for="fullName">FULL NAME</label></td>
                                <td>
                                    <input type="text" id="fullName" name="fullName" value="${not empty param.fullName ? param.fullName : accountToEdit.fullName}" required>
                                    <span class="error" id="fullNameError">${requestScope.fullNameError}</span>
                                </td>
                            </tr>
                            <tr>
                                <td><label for="gender">GENDER</label></td>
                                <td>
                                    <select id="gender" name="gender" required>
                                        <option value="Male"   ${(not empty param.gender and param.gender == 'Male') or (empty param.gender and accountToEdit.gender == 0) ? 'selected' : ''}>Male</option>
                                        <option value="Female" ${(not empty param.gender and param.gender == 'Female') or (empty param.gender and accountToEdit.gender == 1) ? 'selected' : ''}>Female</option>
                                        <option value="Other"  ${(not empty param.gender and param.gender == 'Other') or (empty param.gender and accountToEdit.gender == 2) ? 'selected' : ''}>Other</option>
                                    </select>
                                    <span class="error" id="genderError">${requestScope.genderError}</span>
                                </td>
                            </tr>
                            <tr>
                                <td><label for="dob">DATE OF BIRTH</label></td>
                                <td>
                                    <input type="date" id="dob" name="dob" value="${not empty param.dob ? param.dob : accountToEdit.dob}" required>
                                    <span class="error" id="dobError">${requestScope.dobError}</span>
                                </td>
                            </tr>
                            <tr>
                                <td><label for="phoneNumber">PHONE NUMBER</label></td>
                                <td>
                                    <input type="text" id="phoneNumber" name="mobile" value="${not empty param.mobile ? param.mobile : accountToEdit.phoneNumber}" required>
                                    <span class="error" id="phoneNumberError">${requestScope.mobileError}</span>
                                </td>
                            </tr>
                            <tr>
                                <td><label for="email">EMAIL</label></td>
                                <td>
                                    <input type="email" id="email" name="email" value="${accountToEdit.email}" readonly>
                                    <span class="error" id="emailError">${requestScope.emailError}</span>
                                </td>
                            </tr>
                        </table>
                        <button type="submit" class="save-btn">Save changes</button>
                    </form>
                </div>
            </div>
        </div>

        <%-- Footer bây giờ nằm trong thẻ main --%>
        <jsp:include page="../../component/footer.html"/>

    </main>
    <%-- KẾT THÚC: THẺ MAIN BAO BỌC --%>

    <a href="#" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>

    <%-- Giữ nguyên các link script của bạn --%>
    <script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
    <script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
    <script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
    <script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/HomePage.js"></script>
    <script src="${pageContext.request.contextPath}/js/editProfile.js"></script>
</body>
</html>
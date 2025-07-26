<%-- Document : subjects_list Created on : July 18, 2025, 4:38:34 PM Author : TranHoan --%>
<%@ page import="model.Account" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Course Payment - Quezee</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
    <link href="${pageContext.request.contextPath}/css/lib/css2.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/fontawesome/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/animate/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/payment_qr.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../../component/spinner.html"/>
<jsp:include page="../../component/navbar.jsp"/>

<div class="container py-5">
    <div class="qr-section wow fadeInUp" data-wow-delay="0.1s">
        <c:if test="${pr ne null}">
            <h2 class="text-center mb-4">
                <i class="bi bi-credit-card-2-front me-2"></i>Subject Payment
            </h2>

            <div class="mb-3">
                <span class="info-label">Subject Name:</span>
                <span class="info-value ms-2" id="courseName">${pr.subjectName()}</span>
            </div>
            <div class="mb-3">
                <span class="info-label">Package:</span>
                <span class="info-value ms-2" id="packageName">${pr.packageName()}</span>
            </div>
            <div class="mb-3">
                <span class="info-label">Price:</span>
                <span class="info-value ms-2 text-primary" id="coursePrice">$${pr.price()}</span>
            </div>

            <hr>

            <div class="mb-3">
                <span class="info-label">Registrant:</span>
                <span class="info-value ms-2" id="userName">${pr.registrantName()}</span>
            </div>
            <div class="mb-3">
                <span class="info-label">Email:</span>
                <span class="info-value ms-2" id="userEmail">${pr.email()}</span>
            </div>

            <hr>

            <div class="text-center">
                <img src="${qr}" alt="VietQR" class="qr-img mb-2">
                <div class="qr-guide pt-4">
                    <i class="bi bi-info-circle me-1"></i>
                    Scan the QR code with your banking app to make the payment.<br>
                    <span class="d-block mt-1">Transfer content: <b id="transferContent">QUEZEE-1001</b></span>
                    <span class="d-block mt-1 text-muted qr-note">Note: Please transfer the exact amount and content for automatic confirmation.</span>
                </div>
                <button class="btn btn-primary btn-confirm mt-2 px-4 py-2 w-auto w-sm-100 qr-confirm-btn"
                        id="confirmBtn">
                    <i class="bi bi-check-circle me-2"></i>I have transferred
                </button>
            </div>
        </c:if>
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
    // Demo: Hiển thị thông báo khi nhấn xác nhận
    document.getElementById('confirmBtn').addEventListener('click', async function () {
        this.disabled = true;
        try {
            const res = await fetch(`${pageContext.request.contextPath}/payment`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Source': 'subject_register'
                },
                body: JSON.stringify({status: true})
            });
            if (res.status === 200) {
                this.innerHTML = '<i class="bi bi-check-circle me-2"></i>Đã xác nhận';
                showNotification('Pay successfully');
            }
        } catch (e) {
            console.error(e)
            showNotification('Error during processing', 'not success')
        }
    });
</script>
</body>
</html>
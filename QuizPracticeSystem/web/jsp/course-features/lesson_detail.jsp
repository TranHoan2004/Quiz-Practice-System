<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Lesson List</title>
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
        <link href="${pageContext.request.contextPath}/css/practicelist.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">


    </head>

    <body>
        <jsp:include page="component.subject/header.jsp"/>
        <jsp:include page="component.subject/header.html"/>


        <div class="container py-5">
            <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                <h6 class="section-title bg-white text-center text-primary px-3">Lesson</h6>
                <h1 class="mb-3">
                    ${mode == 'edit' ? 'Edit Lesson' : 'Add New Lesson'}
                </h1>
                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger text-center" role="alert">
                        ${param.error}
                    </div>
                </c:if>
            </div>
            <div class="modal-dialog modal-xl modal-dialog-centered">
                <form id="lessonForm" method="post" action="${pageContext.request.contextPath}/user/subject_lesson/lesson_detail">
                    <input type="hidden" name="lessonId" id="lessonId" value="${lesson.lessonId}"/>
                    <input type="hidden" name="courseId" id="modalCourseId" value="${courseId}"/>


                    <div class="modal-content">

                        <div class="modal-body">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Name</label>
                                    <input type="text" class="form-control" id="lessonTitle" name="title" value="${lesson.name}" required>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Type</label>
                                    <select class="form-select" id="lessonType" name="type">
                                        <option value="Subject Topic" ${lesson.type == 'Subject Topic' ? 'selected' : ''}>Subject Topic</option>
                                        <option value="Video" ${lesson.type == 'Video' ? 'selected' : ''}>Video</option>
                                        <option value="Text" ${lesson.type == 'Text' ? 'selected' : ''}>Text</option>
                                        <option value="Quiz" ${lesson.type == 'Quiz' ? 'selected' : ''}>Quiz</option>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Topic</label>
                                    <select class="form-select" id="lessonTopic" name="topicId">
                                        <c:forEach var="topic" items="${topics}">
                                            <option value="${topic.id}" ${lesson.topicId == topic.id ? 'selected' : ''}>${topic.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Order</label>
                                    <input type="number" class="form-control" id="lessonOrder" name="order" min="1" value="${lesson.order}" readonly>
                                </div>

                                <div class="col-12">
                                    <label class="form-label">Video Link</label>
                                    <input type="text" class="form-control" id="lessonVideo" name="videoLink" placeholder="YouTube URL" value="${lesson.videoLink}">
                                </div>

                                <div class="col-12">
                                    <label class="form-label">HTML Content</label>
                                    <textarea class="form-control" id="lessonContent" name="htmlContent" rows="6">${lesson.htmlContent}</textarea>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer border-0">
                            <a href="${pageContext.request.contextPath}/user/subject_lesson?id=${courseId}" class="btn btn-outline-secondary">Back</a>

                            <button type="submit" class="btn btn-primary">
                                ${mode == 'edit' ? 'Update Lesson' : 'Add Lesson'}
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="../../component/footer.html"/>
        <jsp:include page="../../component/back_to_top.html"/>
        <jsp:include page="../../component/notification.html"/>

        <script src="${pageContext.request.contextPath}/js/lib/jquery-3.4.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/lib/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/wow/wow.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/easing/easing.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/waypoints/waypoints.min.js"></script>
        <script src="${pageContext.request.contextPath}/lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
        <script src="${pageContext.request.contextPath}/js/Notification.js"></script>
        <script>


        </script>
    </body>

</html>
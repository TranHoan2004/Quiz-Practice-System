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

        <style>
            .centered-form {
                max-width: 800px;
                margin: 0 auto;
            }
        </style>

    </head>

    <body>
        <jsp:include page="component.subject/header.jsp"/>
        <jsp:include page="component.subject/header.html"/>


        <div class="container py-5">
            <div class="text-center wow fadeInUp mb-3" data-wow-delay="0.1s">
                <h6 class="section-title bg-white text-center text-primary px-3">Lesson</h6>
                <h1 class="mb-3">Add New Subject</h1>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger text-center" role="alert">
                        ${param.error}
                    </div>
                </c:if>
            </div>
            <div class="modal-dialog modal-xl modal-dialog-centered mx-auto">
                <form class="centered-form" method="post" action="${pageContext.request.contextPath}/user/new_subject">

                    <div class="modal-content">
                        <div class="modal-body">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Name</label>
                                    <input type="text" class="form-control" name="name" placeholder="Enter Subject Name..." required>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Thumbnail URL</label>
                                    <input type="text" class="form-control" name="thumbnailURL" placeholder="Enter Thumbnail URL..." required>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Topic</label>
                                    <select class="form-select" name="topic_id">
                                        <c:forEach var="topic" items="${topicList}">
                                            <option value="${topic.id}"> ${topic.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Owner</label>
                                    <select class="form-select" name="expert_id">
                                        <c:forEach var="expert" items="${expertList}">
                                            <option value="${expert.id}">${expert.fullName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Contact</label>
                                    <select class="form-select" name="contact_id">
                                        <c:forEach var="contact" items="${contactList}">
                                            <option value="${contact.id}">${contact.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Status</label>
                                    <select class="form-select" name="status">
                                        <option value="true">Published</option>
                                        <option value="false">Unpublished</option>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Featured</label><br/>
                                    <input class="form-check-input" type="checkbox" name="featureFlag" value="true">
                                    <label class="form-check-label">Mark as Featured</label>
                                </div>

                                <div class="col-md-12">
                                    <label class="form-label">Description</label>
                                    <input type="text" class="form-control" name="description" placeholder="Enter description..." required>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer border-0">
                            <a href="${pageContext.request.contextPath}/user/subject_list" class="btn btn-outline-secondary">Back</a>
                            <button type="submit" class="btn btn-primary">Add Subject</button>
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
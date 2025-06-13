<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Registration Management</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/resgistration_list.css">
        <style>
            .date-picker {
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 4px;
                width: 100%;
            }
            .error {
                color: red;
                font-size: 0.9em;
            }
        </style>
        <script>
            function toggleAllCheckboxes() {
                var allCheckbox = document.querySelector('input[value="All"]');
                var checkboxes = document.querySelectorAll('input[name="columns"]:not([value="All"])');
                checkboxes.forEach(function (cb) {
                    cb.checked = allCheckbox.checked;
                });
            }

            function updateAllCheckbox() {
                var allCheckbox = document.querySelector('input[value="All"]');
                var checkboxes = document.querySelectorAll('input[name="columns"]:not([value="All"])');
                var allChecked = Array.from(checkboxes).every(cb => cb.checked);
                allCheckbox.checked = allChecked;
            }
        </script>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h2>Registration Management</h2>
                <a href="#" class="add-new">Add New Registration</a>
            </div>
            <form action="${pageContext.request.contextPath}/resgistrationList" method="get">
                <div class="filter-row">
                    <div class="filter-group">
                        <label>SELECT SUBJECTS</label>
                        <select name="subjectId">
                            <option value="">All Subjects</option>
                            <c:forEach var="subject" items="${subjectList}">
                                <option value="${subject.id}" ${subject.id == param.subjectId ? 'selected' : ''}>${subject.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="filter-group">
                        <label>VALID DATE RANGE</label>
                        <input type="date" name="validFrom" value="${param.validFrom}" class="date-picker">
                        <input type="date" name="validTo" value="${param.validTo}" class="date-picker">
                    </div>
                    <div class="filter-group">
                        <label>SELECT STATUS</label>
                        <select name="status">
                            <option value="">Status</option>
                            <c:forEach var="st" items="${statusList}">
                                <option value="${st.status}" ${st.status == param.status ? 'selected' : ''}>${st.status}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="filter-group">
                        <label>EMAIL</label>
                        <input type="text" name="email" value="${param.email}" placeholder="Search by Email">
                    </div>
                    <div class="filter-group">
                        <label>REGISTRATIONS PER PAGE</label>
                        <input type="text" name="numberOfLine" value="${param.numberOfLine}" placeholder="Number of registrations">
                    </div>
                </div>
                <c:if test="${not empty validateError}"><div style="text-align: center; margin-bottom: 10px;"><span class="error">${validateError}</span></div></c:if>
                    <div class="display-fields">
                        <label>DISPLAY FIELDS</label>
                        <input type="checkbox" name="columns" value="All" onchange="toggleAllCheckboxes()" <c:if test="${fn:contains(fn:join(selectedColumns, ','), 'All')}">checked</c:if>> All
                    <input type="checkbox" name="columns" value="ID" onchange="updateAllCheckbox()" <c:if test="${fn:contains(fn:join(selectedColumns, ','), 'ID')}">checked</c:if>> ID
                    <input type="checkbox" name="columns" value="Email" onchange="updateAllCheckbox()" <c:if test="${fn:contains(fn:join(selectedColumns, ','), 'Email')}">checked</c:if>> Email
                    <input type="checkbox" name="columns" value="Registration time" onchange="updateAllCheckbox()" <c:if test="${fn:contains(fn:join(selectedColumns, ','), 'Registration time')}">checked</c:if>> Registration time
                    <input type="checkbox" name="columns" value="Subject" onchange="updateAllCheckbox()" <c:if test="${fn:contains(fn:join(selectedColumns, ','), 'Subject')}">checked</c:if>> Subject
                    <input type="checkbox" name="columns" value="Package" onchange="updateAllCheckbox()" <c:if test="${fn:contains(fn:join(selectedColumns, ','), 'Package')}">checked</c:if>> Package
                    <input type="checkbox" name="columns" value="Total cost" onchange="updateAllCheckbox()" <c:if test="${fn:contains(fn:join(selectedColumns, ','), 'Total cost')}">checked</c:if>> Total cost
                    <input type="checkbox" name="columns" value="Status" onchange="updateAllCheckbox()" <c:if test="${fn:contains(fn:join(selectedColumns, ','), 'Status')}">checked</c:if>> Status
                    <input type="checkbox" name="columns" value="Valid from" onchange="updateAllCheckbox()" <c:if test="${fn:contains(fn:join(selectedColumns, ','), 'Valid from')}">checked</c:if>> Valid from
                    <input type="checkbox" name="columns" value="Valid to" onchange="updateAllCheckbox()" <c:if test="${fn:contains(fn:join(selectedColumns, ','), 'Valid to')}">checked</c:if>> Valid to
                    </div>
                    <button type="submit">Submit</button>
                </form>
                <table>
                    <thead>
                    <c:set var="visibility" value="${columnVisibility}"/>
                    <tr>
                        <c:if test="${visibility[0]}"><th>ID</th></c:if>
                        <c:if test="${visibility[1]}"><th>Email</th></c:if>
                        <c:if test="${visibility[2]}"><th>Registration time</th></c:if>
                        <c:if test="${visibility[3]}"><th>Subject</th></c:if>
                        <c:if test="${visibility[4]}"><th>Package</th></c:if>
                        <c:if test="${visibility[5]}"><th>Total cost</th></c:if>
                        <c:if test="${visibility[6]}"><th>Status</th></c:if>
                        <c:if test="${visibility[7]}"><th class="col-date">Valid from</th></c:if>
                        <c:if test="${visibility[8]}"><th class="col-date">Valid to</th></c:if>
                            <th>Edit</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="reg" items="${RegistrationDtoList}">
                        <tr>
                            <c:if test="${visibility[0]}"><td>${reg.id}</td></c:if>
                            <c:if test="${visibility[1]}"><td>${reg.email}</td></c:if>
                            <c:if test="${visibility[2]}"><td>${reg.registrationTime}</td></c:if>
                            <c:if test="${visibility[3]}"><td>${reg.subject}</td></c:if>
                            <c:if test="${visibility[4]}"><td>${reg.packageName}</td></c:if>
                            <c:if test="${visibility[5]}"><td>${reg.totalCost}</td></c:if>
                            <c:if test="${visibility[6]}"><td>${reg.status}</td></c:if>
                            <c:if test="${visibility[7]}"><td class="col-date">${reg.validFrom}</td></c:if>
                            <c:if test="${visibility[8]}"><td class="col-date">${reg.validTo}</td></c:if>
                                <td><a href="#" class="edit-btn">Edit</a></td>
                            </tr>
                    </c:forEach>
                </tbody>
            </table>
            <c:if test="${not empty RegistrationDtoList}">
                <div class="pagination">
                    <%-- ================= LOGIC ĐÃ SỬA ================= --%>
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/resgistrationList?page=${currentPage - 1}&subjectId=${param.subjectId}&email=${param.email}&status=${param.status}&validFrom=${param.validFrom}&validTo=${param.validTo}&numberOfLine=${param.numberOfLine}<c:forEach var='col' items='${selectedColumns}'>&columns=${col}</c:forEach>">Previous</a>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${endPage}">
                        <a href="${pageContext.request.contextPath}/resgistrationList?page=${i}&subjectId=${param.subjectId}&email=${param.email}&status=${param.status}&validFrom=${param.validFrom}&validTo=${param.validTo}&numberOfLine=${param.numberOfLine}<c:forEach var='col' items='${selectedColumns}'>&columns=${col}</c:forEach>" ${i == currentPage ? 'class="active"' : ''}>${i}</a>
                    </c:forEach>
                    <c:if test="${currentPage < endPage}">
                        <a href="${pageContext.request.contextPath}/resgistrationList?page=${currentPage + 1}&subjectId=${param.subjectId}&email=${param.email}&status=${param.status}&validFrom=${param.validFrom}&validTo=${param.validTo}&numberOfLine=${param.numberOfLine}<c:forEach var='col' items='${selectedColumns}'>&columns=${col}</c:forEach>">Next</a>
                    </c:if>
                    <%-- ================= KẾT THÚC SỬA ================= --%>
                </div>
            </c:if>
        </div>
    </body>
</html>
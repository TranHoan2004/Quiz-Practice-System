<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quiz Settings</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quizdetailsetting.css">
</head>
<body>
    <div class="setting-container">
        <%-- === THANH TAB MỚI === --%>
        <div class="tab-nav">
            <a href="#" class="tab-link" onclick="goToOverview(); return false;">Overview</a>
            <span class="tab-link active">Setting</span>
        </div>

        <%-- Phần hiển thị message giữ nguyên --%>
        <c:if test="${not empty message}">
            <div class="message ${message.startsWith('Lỗi') ? 'error' : 'success'}">${message}</div>
        </c:if>

        <form action="setting" method="post" id="settingForm">
            <%-- Các thẻ input ẩn giữ nguyên --%>
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="idToDelete">

            <%-- Toàn bộ nội dung form (tổng số câu hỏi, loại câu hỏi, phân bổ...) giữ nguyên như cũ --%>
            <div class="form-group">
                <label for="totalQuestions">Tổng số câu hỏi</label>
                <input type="number" id="totalQuestions" name="totalQuestions" value="${quiz.numberOfQuestions}" required>
            </div>
            
            <div class="form-group">
                <label>Loại câu hỏi</label>
                <div class="source-type-selector">
                    <button type="button" class="btn ${sourceItem == 'topic' ? 'active' : ''}" onclick="changeSource('topic')">Theo topic</button>
                    <button type="button" class="btn ${sourceItem == 'group' ? 'active' : ''}" onclick="changeSource('group')">Theo group</button>
                    <button type="button" class="btn ${sourceItem == 'domain' ? 'active' : ''}" onclick="changeSource('domain')">Theo domain</button>
                </div>
            </div>

            <div class="form-group">
                <label>Phân bổ số lượng câu hỏi</label>
                <%-- Vòng lặp và khu vực thêm mới giữ nguyên --%>
                <c:forEach var="config" items="${QuizQuestionSourceConfigList}">
                    <c:if test="${config.sourceType eq sourceItem}">
                        <div class="source-item" id="item-${config.id}">
                            <input type="hidden" name="configId[]" value="${config.id}">
                            <select name="sourceId[]">
                                <c:forEach var="item" items="${sourceItemList}">
                                    <c:if test="${item.sourceType eq sourceItem}">
                                        <option value="${item.id}" ${item.id eq config.sourceId ? 'selected' : ''}>${item.value}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                            <input type="number" name="numberOfQuestions[]" value="${config.numberOfQuestions}" min="0">
                            <button type="button" class="btn btn-delete" onclick="deleteConfig('${config.id}')">Xóa</button>
                        </div>
                    </c:if>
                </c:forEach>
                <div class="source-item" id="add-new-item">
                    <select name="newSourceId">
                        <option value="">-- Chọn để thêm mới --</option>
                        <c:forEach var="item" items="${sourceItemList}"><c:if test="${item.sourceType eq sourceItem}"><option value="${item.id}">${item.value}</option></c:if></c:forEach>
                    </select>
                    <input type="number" name="newNumberOfQuestions" value="0" min="0">
                    <button type="button" class="btn btn-add" onclick="addNewConfig()">Thêm</button>
                </div>
            </div>

            <%-- === FORM ACTIONS ĐÃ BỎ NÚT OVERVIEW === --%>
            <div class="form-actions">
                <button type="button" class="btn btn-save" onclick="saveSettings()">Save</button>
                <button type="button" class="btn btn-secondary" onclick="goBack()">Back</button>
            </div>
        </form>
    </div>

    <%-- === SCRIPT ĐÃ THÊM HÀM goToOverview() === --%>
    <script>
        const form = document.getElementById('settingForm');

        function changeSource(source) {
            const sourceInput = document.createElement('input');
            sourceInput.type = 'hidden';
            sourceInput.name = 'sourceItem';
            sourceInput.value = source;
            form.appendChild(sourceInput);
            form.action.value = 'changeSource';
            form.submit();
        }

        function deleteConfig(id) {
            if (confirm('Bạn có chắc chắn muốn xóa?')) {
                form.action.value = 'delete';
                form.idToDelete.value = id;
                form.submit();
            }
        }

        function addNewConfig() {
            form.action.value = 'add';
            form.submit();
        }

        function saveSettings() {
            form.action.value = 'save';
            form.submit();
        }
        
        function goBack() {
            form.action.value = 'back';
            form.submit();
        }

        function goToOverview() {
            form.action.value = 'overview';
            form.submit();
        }
    </script>
</body>
</html>
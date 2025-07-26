<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Question</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/question-details.css">
        <link href="${pageContext.request.contextPath}/css/notification.css" rel="stylesheet">
    </head>
    <body class="container my-5">
        <div class="card shadow-lg">
            <div class="card-header bg-primary text-white text-center py-3">
                <h2 class="mb-0">Edit question</h2>
            </div>
            <div class="card-body p-4">
                <form id="questionForm" action="question-details" method="POST" enctype="multipart/form-data">
                    <%-- Hidden input to store question ID for updates --%>
                    <input type="hidden" name="questionId" id="questionId" value="${question.id}">
                    <%-- Hidden input to store deleted option IDs --%>
                    <input type="hidden" name="deletedOptionIds" id="deletedOptionIds" value="">

                    <div class="row mb-4">
                        <div class="col-md-6">
                            <label for="subjectSelect" class="form-label fw-bold">SUBJECT</label>
                            <select class="form-select" id="subjectSelect" name="subjectId">
                                <c:forEach var="subject" items="${requestScope.allSubjects}">
                                    <option value="${subject.id}" ${requestScope.question.subjectId == subject.id ? 'selected' : ''}>
                                        ${subject.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label for="lessonSelect" class="form-label fw-bold">LESSON</label>
                            <select class="form-select" id="lessonSelect" name="lessonId">
                                <c:forEach var="lesson" items="${requestScope.lessonsList}">
                                    <option value="${lesson.id}" ${requestScope.question.lessonId == lesson.id ? 'selected' : ''}>
                                        ${lesson.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="row mb-4">
                        <div class="col-md-6">
                            <label for="dimensionSelect" class="form-label fw-bold">DIMENSION(S)</label>
                            <select class="form-select" id="dimensionSelect" name="dimensionId">
                                <c:forEach var="dimension" items="${dimensions}">
                                    <option value="${dimension.id}" ${(not empty requestScope.dimensionCurent and requestScope.dimensionCurent.id == dimension.id) ? 'selected' : ''}>
                                        ${dimension.value}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label for="statusSelect" class="form-label fw-bold">STATUS</label>
                            <select class="form-select" id="statusSelect" name="status">
                                <option value="true" ${requestScope.question.status ? 'selected' : ''}>Active</option>
                                <option value="false" ${!requestScope.question.status ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="questionContent" class="form-label fw-bold">Question Content</label>
                        <textarea class="form-control" id="questionContent" name="questionContent" rows="5"
                                  placeholder="Enter the question content...">${question.content}</textarea>
                    </div>

                    <div class="mb-4">
                        <label for="explanation" class="form-label fw-bold">Explanation (for Question)</label>
                        <textarea class="form-control" id="explanation" name="explanation" rows="5"
                                  placeholder="Enter the explanation for the question...">${requestScope.question.explanation}</textarea>
                    </div>

                    <h4 class="mb-3">Media Upload</h4>
                    <div id="mediaUploadContainer">
                        <c:forEach var="media" items="${requestScope.questionMediaList}" varStatus="loop">
                            <div class="media-item mb-3 p-3 border rounded shadow-sm">
                                <div class="flex-grow-1">
                                    <label class="form-label">Media ${loop.index + 1}</label>
                                    <input type="text" class="form-control mb-2" name="mediaUrl_${media.id}"
                                           value="${pageContext.request.contextPath}/${media.filePath}" placeholder="Media URL">
                                    <textarea class="form-control" rows="2" placeholder="Enter the note/caption..."
                                              name="mediaCaption_${media.id}">${media.caption}</textarea>
                                </div>
                                <div class="d-flex flex-column gap-2">
                                    <a href="${pageContext.request.contextPath}/${media.filePath}" target="_blank" class="btn btn-outline-info btn-sm"><i
                                            class="fas fa-eye"></i> Preview</a>
                                    <button type="button" class="btn btn-outline-danger btn-sm delete-media-btn"
                                            data-media-id="${media.id}"><i class="fas fa-trash"></i> Delete
                                    </button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <button type="button" class="btn btn-outline-primary mb-4" id="addMediaBtn"><i class="fas fa-plus"></i> Add
                        Media
                    </button>

                    <h4 class="mb-3">Options</h4>
                    <div id="optionsContainer">
                        <c:choose>
                            <c:when test="${not empty requestScope.options}">
                                <c:forEach var="option" items="${requestScope.options}" varStatus="loop">
                                    <div class="option-item mb-3 p-3 border rounded shadow-sm">
                                        <div class="form-check me-3">
                                            <%-- Đổi type thành "checkbox" và điều chỉnh thuộc tính name --%>
                                            <input class="form-check-input" type="checkbox" name="optionIsTrue_${option.id}"
                                                   id="option${option.id}isTrue"
                                                   value="true" ${option.isTrue ? 'checked' : ''}>
                                            <label class="form-check-label" for="option${option.id}isTrue">
                                                Correct
                                            </label>
                                        </div>
                                        <div class="option-content-group">
                                            <input type="text" class="form-control mb-2" placeholder="Option Content"
                                                   value="${option.content}" name="optionContent_${option.id}">
                                            <textarea class="form-control" rows="2" placeholder="Option Explanation"
                                                      name="optionExplanation_${option.id}">${option.explanation}</textarea>
                                        </div>
                                        <button type="button" class="btn btn-outline-danger btn-sm delete-option-btn"
                                                data-option-id="${option.id}"><i class="fas fa-trash"></i> Delete
                                        </button>
                                    </div>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </div>
                    <button type="button" class="btn btn-outline-primary mb-4" id="addOptionBtn"><i class="fas fa-plus"></i> Add
                        Option
                    </button>

                    <div class="d-flex justify-content-end gap-2">
                        <button type="submit" class="btn btn-success btn-lg">
                            <i class="fas fa-save"></i> Save
                        </button>
                        <a href="questionsList" class="btn btn-secondary btn-lg" role="button">
                            <i class="fas fa-times"></i> Cancel
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="../../component/notification.html"/>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/Notification.js"></script>

        <script>
            // Hàm tạo UUID ngẫu nhiên
            function generateUUID() {
                return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                    return v.toString(16);
                });
            }

            document.addEventListener('DOMContentLoaded', function () {

                // Logic for "Add Media" button
                document.getElementById('addMediaBtn').addEventListener('click', function () {
                    const container = document.getElementById('mediaUploadContainer');
                    const mediaCount = container.children.length + 1;
                    const newMediaItem = `
                                <div class="media-item mb-3 p-3 border rounded shadow-sm">
                                    <div class="flex-grow-1">
                                        <label class="form-label">Media ${mediaCount}</label>
                                        <input type="file" class="form-control mb-2" name="newMediaFile_${mediaCount}">
                                        <textarea class="form-control" rows="2" placeholder="Enter the note/caption..." name="newMediaCaption_${mediaCount}"></textarea>
                                    </div>
                                    <div class="d-flex flex-column gap-2">
                                        <button type="button" class="btn btn-outline-info btn-sm preview-media-btn"><i class="fas fa-eye"></i> Preview</button>
                                        <button type="button" class="btn btn-outline-danger btn-sm delete-media-btn"><i class="fas fa-trash"></i> Delete</button>
                                    </div>
                                </div>
                            `;
                    container.insertAdjacentHTML('beforeend', newMediaItem);
                    container.lastElementChild.querySelector('.delete-media-btn').addEventListener('click', function () {
                        this.closest('.media-item').remove();
                    });
                    container.lastElementChild.querySelector('.preview-media-btn').addEventListener('click', function () {
                        const fileInput = this.closest('.media-item').querySelector('input[type="file"]');
                        if (fileInput.files.length > 0) {
                            const file = fileInput.files[0];
                            const url = URL.createObjectURL(file);
                            window.open(url, '_blank');
                        } else {
                            alert('No file selected for preview.');
                        }
                    });
                });

                // Logic for "Add Option" button
                document.getElementById('addOptionBtn').addEventListener('click', function () {
                    const container = document.getElementById('optionsContainer');
                    const newOptionId = generateUUID(); // Tạo UUID thật sự

                    const newOptionItem = `
    <div class="option-item ...">
        <div class="form-check me-3">
            <%-- Đổi type thành "checkbox" và dùng name riêng cho đáp án MỚI --%>
            <input class="form-check-input" type="checkbox" name="newOptionIsCorrect_${newOptionId}" id="option${newOptionId}isTrue" value="true">
            <label class="form-check-label" for="option${newOptionId}isTrue">
                Correct
            </label>
        </div>
                                    <div class="option-content-group">
                                        <input type="text" class="form-control mb-2" placeholder="Option Content" name="newOptionContent_${newOptionId}">
                                        <textarea class="form-control" rows="2" placeholder="Option Explanation" name="newOptionExplanation_${newOptionId}"></textarea>
                                    </div>
                                    <button type="button" class="btn btn-outline-danger btn-sm delete-option-btn"><i class="fas fa-trash"></i> Delete</button>
                                </div>
                            `;
                    container.insertAdjacentHTML('beforeend', newOptionItem);

                    // Attach event listener to the new delete button for new options
                    container.lastElementChild.querySelector('.delete-option-btn').addEventListener('click', function () {
                        this.closest('.option-item').remove();
                    });
                });

                // Attach delete event listeners for pre-existing media and options on page load
                document.querySelectorAll('.delete-media-btn').forEach(button => {
                    button.addEventListener('click', function () {
                        // For existing media, consider adding to a hidden input for deletion on submit
                        const mediaIdToDelete = this.dataset.mediaId;
                        if (mediaIdToDelete) {
                            // Add logic to store deleted media IDs if you implement server-side deletion
                        }
                        this.closest('.media-item').remove();
                    });
                });
                document.querySelectorAll('.delete-option-btn').forEach(button => {
                    button.addEventListener('click', function () {
                        const optionIdToDelete = this.dataset.optionId; // Get ID for existing options
                        if (optionIdToDelete) {
                            const deletedIdsInput = document.getElementById('deletedOptionIds');
                            let currentDeletedIds = deletedIdsInput.value ? deletedIdsInput.value.split(',') : [];
                            if (!currentDeletedIds.includes(optionIdToDelete)) {
                                currentDeletedIds.push(optionIdToDelete);
                                deletedIdsInput.value = currentDeletedIds.join(',');
                            }
                        }
                        this.closest('.option-item').remove();
                    });
                });
                document.querySelectorAll('.preview-media-btn').forEach(button => {
                    button.addEventListener('click', function () {
                        // This handles preview for existing media (URL based)
                        const mediaLinkInput = this.closest('.media-item').querySelector('input[name^="mediaUrl_"]');
                        if (mediaLinkInput && mediaLinkInput.value) {
                            window.open(mediaLinkInput.value, '_blank');
                        } else {
                            alert('No media URL available for preview.');
                        }
                    });
                });

                // Display success message if available in URL parameters
                const urlParams = new URLSearchParams(window.location.search);
                if (urlParams.get('success') === 'true') {
                    alert('Cập nhật câu hỏi thành công!');
                }
            });


        </script>

    </body>
</html>
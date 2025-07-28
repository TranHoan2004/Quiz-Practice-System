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
    <!-- Miss Assistant Floating Chat -->
    <style>
        #miss-assistant-button {
            position: fixed;
            bottom: 2rem;
            left: 2rem;
            background-color: #6610f2;
            color: white;
            border: none;
            border-radius: 50%;
            width: 60px;
            height: 60px;
            font-size: 24px;
            z-index: 10000;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
            cursor: pointer;
        }

        #miss-assistant-panel.active {
            display: flex;
        }

        #miss-assistant-panel {
            position: fixed;
            bottom: 90px;
            left: 2rem;
            width: 360px;
            max-height: 500px;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
            z-index: 10000;
            display: none;
            flex-direction: column;
            overflow: hidden;
        }

        #miss-assistant-header {
            background-color: #6610f2;
            color: white;
            padding: 0.75rem 1rem;
            font-weight: bold;
            font-size: 16px;
        }

        #miss-assistant-body {
            padding: 1rem;
            flex: 1;
            overflow-y: auto;
            font-size: 14px;
        }

        #miss-assistant-input {
            display: flex;
            border-top: 1px solid #eee;
        }

        #miss-assistant-input textarea {
            flex: 1;
            border: none;
            resize: none;
            padding: 0.75rem;
            font-size: 14px;
        }

        #miss-assistant-input button {
            background-color: #6610f2;
            color: white;
            border: none;
            padding: 0 1rem;
            cursor: pointer;
        }
    </style>


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
                <form id="lessonForm" method="post" action="${pageContext.request.contextPath}/lesson-detail">
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
                            <a href="${pageContext.request.contextPath}/subject-lesson?id=${courseId}" class="btn btn-outline-secondary">Back</a>

                            <button type="submit" class="btn btn-primary">
                                ${mode == 'edit' ? 'Update Lesson' : 'Add Lesson'}
                            </button>
                        </div>
                    </div>
                    <!-- Floating button -->
                </form>


            </div>
            <!-- ✅ Các nút gợi ý -->
            <div id="lesson-suggestions" style="padding: 0.5rem 1rem; display: flex; flex-wrap: wrap; gap: 0.5rem;">
                <button onclick="sendLessonPrompt('summary')" class="btn btn-sm btn-outline-secondary">📝 Tóm tắt</button>
                <button onclick="sendLessonPrompt('explainConcepts')" class="btn btn-sm btn-outline-secondary">📘 Giải thích</button>
                <button onclick="sendLessonPrompt('exampleHelp')" class="btn btn-sm btn-outline-secondary">🔍 Giải ví dụ</button>
                <button onclick="sendLessonPrompt('quizMe')" class="btn btn-sm btn-outline-secondary">❓ Câu hỏi</button>
                <button onclick="sendLessonPrompt('misunderstanding')" class="btn btn-sm btn-outline-secondary">⚠️ Hiểu nhầm</button>
            </div>

            <!-- ✅ Giao diện Miss Assistant -->
            <button id="miss-assistant-button" type="button" title="Ask Miss Assistant">
                <i class="fas fa-robot"></i>
            </button>
            <div id="miss-assistant-panel">
                <div id="miss-assistant-header">Miss Assistant</div>
                <div id="miss-assistant-body">
                    <div><i>Ask me about this lesson content!</i></div>
                </div>
                <div id="miss-assistant-input">
                    <textarea id="miss-assistant-text" rows="2" placeholder="Type your question..."></textarea>
                    <button id="miss-assistant-send" type="button"><i class="fas fa-paper-plane"></i></button>
                </div>
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
        const lessonPrompts = {
        summary: "📝 Hãy tóm tắt bài học này trong 5–7 dòng.",
        explainConcepts: "📘 Giải thích các khái niệm chính trong bài học.",
        exampleHelp: "🔍 Giải thích ví dụ khó trong bài học.",
        quizMe: "❓ Hãy đặt 3 câu hỏi trắc nghiệm dựa trên bài học này.",
        misunderstanding: "⚠️ Nêu các hiểu nhầm thường gặp liên quan đến bài học này."
    };

    document.addEventListener('DOMContentLoaded', function () {
        const toggleBtn = document.getElementById('miss-assistant-button');
        const panel = document.getElementById('miss-assistant-panel');
        const sendBtn = document.getElementById('miss-assistant-send');
        const textInput = document.getElementById('miss-assistant-text');
        const chatBody = document.getElementById('miss-assistant-body');

        toggleBtn.addEventListener('click', () => {
            panel.classList.toggle('active');
        });

        sendBtn.addEventListener('click', sendPrompt);
        textInput.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendPrompt();
            }
        });

        function appendMessage(sender, message) {
            if (!message || message.trim() === '') return;
            const div = document.createElement('div');
            div.style.margin = '0.5rem 0';
            div.innerHTML = "<b>" + sender + ":</b> " + escapeHTML(message).replace(/\n/g, '<br>');
            chatBody.appendChild(div);
            chatBody.scrollTop = chatBody.scrollHeight;
        }

        function escapeHTML(str) {
            return str.replace(/[&<>"']/g, function (m) {
                return {
                    '&': '&amp;',
                    '<': '&lt;',
                    '>': '&gt;',
                    '"': '&quot;',
                    "'": '&#039;'
                }[m];
            });
        }

        window.sendLessonPrompt = function (type) {
            const prompt = lessonPrompts[type];
            if (!prompt) return;
            textInput.value = prompt;
            sendPrompt();
        };

        async function sendPrompt() {
            const prompt = textInput.value.trim();
            if (!prompt) {
                alert("Please enter a question for Miss.");
                return;
            }

            appendMessage("You", prompt);
            textInput.value = '';

            const context = document.getElementById('lessonContent')?.value || '';
            appendMessage("Miss Assistant", "Thinking...");

            const fullPrompt = `
You are Miss, a friendly teaching assistant.
Please answer the question using the lesson content below.

Lesson Content:
${context}

Question:
${prompt}
            `;

            try {
                const response = await fetch('<%= request.getContextPath() %>/ask', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({
                        prompt: fullPrompt,
                        insightContext: '',
                        target: 'lesson'
                    })
                });

                if (!response.ok)
                    throw new Error("Failed to contact Miss Assistant.");

                const reader = response.body.getReader();
                const decoder = new TextDecoder("utf-8");
                let fullText = '';
                let partialDiv = document.createElement('div');
                partialDiv.innerHTML = `<b>Miss Assistant:</b> <em>Thinking...</em>`;
                chatBody.appendChild(partialDiv);
                let thinkingRemoved = false;

                while (true) {
                    const {done, value} = await reader.read();
                    if (done) break;

                    const chunkText = decoder.decode(value, {stream: true}).trim();
                    const lines = chunkText.split('\n');
                    for (const line of lines) {
                        if (!line.trim()) continue;
                        try {
                            const json = JSON.parse(line);
                            if (typeof json.response === 'string') {
                                fullText += json.response;
                                const cleanText = fullText.replace(/\*\*(.*?)\*\*/g, '$1');

                                if (!thinkingRemoved) {
                                    partialDiv.innerHTML = `<b>Miss Assistant:</b> `;
                                    thinkingRemoved = true;
                                }

                                partialDiv.innerHTML = `<b>Miss Assistant:</b> ` + escapeHTML(cleanText).replace(/\n/g, '<br>');
                                chatBody.scrollTop = chatBody.scrollHeight;
                            }
                        } catch (e) {
                            console.error("Invalid JSON line:", line);
                        }
                    }
                }
            } catch (error) {
                console.error(error);
                appendMessage("Miss Assistant", "<span class='text-danger'>Something went wrong while contacting Miss.</span>");
            }
        }
    });
        </script>


    </body>

</html>
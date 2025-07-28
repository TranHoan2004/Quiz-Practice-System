import {assignSubjects, renderFeaturedSubjects, renderSubjects} from './SubjectsPagination.js';

/**
 * Gửi prompt nhập từ người dùng đến server và xử lý phản hồi AI.
 * @async
 * @param {string} prompt - Prompt mà người dùng nhập.
 * @returns {Promise<number>} - HTTP status code từ server.
 */
async function sendPrompt(prompt) {
    try {
        const path = `${window.contextPath}/subject-list?page=${currentPage}&size=${numberItemsPerPage}`;
        const response = await fetch(path, {
            method: 'POST',
            body: JSON.stringify({prompt})
        });
        const data = await response.json();
        if (!(Object.keys(data[0]).length === 0 && data[0].constructor === Object)) {
            featuredSubjects = data[0].featured_subjects;
            assignSubjects(data);
        }
        socket.send(prompt);
        return response.status;
    } catch (e) {
        showNotification(e.message, 'not success');
        return 400;
    }
}

/**
 * Gửi yêu cầu tìm kiếm dựa trên chuỗi người dùng nhập và cập nhật danh sách subject.
 * @param {SubmitEvent} e - Sự kiện submit form.
 */
document.getElementById('subjectSearchForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const request = document.getElementById('subjectSearch').value.trim();
    const path = `${window.contextPath}/subject-list?query=${request}&page=${currentPage}&size=${numberItemsPerPage}`;
    fetch(path, {method: 'GET', headers: {'Content-Type': 'application/text', 'X-Source': 'search'}})
        .then(res => res.json())
        .then(data => {
            assignSubjects(data);
            renderSubjects();
        });
});

/**
 * Bắt sự kiện click vào thẻ subject và hiển thị thông tin liên hệ của tác giả môn học.
 * @param {MouseEvent} e - Sự kiện click từ người dùng.
 */
document.addEventListener('click', function (e) {
    const element = e.target.closest('[id^="items-"]');
    if (!element)
        return;

    const contactId = element.id.replace('items-', '');
    const subject = subjects.find(s => s.author.id === contactId);
    if (!subject)
        return;

    const {email, phone, link} = subject.author;
    document.getElementById('cardBody').innerHTML = `
    <label class="form-label fw-semibold text-primary-emphasis">
      <i class="bi bi-info-circle me-1"></i>
      <span class="contact_links">Contact & Links</span>
    </label>
    <ul class="list-group">
      <li class="list-group-item border-0 p-1"><i class="fa fa-envelope me-2"></i>${email}</li>
      <li class="list-group-item border-0 p-1"><i class="fa fa-phone-alt me-2"></i>${phone}</li>
      ${Object.entries(link).map(([key, url]) => `
        <li class="list-group-item border-0 p-1">
          <a href="${url}" target="_blank"><i class="fa fa-info me-2"></i>${key}</a>
        </li>`).join('')}
    </ul>`;
});

/**
 * Xử lý form gửi prompt AI (giao diện AI hỏi – đáp).
 * Có 2 form: 1 cho trang chính và 1 cho phần khóa học.
 */
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('aiPromptFormCourse');
    if (!form)
        return;
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const input = form.querySelector('input');
        const prompt = input.value.trim();
        if (!prompt)
            return;

        let responseDiv = document.getElementById('aiResponseCourse');
        responseDiv.classList.remove('d-none');
        responseDiv.innerHTML = '<i class="fas fa-robot me-2"></i> Processing...';

        setTimeout(async () => {
            const status = await sendPrompt(prompt);
            if (status === 400) {
                input.value = '';
                responseDiv?.classList.add('d-none');
            } else if (status === 202) {
                const p = document.getElementById('aiResponseCourse');
                p.innerText = '';
                const content = answer;

                let index = 0;
                const typingSpeed = 20;

                function typeWriter() {
                    if (index < content.length) {
                        p.innerHTML += content.charAt(index) === '\n'
                            ? '<br>'
                            : content.charAt(index);
                        index++;
                        setTimeout(typeWriter, typingSpeed);
                    }
                }

                typeWriter();
            } else {
                responseDiv.classList.add('d-none');
                renderFeaturedSubjects();
                renderSubjects();
                document.getElementById('ai-section')?.classList.add('d-none');
                document.getElementById('course-section').style.display = '';
            }
        }, 1200);
    });
});
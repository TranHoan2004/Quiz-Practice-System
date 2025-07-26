import {assignSubjects, renderSubjects, renderFeaturedSubjects, openOtherPages} from './SubjectsPagination.js';

let topics;
let topicsCloned;
let index = 0;
let content;
let firstAnswer = [];
let secondAnswer;
let thirdAnswer;
let fourthAnswer;

/**
 * @fileoverview Script xử lý luồng onboarding người dùng cho hệ thống học tập.
 * Bao gồm hiển thị từng bước onboarding, chọn mục tiêu học tập, đối tượng người dùng,
 * trình độ học vấn, và chủ đề học tập.
 *
 * @author HoanTX
 * @version 1.0
 */
document.addEventListener('DOMContentLoaded', () => {
    let currentStep = 1;
    const totalSteps = 4;
    let showAllTopics = false;

    /**
     * Hiển thị step tương ứng trong quy trình onboarding.
     * @param {number} step - Bước hiện tại trong quy trình onboarding.
     */
    function showStep(step) {
        document.querySelectorAll('.onboarding-step').forEach(s => s.classList.add('d-none'));
        document.querySelector(`.onboarding-step[data-step="${step}"]`).classList.remove('d-none');

        document.querySelectorAll('#stepper .step').forEach(s => s.classList.remove('active', 'completed'));
        for (let i = 1; i < step; i++) {
            document.querySelector(`#stepper .step[data-step="${i}"]`).classList.add('completed');
        }
        document.querySelector(`#stepper .step[data-step="${step}"]`).classList.add('active');

        if (step === 4) {
            renderTopicCards(document.getElementById('roleSearchInput').value);
        }
    }

    /**
     * Render các topic theo hàng và số lượng per row.
     * @param {Array<Object>} topicsCloned - Danh sách topic cần render.
     * @param {HTMLElement} list - Phần tử DOM để chứa danh sách topic.
     */
    function renderTopic(topicsCloned, list) {
        const cardsPerRow = 6;
        for (let i = 0; i < topicsCloned.length; i += cardsPerRow) {
            const row = document.createElement('div');
            row.className = 'd-flex flex-wrap mb-3 topic-row';

            topicsCloned.slice(i, i + cardsPerRow).forEach(topic => {
                const card = document.createElement('div');
                card.className = 'topic-card me-3 mb-0';
                card.dataset.topic = topic.name;
                card.style.borderColor = topic.color;

                const icon = topic.icon.replace(/\\"/g, '"');
                card.innerHTML = `
                    <span class="topic-icon" style="color:${topic.color}">${icon}</span>
                    <span>${topic.name}</span>
                    <span class="topic-check"><i class="fas fa-check"></i></span>
                    <span class="topic-plus"><i class="fas fa-plus"></i></span>
                `;
                row.appendChild(card);
            });

            list.appendChild(row);
        }
    }

    /**
     * Hiển thị danh sách topic được lọc hoặc phân trang.
     * @param {string} [filter=''] - Từ khóa tìm kiếm để lọc topic.
     */
    window.renderTopicCards = (filter = '') => {
        topics = content[0].topicsUI.length !== 0 ? content[0].topicsUI : content[0].topics;
        const list = document.getElementById('topic-card-list');
        list.innerHTML = '';

        const filteredTopics = filter ? topics.filter(t => {
            return t.name.toLowerCase().includes(filter.toLowerCase())
        }) : topics;
        if (!showAllTopics) {
            index = filter ? 0 : index + 7;
            topicsCloned = filteredTopics.slice(index, index + 7);
        } else {
            topicsCloned = filteredTopics;
        }
        renderTopic(topicsCloned, list);
    };

    /**
     * Lọc topic theo input từ người dùng.
     */
    document.getElementById('roleSearchInput').addEventListener('input', e => renderTopicCards(e.target.value));

    /**
     * Xử lý khi người dùng nhấn "Xem thêm chủ đề".
     */
    document.getElementById('viewMoreRoles').addEventListener('click', function (e) {
        e.preventDefault();
        const list = document.getElementById('topic-card-list');

        showAllTopics = index >= topics.length;

        if (!showAllTopics) {
            topicsCloned = topics.slice(index, index + 7);
            index += 7;
            renderTopic(topicsCloned, list);
            if (index >= topics.length) this.textContent = 'Show fewer topics';
        } else {
            const rows = list.querySelectorAll('.topic-row');
            rows.forEach((row, i) => i >= 1 && list.removeChild(row));
            index = 7;
            showAllTopics = false;
            this.textContent = '+ View more topics';
        }
    });

    /**
     * Toggle class 'selected' cho các topic khi người dùng chọn.
     */
    document.addEventListener('click', e => {
        const card = e.target.closest('.topic-card');
        const errorBox = document.getElementById('topic-error');

        if (card) {
            card.classList.toggle('selected');
        }

        errorBox.innerText = document.querySelectorAll('.topic-card.selected').length === 0 ? 'Please select at least one topic.' : '';
    });

    /**
     * Điều hướng sang bước tiếp theo nếu dữ liệu hợp lệ.
     */
    document.querySelectorAll('.next-step').forEach(btn => {
        btn.addEventListener('click', () => {
            const isValid = (currentStep === 1 && document.querySelector('input[name="goal"]:checked')) || (currentStep === 2 && document.querySelector('input[name="occupation"]:checked')) || (currentStep === 3 && document.querySelector('input[name="education"]:checked')) || (currentStep !== 1 && currentStep !== 2 && currentStep !== 3);

            if (!isValid) {
                alert('Please select or enter the required information before continuing.');
                return;
            }

            if (currentStep < totalSteps) {
                currentStep++;
                showStep(currentStep);
            }
        });
    });

    /**
     * Quay về bước trước đó.
     */
    document.querySelectorAll('.prev-step').forEach(btn => {
        btn.addEventListener('click', () => {
            if (currentStep > 1) {
                currentStep--;
                showStep(currentStep);
            }
        });
    });

    /**
     * Submit dữ liệu onboarding sau khi chọn chủ đề.
     */
    document.getElementById('onboardingForm').addEventListener('submit', function (e) {
        e.preventDefault();
        const selected = document.querySelectorAll('.topic-card.selected');
        const error = document.getElementById('topic-error');

        if (selected.length === 0) {
            error.style.display = 'block';
            return;
        }

        error.style.display = 'none';
        document.getElementById('onboarding-steps').style.display = 'none';
        document.getElementById('ai-section').style.display = 'block';
    });

    showStep(currentStep);

    /**
     * Hàm khởi động lại toàn bộ form onboarding.
     */
    window.reopenOnboarding = () => {
        document.getElementById('onboarding-steps').classList.remove('d-none');
        document.getElementById('course-section').style.display = 'none';

        currentStep = 1;
        showStep(currentStep);

        document.querySelectorAll('input[type="radio"]').forEach(radio => {
            radio.checked = false;
        });

        document.querySelectorAll('.topic-card.selected').forEach(card => {
            card.classList.remove('selected');
        });

        index = 0;
        showAllTopics = false;
        document.getElementById('roleSearchInput').value = '';
        document.getElementById('topic-card-list').innerHTML = '';
        document.getElementById('viewMoreRoles').textContent = "+ View more topics";
    }
});

/**
 * Gửi yêu cầu lấy danh sách chủ đề từ server.
 * @async
 * @function
 */
async function getAllSubjectTopics() {
    const path = `${window.contextPath}/subject-list`;
    document.getElementById('spinner').classList.add('show')
    try {
        const response = await fetch(path, {
            method: 'GET', headers: {'X-Source': 'topic'}
        });
        const data = await response.json();
        if (data[0].signal) {
            assignSubjects(data);
            renderSubjects();
            renderFeaturedSubjects();
            document.getElementById('onboarding-steps').classList.add('d-none');
            document.getElementById('course-section').style.display = 'block';
        }
        content = data;
    } catch (e) {
        console.error(e.message);
    } finally {
        document.getElementById('spinner').classList.remove('show');
    }
}

await getAllSubjectTopics();

assignTitle(content[0]);

/**
 * Submit thông tin người dùng sau khi chọn topic.
 * @async
 * @function
 */
window.submitForm = async () => {
    getSelectedTopic();
    let path = `${window.contextPath}/subject-list`;
    const body = {
        learningTarget: firstAnswer, identified: secondAnswer, educationLevel: thirdAnswer, selectedTopics: fourthAnswer
    }
    document.getElementById('spinner').classList.add('show')
    performance.now();
    try {
        const response = fetch(path, {
            method: 'POST', headers: {
                'X-Source': 'customized_topic', 'Content-Type': 'application/json'
            }, body: JSON.stringify(body)
        });
        performance.now();
        const status = (await response).status;
        if (status === 200) {
            await openOtherPages();
            document.getElementById('onboarding-steps').classList.add('d-none');
            document.getElementById('course-section').style.display = 'block';
        }
    } catch (e) {
        console.error(e.message);
        showNotification('There is an error happening. Please try again!', 'not success')
    } finally {
        document.getElementById('spinner').classList.remove('show');
    }
}

/**
 * Lấy giá trị mục tiêu học tập đã chọn.
 */
window.getLearningTarget = () => {
    const selected = document.querySelectorAll('input[name="goal"]:checked');
    firstAnswer = Array.from(selected).map(input => input.value);

    if (firstAnswer.length < 0) {
        alert("Please select a learning goal.");
    }
}

/**
 * Lấy giá trị đối tượng người dùng đã chọn.
 */
window.getIdentified = () => {
    const selected = document.querySelector('input[name="occupation"]:checked');
    if (selected) {
        secondAnswer = selected.value;
    } else {
        alert("Please select your current status.");
    }
}

/**
 * Lấy trình độ học vấn đã chọn.
 */
window.getEducationLevel = () => {
    const selected = document.querySelector('input[name="education"]:checked');
    if (selected) {
        thirdAnswer = selected.value;
    } else {
        alert("Please select your education level.");
    }
}

/**
 * Lấy danh sách các chủ đề người dùng đã chọn.
 */
const getSelectedTopic = () => {
    fourthAnswer = Array.from(document.querySelectorAll('.topic-card.selected'))
        .map(card => card.dataset.topic);
}
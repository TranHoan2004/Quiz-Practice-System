document.addEventListener('DOMContentLoaded', function () {
    const subjectSelect = document.getElementById('subjectSelect');
    const lessonSelect = document.getElementById('lessonSelect');
    const dimensionSelect = document.getElementById('dimensionSelect');
    // Hàm để tải Lessons và Dimensions qua AJAX 
    async function loadLessonsAndDimensions(selectedSubjectId) {
        console.log("Đang tải Lessons và Dimensions cho Subject ID:", selectedSubjectId);
        try {
            const response = await fetch('question-details', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `subjectId=${selectedSubjectId}`
            });
            console.log("Subject ID gửi đi:", selectedSubjectId);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            console.log("Dữ liệu nhận được từ AJAX:", response);
            // Cập nhật Lessons
            lessonSelect.innerHTML = '';
            if (data.lessons?.length > 0) {
                data.lessons.forEach(lesson => {
                    const option = document.createElement('option');
                    option.value = lesson.id;
                    option.textContent = lesson.name;
                    lessonSelect.appendChild(option);
                });
            } else {
                lessonSelect.innerHTML = `<option value="">-- Không có bài học --</option>`;
            }
        } catch (error) {
            console.error("Lỗi khi tải Lessons và Dimensions:", error);
            lessonSelect.innerHTML = '<option value="">Lỗi tải dữ liệu</option>';
            dimensionSelect.innerHTML = '<option value="">Lỗi tải dữ liệu</option>';
        }
    }

// Lắng nghe sự kiện thay đổi của Subject 
    subjectSelect.addEventListener('change', function () {
        const selectedSubjectId = this.value;
        loadLessonsAndDimensions(selectedSubjectId);
    });

});
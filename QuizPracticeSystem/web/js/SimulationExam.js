$(document).ready(function () {
    window.fetchExams = function (page = 1) {
        const search = $('#searchInput').val().trim();
        const subject = $('#subjectExamFilter').val();
        const size = 10;

        $('#quiz-table-body').html('<tr><td colspan="8" class="text-center">Loading...</td></tr>');

        $.get('http://localhost:9999/qps/simulation-exams', {
            search: search,
            subject: subject,
            page: page,
            size: size
        }, function (data) {
            const quizzes = data.quizzes;
            const totalPages = data.totalPages;

            if (!quizzes || quizzes.length === 0) {
                $('#quiz-table-body').html('<tr><td colspan="8" class="text-center text-muted">No simulation exams found.</td></tr>');
                $('#pagination').html('');
                return;
            }

            const html = quizzes.map((q, i) => `
                <tr>
                    <td>${(page - 1) * size + i + 1}</td>
                    <td>${q.subjectName}</td>
                    <td>${q.title}</td>
                    <td>${q.level}</td>
                    <td>${q.numberOfQuestions}</td>
                    <td>${q.duration} mins</td>
                    <td>${(q.passRate * 100).toFixed(2)}%</td>
                    <td>
                        <a href="javascript:void(0)" onclick="showQuizDetail('${q.id}')" class="btn btn-sm btn-outline-primary">View</a>
                        <a href="quiz-manager?exam=${q.id}" class="btn btn-sm btn-outline-success ms-1">Practice</a>
                    </td>
                </tr>
            `).join('');
            $('#quiz-table-body').html(html);
            renderPagination(page, totalPages);
        });
    };


    function renderPagination(currentPage, totalPages) {
        let html = '';

        // Prev
        html += `<li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
                    <a class="page-link" href="#" onclick="fetchExams(${currentPage - 1}); return false;">« Prev</a>
                 </li>`;

        for (let i = 1; i <= totalPages; i++) {
            html += `<li class="page-item ${i === currentPage ? 'active' : ''}">
                        <a class="page-link" href="#" onclick="fetchExams(${i}); return false;">${i}</a>
                     </li>`;
        }

        html += `<li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="#" onclick="fetchExams(${currentPage + 1}); return false;">Next »</a>
                 </li>`;

        $('#pagination').html(`<ul class="pagination justify-content-center mt-4">${html}</ul>`);
    }

    // Bind
    $('#searchBtn').on('click', function () {
        fetchExams(1);
    });

    $('#subjectExamFilter').on('change', function () {
        fetchExams(1);
    });

    // Initial load
    fetchExams(1);

    window.showQuizDetail = (quizId) => {
        $.get(`/qps/quiz-detail?id=${quizId}`, function (data) {
            $('#quizTitle').text(data.title);
            $('#quizSubject').text(data.subjectName);
            $('#quizLevel').text(data.level);
            $('#quizType').text(data.type);
            $('#quizDuration').text(data.duration);
            $('#quizDescription').text(data.description);
            $('#practiceBtn').attr('href', `quiz-manager?exam=${data.id}`);
            $('#numOfQuestion').text(data.numberOfQuestions + " questions");
            $('#updatedDate').text(data.updatedDate);
            
            const passRate = (data.passRate * 100).toFixed(2);
            const $rateElem = $('#quizPassRate');
            $rateElem
                    .text(passRate + ' %')
                    .removeClass('text-success text-danger')
                    .addClass(passRate >= 50 ? 'text-success' : 'text-danger');

            const modal = new bootstrap.Modal(document.getElementById('quizDetailModal'));
            modal.show();
        }).fail(function () {
            alert('Failed to load quiz detail.');
        });
    }


});

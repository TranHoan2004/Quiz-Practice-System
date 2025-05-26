const itemsPerPage = 4;
const items = Array.from(document.querySelectorAll('.subject-card'));
const pagination = document.getElementById('pagination');
let currentPage = 1;
let filteredItems = items.slice();

function showPage(page) {
    currentPage = page;
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;

    items.forEach(item => item.style.display = 'none');
    filteredItems.slice(start, end).forEach(item => item.style.display = 'block');

    renderPagination();
}

function renderPagination() {
    pagination.innerHTML = '';
    const totalPages = Math.ceil(filteredItems.length / itemsPerPage);

    if (filteredItems.length === 0) {
        pagination.parentElement.style.display = 'none';
        return;
    } else {
        pagination.parentElement.style.display = '';
    }

    // Prev button
    const prev = document.createElement('li');
    prev.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    prev.innerHTML = `<a class="page-link" href="#">Trước</a>`;
    prev.onclick = (e) => {
        e.preventDefault();
        if (currentPage > 1)
            showPage(currentPage - 1);
    };
    pagination.appendChild(prev);

    // Page numbers
    for (let i = 1; i <= totalPages; i++) {
        const page = document.createElement('li');
        page.className = `page-item ${i === currentPage ? 'active' : ''}`;
        page.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        page.onclick = (e) => {
            e.preventDefault();
            showPage(i);
        };
        pagination.appendChild(page);
    }

    // Next button
    const next = document.createElement('li');
    next.className = `page-item ${currentPage === totalPages ? 'disabled' : ''}`;
    next.innerHTML = `<a class="page-link" href="#">Sau</a>`;
    next.onclick = (e) => {
        e.preventDefault();
        if (currentPage < totalPages)
            showPage(currentPage + 1);
    };
    pagination.appendChild(next);
}

function filterSubjects() {
    const searchValue = document.getElementById('subjectSearch').value.trim().toLowerCase();
    const activeCategory = document.querySelector('.category-link.active');
    const category = activeCategory ? activeCategory.dataset.category : 'all';

    filteredItems = items.filter(item => {
        const type = item.dataset.type.toLowerCase();
        const dimension = item.dataset.dimension.toLowerCase();
        const matchTitle = type.includes(searchValue);
        const matchCategory = (category === 'all' || dimension === category);
        return matchTitle && matchCategory;
    });
    showPage(1);
}

// Search form event
document.getElementById('subjectSearchForm').addEventListener('submit', function (e) {
    e.preventDefault();
    filterSubjects();
});

document.getElementById('subjectSearch').addEventListener('input', function () {
    filterSubjects();
});

// Category filter event
document.querySelectorAll('.category-link').forEach(link => {
    link.addEventListener('click', function (e) {
        e.preventDefault();
        document.querySelectorAll('.category-link').forEach(l => l.classList.remove('active'));
        this.classList.add('active');
        filterSubjects();
    });
});
// Set default active category
document.querySelector('.category-link[data-category="all"]').classList.add('active');

// Initial show
showPage(1);

// Modal logic
document.querySelectorAll('.subject-card .btn-primary').forEach(btn => {
    btn.addEventListener('click', function (e) {
        e.preventDefault();
        let modal = new bootstrap.Modal(document.getElementById('subjectRegisterModal'));
        modal.show();
    });
});

function updateUserInfoFields() {
    const isLoggedIn = !document.querySelector('.not-logged-in').classList.contains('d-flex');
    document.getElementById('userInfoFields').classList.toggle('d-none', isLoggedIn);
    document.getElementById('userInfoNotice').classList.toggle('d-none', !isLoggedIn);
}
updateUserInfoFields();

document.getElementById('subjectRegisterForm').addEventListener('submit', function (e) {
    e.preventDefault();
    // showNotification('Đăng ký thành công!', 'success');
    document.activeElement.blur();
    setTimeout(() => {
        bootstrap.Modal.getInstance(document.getElementById('subjectRegisterModal')).hide();
    }, 10);
    this.reset();
});
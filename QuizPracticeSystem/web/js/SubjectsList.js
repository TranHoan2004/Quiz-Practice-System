/**
 * Gửi yêu cầu GET để lấy toàn bộ sliders và hiển thị chúng.
 * @returns {Promise<void>}
 */
async function renderSliders() {
    const response = await fetch(`${window.contextPath}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    if (response.ok) {
        const data = await response.json();
        sliders = data.map(slider => ({
            id: slider.id,
            title: slider.title,
            img: slider.imageUrl,
            link: slider.backlinkUrl,
            status: slider.status,
            author: slider.author
        }));
    }
    filteredSliders = [...sliders];
    renderData(filteredSliders)
}

/**
 * Hiển thị dữ liệu slider theo trang hiện tại.
 * @param {Array<Object>} filteredSliders
 */
window.renderData = function (filteredSliders) {
    const list = document.getElementById('sliderList');
    list.innerHTML = '';

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const slidersToRender = filteredSliders.slice(startIndex, endIndex);

    if (slidersToRender.length === 0) {
        document.getElementById('noResultsMessage').style.display = 'block';
        document.getElementById('pag').style.display = 'none';
    } else {
        document.getElementById('noResultsMessage').style.display = 'none';
        document.getElementById('pag').style.display = 'block';
    }

    slidersToRender.forEach(slider => {
        const col = document.createElement('div');
        col.className = `col-md-3 wow fadeInUp slider-card d-flex`;
        col.setAttribute('data-wow-delay', '0.1s');
        const id = slider.id;
        const arr = slider.status === true ?
            ['btn-danger', '<i class="fas fa-eye-slash me-1"></i> Inactive', 'btn-success', 'Active']
            : ['btn-success', '<i class="fas fa-eye me-1"></i> Active', 'btn-danger', 'Inactive'];

        col.innerHTML = `
                    <div class="card h-100 shadow-sm border border-dark" style="position: relative; border-radius: 12px;">
                    <img src="../../` + slider.img + `" class="card-img-top" alt="` + slider.title + `" style="width: 100%; height: 140px; object-fit: cover; border-top-left-radius: 12px; border-top-right-radius: 12px;">
                    <div class="card-body d-flex flex-column p-2">
                        <h5 class="card-title text-center" style="font-size: 1.1rem;">` + slider.title + `</h5>
                        <p class="card-text mb-1">ID: ` + slider.id + `</p>
                        <p class="card-text mb-1" id="author" hidden>Author: ` + slider.author + `</p>
                        <p class="card-text mb-1">Link: <a href=" ` + slider.link + `" target="_blank">` + slider.link + `</a></p>
                        <p class="card-text mb-2">Status: <button class="btn ` + arr[2] + ` rounded-pill w-auto">` + arr[3] + `</button></p>
                        <div class="d-flex justify-content-center gap-1 mt-auto">
                        <button class="btn btn-sm btn-primary" onclick="editSlider('` + id + `')">
                            <i class="fas fa-edit me-1"></i> Edit
                        </button>
                        <button class="btn btn-sm ` + arr[0] + `" disabled>
                            ` + arr[1] + `
                        </button>
                        <button class="btn btn-sm btn-info text-white" onclick="viewSlider('` + id + `')">
                            <i class="fas fa-eye me-1"></i> View
                        </button>
                        </div>
                    </div>
                    </div>
                    `;
        list.appendChild(col);
    });

    renderPagination(filteredSliders);
}

/**
 * Hiển thị phân trang dựa vào danh sách hiện tại.
 * @param {Array<Object>} slidersToRender
 */
function renderPagination(slidersToRender) {
    const pagination = document.getElementById('pagination');
    const message = document.getElementById('message')
    pagination.innerHTML = `
            <li class="page-item ` + (currentPage === 1 ? 'disabled' : '') + `">
                 <a class="page-link rounded-pill px-3" href="#" onclick="currentPage = ` + (currentPage - 1) + `; renderData(sliders);">
                     <i class="bi bi-chevron-left"></i> Prev
                 </a>
            </li>`

    const totalPages = Math.ceil(slidersToRender.length / itemsPerPage);

    for (let i = 1; i <= totalPages; i++) {
        pagination.innerHTML += `
            <li class="page-item ` + (i === currentPage ? 'active' : '') + `">
                 <a class="page-link rounded-pill px-3" href="#" onclick="currentPage = ` + i + `; renderData(sliders);">`
            + i + `</a>
            </li>`
    }

    pagination.innerHTML += `
            <li class="page-item ` + (currentPage === totalPages ? 'disabled' : '') + `">
                 <a class="page-link rounded-pill px-3" href="#" onclick="currentPage = ` + (currentPage + 1) + `; renderData(sliders);">
                    Next <i class="bi bi-chevron-right"></i>
                 </a>
            </li>`

    message.innerHTML = `Showing ` + ((currentPage - 1) * itemsPerPage + 1) + ` to `
        + Math.min(currentPage * itemsPerPage, slidersToRender.length) + ` entries of ` + slidersToRender.length + ` total`;
}

// Initial render
renderSliders();

// Handle change table's size
document.getElementById('settingOption1').addEventListener('change', function () {
    const input = parseInt(this.value);
    if (input >= 2) {
        itemsPerPage = parseInt(this.value) || 8;
        renderData(filteredSliders);
    } else {
        alert('Table size must larger than 1')
    }
})

// Handle show/hide images
document.getElementById('settingOption2').addEventListener('change', function () {
    imgHidden = this.checked;
    const images = document.querySelectorAll('.slider-card img');
    images.forEach(img => {
        img.style.display = imgHidden ? 'none' : 'block';
    });
    const label = document.querySelector('label[for="settingOption2"]');
    label.innerHTML = imgHidden ? 'Hiện ảnh' : 'Ẩn ảnh';
})

// Handle show/hide authors
document.getElementById('settingOption3').addEventListener('change', function () {
    authorHidden = this.checked;
    const authors = document.querySelectorAll('.slider-card #author');
    authors.forEach(author => {
        author.hidden = !authorHidden;
    });
    const label = document.querySelector('label[for="settingOption3"]');
    label.innerHTML = authorHidden ? 'Ẩn người tạo' : 'Hiện người tạo';
})

// Handle show/hide status
document.getElementById('settingOption4').addEventListener('change', function () {
    const statusButtons = document.querySelectorAll('.slider-card .btn-danger, .slider-card .btn-success');
    statusButtons.forEach(button => {
        button.hidden = this.checked;
    });
    const label = document.querySelector('label[for="settingOption4"]');
    label.innerHTML = this.checked ? 'Hiện trạng thái' : 'Ẩn trạng thái';
})

/**
 * Lọc slider theo từ khóa và trạng thái.
 * @param {string} [searchTerm='']
 * @param {string} [status='all']
 */
function filterSliders(searchTerm = '', status = 'all') {
    let sliders = filteredSliders
    if (!searchTerm.empty || !status.empty) {
        sliders = sliders.filter(slider => {
            const titleMatch = slider.title.toLowerCase().includes(searchTerm.toLowerCase());
            const authorMatch = slider.author.toLowerCase().includes(searchTerm.toLowerCase());
            const statusMatch = status === 'all' || slider.status === status;

            return (titleMatch || authorMatch) && statusMatch;
        });
    }
    currentPage = 1;
    displayFilterSliders = sliders;
    renderData(displayFilterSliders);
}

document.getElementById('sliderSearchForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const searchTerm = document.getElementById('sliderSearch').value;
    getSlidersByKeyword(searchTerm);
});

// Handle status filter change
document.getElementById('statusFilterSelect').addEventListener('change', function () {
    const status = this.value;
    getSlidersByFilter(status);
})

/**
 * Lấy sliders từ backend theo trạng thái.
 * @param {string} status
 * @returns {Promise<void>}
 */
async function getSlidersByFilter(status) {
    const response = await fetch(`${window.contextPath}?status=` + (status === 'all' ? '' : status), {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Type': 'filter'
        }
    })
    if (response.ok) {
        const data = await response.json();
        console.log(data)
        sliders = data.map(slider => ({
            id: slider.id,
            title: slider.title,
            img: slider.imageUrl,
            link: slider.backlinkUrl,
            status: slider.status,
            author: slider.author
        }));
    }
    filteredSliders = [...sliders];
    renderData(filteredSliders)
}

/**
 * Tìm slider theo từ khóa.
 * @param {string} keyword
 * @returns {Promise<void>}
 */
async function getSlidersByKeyword(keyword) {
    const response = await fetch(`${window.contextPath}?keyword=` + keyword, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Type': 'keyword'
        }
    })
    if (response.ok) {
        const data = await response.json();
        sliders = data.map(slider => ({
            id: slider.id,
            title: slider.title,
            img: slider.imageUrl,
            link: slider.backlinkUrl,
            status: slider.status,
            author: slider.author
        }));
    }
    filteredSliders = [...sliders];
    renderData(filteredSliders)
}

/**
 * Chuyển hướng tới trang chỉnh sửa slider.
 * @param {string} id
 */
window.editSlider = function (id) {
    console.log(`${window.contextPath}?id=` + id)
    window.location.href = `${window.contextPath}?id=` + id;
}

/**
 * Hiển thị hình ảnh slider trong modal.
 * @param {string} id
 */
window.viewSlider = function (id) {
    const slider = sliders.find(s => s.id === id);
    if (!slider) return;

    const imgEl = document.getElementById('theatreImage');
    imgEl.src = `../../` + slider.img;
    imgEl.alt = slider.title || 'Slider Image';

    const modal = new bootstrap.Modal(document.getElementById('sliderImageModal'));
    modal.show();
}

/**
 * Gửi yêu cầu lấy tiêu đề trang từ backend.
 * @returns {Promise<void>}
 */
const getHeaderTitle = async () => {
    try {
        const response = await fetch(`${window.contextPath}`, {
            method: 'GET',
            headers: {
                'X-Source': 'getTitle'
            }
        });
        const content = await response.json();
        assignTitle(content[0]);
    } catch (e) {
        console.error(e);
    }
};

await getHeaderTitle();
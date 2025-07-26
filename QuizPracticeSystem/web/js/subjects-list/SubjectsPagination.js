/**
 * Hiển thị danh sách môn học sau khi sắp xếp theo `updatedDate`.
 * Xử lý tạo giao diện từng subject và hiển thị phân trang.
 */
export function renderSubjects() {
    subjects.sort((a, b) => (b.updatedDate || '').localeCompare(a.updatedDate || ''));

    const list = document.getElementById('subjectList');
    list.innerHTML = '';

    const noResultsMsg = document.getElementById('noSubjectResultsMessage');
    noResultsMsg.style.display = subjects.length === 0 ? 'block' : 'none';

    subjects.forEach((subject) => {
        const col = document.createElement('div');
        col.className = 'col-md-6 col-lg-4 wow fadeInUp';
        col.setAttribute('data-wow-delay', '0.1s');

        const wrapper = document.createElement('div');
        wrapper.className = 'subject-card-wrapper h-100';
        col.appendChild(wrapper);

        const taglinesHTML = subject.tagline
            .map((tag) => `<p class="card-text mb-1 text-center tagline">#${tag}</p>`)
            .join(' ');

        const priceHTML = subject.lowestPrice && subject.salePrice ? `
      <div class="mb-2 text-center content">
        <span class="content">Cheapest Course: From </span>
        <span class="text-decoration-line-through text-muted content">${subject.lowestPrice}đ</span>
        <span class="fw-bold text-success sale-price">${subject.salePrice}đ</span>
      </div>` : '';

        wrapper.innerHTML = `
      <div class="card h-100 shadow-sm border border-dark subjectBox" id="items-${subject.author.id}">
        <a href="subject-detail.html?id=${subject.id}" class="subjectLink">
          <img src="../../${subject.thumbnailURL}" class="card-img-top" alt="${subject.name}">
        </a>
        <div class="card-body d-flex flex-column p-2">
          <h5 class="card-title text-center subjectName">${subject.name}</h5>
          ${taglinesHTML}
          ${priceHTML}
          <div class="d-flex justify-content-center gap-1 mt-auto">
            <button class="btn btn-sm btn-primary" onclick="window.location.href='subject-detail.html?id=${subject.id}'">
              <i class="fas fa-info-circle me-1"></i> Details
            </button>
            <button class="btn btn-sm btn-success" onclick="openRegisterModal('${subject.id}')">
              <i class="fas fa-user-plus me-1"></i> Register
            </button>
          </div>
        </div>
      </div>`;

        list.appendChild(col);
    });

    filteredSubjects = [...subjects];
    renderPaginationElements(numberOfPages);
}

/**
 * Tạo và hiển thị các nút phân trang dựa trên tổng số trang.
 * @param {number} totalPages - Tổng số trang cần hiển thị.
 */
function renderPaginationElements(totalPages) {
    const pagination = document.getElementById('subjectPagination');
    pagination.innerHTML = '';

    const createPageItem = (label, page, disabled = false, active = false) => {
        const li = document.createElement('li');
        li.className = 'page-item' + (disabled ? ' disabled' : '') + (active ? ' active' : '');
        const a = document.createElement('a');
        a.className = 'page-link';
        a.href = '#';
        a.innerHTML = label;
        a.addEventListener('click', async (e) => {
            e.preventDefault();
            if (!disabled && currentPage !== page) {
                currentPage = page;
                await openOtherPages();
            }
        });
        li.appendChild(a);
        return li;
    };

    pagination.appendChild(createPageItem('&laquo;', currentPage - 1, currentPage === 1));

    for (let i = 1; i <= totalPages; i++) {
        pagination.appendChild(createPageItem(i, i, false, i === currentPage));
    }

    pagination.appendChild(createPageItem('&raquo;', currentPage + 1, currentPage === totalPages));
    updateSubjectEntryInfo();
}

/**
 * Cập nhật thông tin về số lượng entry đang được hiển thị.
 */
function updateSubjectEntryInfo() {
    const startEntry = (currentPage - 1) * numberItemsPerPage + 1;
    const endEntry = Math.min(currentPage * numberItemsPerPage, numberOfItems);

    document.getElementById('subjectStartEntry').innerText = numberOfItems === 0 ? 0 : startEntry;
    document.getElementById('subjectEndEntry').innerText = endEntry;
    document.getElementById('subjectTotalEntries').innerText = numberOfItems;
}

/**
 * Hiển thị danh sách các môn học nổi bật ở sidebar.
 */
export function renderFeaturedSubjects() {
    const ul = document.getElementById('featuredSubjects');
    ul.innerHTML = featuredSubjects.map(s => `
    <li class="list-group-item border-0 p-1">
      <a href="subject-detail.html?id=${s.id}" class="pageIndex">
        <img src="../../${s.thumbnailURL}" alt="${s.name}"> ${s.name}
      </a>
    </li>`).join('');
}

/**
 * Gán dữ liệu môn học từ server vào biến `subjects` và các biến toàn cục liên quan.
 * @param {Array<Object>} data - Dữ liệu trả về từ API chứa danh sách môn học.
 * @throws {Error} Nếu không có dữ liệu môn học.
 */
export function assignSubjects(data) {
    const d = data[0].subjects;
    if (!d)
        throw new Error("There is no subjects like your description");

    numberOfPages = data[0].numberOfPages;
    numberOfItems = data[0].numberOfItems;
    subjects = d.map(sub => ({
        id: sub.id,
        lowestPrice: sub.lowestPrice,
        name: sub.name,
        salePrice: sub.salePrice,
        tagline: sub.tagline,
        thumbnailURL: sub.thumbnailURL,
        updatedDate: sub.updatedDate,
        author: sub.contactInfo,
        pricePackage: sub.pricePackage
    }));
    featuredSubjects = data[0].featured_subjects;
}

/**
 * Gửi yêu cầu đến server để lấy dữ liệu trang khác dựa trên phân trang.
 * @async
 */
export async function openOtherPages() {
    try {
        const path = `${window.contextPath}/subject-list?page=${currentPage}&size=${numberItemsPerPage}`;
        const response = await fetch(path, {method: 'GET', headers: {'X-Source': 'pagination'}});
        const data = await response.json();
        assignSubjects(data);
        renderSubjects();
        renderFeaturedSubjects();
    } catch (e) {
        console.error(e);
        showNotification(e.message, 'not success');
    }
}

let selectedSubjectId = null;
const subjectRegisterModal = new bootstrap.Modal(document.getElementById('subjectRegisterModal'));

/**
 * Hiển thị modal đăng ký môn học với các gói giá khác nhau.
 * Tác giả: HoanTX
 *
 * @param {number|string} subjectId - ID của môn học được chọn để đăng ký.
 */
window.openRegisterModal = (subjectId) => {
    selectedSubjectId = subjectId;
    const userInfoNotice = document.getElementById('userInfoNotice');
    userInfoNotice.classList.add('d-none');

    document.getElementById('subjectRegisterForm').reset();
    subjectRegisterModal.show();

    const selection = document.getElementById('registerPackageSelect')
    const subject = subjects.find(subject => subject.id === subjectId);
    if (Object.keys(subject.pricePackage).length > 0) {
        selection.innerHTML += `
         ${subject.pricePackage.Bronze === 'undefined' || subject.pricePackage.Bronze === 0 ? '' : `<option value="${subject.pricePackage.Bronze}" data-type="Bronze">Bronze - ${subject.pricePackage.Bronze}đ</option>`}
         ${subject.pricePackage.Silver === 'undefined' || subject.pricePackage.Silver === 0 ? '' : `<option value="${subject.pricePackage.Silver}" data-type="Silver">Silver - ${subject.pricePackage.Silver}đ</option>`}
         ${subject.pricePackage.Gold === 'undefined' || subject.pricePackage.Gold === 0 ? '' : `<option value="${subject.pricePackage.Gold}" data-type="Gold">Gold - ${subject.pricePackage.Gold}đ</option>`}
    `;
    }
}

/**
 * Xử lý sự kiện submit form đăng ký môn học.
 * Tác giả: HoanTX
 *
 * Gửi thông tin người dùng và gói đăng ký đến server.
 *
 * @param {SubmitEvent} e - Sự kiện submit form.
 */
document.getElementById('subjectRegisterForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const pricePackage = document.getElementById('registerPackageSelect').value;
    const fullName = document.getElementById('fullname').value;
    const email = document.getElementById('email').value;
    const phoneNumber = document.getElementById('phone').value;
    const gender = document.getElementById('gender').value;
    const selectedOption = document.querySelector('#registerPackageSelect option:checked');
    const pricePackageName = selectedOption?.dataset.type;

    try {
        const response = await fetch(`${window.contextPath}/subject-list`, {
            method: 'POST',
            headers: {
                'X-Source': 'register',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                pricePackage,
                fullName,
                email,
                phoneNumber,
                gender,
                id: selectedSubjectId,
                pricePackageName
            })
        })
        subjectRegisterModal.hide();
        if (response.status === 200) {
            showNotification('Register successfully! Please check your email');
        } else {
            const data = await response.text();
            const parsed = JSON.parse(data);
            const message = parsed[0];
            showNotification(message, 'not success');
        }
    } catch (e) {
        showNotification('There is an error happening', 'not success')
    }
});
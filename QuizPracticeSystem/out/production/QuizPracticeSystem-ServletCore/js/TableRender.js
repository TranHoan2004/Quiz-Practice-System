const rowsPerPage = 10;
const tableBody = document.querySelector("table tbody");
const rows = Array.from(tableBody.querySelectorAll("tr"));
const pagination = document.getElementById("pagination");
let currentPage = 1;
const totalPages = Math.ceil(rows.length / rowsPerPage);

function renderPagination() {
    let html = '';

    html += `
        <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
            <a class="page-link rounded-pill px-3" href="#" data-page="prev">
                <i class="bi bi-chevron-left"></i> Prev
            </a>
        </li>
    `;

    for (let i = 1; i <= totalPages; i++) {
        html += `
            <li class="page-item ${i === currentPage ? "active" : ""}">
                <a class="page-link rounded-pill px-3" href="#" data-page="${i}">${i}</a>
            </li>
        `;
    }

    html += `
        <li class="page-item ${currentPage === totalPages ? "disabled" : ""}">
            <a class="page-link rounded-pill px-3" href="#" data-page="next">
                Next <i class="bi bi-chevron-right"></i>
            </a>
        </li>
    `;

    pagination.innerHTML = html;
}

function showPage(page) {
    rows.forEach((row, i) => {
        row.style.display = (i >= (page - 1) * rowsPerPage && i < page * rowsPerPage) ? "" : "none";
    });
    renderPagination();
}

pagination.addEventListener("click", (e) => {
    e.preventDefault();
    const target = e.target.closest("a.page-link");
    if (!target) return;

    const page = target.dataset.page;
    if (page === "prev" && currentPage > 1) currentPage--;
    else if (page === "next" && currentPage < totalPages) currentPage++;
    else if (!isNaN(page)) currentPage = parseInt(page);

    showPage(currentPage);
});

showPage(currentPage);

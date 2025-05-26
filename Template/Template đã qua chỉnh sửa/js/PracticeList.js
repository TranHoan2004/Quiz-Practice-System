document.querySelector('.btn-success').addEventListener('click', function (e) {
    if (this.innerText.includes('Add New')) {
        e.preventDefault();
        let modal = new bootstrap.Modal(document.getElementById('addPracticeModal'));
        modal.show();
    }
});
let rowToDelete = null;

document.querySelectorAll('.delete-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        rowToDelete = btn.closest('tr');
        const modalEl = document.getElementById('deleteModal');
        if (modalEl) {
            const modal = new bootstrap.Modal(modalEl);
            modal.show();
        }
    });
});

const confirmBtn = document.getElementById('confirmDeleteBtn');
if (confirmBtn) {
    confirmBtn.addEventListener('click', () => {
        if (rowToDelete) {
            const section = rowToDelete.getAttribute('data-section');
            const allRowsInSection = document.querySelectorAll(`tr[data-section='${section}']`);
            const dataRows = Array.from(allRowsInSection).filter(tr => !tr.classList.contains('section-footer'));

            if (dataRows.length === 1) {
                allRowsInSection.forEach(tr => tr.remove());
            } else {
                rowToDelete.remove();
            }

            rowToDelete = null;
        }

        const modalEl = document.getElementById('deleteModal');
        const modal = bootstrap.Modal.getInstance(modalEl);
        if (modal) modal.hide();
    });
}

document.addEventListener('DOMContentLoaded', function () {
    const addPracticeForm = document.getElementById('addPracticeForm');
    if (addPracticeForm) {
        addPracticeForm.addEventListener('submit', function (e) {
            e.preventDefault();
            showNotification('Practice added successfully!', 'success');
            const addModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('addPracticeModal'));
            addModal.hide();
            addPracticeForm.reset();
        });
    }

    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', function () {
            showNotification('Practice deleted successfully!', 'success'); // Always green
            const deleteModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('deleteModal'));
            deleteModal.hide();
        });
    }
});


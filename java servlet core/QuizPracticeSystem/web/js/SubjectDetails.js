function notification(message) {
    showNotification(message, "success");
}

document.addEventListener('DOMContentLoaded', function () {
    const addDimensionForm = document.getElementById('addDimensionForm');
    const addDimensionModalEl = document.getElementById('addDimensionModal');

    if (addDimensionForm && addDimensionModalEl) {
        const addDimensionModal = new bootstrap.Modal(addDimensionModalEl);

        addDimensionForm.addEventListener('submit', function (e) {
            e.preventDefault();

            if (addDimensionForm.checkValidity()) {
                showNotification("Add dimension successfully", "success");

                // Close modal
                addDimensionModal.hide();

                // Reset form
                addDimensionForm.reset();
            } else {
                addDimensionForm.reportValidity();
            }
        });
    }

    const addPricePackageModalEl = document.getElementById('addPricePackageModal');
    const addPricePackageForm = addPricePackageModalEl?.querySelector('form');

    if (addPricePackageForm && addPricePackageModalEl) {
        const addPricePackageModal = new bootstrap.Modal(addPricePackageModalEl);

        addPricePackageForm.addEventListener('submit', function (e) {
            e.preventDefault();

            if (addPricePackageForm.checkValidity()) {
                showNotification("Add price package successfully", "success");
                addPricePackageModal.hide();
                addPricePackageForm.reset();
            } else {
                addPricePackageForm.reportValidity();
            }
        });
    }
});
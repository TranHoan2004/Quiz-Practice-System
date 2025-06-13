
document.addEventListener('DOMContentLoaded', function () {
    // Toggle column visibility
    document.querySelectorAll('.column-toggle').forEach((checkbox) => {
        checkbox.addEventListener('change', function () {
            const columnClass = this.value;
            const header = document.querySelector(`th.${columnClass}`);
            const cells = document.querySelectorAll(`td.${columnClass}`);
            if (this.checked) {
                header.classList.remove('d-none');
                cells.forEach((cell) => cell.classList.remove('d-none'));
            } else {
                header.classList.add('d-none');
                cells.forEach((cell) => cell.classList.add('d-none'));
            }
        });
    });

    // Preview media files
    document.getElementById('mediaInput').addEventListener('change', function () {
        const files = this.files;
        const previewArea = document.getElementById('previewArea');
        previewArea.innerHTML = '';

        if (!files || files.length === 0) return;

        Array.from(files).forEach((file, index) => {
            const url = URL.createObjectURL(file);
            const wrapper = document.createElement('div');
            wrapper.classList.add('mb-3');

            let mediaElement;
            if (file.type.startsWith('image/')) {
                mediaElement = document.createElement('img');
                mediaElement.src = url;
                mediaElement.style.maxWidth = '200px';
                mediaElement.classList.add('d-block', 'mb-2');
            } else if (file.type.startsWith('video/')) {
                mediaElement = document.createElement('video');
                mediaElement.src = url;
                mediaElement.controls = true;
                mediaElement.style.maxWidth = '200px';
                mediaElement.classList.add('d-block', 'mb-2');
            } else {
                wrapper.textContent = 'Unsupported file type';
                previewArea.appendChild(wrapper);
                return;
            }

            const captionInput = document.createElement('input');
            captionInput.type = 'text';
            captionInput.name = `caption_${index}`;
            captionInput.placeholder = 'Enter caption for this media';
            captionInput.classList.add('form-control');

            wrapper.appendChild(mediaElement);
            wrapper.appendChild(captionInput);
            previewArea.appendChild(wrapper);
        });
    });

    // Preview media files
    document.getElementById('editMediaInput').addEventListener('change', function () {
        const files = this.files;
        const previewArea = document.getElementById('editPreviewArea');
        previewArea.innerHTML = '';

        if (!files || files.length === 0) return;

        Array.from(files).forEach((file, index) => {
            const url = URL.createObjectURL(file);
            const wrapper = document.createElement('div');
            wrapper.classList.add('mb-3');

            let mediaElement;
            if (file.type.startsWith('image/')) {
                mediaElement = document.createElement('img');
                mediaElement.src = url;
                mediaElement.style.maxWidth = '200px';
                mediaElement.classList.add('d-block', 'mb-2');
            } else if (file.type.startsWith('video/')) {
                mediaElement = document.createElement('video');
                mediaElement.src = url;
                mediaElement.controls = true;
                mediaElement.style.maxWidth = '200px';
                mediaElement.classList.add('d-block', 'mb-2');
            } else {
                wrapper.textContent = 'Unsupported file type';
                previewArea.appendChild(wrapper);
                return;
            }

            const captionInput = document.createElement('input');
            captionInput.type = 'text';
            captionInput.name = `caption_${index}`;
            captionInput.placeholder = 'Enter caption for this media';
            captionInput.classList.add('form-control');

            wrapper.appendChild(mediaElement);
            wrapper.appendChild(captionInput);
            previewArea.appendChild(wrapper);
        });
    });
});
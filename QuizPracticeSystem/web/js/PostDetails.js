
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

        if (!files || files.length === 0)
            return;

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

        if (!files || files.length === 0)
            return;

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

function isVideoFile(url) {
    const videoExtensions = ['.mp4', '.mov', '.mkv', '.avi'];
    return videoExtensions.some(ext => url.toLowerCase().endsWith(ext));
}

document.addEventListener('DOMContentLoaded', function () {
    const viewButtons = document.querySelectorAll('.btn-view');

    viewButtons.forEach(button => {
        button.addEventListener('click', function () {
            document.getElementById('viewTitle').textContent = this.dataset.title;
            document.getElementById('viewCategory').textContent = this.dataset.category;
            document.getElementById('viewBriefInfo').textContent = this.dataset.brief;
            document.getElementById('viewDescription').textContent = this.dataset.content;

            document.querySelector('#viewFeaturing span').textContent = this.dataset.featuring;
            document.querySelector('#viewStatus span').textContent = this.dataset.status;
            document.querySelector('#viewCreatedDate span').textContent = this.dataset.date;

            // --- Media List ---
            const mediaContainer = document.getElementById('viewMediaContainer');
            mediaContainer.innerHTML = '';

            try {
                const mediaList = JSON.parse(this.dataset.blogMedia);

                const createMediaElement = (tagName, mediaUrl) => {
                    const media = document.createElement(tagName);
                    media.src = `${pageContext.request.contextPath}/` + mediaUrl;
                    media.width = 250;
                    media.classList.add('me-2', 'mb-2');
                    if (tagName === 'video') {
                        media.controls = true;
                        media.preload = 'metadata';
                    }
                    return media;
                };

                mediaList.forEach(({ mediaType, mediaUrl, caption }) => {
                    const tag = mediaType === 'image' ? 'img' : 'video';
                    const mediaEl = createMediaElement(tag, mediaUrl);
                    mediaContainer.appendChild(mediaEl);

                    const captionEl = document.createElement('p');
                    captionEl.textContent = caption;
                    captionEl.classList.add('text-center');
                    mediaContainer.appendChild(captionEl);
                });

            } catch (e) {
                console.error('Lỗi khi parse JSON blogMedia:', e);
            }
        });
    });

    const editButtons = document.querySelectorAll('.btn-edit');

    editButtons.forEach(button => {
        button.addEventListener('click', function () {
            document.getElementById('editPostId').value = this.dataset.id;
            document.getElementById('editTitle').value = this.dataset.title;
            document.getElementById('editBriefInfo').value = this.dataset.brief;
            document.getElementById('editDescription').value = this.dataset.content;

            const categoryId = this.dataset.category; // ví dụ: "2"
            const selectElement = document.getElementById('editCategory');
            console.log("Setting category:", categoryId);
            Array.from(selectElement.options).forEach(option => {
                option.selected = option.value === categoryId;
            });

            document.getElementById('editStatus').checked = this.dataset.status === 'true';

            document.getElementById('editFeaturing').checked = this.dataset.featuring === 'true';

            const mediaContainer = document.getElementById('editMediaContainer');
            if (mediaContainer) {
                mediaContainer.innerHTML = '';

                try {
                    const mediaList = JSON.parse(this.dataset.blogMedia);

                    mediaList.forEach(({ mediaType, mediaUrl, caption }) => {
                        const fullUrl = `${pageContext.request.contextPath}/` + mediaUrl;

                        let mediaEl;
                        if (mediaType === 'image') {
                            mediaEl = document.createElement('img');
                            mediaEl.src = fullUrl;
                            mediaEl.width = 250;
                        } else if (mediaType === 'video') {
                            mediaEl = document.createElement('video');
                            mediaEl.src = fullUrl;
                            mediaEl.width = 250;
                            mediaEl.controls = true;
                            mediaEl.preload = 'metadata';
                        }

                        if (mediaEl) {
                            mediaEl.classList.add('me-2', 'mb-2');
                            mediaContainer.appendChild(mediaEl);

                            const captionEl = document.createElement('p');
                            captionEl.textContent = caption;
                            captionEl.classList.add('text-center');
                            mediaContainer.appendChild(captionEl);
                    }
                    });

                } catch (e) {
                    console.error('Lỗi khi parse JSON blogMedia:', e);
                }
            }
        });
    });

});

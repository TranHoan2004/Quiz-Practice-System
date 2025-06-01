function previewImage() {
    const fileInput = document.getElementById('imageInput');
    const profileImg = document.getElementById('profileImg');

    const file = fileInput.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            profileImg.src = e.target.result;
        };
        reader.readAsDataURL(file);
    } else {
        alert("Vui lòng chọn một hình ảnh trước khi gửi!");
    }
}
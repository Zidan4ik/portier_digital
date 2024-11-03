let fileImage = null;
const deleteFileBtn = document.getElementById('deleteFileBtn_');
const selectFileBtn = document.getElementById('selectFileBtn_');
const fileInput = document.getElementById('fileInput');
const fileImageElement = document.getElementById('fileImage_');

if (deleteFileBtn && selectFileBtn && fileInput && fileImageElement) {
    document.getElementById('deleteFileBtn_').addEventListener('click', function () {
        document.getElementById('fileInput').value = null;
        document.getElementById('fileImage_').src = path_to_image = path_default_image;
        fileImage = null;
    });

    document.getElementById('selectFileBtn_').addEventListener('click', function () {
        document.getElementById("fileInput").click();
    });

    document.getElementById('fileInput').addEventListener('change', function (event) {
        const file = event.target.files[0];
        if (file) {
            const imgElement = document.getElementById('fileImage_');
            imgElement.src = URL.createObjectURL(file);
            fileImage = file;
        }
    });
}
document.getElementById('deleteImage_').addEventListener('click', function () {
    document.getElementById('imageInput').value = null;
    document.getElementById('fileImage_').src = path_to_image = path_default_image;
    fileImage = null;
});
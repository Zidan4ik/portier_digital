let fileImage = null;
let path_default_image = '/uploads/default-images/default-image.jpg';
document.getElementById('selectImage_').addEventListener('click', function () {
    document.getElementById("imageInput").click();
});

document.getElementById('imageInput').addEventListener('change', function (event) {
    const file = event.target.files[0];
    if (file) {
        const imgElement = document.getElementById('fileImage_');
        imgElement.src = URL.createObjectURL(file);
        fileImage = file;
    }
});


function invokeCreateEntityModal() {
    let modal = new bootstrap.Modal(document.querySelector(".modalEntity_"));

    document.getElementById('modal-title_').innerText = 'Add new article';
    document.getElementById('submit-article_').innerText = 'Add';

    document.getElementById('title_').value = null;
    document.getElementById('description_').value = null;
    document.getElementById('fileImage_').src = path_to_image = path_default_image;

    document.getElementById('submit-article_').onclick = function () {
        requestSaveEntity(pathForSavingEntity + `add`);
    };
    modal.show();
}

function invokeEditEntityModal(id, title, description, pathToImage) {
    let modal = new bootstrap.Modal(document.querySelector(".modalEntity_"));

    document.getElementById('modal-title_').innerText = 'Update article';
    document.getElementById('submit-article_').innerText = 'Update';

    document.getElementById('title_').value = title;
    document.getElementById('description_').value = description;
    path_to_image = pathToImage != null ? pathToImage : path_default_image;
    document.getElementById('fileImage_').src = path_to_image;

    document.getElementById('submit-article_').onclick = function () {
        requestSaveEntity(pathForSavingEntity + id + `/edit`);
    };
    modal.show();
}
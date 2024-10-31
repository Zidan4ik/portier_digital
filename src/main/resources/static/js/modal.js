let fileImage = null;

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

document.getElementById('deleteImage_').addEventListener('click', function () {
    document.getElementById('imageInput').value = null;
    document.getElementById('fileImage_').src = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvf9wn1WvKWCp2eCV0atTl56ONzL6TyTPh702UMXqeHag2ZUG0YPch6-XWd2o4S_dK1J4&usqp=CAU";
    fileImage = null;
});

function createEntityModal() {
    let modal = new bootstrap.Modal(document.querySelector(".modalEntity_"));
    document.getElementById('title_').value = null;
    document.getElementById('description_').value = null;
    document.getElementById('fileImage_').src =
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvf9wn1WvKWCp2eCV0atTl56ONzL6TyTPh702UMXqeHag2ZUG0YPch6-XWd2o4S_dK1J4&usqp=CAU';
    modal.show();
}

function editEntityModal(title, description, pathToImage) {
    let modal = new bootstrap.Modal(document.querySelector(".modalEntity_"));
    document.getElementById('title_').value = title;
    document.getElementById('description_').value = description;
    document.getElementById('fileImage_').src = pathToImage != null ? pathToImage : 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvf9wn1WvKWCp2eCV0atTl56ONzL6TyTPh702UMXqeHag2ZUG0YPch6-XWd2o4S_dK1J4&usqp=CAU';
    modal.show();
}
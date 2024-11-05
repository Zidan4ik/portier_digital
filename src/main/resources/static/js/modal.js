let path_default_image = `${contextPath.substring(0,contextPath.indexOf('/',2))}/uploads/default-images/default-image.jpg`;
let name_entity = '';

function invokeCreateModal() {
    let modal = new bootstrap.Modal(document.querySelector(".modalEntity_"));
    const filterElements = $('.fields-entity');
    filterElements.map(function () {
        document.getElementById(this.name + '_').value = null;
    });

    document.getElementById('modal-title_').innerText = `Add new ${name_entity}`;
    document.getElementById('submit-modal_').innerText = 'Add';

    let image_container = document.getElementById('fileImage_');
    if (image_container) {
        image_container.src = path_to_image = path_default_image;
    }

    document.getElementById('submit-modal_').onclick = function () {
        requestSaveEntity(pathForSavingEntity + `add`);
    };
    modal.show();
}

function invokeEditModal(id, data) {
    let modal = new bootstrap.Modal(document.querySelector(".modalEntity_"));
    const filterElements = $('.fields-entity');

    filterElements.map(function () {
        const fieldName = $(this).attr('name');
        if (data.hasOwnProperty(fieldName)) {
            document.getElementById(this.name + '_').value = data[fieldName];
        }
    });

    document.getElementById('modal-title_').innerText = `Update ${name_entity}`;
    document.getElementById('submit-modal_').innerText = 'Update';
    path_to_image = data.pathToImage != null ? contextPath.substring(0,contextPath.indexOf('/',2)) + data.pathToImage : path_default_image;
    let image_container = document.getElementById('fileImage_');
    if (image_container) {
        image_container.src = path_to_image;
    }
    document.getElementById('submit-modal_').onclick = function () {
        requestSaveEntity(pathForSavingEntity + id + `/edit`);
    };
    modal.show();
}

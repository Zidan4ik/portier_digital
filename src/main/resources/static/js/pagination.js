let context_full_path = window.location.pathname;
let content = [];
let currentMetaData = {
    page: null,
    size: null,
    totalElements: null,
    totalPages: null
};

function generatePageSequence(currentPage, totalPages) {
    const startPage = currentPage > 2 ? currentPage - 2 : 0;
    const endPage = currentPage + 2 < totalPages ? currentPage + 2 : totalPages - 1;
    const pageSequence = [];
    for (let i = startPage; i <= endPage; i++) {
        pageSequence.push(i);
    }
    return pageSequence;
}

function getFilters(filters) {
    let filters_ = {};
    filters.each(function () {
        const key = $(this).attr('name');
        const value = $(this).val();
        if (key) {
            filters_[key] = value;
        }
    });
    return filters_;
}

function getPageWithFilter(page, size) {
    const filterElements = $('.for-filter');
    const filters = getFilters(filterElements);
    console.log(filters);
    $.ajax({
        type: "GET",
        url: context_full_path + '/data',
        data: {
            page: page,
            size: size,
            ...filters
        },
        success: function (data) {
            console.log(data);
            content = data.content;
            currentMetaData = data.metadata;
            const containerTable = document.getElementById("table-data-container_");
            const containerPagination = document.getElementById("pagination-container_");
            const containerPickSize = document.getElementById("pick-size-for-pagination_");
            containerPickSize.value = currentMetaData.size;
            containerTable.innerHTML = '';
            containerPagination.innerHTML = '';
            if (content.length > 0) {
                content.forEach(function (element) {
                    containerTable.innerHTML += getRowData(element);
                });
                containerPagination.innerHTML = getPagination(currentMetaData.page, currentMetaData.totalPages, currentMetaData.size);
                let firstElement = document.querySelector(".first");
                let prevElement = document.querySelector(".prev");
                let nextElement = document.querySelector(".next");
                let lastElement = document.querySelector(".last");

                if (currentMetaData.page === 0) {
                    firstElement.classList.add('disabled');
                    prevElement.classList.add('disabled');
                    firstElement.style.cursor = 'default';
                    prevElement.style.cursor = 'default';
                }
                if (currentMetaData.page === currentMetaData.totalPages - 1) {
                    nextElement.classList.add('disabled');
                    lastElement.classList.add('disabled');
                    nextElement.style.cursor = 'default';
                    lastElement.style.cursor = 'default';
                }
                createSequence(currentMetaData.page, currentMetaData.totalPages, currentMetaData.size);
            } else {
                const fields = document.querySelectorAll('.fields-entity');
                console.log(fields.length + 2);
                containerTable.innerHTML = `<tr>
                                <td colspan="${fields.length + 2}" style="text-align: center; vertical-align: middle;">
                                No matching records found</td>
                                </tr>`
            }
            updateEntries(currentMetaData.totalElements, currentMetaData.size, currentMetaData.page);
        }
    });
}

function createSequence(currentPage, totalPages, size) {
    const sequence = generatePageSequence(currentPage, totalPages);
    const containerSequence = document.getElementById('container-sequence_');
    sequence.forEach(i => {
        const li = document.createElement('li');
        li.className = 'page-item';
        li.style.display = 'flex';
        li.style.justifyContent = 'center';
        const a = document.createElement('a');
        a.className = 'page-link';
        a.onclick = function () {
            getPageWithFilter(i, size);
        }
        a.textContent = i + 1;
        a.style.display = 'block';
        a.style.width = '100%';
        a.style.textAlign = 'center';
        a.style.cursor = 'pointer';
        if (currentPage === i) {
            a.classList.add('active');
        }
        li.appendChild(a);
        containerSequence.appendChild(li);
    });
}

function getPagination(currentPage, totalPages, size) {
    return `
                            <ul class="pagination">
                                    <li class="page-item first" style="cursor: pointer;">
                                        <a class="page-link"
                                           onclick="getPageWithFilter(${0},${size})"><i
                                                class="ti ti-chevrons-left ti-sm"></i></a>
                                    </li>
                                    
                                    <li class="page-item prev" style="margin-right: 3px;cursor: pointer;">
                                        <a class="page-link"
                                           onclick="getPageWithFilter(${currentPage - 1},${size})"><i
                                                class="ti ti-chevron-left ti-sm"></i></a>
                                    </li>
                                    
                                    <ul id="container-sequence_" class="pagination"></ul>
                                    
                                    <li class="page-item next" style="cursor: pointer;">
                                        <a class="page-link"
                                           onclick="getPageWithFilter(${currentPage + 1},${size})"><i
                                                class="ti ti-chevron-right ti-sm"></i></a>
                                    </li>

                                    <li class="page-item last" style="cursor: pointer;">
                                        <a class="page-link"
                                          onclick="getPageWithFilter(${totalPages - 1},${size})"><i
                                                class="ti ti-chevrons-right ti-sm"></i></a>
                                    </li>
                                </ul>
        `;
}

function getRowData(element) {
    let row = '<tr>';

    Object.keys(element).forEach(field => {
        const value = element[field] !== null ? element[field] : '';
        row += `<td class="divided-text">${value}</td>`;
    });

    row += `
        <td>
            <div class="text-center" style="width: 200px;">
                <button onclick="requestEditData(${element.id})" class="btn btn-sm btn-github" type="button">
                    <i class="ti ti-pencil"></i>
                </button>
                <button onclick="requestDelete(${element.id})" class="btn btn-sm btn-github" type="button">
                    <i class="ti ti-trash"></i>
                </button>
            </div>
        </td>
    `;

    row += '</tr>';
    return row;
}

function handleInputChange(input) {
    if (input.name === 'id') {
        if (/[^0-9]/.test(input.value)) {
            return;
        }
    }
    getPageWithFilter(currentMetaData.page, currentMetaData.size);
}

document.getElementById('pick-size-for-pagination_').addEventListener('change', function () {
    getPageWithFilter(0, this.value);
});

function getPageStart(pageSize, pageNr) {
    return pageSize * pageNr;
}

function updateEntries(total, pageSize, pageNr) {
    if (total === 0) {
        document.getElementById("showing-elements_").textContent =
            `Showing 0 to 0 of 0 entries`;
    } else {
        const start = Math.max(getPageStart(pageSize, pageNr), 0);
        const end = Math.min(getPageStart(pageSize, pageNr + 1), total);
        document.getElementById("showing-elements_").textContent =
            `Showing ${start + 1} to ${end} of ${total} entries`;
    }
}
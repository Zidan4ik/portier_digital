function modalForRegistration() {
    if ($('#modalForRegistration').html())
        $('#modalForRegistration').remove()
    var modalBlock = document.createElement('div');
    modalBlock.innerHTML = `
        <div class="modal fade" id="modalForRegistration" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Registry</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="card">
                            <div class="card-content p-3">
                                <div class="row">
                                    <div class="col-lg-6 col-sm-12">
                                        Name
                                        <input id="firstName" class="form-control" placeholder="enter your name">
                                    </div>
                                    <div class="col-lg-6 col-sm-12">
                                        E-mail
                                        <input id="email" class="form-control" placeholder="enter your e-mail">
                                    </div>
                                </div>               
                                <div class="row">
                                    <div class="col-lg-6 col-sm-12">
                                        Password
                                        <div class="form-password-toggle">
                                            <div id="password" class="input-group input-group-merge">
                                                <input type="password" class="form-control" id="password-value" 
                                                placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;"
                                                 aria-describedby="basic-default-password2" />
                                                <span id="basic-default-password2" class="input-group-text cursor-pointer"><i id="eye" class="ti ti-eye-off"></i></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-6 col-sm-12">
                                        Repeat password
                                        <input id="passwordRepeat" placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;" type="password" class="form-control">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                          <div class="col-lg-12 col-sm-12">
                                <button class="float-end btn btn-primary mt-4" onclick="registration()">Зареєструватись</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `
    document.body.appendChild(modalBlock)
    $('#modalForRegistration').modal('show');

    const passwordInput = $('#password-value');
    const eye = $('#eye');

    eye.on('click', function () {
        if (passwordInput.attr('type') === 'password') {
            passwordInput.attr('type', 'text')
            eye.removeClass('ti-eye-off').addClass('ti-eye')
        } else if (passwordInput.attr('type') === 'text') {
            passwordInput.attr('type', 'password')
            eye.removeClass('ti-eye').addClass('ti-eye-off')
        }
    });
}

function registration() {
    let formData = new FormData()
    formData.append("firstName", $("#firstName").val())
    formData.append("email", $("#email").val())
    formData.append("password", $("#password-value").val())
    formData.append("passwordRepeat", $("#passwordRepeat").val())

    $.ajax({
        url: contextPath + 'registration',
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function () {
            window.location.href = contextPath + 'login';
        },
        error: function (xhr, status, error) {
            if (xhr.status === 400) {
                validDataFromResponse(xhr.responseJSON);
                const errors = xhr.responseJSON;
                console.log(errors);
                for (const field in errors) {
                    if (errors.hasOwnProperty(field)) {
                        console.log("Поле: " + field + ", Помилка: " + errors[field]);
                    }
                }
            }else if(xhr.status === 409){
                cleanInputs();
                addText($('#email'), "User with this email already exist");
                $('#email').css("border", "1px solid #ff0000");
            }
            else {
                console.error('Помилка відправки файлів на сервер:', error);
            }
        }
    });

    function cleanInputs() {
        $('.text-for-validating').remove()
        var elements = document.querySelectorAll('input, select, textarea, button, .ql-editor,form');
        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];
            element.style.borderColor = '';
        }
        var select2Selects = document.querySelectorAll('.select2-selection');
        for (var i = 0; i < select2Selects.length; i++) {
            var select2Select = select2Selects[i];
            select2Select.style.borderColor = '';
        }
        $("#goal").css("border", "")
    }

    var countError = 0;

    function validDataFromResponse(errors) {
        cleanInputs();
        for (var fieldName in errors) {
            if (errors.hasOwnProperty(fieldName)) {
                var errorMessage = errors[fieldName];
                scrollToElement($('#' + fieldName.toString()));
                addText($('#' + fieldName.toString()), errorMessage)
                $('#' + fieldName.toString()).css("border", "1px solid #ff0000")
            }
        }
        countError = 0
    }

    function addText(input, message) {
        var icon = $('<p class="text-for-validating" style="color: #ff0000;">' + message + '</p>')
        icon.tooltip({
            content: message,
            position: {my: "left+15 center", at: "right center"}
        })
        input.after(icon);
        input.css("border-color", "#ff0000")
    }

    function scrollToElement($element) {
        if (countError !== 0) return
        countError++
        if ($element.length > 0) {
            var windowHeight = $(window).height();
            var targetOffset = $element.offset().top - windowHeight / 4;

            $('html, body').animate({
                scrollTop: targetOffset
            }, 100);
        }
    }
}
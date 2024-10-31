const iconMenu = document.querySelector('.menu__icon');
const body = document.body;
body.classList.remove('no-scroll');
if (iconMenu) {
    const menuBody = document.querySelector('.header__menu');
    iconMenu.addEventListener("click", function (e) {
        iconMenu.classList.toggle('active');
        menuBody.classList.toggle('active');

        // Добавляем/удаляем класс на body только при активированном меню
        if ( iconMenu.classList.contains('active')) {
            body.classList.add('no-scroll'); // Добавляем класс, если меню открыто
        } else {
            body.classList.remove('no-scroll'); // Удаляем класс, если меню закрыто
        }
    });
}


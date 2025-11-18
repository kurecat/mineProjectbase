document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('signup-form-element');
    if (form) {
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const passwordCheck = document.getElementById('password-check');
    const nickname = document.getElementById('nickname');

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{4,16}$/;
        const usernameRegex = /^[가-힣a-zA-Z0-9_-]{2,16}$/;


        form.addEventListener('submit', function(event) {
    event.preventDefault();

    const isEmailValid = validateEmail();
    const isPasswordValid = validatePassword();
    const isPasswordCheckValid = validatePasswordCheck();
    const isUsernameValid = validateUsername();

    if (isEmailValid && isPasswordValid && isPasswordCheckValid && isUsernameValid) {
    form.submit();
} else {
    alert('입력 정보를 다시 확인해주세요.');
}
});

    email.addEventListener('input', validateEmail);
    password.addEventListener('input', validatePassword);
    passwordCheck.addEventListener('input', validatePasswordCheck);
    nickname.addEventListener('input', validateUsername);

    function validateEmail() {
    if (email.value === '') {
    showError(email, 'email-error', '필수 입력 항목입니다.');
    return false;
} else if (!emailRegex.test(email.value)) {
    showError(email, 'email-error', '유효한 이메일 주소를 입력해주세요.');
    return false;
} else {
    showSuccess(email, 'email-error');
    return true;
}
}

    function validatePassword() {
    if (password.value === '') {
    showError(password, 'password-error', '필수 입력 항목입니다.');
    return false;
} else if (!passwordRegex.test(password.value)) {
    showError(password, 'password-error', '4~16자, 문자·숫자·특수문자(!@#$%^&*)를 포함해야 합니다.');
    return false;
} else {
    showSuccess(password, 'password-error');
    validatePasswordCheck();
    return true;
}
}

    function validatePasswordCheck() {
    if (passwordCheck.value === '') {
    showError(passwordCheck, 'password-check-error', '필수 입력 항목입니다.');
    return false;
} else if (password.value !== passwordCheck.value) {
    showError(passwordCheck, 'password-check-error', '비밀번호가 일치하지 않습니다.');
    return false;
} else {
    showSuccess(passwordCheck, 'password-check-error');
    return true;
}
}

    function validateUsername() {
    if (nickname.value === '') {
    showError(nickname, 'nickname-error', '필수 입력 항목입니다.');
    return false;
} else if (!usernameRegex.test(nickname.value)) {
    showError(nickname, 'nickname-error', '2~16자의 한글, 영문자, 숫자, 밑줄(_), 하이픈(-)만 사용 가능합니다.');
    return false;
} else {
    showSuccess(nickname, 'nickname-error');
    return true;
}
}

    function showError(input, errorElementId, message) {
    const errorElement = document.getElementById(errorElementId);
    input.classList.add('signup-form__input--error');
    input.classList.remove('signup-form__input--success');
    errorElement.textContent = message;
    errorElement.style.display = 'block';
}

    function showSuccess(input, errorElementId) {
    const errorElement = document.getElementById(errorElementId);
    input.classList.remove('signup-form__input--error');
    input.classList.add('signup-form__input--success');
    errorElement.style.display = 'none';
}
}
});

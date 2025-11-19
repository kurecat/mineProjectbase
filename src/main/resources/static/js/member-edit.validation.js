document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("edit-fields-section");
  if (form) {
    const password = document.getElementById("edit-password");
    const passwordCheck = document.getElementById("password-check");
    const nickname = document.getElementById("edit-username");

    const passwordRegex =
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{4,16}$/;
    const usernameRegex = /^[가-힣a-zA-Z0-9_-]{2,16}$/;

    form.addEventListener("submit", function (event) {
      // 어떤 버튼으로 제출됐는지 확인
      const submitter = event.submitter;

      // Delete 버튼이면 검사 없이 통과
      if (submitter && submitter.id === "delete-button") {
        return; // 유효성 검사 생략
      }

      event.preventDefault();

      const isPasswordValid = validatePassword();
      const isPasswordCheckValid = validatePasswordCheck();
      const isUsernameValid = validateUsername();

      if (isPasswordValid && isPasswordCheckValid && isUsernameValid) {
        form.submit();
      } else {
        alert("입력 정보를 다시 확인해주세요.");
      }
    });

    password.addEventListener("input", validatePassword);
    password.addEventListener("input", validatePasswordCheck);
    passwordCheck.addEventListener("input", validatePasswordCheck);
    passwordCheck.addEventListener("input", validatePassword);
    nickname.addEventListener("input", validateUsername);

    validatePassword();
    validatePasswordCheck();
    validateUsername();

    function validatePassword() {
      if (password.value === "") {
        showSuccess(password, "password-error");
        return true;
      } else if (!passwordRegex.test(password.value)) {
        showError(
          password,
          "password-error",
          "4~16자, 문자·숫자·특수문자(!@#$%^&*)를 포함해야 합니다."
        );
        return false;
      } else {
        showSuccess(password, "password-error");
        validatePasswordCheck();
        return true;
      }
    }

    function validatePasswordCheck() {
      if (password.value !== passwordCheck.value) {
        showError(
          passwordCheck,
          "password-check-error",
          "비밀번호가 일치하지 않습니다."
        );
        return false;
      } else {
        showSuccess(passwordCheck, "password-check-error");
        return true;
      }
    }

    function validateUsername() {
      if (nickname.value === "") {
        showSuccess(nickname, "nickname-error");
        return true;
      } else if (!usernameRegex.test(nickname.value)) {
        showError(
          nickname,
          "nickname-error",
          "2~16자의 한글, 영문자, 숫자, 밑줄(_), 하이픈(-)만 사용 가능합니다."
        );
        return false;
      } else {
        showSuccess(nickname, "nickname-error");
        return true;
      }
    }

    function showError(input, errorElementId, message) {
      const errorElement = document.getElementById(errorElementId);
      input.classList.add("signup-form__input--error");
      input.classList.remove("signup-form__input--success");
      errorElement.textContent = message;
      errorElement.style.display = "block";
    }

    function showSuccess(input, errorElementId) {
      const errorElement = document.getElementById(errorElementId);
      input.classList.remove("signup-form__input--error");
      input.classList.add("signup-form__input--success");
      errorElement.style.display = "none";
    }
  }
});

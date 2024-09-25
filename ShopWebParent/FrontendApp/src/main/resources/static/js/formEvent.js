const inputNameRegister = document.querySelector('.input-name')
const inputEmailRegister = document.querySelector('.input-email')
const inputPasswordRegister = document.querySelector('.input-password')
const inputConfirmRegister = document.querySelector('.input-confirm-password')

const listInputField = [ inputNameRegister, inputEmailRegister, inputPasswordRegister, inputConfirmRegister]

function validateForm(){
    listInputField.forEach(element => {
        blurCheck("blur", element);
        onInputCheck("input", element);
    })

    checkLengthPassword(inputPasswordRegister);
    checkMatchPassword(inputPasswordRegister, inputConfirmRegister);
    checkRegexMail(inputEmailRegister)
}

if (inputEmailRegister && inputNameRegister && inputEmailRegister && inputPasswordRegister && inputConfirmRegister){
    validateForm();
}

function checkRegexMail(input){
    input.addEventListener("blur", () => {
        const mailValue = input.value;
        const isGmail = mailValue.match(/^[a-zA-Z0-9.]+(@gmail.com)$/g)
        if(isGmail == null && mailValue.length > 0){
            createErrorElement("Vui lòng nhập đúng định dạng '@gmail.com'", input)
        }
    });
}

function blurCheck(event, input){
    input.addEventListener(event, function(){
        removeErrorElementExist(this);
        if(this.value.trim() == ''){
            createErrorElement("Dữ liệu không được bỏ trống!", this)
        }
    })
}

function onInputCheck(event, input){
    input.addEventListener(event, function(){
        removeErrorElementExist(this);
    })
}

function checkLengthPassword(input){
    input.addEventListener("input", function(){
        removeErrorElementExist(this);
        if(this.value.length < 8){
            createErrorElement("Mật khẩu phải có ít nhất 8 ký tự!", this)
        }
    })
}

function removeErrorElementExist(input){
    let existErrorElement = input.parentNode.querySelector('.error');
    if(existErrorElement){
        input.parentNode.removeChild(existErrorElement);
    }
}

function createErrorElement(errorMessage, input){
    let errorElement = document.createElement("p")
    errorElement.classList.add('error')
    errorElement.textContent = errorMessage
    input.insertAdjacentElement("afterend", errorElement)
}

function checkMatchPassword(inputPassword, inputConfirm){
    inputPassword.addEventListener('blur', function(){
        removeErrorElementExist(inputConfirm)
        removeErrorElementExist(inputPassword)

        if(inputPassword.value.length < 8){
            createErrorElement("Mật khẩu phải có ít nhất 8 ký tự!", inputPassword)
        }

        if(inputConfirm.value != ''){
            if(inputPassword.value !== inputConfirm.value){
                createErrorElement("Mật khẩu không khớp!", inputConfirm)
            }
        }
    })

    inputConfirm.addEventListener("blur", function(){
        removeErrorElementExist(inputConfirm)
        if(inputPassword.value !== inputConfirm.value && inputPassword.value.length > 0){
            createErrorElement("Mật khẩu không khớp!", inputConfirm)
        }
    })
}

if(document.querySelector(".logout-link")){
    document.querySelector(".logout-link").addEventListener("click", function(e){
        e.preventDefault()
        document.querySelector("#form-logout").submit()
    })
}

if(document.querySelector(".icon-back")){
    document.querySelector(".icon-back").addEventListener("click", function(){
        window.history.back();
    })
}

if(document.querySelector(".form-register")){
    document.querySelector(".form-register").addEventListener("submit", function(e){
        if(document.querySelector(".error")){
            e.preventDefault()
        }else{
            this.submit()
        }
    })
}

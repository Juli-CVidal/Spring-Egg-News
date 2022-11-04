let signUpButton;
let signInButton;
let signUpPassword;
let signInPassword;
let container;
let signUpForm;
let signInForm;
let signUpToggle;
let signInToggle;
let confirm;

const addStyles = () => {
    signUpButton.addEventListener('click', () => {
	container.classList.add("right-panel-active");
        setTimeout(document.getElementById("signInForm").reset(), 500);
        setTimeout(signInForm.classList.remove("was-validated"),500);
    });

    signInButton.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
        setTimeout(document.getElementById("signUpForm").reset(), 500);
        setTimeout(signUpForm.classList.remove("was-validated"),500);
    });
};

const addValidation = (form) => {
    form.addEventListener("submit", (event) => {
    signUpPassword.setCustomValidity(
        signUpPassword.value.length < 8 ? "password too short" : ""
    );
    
    confirm.setCustomValidity(
        confirm.value != signUpPassword.value ? "passwords doesn't match" : ""
    );
 
    if (!form.checkValidity()) {
      event.preventDefault();
      event.stopPropagation();
    }

    form.classList.add("was-validated");
  });
};

const addToggles = () => {
    signUpToggle.addEventListener("click", () => {
	signUpPassword.setAttribute("type", signUpPassword.type === "password" ? "text": "password");
    })

    signInToggle.addEventListener("click", () => {
	signInPassword.setAttribute("type", signInPassword.type === "password" ? "text" : "password");
    });
}


const getAll = () => {
  container = document.getElementById('container');
  confirm = document.getElementById("confirm");

   signUpForm = document.querySelector("#signUpForm");
   signInForm = document.querySelector("#signInForm");
  
  signUpButton = document.getElementById('signUpBtn');
  signInButton = document.getElementById('signInBtn');
 
  signUpPassword = document.getElementById("signUpPassword");
  signUpToggle =  document.getElementById("signUpToggle");
  
  signInPassword = document.getElementById("signInPassword");
  signInToggle =  document.getElementById("signInToggle");
}


const init = () => {
  getAll();
  addToggles();
  addStyles();
  addValidation(signUpForm);
  addValidation(signInForm);
};

window.onload = init;

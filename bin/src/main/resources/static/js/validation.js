let form;


const addValidation = () => {
  form.addEventListener("submit", (event) => {

    if (!form.checkValidity()) {
      event.preventDefault();
      event.stopPropagation();
    }

    form.classList.add("was-validated");
  });
};

const init = () => {
  form = document.querySelector(".needs-validation");
  addValidation();
};

window.onload = init;

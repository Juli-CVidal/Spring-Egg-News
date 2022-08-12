const addValidation = (form) => {
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    if (!form.checkValidity()) {
      event.stopPropagation();
    } else {
      form.submit();
    }

    form.classList.add("was-validated");
  });
};

const init = () => {
  form = document.querySelector(".needs-validation");
  addValidation(form);
};

window.onload = init;

let form;

const addInvalidStyle = () => {
  var css = "input:invalid {border-color: red}";
  var style = document.createElement("style");

  style.styleSheet
    ? (style.styleSheet.cssText = css)
    : style.appendChild(document.createTextNode(css));

  document.getElementsByTagName("head")[0].appendChild(style);
};

const addStyles = () => {
  document.querySelectorAll(".js-input").forEach((element) => {
    element.addEventListener("blur", (event) => {
      const nextSibling = event.target.nextElementSibling;
      event.target.value != ""
        ? nextSibling.classList.add("filled")
        : nextSibling.classList.remove("filled");
    });
  });
};

const addValidation = () => {
  form.addEventListener("submit", (event) => {
    if (!form.checkValidity()) {
      event.preventDefault();
      event.stopPropagation();
      addInvalidStyle();
    }

    form.classList.add("was-validated");
  });
};

const init = () => {
  form = document.querySelector(".needs-validation");
  addValidation();
  addStyles();
};

window.onload = init;

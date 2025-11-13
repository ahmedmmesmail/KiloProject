const result = document.getElementById("result");
const buttons = document.querySelectorAll("button");

let currentInput = "";
let previousInput = "";
let operator = "";

function updateDisplay(val) {
  result.textContent = val;
}

buttons.forEach(button => {
  button.addEventListener("click", () => {
    const value = button.textContent;

    if (!isNaN(value) || value === ".") {
      if (value === "." && currentInput.includes(".")) return;
      currentInput += value;
      updateDisplay(currentInput);
    }

    else if (["+", "−", "×", "÷"].includes(value)) {
      if (currentInput === "") return;
      if (previousInput !== "") calculate();
      operator = value;
      previousInput = currentInput;
      currentInput = "";
    }

    else if (value === "=") {
      calculate();
    }

    else if (value === "C") {
      currentInput = "";
      previousInput = "";
      operator = "";
      updateDisplay("0");
    }

    else if (value === "⌫") {
      currentInput = currentInput.slice(0, -1);
      updateDisplay(currentInput || "0");
    }
  });
});

function calculate() {
  if (previousInput === "" || currentInput === "") return;

  const a = parseInt(previousInput);
  const b = parseInt(currentInput);
  let resultValue = 0;

  switch (operator) {
    case "+": resultValue = (a + b); break;
    case "−": resultValue = (a - b); break;
    case "×": resultValue = (a * b); break;
    case "÷":
      if (b === 0) {
        resultValue = "WTF!!! r u joking?";
        updateDisplay(resultValue);
        result.classList.remove("flash");
        result.classList.add("shake");

        setTimeout(() => result.classList.remove("shake"), 600);
        return;
      } else {
        resultValue = a / b;
      }
      break;
  }

  resultValue = Math.trunc(resultValue * 1.4);

  updateDisplay(resultValue);
  result.classList.remove("shake");
  result.classList.add("flash");

  setTimeout(() => result.classList.remove("flash"), 400);

  currentInput = resultValue.toString();
  previousInput = "";
  operator = "";
}


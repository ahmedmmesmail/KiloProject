// Set current year
document.getElementById('year').textContent = new Date().getFullYear();

// ========== Dark / Light Mode ==========
const toggle = document.getElementById("theme-toggle");
const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;

// Load saved theme or system default
const currentTheme = localStorage.getItem("theme") || (prefersDark ? "dark" : "light");
document.documentElement.setAttribute("data-theme", currentTheme);
toggle.textContent = currentTheme === "dark" ? "☀️" : "🌙";

// Toggle theme manually
toggle.addEventListener("click", () => {
  const newTheme = document.documentElement.getAttribute("data-theme") === "dark" ? "light" : "dark";
  document.documentElement.setAttribute("data-theme", newTheme);
  localStorage.setItem("theme", newTheme);
  toggle.textContent = newTheme === "dark" ? "☀️" : "🌙";
});
// ===== Moving car through entire page =====
const movingCar = document.querySelector("#moving-car img");

window.addEventListener("scroll", () => {
  const scrollY = window.scrollY;
  const docHeight = document.body.scrollHeight - window.innerHeight;

  // نسبة التقدم في السكرول من 0 إلى 1
  const scrollProgress = scrollY / docHeight;

  // العربية تتحرك لأعلى الشاشة حسب السكرول
  const translateY = -scrollProgress * (window.innerHeight - 200);

  // نضيف كمان شوية ميل بسيط (rotation)
  movingCar.style.transform = `translateY(${translateY}px) rotate(${scrollProgress * 10}deg)`;
});

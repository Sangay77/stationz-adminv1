function checkPasswordMatch(confirmPassword) {
    const password = document.getElementById("password").value;
    const msg = document.getElementById("passwordMismatchMsg");

    if (confirmPassword.value !== password) {
        confirmPassword.setCustomValidity("Passwords do not match!");
        if (msg) msg.style.display = "block";
    } else {
        confirmPassword.setCustomValidity("");
        if (msg) msg.style.display = "none";
    }
}

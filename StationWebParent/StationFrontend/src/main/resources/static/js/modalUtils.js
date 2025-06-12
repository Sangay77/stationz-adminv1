function showErrorModal(message) {
    showModal("Error", message, "bg-danger text-white");
}

function showWarningModal(message) {
    showModal("Warning", message, "bg-warning text-dark");
}

function showModal(title, message) {
    $("#modalTitle").text(title).attr("class", "modal-title ");
    $("#modalBody").text(message);
    const modal = new bootstrap.Modal(document.getElementById("modalMessage"));
    modal.show();
}
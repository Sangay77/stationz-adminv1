// quantity_control.js

function showErrorModal(message) {
    showModal("Error", message, "bg-danger text-white");
}

function showWarningModal(message) {
    showModal("Warning", message, "bg-warning text-dark");
}

function showModal(title, message, headerClass) {
    $("#modalTitle").text(title).attr("class", "modal-title " + headerClass);
    $("#modalBody").text(message);
    const modal = new bootstrap.Modal(document.getElementById("modalMessage"));
    modal.show();
}

$(document).ready(function () {
    $(".linkMinus").on("click", function (evt) {
        evt.preventDefault();

        const productId = $(this).attr("pid");
        const quantityInput = $("#quantity" + productId);
        const currentQuantity = parseInt(quantityInput.val()) || 1;
        const newQuantity = currentQuantity - 1;

        if (newQuantity > 0) {
            quantityInput.val(newQuantity);
        } else {
            showWarningModal("Minimum quantity is 1.");
        }
    });

    $(".linkPlus").on("click", function (evt) {
        evt.preventDefault();

        const productId = $(this).attr("pid");
        const quantityInput = $("#quantity" + productId);
        const newQuantity = parseInt(quantityInput.val()) + 1;

        if(newQuantity<=5){

            quantityInput.val(newQuantity);

        }else{
                    showWarningModal("Maximum quantity is 5.");

        }

    });
});

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
        decreaseQuantity($(this));
    });

    $(".linkPlus").on("click", function (evt) {
        evt.preventDefault();
        increaseQuantity($(this));
    });


    $(".linkPlus").on("click", function (evt) {
        evt.preventDefault();
        increaseQuantity($(this));
    });

    $(".linkRemove").on("click", function (evt) {
            evt.preventDefault();
            removeProduct($(this));


        });
});

function decreaseQuantity(link) {
    const productId = link.attr("pid");
    const quantityInput = $("#quantity" + productId);
    const currentQuantity = parseInt(quantityInput.val()) || 1;
    const newQuantity = currentQuantity - 1;

    if (newQuantity > 0) {
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity);
    } else {
        showWarningModal("Minimum quantity is 1.");
    }
}

function increaseQuantity(link) {
    const productId = link.attr("pid");
    const quantityInput = $("#quantity" + productId);
    const newQuantity = parseInt(quantityInput.val()) + 1;

    if (newQuantity <= 5) {
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity);
    } else {
        showWarningModal("Maximum quantity is 5.");
    }
}


function updateSubtotal(updatedSubtotal, productId){
    console.log("Updating total for productId=" + productId + " with value: Nu " + updatedSubtotal);
    const selector = "#total-" + productId;

    if ($(selector).length === 0) {
        console.error("Element not found with selector: " + selector);
        return;
    }
    $(selector).text("Nu " + updatedSubtotal);
}

function updateEstimatedTotal(newEstimatedTotal) {
    console.log("Updating estimated total: Nu " + newEstimatedTotal);
    $("#estimatedTotal").text("Nu " + newEstimatedTotal);
}

function updateQuantity(productId, quantity) {
    const url = contextPath + "cart/update/" + productId + "/" + quantity;

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response) {
        // Expecting: { updatedSubtotal: "270.00", estimatedTotal: "1250.00" }
        console.log("AJAX response: ", response);
        updateSubtotal(response.updatedSubtotal, productId);
        updateEstimatedTotal(response.estimatedTotal);
    }).fail(function () {
        showErrorModal("Error while updating product quantity in the shopping cart.");
    });
}

function removeProduct(link) {
    const url = link.attr("href");

    $.ajax({
        type: "DELETE",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response) {
        const rowNumber = link.attr("rowNumber");
        removeProductHTML(rowNumber);
        updateEstimatedTotal(response.estimatedTotal);
        showModal("Shopping Cart", response.message);
    }).fail(function () {
        showErrorModal("Error while removing product from shopping cart.");
    });
}


function removeProductHTML(rowNumber){
    $("#row"+rowNumber).remove();
}





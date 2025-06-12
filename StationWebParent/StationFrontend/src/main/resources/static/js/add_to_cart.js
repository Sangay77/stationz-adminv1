$(document).ready(function () {
    $("#buttonAdd2Cart").on("click", function (evt) {
        evt.preventDefault();
        addToCart();
    });
});

function addToCart() {
    const quantity = $("#quantity" + productId).val();
    const url = contextPath + "cart/add/" + productId + "/" + quantity;

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response) {
        showModal("Shopping Cart", response, "bg-success text-white");
    }).fail(function () {
        showErrorModal("Error while adding product to shopping cart.");
    });
}




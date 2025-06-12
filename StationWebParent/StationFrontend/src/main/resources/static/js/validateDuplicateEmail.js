function validateNewCustomerEmail(form) {
    const customerEmail = $("#email").val();
    const csrfValue = $("input[name='_csrf']").val();

    const url = $(form).data("check-url");
    const params = {
        email: customerEmail,
        _csrf: csrfValue
    };

    $.post(url, params, function (response) {
        if (response === 'OK') {
            form.submit();
        } else if (response === 'Duplicate') {
            showWarningModal("There is another customer having the same email: " + customerEmail);
        } else {
            showErrorModal("Unknown response from server");
        }
    }).fail(function () {
        showErrorModal("Could not connect to server");
    });

    return false;
}

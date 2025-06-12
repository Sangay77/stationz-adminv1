var buttonLoad4States;
var dropDownCountry4States;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function () {
    buttonLoad4States = $("#buttonLoadCountriesForStates");
    dropDownCountry4States = $("#dropDownCountriesForStates");
    dropDownStates = $("#dropDownStates");
    buttonAddState = $("#buttonAddState");
    buttonUpdateState = $("#buttonUpdateState");
    buttonDeleteState = $("#buttonDeleteState");
    labelStateName = $("#labelStateName");
    fieldStateName = $("#fieldStateName");

    buttonLoad4States.click(function () {
        loadCountries4States();
    });

    dropDownCountry4States.on("change", function () {
        loadStates4Country();
    });

    dropDownStates.on("change", function () {
        changeFormStateToSelectedState();
    });

    buttonAddState.click(function () {
        if (buttonAddState.val() === "Add") {
            addState();
        } else {
            changeFormStateToNew();
        }
    });

    buttonUpdateState.click(function () {
        updateState();
    });

    buttonDeleteState.click(function () {
        deleteState();
    });
});

function loadCountries4States() {
    const url = contextPath + "countries/list";

    $.get(url, function (responseJSON) {
        dropDownCountry4States.empty();
        $.each(responseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountry4States);
        });
    }).done(function () {
        buttonLoad4States.val("Refresh Country List");
        showToastMessage("All countries loaded.");
    }).fail(function () {
        showToastMessage("Could not load country list.");
    });
}

function loadStates4Country() {
    const selectedCountry = $("#dropDownCountriesForStates option:selected");
    const countryId = selectedCountry.val();
    const url = contextPath + "states/list_by_country/" + countryId;

    $.get(url, function (responseJSON) {
        dropDownStates.empty();
        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
        });
    }).done(function () {
        changeFormStateToNew();
        showToastMessage("All states loaded for country " + selectedCountry.text());
    }).fail(function () {
        showToastMessage("Error while loading states for country " + selectedCountry.text());
    });
}

function deleteState() {

    const optionValue = dropDownStates.val();
    const stateId = optionValue.split("_")[0];
    const url = contextPath + "states/delete/" + stateId;

    $.get(url, function () {
        $("#dropDownStates option[value='" + stateId + "']").remove();
        changeFormStateToNew();
    }).done(function () {
        showToastMessage("The state has been deleted");
    }).fail(function () {
        showToastMessage("Error: Could not connect to server or Server encountered an error");
    });
}

function changeFormStateToNew() {
    buttonAddState.val("Add");
    labelStateName.text("State/Province Name:");
    buttonUpdateState.prop("disabled", true);
    buttonDeleteState.prop("disabled", true);
    fieldStateName.val("").focus();
}

function changeFormStateToSelectedState() {
    buttonAddState.val("New");
    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);

    labelStateName.text("Selected State/Province");

    const selectedStateName = $("#dropDownStates option:selected").text();
    fieldStateName.val(selectedStateName);
}

function selectNewlyAddedState(stateId, stateName) {
    $("<option>").val(stateId).text(stateName).appendTo(dropDownStates);
    $("#dropDownStates option[value='" + stateId + "']").prop("selected", true);
    fieldStateName.val("").focus();
}

function addState() {
    const url = contextPath + "states/save";

    const stateName = fieldStateName.val();
    const selectedCountry = $("#dropDownCountriesForStates option:selected");
    const countryId = selectedCountry.val();
    const countryName = selectedCountry.text();

    const jsonData = {
        name: stateName,
        country: {
            id: countryId,
            name: countryName
        }
    };

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (state) {
        selectNewlyAddedState(state.id, state.name);
        showToastMessage("The new state has been added");
    }).fail(function () {
        showToastMessage("Error: Could not connect to server or server encountered an error");
    });
}

function updateState() {
    const url = contextPath + "states/save";

    const stateId = dropDownStates.val();
    const stateName = fieldStateName.val();
    const selectedCountry = $("#dropDownCountriesForStates option:selected");
    const countryId = selectedCountry.val();
    const countryName = selectedCountry.text();

    const jsonData = {
        id: stateId,
        name: stateName,
        country: {
            id: countryId,
            name: countryName
        }
    };

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function () {
        $("#dropDownStates option:selected").text(stateName);
        showToastMessage("The state has been updated");
    }).fail(function () {
        showToastMessage("Error: Could not connect to server or server encountered an error");
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}

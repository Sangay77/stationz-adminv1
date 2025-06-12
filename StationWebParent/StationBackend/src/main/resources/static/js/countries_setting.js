var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function(){
    buttonLoad = $("#buttonLoadCountries");
    dropDownCountry = $("#dropDownCountries");

    buttonAddCountry = $("#buttonAddCountry");
    buttonUpdateCountry = $("#buttonUpdateCountry");
    buttonDeleteCountry = $("#buttonDeleteCountry");
    labelCountryName = $("#labelCountryName");
    fieldCountryName = $("#fieldCountryName");
    fieldCountryCode = $("#fieldCountryCode");

    buttonLoad.click(function(){
        loadCountries();
    });

    dropDownCountry.on("change", function(){
        changeFormStateToSelectedCountry();
    });

    buttonAddCountry.click(function(){
        if(buttonAddCountry.val()=="Add"){
            addCountry();
        }else{
              changeFormStateToNew();
        }
    });

    buttonUpdateCountry.click(function(){
        updateCountry();
    });

     buttonDeleteCountry.click(function(){
            deleteCountry();
        });
});

function deleteCountry(){

    optionValue=dropDownCountry.val();
    countryId=optionValue.split("_")[0];

    url=contextPath+"countries/delete/"+ countryId;

    $.get(url, function(responseJSON) {

        }).done(function() {
            $("#dropDownCountries option[value='" + optionValue + "']").remove();
            changeFormStateToNew();
            showToastMessage("The country have been deleted");
        }).fail(function() {
            showToastMessage("Server encountered error");
        });
}

function updateCountry() {
    var url = contextPath + "countries/save";
    var countryName = fieldCountryName.val();
    var countryCode = fieldCountryCode.val();

    var countryId = dropDownCountry.val().split("_")[0];

    var jsonData = {
        id: countryId,
        name: countryName,
        code: countryCode
    };

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(countryId) {
        $("#dropDownCountries option:selected").val(countryId + "_" + countryCode);
        $("#dropDownCountries option:selected").text(countryName);
        showToastMessage("The country has been updated successfully");
        changeFormStateToNew();
    }).fail(function(jqXHR, textStatus, errorThrown) {
        showToastMessage("Error updating country");
    });
}


function addCountry() {
    var url = contextPath + "countries/save";
    var countryName = fieldCountryName.val();
    var countryCode = fieldCountryCode.val();

    var jsonData = { name: countryName, code: countryCode };

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(countryId) {
    selectNewlyAddedCountry(countryId,countryName,countryCode);
        showToastMessage("New Country has been added successfully");
    }).fail(function(jqXHR, textStatus, errorThrown) {
        showToastMessage("Error adding country");
    });
}

function selectNewlyAddedCountry(countryId,countryName,countryCode){

optionValue=countryId+ "_" +countryCode;
$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
$("#dropDownCountries option[value='" + optionValue + "']").prop("selected",true);
fieldCountryCode.val("");
fieldCountryName.val("").focus();



}


function changeFormStateToNew(){

    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name:");
     buttonUpdateCountry.prop("disabled", true);
     buttonDeleteCountry.prop("disabled", true);
     fieldCountryCode.val("");
     fieldCountryName.val("").focus();


}

function changeFormStateToSelectedCountry(){
    buttonAddCountry.prop("value", "New");
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);

    labelCountryName.text("Selected Country: ");
    var selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);

    var countryCode = dropDownCountry.val().split("_")[1];
    fieldCountryCode.val(countryCode);
}


function loadCountries() {
    var url = contextPath + "countries/list";
    $.get(url, function(responseJSON) {
        dropDownCountry.empty();

        $.each(responseJSON, function(index, country) {
            var optionValue = country.id + "_" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
        });
    }).done(function() {
        buttonLoad.text("Refresh countries list");
        showToastMessage("All countries have been loaded");
    }).fail(function() {
        showToastMessage("Server encountered error");
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}

$(document).ready(function(){
    customizeDropDonMenu();
});

function customizeDropDonMenu(){
    $(".navbar .dropdown").hover(
        function(){
            $(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown();
        },
        function(){
            $(this).find('.dropdown-menu').first().stop(true,true).delay(100).slideUp();
        }
    );

    $(".dropdown > a").click(function(){
        location.href = this.href;
    });
}

function checkPasswordMatch(confirmPassword){

    if(confirmPassword.value!=$("#password").val()){

        confirmPassword.setCustomValidity("Passwords do not match");
    }else{
        confirmPassword.setCustomValidity("");
    }
}


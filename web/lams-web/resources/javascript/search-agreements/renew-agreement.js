$(document).ready(function() {

    let user = {};

    // if (Cookies.get('loginDetails')) {
    //   console.log(Cookies.get('loginDetails'));
    //   $("#dashboard").show();
    // } else {
    //   console.log("hi2");
    //   $("#login").show();
    // }
    // $("input").on("keyup", function() {
    //     console.log(this.value);
    //     user[this.id] = this.value;
    // });
    $('#myOptions').change(function() {
        var val = $("#myOptions option:selected").text();
        // alert(val);
        if(val==="view")
        {
            window.location="./view-agreement.html"
        }
        else if (val==="renew") {
            window.location="./renew-agreement.html"
        }
        else {
            window.location="./view-dcp.html"
        }

    });


    $("#logout").on("click", function() {
        //clear cookies and logout
        $("#login").hide();
        $("#dashboard").show();
    });


});

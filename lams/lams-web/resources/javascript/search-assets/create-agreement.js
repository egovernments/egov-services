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
    $('#submit').click(function() {

        // alert(val);
        // window.location="./create-agreement-ack.html"
          window.open("./create-agreement-ack.html","fs","fullscreen=yes")
    });

    $('#close').on("click",function() {
          window.close();
    })


    $("#logout").on("click", function() {
        //clear cookies and logout
        $("#login").hide();
        $("#dashboard").show();
    });


});

$(document).ready(function() {
    // $("#dashoboard").hide();
    // $("#login").hide();
    // console.log("hi");
    let user = {};
    if (Cookies.get('loginDetails')) {
        console.log(Cookies.get('loginDetails'));
        $("#dashboard").show();
    } else {
        console.log("hi2");
        $("#login").show();
    }

    $("input").on("keyup", function() {
        console.log(this.value);
        user[this.id] = this.value;
    });

    $("#loginForm").on("click", function() {
        //      console.log("clicked" + this);
        if (user) {
            if (user.Username && user.Password) {
                $.post('https://peaceful-headland-36194.herokuapp.com/v1/mSevaAndLA/users/_login?tenant_id=kul.am', user);
                  // console.log(data);
                  $("#login").hide();
                  $("#dashboard").show();
                  // Cookies.set("loginDetails",{})
            }
        }
    })

    $("#logout").on("click", function() {
        //clear cookies and logout
        $("#login").hide();
        $("#dashboard").show();
    });


});

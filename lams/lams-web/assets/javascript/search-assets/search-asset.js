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
        var val = $("#myOptions option:selected").val();
        // alert(val);
        // window.location="./create-agreement.html";
        window.open("../../../app/agreements/new.html?type=" + val, "fs", "width=1200,height=800")
    });


    $('#close').on("click", function() {
        window.close();
    })


    $("#logout").on("click", function() {
        //clear cookies and logout
        $("#login").hide();
        $("#dashboard").show();
    });

    $("#table").hide();


    var agreement = {};
    //Getting data for user input
    $("input").on("keyup", function() {
        // console.log(this.value);
        agreement[this.id] = this.value;
    });

    //Getting data for user input
    $("select").on("change", function() {
        // console.log(this.value);
        if(this.selectedIndex!=0)
        {
          agreement[this.id] = this.value;

        }
    });

    //file change handle for file upload
    $("input[type=file]").on("change", function(evt) {
        // console.log(this.value);
        // agreement[this.id] = this.value;
        var file = evt.currentTarget.files[0];

        //call post api update and update that url in pur agrement object
    });

    var validation_rules = {};
    var final_validatin_rules = {};
    var commom_fields_rules = {
        asset_category: {
            required: true
        }
    }

    final_validatin_rules = Object.assign({}, commom_fields_rules);

    for (var key in final_validatin_rules) {
        if (final_validatin_rules[key].required) {
            $(`label[for=${key}]`).append(`<span> *</span>`);
        }
        // $(`#${key}`).attr("disabled",true);
    };

    final_validatin_rules["messages"] = {
        asset_category: {
            required: "Select name of category to search"
        }
      }

    $("#searchAssestForm").validate({
        rules: final_validatin_rules,
        messages: final_validatin_rules["messages"],
        submitHandler: function(form) {
              $("#table").show();
            // form.submit();

            // console.log(agreement);
            // $.post(`${baseUrl}agreements?tenant_id=kul.am`, {
            //     RequestInfo: requestInfo,
            //     Agreement: agreement
            // }, function(response) {
            //     // alert("submit");
            //     window.open("../../../../app/search-assets/create-agreement-ack.html?&agreement_id=aeiou", "", "width=1200,height=800")
            //     console.log(response);
            // })
        }
    })




});

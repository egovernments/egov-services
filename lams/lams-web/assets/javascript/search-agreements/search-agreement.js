$(document).ready(function() {

    let user = {};
    var basUrl = "http://peaceful-headland-36194.herokuapp.com/v1/mSevaAndLA/";

    $("#table").hide();

      $("#search").on("click",function()
      {
              $("#table").show();
      })

    $.ajax({
            method: "POST",
            url: `${basUrl}agreements?tenant_id=kul.am`,
            data: {
                "api_id": "string",
                "ver": "string",
                "ts": "2017-01-18T07:18:23.130Z",
                "action": "string",
                "did": "string",
                "key": "string",
                "msg_id": "string",
                "requester_id": "string",
                "auth_token": "aeiou"
            }
        })
        .done(function(response) {
            // alert("Data Saved: " + msg);
            $("#searchTableBody").html("");
            $("#searchTableBody").append(`<tr>`);
            console.log(response.Agreements[0]);
            console.log(response.Agreements.length);
            var agreement=[];

            // <td>${response.Agreements[i].asset.ward}</td>
            // <td>${response.Agreements[i].asset.electionward}</td>

            for (var i = 0; i < response.Agreements.length; i++) {
                // response.Agreements[i]
                $("#searchTableBody").append(`<td>${i+1}</td>
                  <td>${response.Agreements[i].agreement_number} </td>
                  <td>${response.Agreements[i].allottee.name}</td>
                  <td>${response.Agreements[i].allottee.contact_no}</td>
                  <td>${response.Agreements[i].asset.zone}</td>


                  <td>${response.Agreements[i].asset.category}</td>
                  <td>${response.Agreements[i].asset.code}</td>
                  <td>${response.Agreements[i].tradelicense_number}</td>
                  <td>${response.Agreements[i].agreement_date}</td>
                  <td>
                      <div class="styled-select">
                          <select id="myOptions">
                            <option>Select Action</option>
                              <option value="view">View</option>
                                <option value="renew">Renew</option>

                          </select>
                      </div>
                  </td>`);
            }
            $("#searchTableBody").append(`</tr>`);


            $('#myOptions').change(function() {
                var val = $("#myOptions option:selected").text();
                if (val === "Renew") {
                    window.open("./view-renew-agreement.html?type=land&view=renew&agreement_id=aeiou", "", "width=1200,height=800")
                } else {
                    window.open("./view-renew-agreement.html?type=land&view=new&agreement_id=aeiou", "", "width=1200,height=800")
                }
            });






        });

        $('#close').on("click",function() {
              window.close();
        })



    $('#searchAgreement').on("click", function() {
        $.ajax({
                method: "POST",
                url: `${basUrl}agreements?tenant_id=kul.am`,
                data: {
                    "api_id": "string",
                    "ver": "string",
                    "ts": "2017-01-18T07:18:23.130Z",
                    "action": "string",
                    "did": "string",
                    "key": "string",
                    "msg_id": "string",
                    "requester_id": "string",
                    "auth_token": "aeiou"
                }
            })
            .done(function(response) {
                // alert("Data Saved: " + msg);

            });
    });


    $('#myOptions').change(function() {
        var val = $("#myOptions option:selected").text();
        if (val === "Renew") {
            window.open("./renew-agreement.html", "fs", "fullscreen=yes")
        } else {
            window.open("./view-agreement.html", "fs", "fullscreen=yes")
        }
        // alert(val);
        // if(val==="View")
        // {
        //     window.location="./view-agreement.html"
        // }
        // else if (val==="Renew") {
        //     window.location="./renew-agreement.html"
        // }
        // else {
        //     window.location="./view-dcp.html"
        // }

    });

    $("#shopping_complex_number").hide();


    $("#logout").on("click", function() {
        //clear cookies and logout
        $("#login").hide();
        $("#dashboard").show();
    });

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
        if(this.value==="shop")
        {
          $("#shopping_complex_number").show();
        }
        else {
          $("#shopping_complex_number").hide();
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

    $("#searchAgreementForm").validate({
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

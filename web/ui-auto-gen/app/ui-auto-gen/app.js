$(document).ready(function() {
    var clicked = 0;
    var checkedList = [];

    $(document).on('click','.check',function(e){
        if(e.target.checked) {
            checkedList.push(e.target.id);
        } else {
            checkedList.splice(checkedList.indexOf(e.target.id), 1);
        }
    });

    $("#genForm").submit(function(event) {
        event.preventDefault();
        $('.alert').show();
        $("#final-block").hide();
        $("#error-block").html("");
        if(clicked === 0) {
            clicked = 1;
            checkedList = [];
            var request = new XMLHttpRequest();
            request.onreadystatechange = function() {
                if (request.readyState == 4) {
                    if (request.status == 200) {
                        var list = JSON.parse(request.response);
                        $("#references").html("");
                        for(var i=0; i<list.length; i++) {
                            $("#references").append("<input id="+ list[i] +" class='check' type='checkbox'> " + list[i] + "<br/>");
                        }
                        $("#referencesDiv").show();
                        $('#yamlURL').prop('disabled', true);
                        $('#module').prop('disabled', true);
                    } else {
                        clicked = 0;
                        showErrors(request);
                    }
                    $('.alert').hide();
                }
            };

            request.open("POST", "/reference/get", true);
            request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            request.send(JSON.stringify({
                url: $("#yamlURL").val()
            }));
        } else {
            var request = new XMLHttpRequest();
            request.onreadystatechange = function() {
                if (request.readyState == 4) {
                    if (request.status == 200) {
                        console.log(typeof request.response); // should be a blob
                        var link = document.createElement('a');
                        link.href = window.URL.createObjectURL(request.response);
                        link.download = "specs.zip";
                        link.click();
                        $("#yamlURL").val("");
                        $("#module").val("");
                    } else {
                        showErrors(request);
                    }

                    clicked = 0;
                    $("#referencesDiv").hide();
                    $('#yamlURL').prop('disabled', false);
                    $('#module').prop('disabled', false);

                    $('.alert').hide();
                } else if (request.readyState == 2) {
                    if (request.status == 200) {
                        request.responseType = "blob";
                    } else {
                        request.responseType = "text";
                    }
                }
            };
            request.open("POST", "/yaml/create", true);
            request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            request.send(JSON.stringify({
                url: $("#yamlURL").val(),
                module: $("#module").val(),
                references: checkedList
            }));
        }
    });

    $("#resetBtn").click(function() {
        $("#yamlURL").val("");
        $("#module").val("");
        checkedList = [];
        clicked = 0;
        $("#references").html("");
        $("#referencesDiv").hide();
        $('#yamlURL').prop('disabled', false);
        $('#module').prop('disabled', false);
    })
})

function showErrors(request) {
    try {
        var errors = JSON.parse(request.responseText);
        if (errors.message) {
            $("#error-block").append(
                "<p><i>" + errors.message + "</i></p>"
            )
        } else {
            errors = errors.errors;
            for (var key in errors) {
                $("#error-block").append(
                    "<p><i>" + key + ": " + errors[key] + "</i></p>"
                )
            }
        }

        $("#final-block").show();
    } catch (e) {
        $("#error-block").append(
            "<p>" + request.responseText + "</p>"
        )
    }
}
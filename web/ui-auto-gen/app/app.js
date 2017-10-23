$(document).ready(function() {
    $('#submitBtn').click(function() {
        if ($("#yamlURL").val() && $("#module").val()) {
            $('.alert').show();
            $("#final-block").hide();
            $("#error-block").html("");

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
                module: $("#module").val()
            }));
        }
    })
})
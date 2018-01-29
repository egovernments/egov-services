login = async (username, password) => {
    username = username || "{{username}}"
    password = password || "{{password}}"
    console.log("Executing login function")
    const login_request = {
        "url": "{{url}}/user/oauth/token",
        "method": "POST",
        "header": [
            {
                "key": "Content-Type", "value": "application/json"
            },
            {
                "key": "Authorization", "value": "Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0"
            }
        ],
        "body": {
            "mode": "urlencoded",
            "urlencoded": [
                {"key": "username", "value": username},
                {"key": "password", "value": password},
                {"key": "grant_type", "value": "password"},
                {"key": "scope", "value": "read"},
                {"key": "tenantId", "value": "{{tenantId}}"},
            ]
        }
    }

    try {
        let data = await pmutil.fetch(pmutil.resolveParamsObject(login_request, true))
        let body = JSON.parse(data.text())
        pm.variables.set("access_token", body.access_token);
    } catch (ex) {
        console.log("Exception occurred during login")
        console.log(ex)
    }
}

const ErrorUtil = function() {
    this.getErrorDetails = (test) => {
        let minimum = test["minimum"];
        const maximum = test["maximum"];
        const field_name = test["field_name"];
        const field_path = test["field_path"];
        const test_type = test["type"];
        const test_subtype = test["subtype"];
        const api_path = test["api_path"];

        if (minimum === 0 )
            minimum = 1;

        switch (test_type + "." + test_subtype + "." + field_name) {
            case "length.maximum.mobileNo":
            case "length.minimum.mobileNo":
                return {code: field_name, message: "Please enter a ten digit mobile number"}
            case "length.maximum.contactNo":
            case "length.minimum.contactNo":
                return {code: field_name, message: "Please enter a ten digit contact number"}
            case "length.maximum.faxNumber":
            case "length.minimum.faxNumber":
                return {code: field_name, message: "Please enter a ten digit fax number"}
        }

        let custom_error_map = {
            "required": {
                "missing": {code: field_name, message: "may not be null"}
            },
            "length": {
                "minimum": {
                    code: field_name, message: `Value of ${field_name} shall be between ${minimum} and ${maximum}`
                },
                "maximum": {
                    code: field_name, message: `Value of ${field_name} shall be between ${minimum} and ${maximum}`
                },
            }
        }

        if (test_type in custom_error_map) {
            if (test_subtype in custom_error_map[test_type]) {
                return custom_error_map[test_type][test_subtype]
            }
        }

        return {code: field_name, message:"Please handle the error message in egov_utils.js"}
    }
}

var error = new ErrorUtil();

function bad_api_tests(api_name, data) {
    let error_details = error.getErrorDetails(data);

    describe(api_name  + " - " + data["field_name"] + " " + data["type"] + " " + data["subtype"], () => {
        it("should have 400 status", () => {
            response.should.have.status(400);
        })

        it('should return a JSON response', () => {
            response.should.be.json;
        });

        it("Response body present", () => {
            response.body.should.not.be.null;
        })

        it("Response body has errors", () => {
            response.body.Errors.should.not.be.null;
        })

        it("Error message has code with field name " + error_details.code, () => {
            response.body.Errors[0].code.should.contain(error_details.code)
        })

        it("Error message is - " + error_details.message, () => {
            response.body.Errors[0].message.should.equal(error_details.message);
        })
    })
}
package resources;

import com.jayway.restassured.response.Response;
import utils.APILogger;
import utils.Properties;

import static com.jayway.restassured.RestAssured.given;

public class UserServiceResource {

    public Response getUserDetails(String json) {
        new APILogger().log("User Details Request For Search is Started with--" + json);

        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.userUrl);

        new APILogger().log("User Details Response For Search is Generated as--" + response.asString());

        return response;
    }

    public Response createUserNoValidate(String jsonString) {

        new APILogger().log("User Details Request For Create is Started with--" + jsonString);

        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(jsonString)
                .when()
                .post(Properties.userCreateUrl);

        new APILogger().log("User Details Response For Create is Generated as--" + response.asString());

        return response;
    }

    public Response createOtp(String json) {

        new APILogger().log("Create Otp Request is Started with--" + json);

        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.createOtpUrl);

        new APILogger().log("Create Otp Response is Generated as--" + response.asString());

        return response;
    }

    public Response validateOtp(String json) {

       new APILogger().log("Validate Otp Request is Started with--" + json);

       Response response = given().request().with()
               .header("Content-Type", "application/json")
               .body(json)
               .when()
               .post(Properties.validateOtpUrl);

        new APILogger().log("Validate Otp Response is Generated as--" + response.asString());

        return response;
    }

    public Response createCitizenUser(String json) {

        new APILogger().log("Create Citizen User Request is Started with--" + json);

        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.createCitizenUrl);

        new APILogger().log("Create Citizen User Response is Generated as--" + response.asString());

        return response;
    }

    public Response updateUserDetails(String json, int id) {

        new APILogger().log("Update User Request is Started with--" + json);

        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.userUpdateUrl+id+"/_updatenovalidate");

        new APILogger().log("Update User Response is Generated as--" + response.asString());

        return response;
    }

    public Response getLoginUserDetails(String json, String access_token) {

        new APILogger().log("Login User Details Request is Started with--" + json);

        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.loginUserDetailsUrl+access_token);

        new APILogger().log("Login User Details Response is Generated as--" + response.asString());

        return response;
    }

    public Response searchOtp(String json) {

       new APILogger().log("Search OTP Request is Started with--" + json);

       Response response = given().request().with()
               .header("Content-Type", "application/json")
               .body(json)
               .when()
               .post(Properties.searchOtpUrl);

       new APILogger().log("Search OTP Response is Generated as--" + response.asString());

        return response;
    }
}

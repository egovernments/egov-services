package resources;

import com.jayway.restassured.response.Response;
import utils.APILogger;
import utils.Properties;
import utils.RequestHelper;

import static com.jayway.restassured.RestAssured.given;

public class EgovEISResource {

    public Response searchAttendance(String jsonData, String auth_token) {
        new APILogger().log("Search Attendance Request is started with -- " + jsonData);
        Response response = given().request().with()
                .urlEncodingEnabled(false)
                .header("Content-Type", "application/json")
//                .header("auth-token", auth_token)
                .body(jsonData)
                .when()
                .post(Properties.searchAttendanceUrl);

        new APILogger().log("Search Attendance Response is Generated as -- " + response.asString());
        return response;
    }

    public Response createAttendance(String jsonData, String access_token) {

        new APILogger().log("Create Attendance Request is started with -- " + jsonData);
        Response response = given().request().with()
                .urlEncodingEnabled(false)
                .header("Content-Type", "application/json")
                .body(jsonData)
                .when()
                .post(Properties.createAttendanceURL);

        new APILogger().log("Create Attendance Response is Generated as -- " + response.asString());
        return response;

    }

    public Response searchEmployee(String jsonData, String criteria) {
        new APILogger().log("Search createEmployee Request is started with -- " + jsonData);
        Response response = given().request().with()
                .urlEncodingEnabled(false)
                .header("Content-Type", "application/json")
                .body(jsonData)
                .when()
                .post(Properties.searchEmployeeURL + criteria);

        new APILogger().log("Search createEmployee Response is generated as -- " + response.asString());
        return response;
    }

    public Response searchEmployee(String jsonData) {
        new APILogger().log("Search createEmployee Request is started with -- " + jsonData);
        Response response = given().request().with()
                .urlEncodingEnabled(false)
                .header("Content-Type", "application/json")
                .body(jsonData)
                .when()
                .post("http://10.0.0.151:32656/hr-employee/employees/_search?tenantId=ap.kurnool");

        new APILogger().log("Search createEmployee Response is generated as -- " + response.asString());
        return response;
    }

    public Response createEmployee(String jsonData) {
        new APILogger().log("Create createEmployee Request Test is started with-- " + jsonData);
        Response response = given().request().with()
                .urlEncodingEnabled(false)
                .header("Content-Type", "application/json")
                .body(jsonData).when()
                .post(Properties.createEmployeeUrl);

        new APILogger().log("Create createEmployee Response Test is generated as-- " + response.asString());
        return response;
    }

    public Response searchEmployeeLeave(String jsonData) {
        new APILogger().log("Search Employee Leave Request Test is started with-- " + jsonData);

        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(jsonData)
                .when()
                .post(Properties.searchEmployeeLeaveUrl);

        new APILogger().log("Search Employee Leave Response Test is generated as-- " + response.asString());
        return response;
    }

    public Response searchLeaveApplications(String jsonData) {
        new APILogger().log("Search Leave Applications Test is started with-- " + jsonData);

        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(jsonData).when()
                .post(Properties.eisSearchLeaveApplicationsUrl);

        new APILogger().log("Search Leave Applications Test is completed with-- " + response.asString());
        return response;
    }

    public Response hrLeaveCreateOpeningBalance(String jsonData) {

        new APILogger().log("Create HR Leave Opening Balance Test is started with-- " + jsonData);

        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(jsonData)
                .when()
                .post("http://10.0.0.151:32656/hr-leave/leaveopeningbalances/_create");
//                .post(Properties.createOpeningBalanceUrlUrl);

        new APILogger().log("Create HR Leave Opening Balance Test is completed with-- " + response.asString());
        return response;
    }

    public Response hrLeaveSearchOpeningBalance(String jsonData, String path) {

        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(jsonData)
                .when()
                .post("http://10.0.0.151:32656/hr-leave/leaveopeningbalances/_search?tenantId=ap.kurnool" + path);

        return response;
    }

    public Response hrLeaveUpdateOpeningBalance(String json , int id) {

        new APILogger().log("Update HR Leave Opening Balance Test is started with-- " + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("http://10.0.0.151:32656/hr-leave/leaveopeningbalances/" + id + "/_update");

        new APILogger().log("Update HR Leave Opening Balance Test is completed with-- " + response.asString());

        return response;

    }
}
package resources.employeeMaster;

import com.jayway.restassured.response.Response;
import utils.APILogger;

import static com.jayway.restassured.RestAssured.given;

public class LeaveTypeResource {

    public Response create(String json) {

        new APILogger().log("Create Leave Type Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("http://10.0.0.151:32656/hr-leave/leavetypes/_create?tenantId=ap.kurnool");

        new APILogger().log("Create Leave Type Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response search(String json) {
        new APILogger().log("Search Leave Type Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("http://10.0.0.151:32656/hr-leave/leavetypes/_search?tenantId=ap.kurnool&pageSize=100");

        new APILogger().log("Search Leave Type Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response update(String json, int leaveId) {
        new APILogger().log("Update Leave Type Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("http://10.0.0.151:32656/hr-leave/leavetypes/" + leaveId + "/_update");

        new APILogger().log("Update Leave Type Test Response is Generated as  --" + response.asString());
        return response;
    }
}

package resources.employeeMaster.leaveApplication;

import com.jayway.restassured.response.Response;
import utils.APILogger;

import static com.jayway.restassured.RestAssured.given;

public class LeaveApplicationResource {

    public Response create(String json){

        new APILogger().log("Create Leave Application Test Request is Started with--" + json);
        Response response = given().request().with()
                            .header("Content-Type", "application/json")
                            .when()
                            .body(json)
                            .post("http://10.0.0.151:32656/hr-leave/leaveapplications/_create?tenantId=ap.kurnool");

        new APILogger().log("Create Leave Application Test Response is generated as--" + response.asString());
        return response;
    }

    public Response search(String json, String appNum) {

        new APILogger().log("Search Leave Application Test Request is Started with--" + json);
        Response response = given().request().with()
                            .header("Content-Type", "application/json")
                            .when()
                            .body(json)
                            .post("http://10.0.0.151:32656/hr-leave/leaveapplications/_search?tenantId=ap.kurnool&applicationNumber="+appNum);

        new APILogger().log("Search Leave Application Test Response is generated as--" + response.asString());
        return response;
    }
}


package resources.hrMaster;

import com.jayway.restassured.response.Response;
import utils.APILogger;

import static com.jayway.restassured.RestAssured.given;

public class HRMasterDesignationResource {

    public Response create(String json) {

        new APILogger().log("Create Designation Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("http://10.0.0.151:32656/hr-masters/designations/_create?tenantId=ap.kurnool");

        new APILogger().log("Create Designation Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response search(String json, String name) {
        new APILogger().log("Search Designation Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("http://10.0.0.151:32656/hr-masters/designations/_search?tenantId=ap.kurnool&name=" + name);

        new APILogger().log("Search Designation Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response update(String json, int id) {

        new APILogger().log("Update Designation Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("http://10.0.0.151:32656/hr-masters/designations/" + id + "/_update");

        new APILogger().log("Update Designation Test Response is Generated as  --" + response.asString());
        return response;

    }
}

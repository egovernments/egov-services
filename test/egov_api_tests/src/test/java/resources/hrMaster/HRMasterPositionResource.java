package resources.hrMaster;

import com.jayway.restassured.response.Response;
import utils.APILogger;

import static com.jayway.restassured.RestAssured.given;

public class HRMasterPositionResource {

    public Response create(String json) {

        new APILogger().log("Create Position Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("http://10.0.0.151:32656/hr-masters/positions/_create");

        new APILogger().log("Create Position Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response search(String json, String positionName) {

        new APILogger().log("Search Position Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("http://10.0.0.151:32656/hr-masters/positions/_search?tenantId=ap.kurnool&name=" + positionName);

        new APILogger().log("Search Position Test Response is Generated as  --" + response.asString());
        return response;

    }

    public Response update(String json, int positionId) {

        new APILogger().log("Update Position Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post("http://10.0.0.151:32656/hr-masters/positions/" + positionId + "/_update");

        new APILogger().log("Update Position Test Response is Generated as  --" + response.asString());
        return response;

    }
}

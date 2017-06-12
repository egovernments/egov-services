package resources;

import com.jayway.restassured.response.Response;
import utils.APILogger;
import utils.Properties;

import static com.jayway.restassured.RestAssured.given;

public class AssetServiceResource {

    public Response getSearchAssetService(String json, String sessionId) {

        new APILogger().log("Search Asset Service Request is started with --" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .header("cookie", "SESSIONID=" + sessionId)
//                .header("auth-token", auth_token)
                .body(json)
                .when()
                .post(Properties.searchAssetServiceUrl);

        new APILogger().log("Search Asset Service Response is Generated as --" + response.asString());
        return response;
    }

    public Response getCreateAssetService(String jsonString, String auth_token) {

        new APILogger().log("Create Asset Service Request is started with --" + jsonString);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .header("auth-token", auth_token)
                .body(jsonString)
                .when()
                .post(Properties.createAssetServiceUrl);

        new APILogger().log("Create Asset Service Response is Generated with --" + response.asString());
        return response;
    }
}

package resources;

import com.jayway.restassured.response.Response;
import utils.APILogger;
import utils.Properties;

import static com.jayway.restassured.RestAssured.given;

public class AssetCategoryResource {

    public Response create(String json, String sessionId) {

        new APILogger().log("Create Asset Category Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .header("cookie", "SESSIONID=" + sessionId)
//                .header("auth-token", auth_token)
                .body(json)
                .when()
                .post(Properties.assetCategoryCreateUrl);

        new APILogger().log("Create Asset Category Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response search(String jsonString, String sessionId) {

        new APILogger().log("Search Asset Category Request Test is started with --" + jsonString);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .header("cookie", "SESSIONID=" + sessionId)
//                .header("auth-token", auth_token)
                .body(jsonString)
                .when()
                .post(Properties.assetCategorySearchUrl);

        new APILogger().log("Search Asset Category Response Test is Generated with --" + response.asString());
        return response;
    }
}

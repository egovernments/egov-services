package resources.searchEISMaster;

import com.jayway.restassured.response.Response;
import utils.APILogger;
import utils.Properties;

import static com.jayway.restassured.RestAssured.given;

public class EISMasterResource {

    public Response searchEmployeeType(String json) {

        new APILogger().log("Search createEmployee Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.eisSearchEmployeeTypeUrl);

        new APILogger().log("Search createEmployee Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response searchDesignationType(String json) {

        new APILogger().log("Search Designation Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.eisSearchDesignationTypeUrl);

        new APILogger().log("Search Designation Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response searchPosition(String json) {

        new APILogger().log("Search Position Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.eisSearchPositionUrl);

        new APILogger().log("Search Position Test Response is Generated as  --" + response.asString());
        return response;
    }


    public Response searchPositionHierarchy(String json) {
        new APILogger().log("Search Position Hierarchy Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.eisSearchPositionHierarchyUrl);

        new APILogger().log("Search Position Hierarchy Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response searchGradeType(String json) {
        new APILogger().log("Search Grade Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.eisSearchGradeUrl);

        new APILogger().log("Search Grade Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response searchEmployeeGroup(String json) {
        new APILogger().log("Search createEmployee Group Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.eisSearchEmployeeGroupUrl);

        new APILogger().log("Search createEmployee Group Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response searchRecruitmentQuotaType(String json) {
        new APILogger().log("Search Recruitment Quota Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.eisSearchRecruitmentQuotaUrl);

        new APILogger().log("Search Recruitment Quota Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response searchRecruitmentModesType(String json) {
        new APILogger().log("Search Recruitment Modes Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.eisSearchRecruitmentModesUrl);

        new APILogger().log("Search Recruitment Modes Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response searchHRConfigurationsType(String json) {
        new APILogger().log("Search HR Configurations Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.eisSearchHrConfigurationsUrl);

        new APILogger().log("Search HR Configurations Test Response is Generated as  --" + response.asString());
        return response;
    }

    public Response searchHRStatusesType(String json) {

        new APILogger().log("Search HR Statuses Test Request is Started with--" + json);
        Response response = given().request().with()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(Properties.eisSearchHrStatusesUrl);

        new APILogger().log("Search HR Statuses Test Response is Generated as  --" + response.asString());
        return response;
    }
}

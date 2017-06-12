package tests.assetManagement;

import builders.assetManagement.RequestInfoBuilder;
import builders.assetManagement.assetCategory.AssetCategoryCreateRequestBuilder;
import builders.assetManagement.assetCategory.AssetCategorySearchRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.assetManagement.RequestInfo;
import entities.requests.assetManagement.SearchAssetRequest;
import entities.requests.assetManagement.assetCategory.AssetCategoryCreateRequest;
import entities.responses.assetManagement.assetCategory.AssetCategoryResponse;
import entities.responses.login.LoginResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.AssetCategoryResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.ADMIN;
import static data.UserData.NARASAPPA;

public class AssetCategoryTest extends BaseAPITest {

    @Test(groups = {Categories.ASSET, Categories.SANITY, Categories.PILOT})
    public void CreateAssetCategoryTest() throws IOException {

        // Login Test
        String sessionId = LoginAndLogoutHelper.loginFromPilotService(ADMIN);

        // Create Asset Category Test
        createAssetCategoryTestMethod(sessionId);
    }

    private void createAssetCategoryTestMethod(String sessionId) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().build();
        AssetCategoryCreateRequest request = new AssetCategoryCreateRequestBuilder().withRequestInfo(requestInfo).build();

        String jsonString = RequestHelper.getJsonString(request);
        Response response = new AssetCategoryResource().create(jsonString, sessionId);

        AssetCategoryResponse assetCategoryResponse = (AssetCategoryResponse)
                ResponseHelper.getResponseAsObject(response.asString(), AssetCategoryResponse.class);

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(assetCategoryResponse.getAssetCategory()[0].getName(), request.getAssetCategory().getName());

        new APILogger().log("Create Asset Category Test is Completed --");

        // Logout Test
        pilotLogoutService(sessionId);
    }

    @Test(groups = {Categories.ASSET, Categories.SANITY, Categories.PILOT})
    public void SearchAssetCategoryTest() throws IOException {

        // Login Test
        String sessionId = LoginAndLogoutHelper.loginFromPilotService(ADMIN);

        // Search Asset Category Test
        searchAssetCategoryTestMethod(sessionId);
    }

    private void searchAssetCategoryTestMethod(String sessionId) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().build();

        SearchAssetRequest request = new AssetCategorySearchRequestBuilder().withRequestInfo(requestInfo).build();
        String jsonString = RequestHelper.getJsonString(request);

        Response response = new AssetCategoryResource().search(jsonString, sessionId);

        AssetCategoryResponse assetCategoryResponse = (AssetCategoryResponse)
                ResponseHelper.getResponseAsObject(response.asString(), AssetCategoryResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);

        new APILogger().log("Search Asset Category Test is Completed --");

        // Logout Test
        pilotLogoutService(sessionId);
    }
}

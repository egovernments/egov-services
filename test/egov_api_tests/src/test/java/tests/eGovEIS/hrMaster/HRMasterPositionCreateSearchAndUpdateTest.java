package tests.eGovEIS.hrMaster;

import builders.eGovEIS.hrMaster.position.create.HRMasterPositionCreateRequestBuilder;
import builders.eGovEIS.hrMaster.position.create.PositionBuilder;
import builders.eGovEIS.hrMaster.position.search.HRMasterPositionSearchRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.eGovEIS.hrMaster.position.create.HRMasterPositionCreateRequest;
import entities.requests.eGovEIS.hrMaster.position.create.Position;
import entities.responses.eGovEIS.hrMaster.position.create.HRMasterPositionResponse;
import entities.responses.eGovEIS.hrMaster.position.search.HRMasterPositionSearchRequest;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.hrMaster.HRMasterPositionResource;
import utils.APILogger;
import utils.RequestHelper;
import utils.ResponseHelper;

import java.io.IOException;

public class HRMasterPositionCreateSearchAndUpdateTest {

    @Test
    public void hrMasterPositionCreateSearchAndUpdate() throws IOException {

        //Login Test
//        LoginResponse loginResponse = LoginAndLogoutHelper.login(narasappa);

        // Creating a HR Position
        hrMasterPositionCreateTestMethod();
    }

    private void hrMasterPositionCreateTestMethod() throws IOException {

        HRMasterPositionCreateRequest hrMasterPositionCreateRequest =
                new HRMasterPositionCreateRequestBuilder().build();

        Response response = new HRMasterPositionResource().create(RequestHelper.getJsonString(hrMasterPositionCreateRequest));

        HRMasterPositionResponse hrMasterPositionCreateResponse = (HRMasterPositionResponse)
                ResponseHelper.getResponseAsObject(response.asString(), HRMasterPositionResponse.class);
        Assert.assertEquals(response.getStatusCode(), 200);

        new APILogger().log("HR Master Position Create Test is Completed --");
        // Search a created position
        hrMasterPositionSearchTestMethod(hrMasterPositionCreateResponse.getPosition()[0].getName(), hrMasterPositionCreateRequest);
    }

    private void hrMasterPositionSearchTestMethod(String positionName, HRMasterPositionCreateRequest hrMasterPositionCreateRequest) throws IOException {

        HRMasterPositionSearchRequest hrMasterPositionSearchRequest =
                new HRMasterPositionSearchRequestBuilder().build();

        Response response = new HRMasterPositionResource().search(RequestHelper.getJsonString(hrMasterPositionSearchRequest), positionName);
        HRMasterPositionResponse hrMasterPositionSearchResponse = (HRMasterPositionResponse)
                ResponseHelper.getResponseAsObject(response.asString(), HRMasterPositionResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(hrMasterPositionSearchResponse.getPosition()[0].getName(), positionName);
        new APILogger().log("HR Master Position Search Test is Completed --");

        // Search a created position
        hrMasterPositionUpdateTestMethod(hrMasterPositionSearchResponse.getPosition()[0].getId(), hrMasterPositionCreateRequest);

    }

    private void hrMasterPositionUpdateTestMethod(int id, HRMasterPositionCreateRequest hrMasterPositionCreateRequest) throws IOException {

        Position position = new PositionBuilder()
                .withName("Updated_" + hrMasterPositionCreateRequest.getPosition()[0].getName())
                .withDeptdesig(hrMasterPositionCreateRequest.getPosition()[0].getDeptdesig())
                .withActive(hrMasterPositionCreateRequest.getPosition()[0].getActive())
                .withIsPostOutsourced(hrMasterPositionCreateRequest.getPosition()[0].getIsPostOutsourced())
                .withTenantId(hrMasterPositionCreateRequest.getPosition()[0].getTenantId())
                .build();

        Position[] positions = new Position[1];
        positions[0] = position;

        HRMasterPositionCreateRequest hrMasterPositionUpdateRequest =
                new HRMasterPositionCreateRequestBuilder()
                        .withPosition(positions)
                        .build();

        Response response = new HRMasterPositionResource().update(RequestHelper.getJsonString(hrMasterPositionUpdateRequest), id);

        HRMasterPositionResponse hrMasterPositionUpdateResponse = (HRMasterPositionResponse)
                ResponseHelper.getResponseAsObject(response.asString(), HRMasterPositionResponse.class);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(hrMasterPositionUpdateResponse.getPosition()[0].getName().split("_")[0].contains("Updated"));
        new APILogger().log("HR Master Position Update Test is Completed --");

        // Search the position after update
        hrMasterPositionSearchAfterUpdateTestMethod(hrMasterPositionUpdateResponse.getPosition()[0].getName());

    }

    private void hrMasterPositionSearchAfterUpdateTestMethod(String updatedPositionName) throws IOException {

        HRMasterPositionSearchRequest hrMasterPositionSearchRequest =
                new HRMasterPositionSearchRequestBuilder().build();

        Response response = new HRMasterPositionResource().search(RequestHelper.getJsonString(hrMasterPositionSearchRequest), updatedPositionName);
        HRMasterPositionResponse hrMasterPositionSearchAfterUpdateResponse = (HRMasterPositionResponse)
                ResponseHelper.getResponseAsObject(response.asString(), HRMasterPositionResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(hrMasterPositionSearchAfterUpdateResponse.getPosition()[0].getName().split("_")[0].contains("Updated"));
        new APILogger().log("HR Master Position Search After Update Test is Completed --");

    }
}

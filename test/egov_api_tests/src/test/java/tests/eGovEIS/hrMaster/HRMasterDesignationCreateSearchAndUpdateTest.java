package tests.eGovEIS.hrMaster;

import builders.eGovEIS.hrMaster.designation.RequestInfoBuilder;
import builders.eGovEIS.hrMaster.designation.create.DesignationBuilder;
import builders.eGovEIS.hrMaster.designation.create.HRMasterDesignationCreateRequestBuilder;
import builders.eGovEIS.hrMaster.designation.search.HRMasterDesignationSearchRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.eGovEIS.hrMaster.RequestInfo;
import entities.requests.eGovEIS.hrMaster.designation.create.Designation;
import entities.requests.eGovEIS.hrMaster.designation.create.HRMasterDesignationCreateRequest;
import entities.requests.eGovEIS.hrMaster.designation.search.HRMasterDesignationSearchRequest;
import entities.responses.eGovEIS.hrMaster.designation.create.HRMasterDesignationCreateSearchAndUpdateResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.hrMaster.HRMasterDesignationResource;
import utils.APILogger;
import utils.RequestHelper;
import utils.ResponseHelper;

import java.io.IOException;

public class HRMasterDesignationCreateSearchAndUpdateTest {

    @Test
    public void hrMasterDesignationCreateSearchAndUpdate() throws IOException {

        //Login Test
//        LoginResponse loginResponse = LoginAndLogoutHelper.login(narasappa);

        // Creating a HR Designation Type
        hrMasterDesignationCreateTestMethod();
    }

    private void hrMasterDesignationCreateTestMethod() throws IOException {

        RequestInfo requestInfo = new RequestInfoBuilder().build();
        Designation designation = new DesignationBuilder().build();

        HRMasterDesignationCreateRequest hrMasterDesignationCreateRequest =
                new HRMasterDesignationCreateRequestBuilder()
                        .withRequestInfo(requestInfo)
                        .withDesignation(designation)
                        .build();


        Response response = new HRMasterDesignationResource().create(RequestHelper.getJsonString(hrMasterDesignationCreateRequest));
        HRMasterDesignationCreateSearchAndUpdateResponse hrMasterDesignationCreateResponse = (HRMasterDesignationCreateSearchAndUpdateResponse)
                ResponseHelper.getResponseAsObject(response.asString(), HRMasterDesignationCreateSearchAndUpdateResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        new APILogger().log("HR Master Designation Create Test is Completed --");

        // Search A Created Designation
        hrMasterDesignationSearchTestMethod(hrMasterDesignationCreateResponse.getDesignation()[0].getName(), hrMasterDesignationCreateResponse);
    }

    private void hrMasterDesignationSearchTestMethod(String designationName, HRMasterDesignationCreateSearchAndUpdateResponse hrMasterDesignationCreateRequest) throws IOException {

        RequestInfo requestInfo = new RequestInfoBuilder().build();
        HRMasterDesignationSearchRequest hrMasterDesignationSearchRequest =
                new HRMasterDesignationSearchRequestBuilder()
                        .withRequestInfo(requestInfo)
                        .build();

        Response response = new HRMasterDesignationResource().search(RequestHelper.getJsonString(hrMasterDesignationSearchRequest),
                designationName);

        HRMasterDesignationCreateSearchAndUpdateResponse hrMasterDesignationSearchResponse = (HRMasterDesignationCreateSearchAndUpdateResponse)
                ResponseHelper.getResponseAsObject(response.asString(), HRMasterDesignationCreateSearchAndUpdateResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(hrMasterDesignationSearchResponse.getDesignation()[0].getName(), designationName);
        new APILogger().log("HR Master Designation Search Test is Completed --");

        // Update the Designation
        hrMasterDesignationUpdateTestMethod(hrMasterDesignationSearchResponse.getDesignation()[0].getId(), hrMasterDesignationCreateRequest);

    }

    private void hrMasterDesignationUpdateTestMethod(int id, HRMasterDesignationCreateSearchAndUpdateResponse hrMasterDesignationCreateRequest) throws IOException {

        RequestInfo requestInfo = new RequestInfoBuilder().build();
        Designation designation = new DesignationBuilder()
                .withName(hrMasterDesignationCreateRequest.getDesignation()[0].getName())
                .withTenantId(hrMasterDesignationCreateRequest.getDesignation()[0].getTenantId())
                .withCode(hrMasterDesignationCreateRequest.getDesignation()[0].getCode())
                .withDescription("Modified Description")
                .withChartOfAccounts(hrMasterDesignationCreateRequest.getDesignation()[0].getChartOfAccounts())
                .withActive(hrMasterDesignationCreateRequest.getDesignation()[0].getActive())
                .build();

        HRMasterDesignationCreateRequest hrMasterDesignationUpdateRequest =
                new HRMasterDesignationCreateRequestBuilder()
                        .withRequestInfo(requestInfo)
                        .withDesignation(designation)
                        .build();

        Response response = new HRMasterDesignationResource().update(RequestHelper.getJsonString(hrMasterDesignationUpdateRequest), id);

        HRMasterDesignationCreateSearchAndUpdateResponse hrMasterDesignationUpdateResponse = (HRMasterDesignationCreateSearchAndUpdateResponse)
                ResponseHelper.getResponseAsObject(response.asString(), HRMasterDesignationCreateSearchAndUpdateResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(hrMasterDesignationUpdateResponse.getDesignation()[0].getDescription(), "Modified Description");
        new APILogger().log("HR Master Designation Update Test is Completed --");

        // Search the designation after update
        hrMasterDesignationSearchAfterUpdateTestMethod(hrMasterDesignationUpdateResponse.getDesignation()[0].getName());

    }

    private void hrMasterDesignationSearchAfterUpdateTestMethod(String name) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().build();
        HRMasterDesignationSearchRequest hrMasterDesignationSearchRequest =
                new HRMasterDesignationSearchRequestBuilder()
                        .withRequestInfo(requestInfo)
                        .build();

        Response response = new HRMasterDesignationResource().search(RequestHelper.getJsonString(hrMasterDesignationSearchRequest),
                name);

        HRMasterDesignationCreateSearchAndUpdateResponse hrMasterDesignationSearchAfterUpdateResponse = (HRMasterDesignationCreateSearchAndUpdateResponse)
                ResponseHelper.getResponseAsObject(response.asString(), HRMasterDesignationCreateSearchAndUpdateResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(hrMasterDesignationSearchAfterUpdateResponse.getDesignation()[0].getDescription(), "Modified Description");
        new APILogger().log("HR Master Designation Search After Update Test is Completed --");
    }
}

package tests.pgrCollection;

import builders.pgrCollection.createComplaint.ComplaintRequestBuilder;
import builders.pgrCollection.createComplaint.RequestInfoBuilder;
import builders.pgrCollection.createComplaint.ServiceRequestBuilder;
import builders.pgrCollection.searchComplaint.SearchComplaintRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.pgrCollections.createComplaint.ComplaintRequest;
import entities.requests.pgrCollections.createComplaint.RequestInfo;
import entities.requests.pgrCollections.createComplaint.ServiceRequest;
import entities.requests.pgrCollections.searchComplaint.SearchComplaintRequest;
import entities.responses.login.LoginResponse;
import entities.responses.pgrCollections.CreateComplaint.ComplaintResponse;
import entities.responses.pgrCollections.SearchComplaint.SearchComplaintResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.PGRResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static data.UserData.NARASAPPA;

public class ComplaintVerificationTest extends BaseAPITest {

    @Test(groups = {Categories.PGR, Categories.SANITY, Categories.DEV})
    public void createAndGetComplaintInPGR() throws IOException {

        //Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        // Create A Complaint
        ComplaintResponse create = createComplaintInPGR(loginResponse);

        // Get Complaint
        getComplaintInPGR(create,loginResponse);

        // Update Complaint
        ComplaintResponse update = updateComplaintInPGR(create,loginResponse);

        // Get Complaint
//        getComplaintInPGR(update,loginResponse);

        // Logout Test
        LoginAndLogoutHelper.logout(loginResponse);
    }

    private ComplaintResponse createComplaintInPGR(LoginResponse loginResponse) throws IOException {

        new APILogger().log("Creating complaint test for PGR is Started  -- ");

        RequestInfo requestInfo = new RequestInfoBuilder().build();

        ComplaintRequest request = new ComplaintRequestBuilder().withRequestInfo(requestInfo).build();

        String jsonString = RequestHelper.getJsonString(request);

        Response response = new PGRResource().createComplaint(jsonString);

        Assert.assertEquals(response.getStatusCode(), 201);

        ComplaintResponse response1 = (ComplaintResponse)
                                           ResponseHelper.getResponseAsObject(response.asString(),ComplaintResponse.class);

        Assert.assertEquals(request.getServiceRequest().getDescription(),response1.getServiceRequests()[0].getDescription());

        new APILogger().log("Creating complaint test for PGR is completed  -- ");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return response1;
    }

    private void getComplaintInPGR(ComplaintResponse complaint,LoginResponse loginResponse) throws IOException {

        new APILogger().log("Getting a PGR complaint test is started -- ");

        entities.requests.pgrCollections.searchComplaint.RequestInfo requestInfo = new
                               builders.pgrCollection.searchComplaint.RequestInfoBuilder()
                              .withAuthToken(loginResponse.getAccess_token()).build();

        SearchComplaintRequest request = new SearchComplaintRequestBuilder().withRequestInfo(requestInfo).build();

        String jsonString = RequestHelper.getJsonString(request);

        Response response = new PGRResource().getPGRComplaint(complaint.getServiceRequests()[0].getServiceRequestId(),jsonString);

        Assert.assertEquals(response.getStatusCode(), 200);

        SearchComplaintResponse response1 = (SearchComplaintResponse)
                               ResponseHelper.getResponseAsObject(response.asString(),SearchComplaintResponse.class);

        Assert.assertEquals(complaint.getServiceRequests()[0].getServiceRequestId(),response1.getServiceRequests()[0].getServiceRequestId());

        String status = null;

        for(int i=0;i<complaint.getServiceRequests()[0].getAttribValues().length;i++){
            if(complaint.getServiceRequests()[0].getAttribValues()[i].getKey().equals("status")){
                status = complaint.getServiceRequests()[0].getAttribValues()[i].getName();
            }
        }

        String getStatus = null;

        for(int i=0;i<response1.getServiceRequests()[0].getAttribValues().length;i++){
            if(response1.getServiceRequests()[0].getAttribValues()[i].getKey().equals("status")){
                getStatus = response1.getServiceRequests()[0].getAttribValues()[i].getName();
            }
        }

        Assert.assertEquals(getStatus,status);

        new APILogger().log("Getting a PGR complaint test is Completed -- ");
    }


    private ComplaintResponse updateComplaintInPGR(ComplaintResponse complaint,LoginResponse loginResponse) throws IOException {

        new APILogger().log("Update complaint for PGR test is started  -- ");

        RequestInfo requestInfo = new RequestInfoBuilder("").withAuth_token(loginResponse.getAccess_token()).build();

        ServiceRequest serviceRequest = new ServiceRequestBuilder("").withServiceRequestId(complaint.getServiceRequests()[0].getServiceRequestId()).build();

        ComplaintRequest request = new ComplaintRequestBuilder("").withServiceRequest(serviceRequest)
                                       .withRequestInfo(requestInfo).build();

        String jsonString  = RequestHelper.getJsonString(request);

        Response response = new PGRResource().updateAndClosePGRComplaint(jsonString);

        Assert.assertEquals(response.getStatusCode(),200);

        ComplaintResponse response1 = (ComplaintResponse)
                                 ResponseHelper.getResponseAsObject(response.asString(),ComplaintResponse.class);

        Assert.assertEquals(complaint.getServiceRequests()[0].getServiceRequestId(),response1.getServiceRequests()[0].getServiceRequestId());

        Assert.assertEquals(request.getServiceRequest().getDescription(),response1.getServiceRequests()[0].getDescription());

        new APILogger().log("Update complaint for PGR test is Completed  -- ");

        return response1;
    }
}

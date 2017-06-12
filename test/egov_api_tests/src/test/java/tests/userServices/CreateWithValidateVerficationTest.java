package tests.userServices;

import builders.userServices.createNoValidate.*;
import builders.userServices.createWithValidate.*;
import builders.userServices.createWithValidate.RequestInfoBuilder;
import builders.userServices.createWithValidate.UserBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.userServices.createNoValidate.*;
import entities.requests.userServices.createWithValidate.*;
import entities.requests.userServices.createWithValidate.RequestInfo;
import entities.requests.userServices.createWithValidate.User;
import entities.responses.login.LoginResponse;
import entities.responses.userServices.createUser.UserResponse;
import entities.responses.userServices.createUserWithValidation.OtpResponse;
import entities.responses.userServices.getUser.GetUserResponse;
import entities.responses.userServices.searchOtp.SearchOtpResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.UserServiceResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class CreateWithValidateVerficationTest extends BaseAPITest {

    @Test(groups = {Categories.SANITY, Categories.DEV, Categories.USER})
    public void createWithValidateAndGetTest() throws IOException {

        // Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        //Create OTP
        OtpResponse otp = createOtp(loginResponse);

        //Search OTP
        searchOtp(otp, loginResponse);

        //Validate OTP
        OtpResponse validatedOtp = validateOtp(loginResponse, otp);

        //Search OTP
        searchOtp(otp, loginResponse);

        //Create User With Validation
        UserResponse user = createUser(loginResponse, validatedOtp);

        //Get User Details with id
        getTheNewlyCreatedUser(loginResponse,user,"id");

        //Get User Details with username
        getTheNewlyCreatedUser(loginResponse,user,"userName");
    }

    private void searchOtp(OtpResponse otpGene, LoginResponse loginResponse) throws IOException {

        new APILogger().log("Search OTP Test is started ---");

        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        Otp otp = new OtpBuilder().withUuid(otpGene.getOtp().getUUID()).build();

        SearchOtpRequest request = new SearchOtpRequestBuilder().withRequestInfo(requestInfo).withOtp(otp).build();

        String json = RequestHelper.getJsonString(request);

        Response response = new UserServiceResource().searchOtp(json);

        Assert.assertEquals(response.getStatusCode(), 200);

        SearchOtpResponse response1 = (SearchOtpResponse)
                ResponseHelper.getResponseAsObject(response.asString(), SearchOtpResponse.class);

        System.out.println("Is OTP Validated :" + response1.getOtp().getIsValidationSuccessful());

        new APILogger().log("Search OTP Test is completed ---");
    }

    private UserResponse createUser(LoginResponse loginResponse, OtpResponse validatedOtp) throws IOException {

        new APILogger().log("Create User Test with OTP is started ---");

        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        User user = new UserBuilder()
                .withUserName("Test_" + get3DigitRandomInt() + get3DigitRandomInt())
                .withMobileNumber(validatedOtp.getOtp().getIdentity())
                .withOtpReference(validatedOtp.getOtp().getUUID())
                .build();

        CreateUserValidateRequest request = new CreateUserValidateRequestBuilder().withRequestInfo(requestInfo)
                .withUser(user).build();

        String json = RequestHelper.getJsonString(request);

        Response response = new UserServiceResource().createCitizenUser(json);

        Assert.assertEquals(response.getStatusCode(), 200);

        UserResponse response1 = (UserResponse)
                ResponseHelper.getResponseAsObject(response.asString(), UserResponse.class);

        Assert.assertEquals(response1.getUser()[0].getMobileNumber(), validatedOtp.getOtp().getIdentity());
        Assert.assertEquals(request.getUser().getUserName(), response1.getUser()[0].getUserName());

        new APILogger().log("Create User Test with OTP is completed ---");

        return response1;
    }

    private OtpResponse validateOtp(LoginResponse loginResponse, OtpResponse otpgenerated) throws IOException {

        new APILogger().log("Validate OTP Test is started ---");

        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        Otp otp = new OtpBuilder().withIdentity(otpgenerated.getOtp().getIdentity()).withOtp(otpgenerated.getOtp().getOtp()).build();

        ValidateOtpRequest request = new ValidateOtpRequestBuilder().withRequestInfo(requestInfo).withOtp(otp).build();

        String json = RequestHelper.getJsonString(request);

        Response response = new UserServiceResource().validateOtp(json);

        System.out.println(response.getHeaders().get("x-correlation-id"));

        Assert.assertEquals(response.getStatusCode(), 200);

        OtpResponse response1 = (OtpResponse)
                ResponseHelper.getResponseAsObject(response.asString(), OtpResponse.class);

        Assert.assertEquals(otpgenerated.getOtp().getIdentity(), response1.getOtp().getIdentity());

        Assert.assertTrue(response1.getOtp().getIsValidationSuccessful());

        new APILogger().log("Validate OTP Test is completed ---");

        return response1;
    }

    private OtpResponse createOtp(LoginResponse loginResponse) throws IOException {

        new APILogger().log("Create OTP Test is started ---");

        String phoneNo = "9" + get3DigitRandomInt() + get3DigitRandomInt() + get3DigitRandomInt();

        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        Otp otp = new OtpBuilder().withIdentity(phoneNo).build();

        CreateOtpRequest request = new CreateOtpRequestBuilder().withRequestInfo(requestInfo).withOtp(otp).build();

        String json = RequestHelper.getJsonString(request);

        Response response = new UserServiceResource().createOtp(json);

        Assert.assertEquals(response.getStatusCode(), 201);

        OtpResponse response1 = (OtpResponse)
                ResponseHelper.getResponseAsObject(response.asString(), OtpResponse.class);

        Assert.assertEquals(phoneNo, response1.getOtp().getIdentity());

        new APILogger().log("Create OTP Test is completed ---");

        return response1;
    }

    private void getTheNewlyCreatedUser(LoginResponse loginResponse, UserResponse create, String searchType) throws IOException {

        new APILogger().log("get user details with search type " + searchType + " is started ---");

        entities.requests.userServices.createNoValidate.RequestInfo requestInfo = new builders.userServices.createNoValidate.RequestInfoBuilder("").withAuthToken(loginResponse.getAccess_token()).build();

        GetUserRequest request = new GetUserRequest();

        switch (searchType) {
            case "id":
                int[] ids = new int[1];
                int id = create.getUser()[0].getId();
                ids[0] = id;
                request = new GetUserRequestBuilder().withRequestInfo(requestInfo).withId(ids).build();
                break;

            case "userName":
                request = new GetUserRequestBuilder().withRequestInfo(requestInfo).withUserName(create.getUser()[0].getUserName()).build();
                break;
        }

        String json = RequestHelper.getJsonString(request);

        Response response = new UserServiceResource().getUserDetails(json);

        Assert.assertEquals(response.getStatusCode(), 200);

        GetUserResponse response1 = (GetUserResponse)
                ResponseHelper.getResponseAsObject(response.asString(), GetUserResponse.class);

        Assert.assertEquals(response1.getUser()[0].getUserName(), create.getUser()[0].getUserName());

        new APILogger().log("get user details with search type " + searchType + " is completed ---");
    }
}
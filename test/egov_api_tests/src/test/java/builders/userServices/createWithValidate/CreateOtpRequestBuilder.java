package builders.userServices.createWithValidate;

import entities.requests.userServices.createWithValidate.CreateOtpRequest;
import entities.requests.userServices.createWithValidate.Otp;
import entities.requests.userServices.createWithValidate.RequestInfo;

public class CreateOtpRequestBuilder {

    CreateOtpRequest request = new CreateOtpRequest();

    public CreateOtpRequest build(){
        return request;
    }

    public CreateOtpRequestBuilder withRequestInfo(RequestInfo requestInfo){
        request.setRequestInfo(requestInfo);
        return this;
    }

    public CreateOtpRequestBuilder withOtp(Otp otp){
        request.setOtp(otp);
        return this;
    }
}

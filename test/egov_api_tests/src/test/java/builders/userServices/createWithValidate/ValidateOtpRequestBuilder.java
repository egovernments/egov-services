package builders.userServices.createWithValidate;

import entities.requests.userServices.createWithValidate.Otp;
import entities.requests.userServices.createWithValidate.RequestInfo;
import entities.requests.userServices.createWithValidate.ValidateOtpRequest;

public class ValidateOtpRequestBuilder {

    ValidateOtpRequest request = new ValidateOtpRequest();

    public ValidateOtpRequestBuilder withRequestInfo(RequestInfo requestInfo){
        request.setRequestInfo(requestInfo);
        return this;
    }

    public ValidateOtpRequestBuilder withOtp(Otp otp){
        request.setOtp(otp);
        return this;
    }

    public ValidateOtpRequest build(){
        return request;
    }
}

package builders.userServices.createWithValidate;

import entities.requests.userServices.createWithValidate.Otp;
import entities.requests.userServices.createWithValidate.RequestInfo;
import entities.requests.userServices.createWithValidate.SearchOtpRequest;

public class SearchOtpRequestBuilder {

    SearchOtpRequest request = new SearchOtpRequest();

    public SearchOtpRequestBuilder withRequestInfo(RequestInfo requestInfo){
        request.setRequestInfo(requestInfo);
        return this;
    }

    public SearchOtpRequestBuilder withOtp(Otp otp){
        request.setOtp(otp);
        return this;
    }

    public SearchOtpRequest build(){
        return request;
    }
}

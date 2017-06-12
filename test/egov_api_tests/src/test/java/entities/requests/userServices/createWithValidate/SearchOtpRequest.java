package entities.requests.userServices.createWithValidate;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchOtpRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    private Otp otp;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public Otp getOtp() {
        return otp;
    }

    public void setOtp(Otp otp) {
        this.otp = otp;
    }
}

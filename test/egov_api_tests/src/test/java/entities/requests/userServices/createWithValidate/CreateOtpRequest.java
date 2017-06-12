package entities.requests.userServices.createWithValidate;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateOtpRequest {

    @JsonProperty("Otp")
    private Otp otp;

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public Otp getOtp() {
        return this.otp;
    }

    public void setOtp(Otp otp) {
        this.otp = otp;
    }

    public RequestInfo getRequestInfo() {
        return this.RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}

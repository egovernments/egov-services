package entities.responses.userServices.createUserWithValidation;

import org.codehaus.jackson.annotate.JsonProperty;

public class OtpResponse {

    @JsonProperty("Otp")
    private Otp otp;

    private ResponseInfo responseInfo;

    public Otp getOtp() {
        return this.otp;
    }

    public void setOtp(Otp otpnu) {
        this.otp = otpnu;
    }

    public ResponseInfo getResponseInfo() {
        return this.responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }
}

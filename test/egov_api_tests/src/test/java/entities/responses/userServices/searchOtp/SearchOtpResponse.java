package entities.responses.userServices.searchOtp;

public class SearchOtpResponse {
    private Otp otp;
    private Object responseInfo;

    public Otp getOtp() {
        return this.otp;
    }

    public void setOtp(Otp otp) {
        this.otp = otp;
    }

    public Object getResponseInfo() {
        return this.responseInfo;
    }

    public void setResponseInfo(Object responseInfo) {
        this.responseInfo = responseInfo;
    }
}

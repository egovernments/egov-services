package builders.userServices.createWithValidate;

import entities.requests.userServices.createWithValidate.Otp;

public class OtpBuilder {

    Otp otp = new Otp();

    public OtpBuilder(){
        otp.setTenantId("default");
    }

    public OtpBuilder withIdentity(String identity){
        otp.setIdentity(identity);
        return this;
    }

    public OtpBuilder withOtp(String otpNumber){
        otp.setOtp(otpNumber);
        return this;
    }

    public OtpBuilder withUuid(String uuid){
        otp.setUUID(uuid);
        return this;
    }

    public Otp build(){
        return otp;
    }
}

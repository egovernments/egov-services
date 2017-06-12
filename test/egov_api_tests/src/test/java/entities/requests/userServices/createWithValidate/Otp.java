package entities.requests.userServices.createWithValidate;

public class Otp {

    private String identity;
    private String tenantId;
    private String otp;
    private String UUID;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getIdentity() {
        return this.identity;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}

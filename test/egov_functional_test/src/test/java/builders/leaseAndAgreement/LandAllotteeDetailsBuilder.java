package builders.leaseAndAgreement;

import entities.leaseAndAgreement.LandAllotteeDetails;

public final class LandAllotteeDetailsBuilder {
    LandAllotteeDetails landAllotteeDetails = new LandAllotteeDetails();

    public LandAllotteeDetailsBuilder withAadharNumber(String aadharNumber) {
        landAllotteeDetails.setAadharNumber(aadharNumber);
        return this;
    }

    public LandAllotteeDetailsBuilder withMobileNumber(String mobileNumber) {
        landAllotteeDetails.setMobileNumber(mobileNumber);
        return this;
    }

    public LandAllotteeDetailsBuilder withName(String name) {
        landAllotteeDetails.setName(name);
        return this;
    }

    public LandAllotteeDetailsBuilder withEmail(String emailIdx) {
        landAllotteeDetails.setEmail(emailIdx);
        return this;
    }

    public LandAllotteeDetailsBuilder withPan(String pan) {
        landAllotteeDetails.setPan(pan);
        return this;
    }

    public LandAllotteeDetails build() {
        return landAllotteeDetails;
    }
}

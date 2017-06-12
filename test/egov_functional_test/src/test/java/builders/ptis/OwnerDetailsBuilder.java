package builders.ptis;

import entities.ptis.OwnerDetails;

public class OwnerDetailsBuilder {

    OwnerDetails ownerDetails = new OwnerDetails();

    public OwnerDetailsBuilder withMobileNumber(String mobileNumber) {
        ownerDetails.setMobileNumber(mobileNumber);
        return this;
    }

    public OwnerDetailsBuilder withOwnerName(String ownerName) {
        ownerDetails.setOwnerName(ownerName);
        return this;
    }

    public OwnerDetailsBuilder withGender(String gender) {
        ownerDetails.setGender(gender);
        return this;
    }

    public OwnerDetailsBuilder withEmailAddress(String emailAddress) {
        ownerDetails.setEmailAddress(emailAddress);
        return this;
    }

    public OwnerDetailsBuilder withGuardianRelation(String guardianRelation) {
        ownerDetails.setGuardianRelation(guardianRelation);
        return this;
    }

    public OwnerDetailsBuilder withGuardianName(String guardianName) {
        ownerDetails.setGuardianName(guardianName);
        return this;
    }

    public OwnerDetails build() {
        return ownerDetails;
    }
}

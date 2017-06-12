package builders.grievances;

import entities.grievances.CreateComplaintDetails;

public class CreateComplaintDetailsBuilder {

    CreateComplaintDetails createComplaintDetails = new CreateComplaintDetails();

    public CreateComplaintDetailsBuilder withCitizenName(String citizenName) {
        createComplaintDetails.setCitizenname(citizenName);
        return this;
    }

    public CreateComplaintDetailsBuilder withCitizenMobNo(String citizenMobNo) {
        createComplaintDetails.setCitizenMobNo(citizenMobNo);
        return this;
    }

    public CreateComplaintDetailsBuilder withEmailId(String emailId) {
        createComplaintDetails.setEmailId(emailId);
        return this;
    }

    public CreateComplaintDetailsBuilder withGrievanceCategory(String grievanceCategory) {
        createComplaintDetails.setGrievanceCategory(grievanceCategory);
        return this;
    }

    public CreateComplaintDetailsBuilder withGrievanceType(String grievanceType) {
        createComplaintDetails.setGrievanceType(grievanceType);
        return this;
    }

    public CreateComplaintDetailsBuilder withGrievanceDetails(String grievanceDetailsText) {
        createComplaintDetails.setGrievanceDetails(grievanceDetailsText);
        return this;
    }

    public CreateComplaintDetailsBuilder withGrievanceLocation(String grievanceLocation) {
        createComplaintDetails.setGrievanceLocation(grievanceLocation);
        return this;
    }

    public CreateComplaintDetailsBuilder withLocationLandmark(String locationLandmark) {
        createComplaintDetails.setLocationLandmark(locationLandmark);
        return this;
    }

    public CreateComplaintDetails build() {
        return createComplaintDetails;
    }
}

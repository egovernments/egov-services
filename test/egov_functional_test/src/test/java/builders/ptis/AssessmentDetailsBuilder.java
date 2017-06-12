package builders.ptis;

import entities.ptis.AssessmentDetails;

public class AssessmentDetailsBuilder {

    AssessmentDetails assessmentDetails = new AssessmentDetails();

    public AssessmentDetailsBuilder withReasonForCreation(String reasonForCreation) {
        assessmentDetails.setReasonForCreation(reasonForCreation);
        return this;
    }

    public AssessmentDetailsBuilder withExtentOfSite(String extentOfSite) {
        assessmentDetails.setExtentOfSite(extentOfSite);
        return this;
    }

    public AssessmentDetailsBuilder withOccupancyCertificateNumber(String occupancyCertificateNumber) {
        assessmentDetails.setOccupancyCertificateNumber(occupancyCertificateNumber);
        return this;
    }

    public AssessmentDetailsBuilder withBifurcationReasonForcreation(String bifurcationreasonForCreation) {
        assessmentDetails.setBifurcationReasonForCreation(bifurcationreasonForCreation);
        return this;
    }

    public AssessmentDetailsBuilder withParentAssessmentNo(String parentAssessmentNo) {
        assessmentDetails.setParentAssessmentNo(parentAssessmentNo);
        return this;
    }

    public AssessmentDetails build() {
        return assessmentDetails;
    }


}


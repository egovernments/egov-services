package builders.ptis;

import entities.ptis.EditAssessmentDetails;

public class EditAssessmentDetailsBuilder {

    EditAssessmentDetails editAssesmentDetails = new EditAssessmentDetails();

    public EditAssessmentDetailsBuilder() {
        editAssesmentDetails.setExtentOfSite("100");
    }

    public EditAssessmentDetailsBuilder withExtentOfSite(String extentOfSite) {
        editAssesmentDetails.setExtentOfSite(extentOfSite);
        return this;
    }

    public EditAssessmentDetailsBuilder withOccupancyCertificateNumber(String certificateNumber) {
        editAssesmentDetails.setOccupancyCertificateNumber(certificateNumber);
        return this;
    }

    public EditAssessmentDetails build() {
        return editAssesmentDetails;
    }

}

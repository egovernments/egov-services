package builders.wcms;

import entities.wcms.ApplicantInfo;

public class ApplicantInfoBuilder {

    ApplicantInfo applicantInfo = new ApplicantInfo();

    public ApplicantInfoBuilder withPTAssessmentNumber(String assessmentNumber) {
        applicantInfo.setPtAssessmentNumber(assessmentNumber);
        return this;
    }

    public ApplicantInfoBuilder withHSCNumber(String hscNumber) {
        applicantInfo.setHscNumber(hscNumber);
        return this;
    }

    public ApplicantInfoBuilder withConnectionDate(String connectionDate) {
        applicantInfo.setConnectionDate(connectionDate);
        return this;
    }

    public ApplicantInfo build() {
        return applicantInfo;
    }
}

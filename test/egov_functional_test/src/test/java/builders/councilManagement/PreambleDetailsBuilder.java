package builders.councilManagement;

import entities.councilManagement.CreatePreambleDetails;

public class PreambleDetailsBuilder {

    CreatePreambleDetails createPreambleDetails = new CreatePreambleDetails();

    public PreambleDetailsBuilder withPreambleDepartment(String preambleDepartment) {
        createPreambleDetails.setPreambleDepartment(preambleDepartment);
        return this;
    }

    public PreambleDetailsBuilder withSanctionAmount(String amount) {
        createPreambleDetails.setAmount(amount);
        return this;
    }

    public PreambleDetailsBuilder withGistOfPreamble(String gistOfPreamble) {
        createPreambleDetails.setGistOfPreamble(gistOfPreamble);
        return this;
    }

    public PreambleDetailsBuilder withPreambleNumber(String preambleNumber) {
        createPreambleDetails.setPreambleNumber(preambleNumber);
        return this;
    }

    public PreambleDetailsBuilder withCommitteeType(String committeeType) {
        createPreambleDetails.setCommitteeType(committeeType);
        return this;
    }

    public PreambleDetailsBuilder withCouncilMeetingDate(String meetingDate) {
        createPreambleDetails.setCouncilMeetingDate(meetingDate);
        return this;
    }

    public PreambleDetailsBuilder withCouncilMeetingTime(String meetingTime) {
        createPreambleDetails.setCouncilMeetingTime(meetingTime);
        return this;
    }

    public PreambleDetailsBuilder withCouncilMeetingPlace(String meetingPlace) {
        createPreambleDetails.setCouncilMeetingPlace(meetingPlace);
        return this;
    }

    public PreambleDetailsBuilder withCouncilMOMResolution(String resolutionComment) {
        createPreambleDetails.setCouncilMOMResolution(resolutionComment);
        return this;
    }

    public PreambleDetailsBuilder withCouncilMOMAction(String actionTaken) {
        createPreambleDetails.setCouncilMOMAction(actionTaken);
        return this;
    }

    public CreatePreambleDetails build() {
        return createPreambleDetails;
    }
}

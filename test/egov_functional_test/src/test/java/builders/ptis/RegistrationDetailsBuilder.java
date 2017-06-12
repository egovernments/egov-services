package builders.ptis;

import entities.ptis.RegistrationDetails;

public class RegistrationDetailsBuilder {

    RegistrationDetails registrationDetails = new RegistrationDetails();

    public RegistrationDetailsBuilder withSellerExecutantName(String sellerExecutantName) {
        registrationDetails.setSellerExecutantName(sellerExecutantName);
        return this;
    }

    public RegistrationDetailsBuilder withBuyerClaimantName(String buyerClaimantName) {
        registrationDetails.setBuyerClaimantName(buyerClaimantName);
        return this;
    }

    public RegistrationDetailsBuilder withDoorNo(String doorNo) {
        registrationDetails.setDoorNo(doorNo);
        return this;
    }

    public RegistrationDetailsBuilder withPropertyAddress(String propertyAddress) {
        registrationDetails.setPropertyAddress(propertyAddress);
        return this;
    }

    public RegistrationDetailsBuilder withRegisteredPlinthArea(String registeredPlinthArea) {
        registrationDetails.setRegisteredPlinthArea(registeredPlinthArea);
        return this;
    }

    public RegistrationDetailsBuilder withRegisteredPlotArea(String registeredPlotArea) {
        registrationDetails.setRegisteredPlotArea(registeredPlotArea);
        return this;
    }

    public RegistrationDetailsBuilder withEastBoundary(String eastBoundary) {
        registrationDetails.setEastBoundary(eastBoundary);
        return this;
    }

    public RegistrationDetailsBuilder withWestBoundary(String westBoundary) {
        registrationDetails.setWestBoundary(westBoundary);
        return this;
    }

    public RegistrationDetailsBuilder withNorthBoundary(String northBoundary) {
        registrationDetails.setNorthBoundary(northBoundary);
        return this;
    }

    public RegistrationDetailsBuilder withSouthBoundary(String southBoundary) {
        registrationDetails.setSouthBoundary(southBoundary);
        return this;
    }

    public RegistrationDetailsBuilder withSroName(String sroName) {
        registrationDetails.setSroName(sroName);
        return this;
    }

    public RegistrationDetailsBuilder withReasonForChange(String reasonForChange) {
        registrationDetails.setReasonForChange(reasonForChange);
        return this;
    }

    public RegistrationDetailsBuilder withRegistrationDocumentNumber(String registrationDocumentNumber) {
        registrationDetails.setRegistrationDocumentNumber(registrationDocumentNumber);
        return this;
    }

    public RegistrationDetailsBuilder withRegistrationDocumentDate(String registrationDocumentDate) {
        registrationDetails.setRegistrationDocumentDate(registrationDocumentDate);
        return this;
    }

    public RegistrationDetailsBuilder withPartiesConsiderationValue(String partiesConsiderationValue) {
        registrationDetails.setPartiesConsiderationValue(partiesConsiderationValue);
        return this;
    }

    public RegistrationDetailsBuilder withdePartmentGuidelinesValue(String dePartmentGuidelinesValue) {
        registrationDetails.setDepartmentGuidelinesValue(dePartmentGuidelinesValue);
        return this;
    }

    public RegistrationDetails build() {
        return registrationDetails;
    }
}
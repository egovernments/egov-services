package builders.assetManagement.assetService;

import entities.responses.assetManagement.assetService.LocationDetails;

public final class LocationDetailsBuilder {

    LocationDetails locationDetails = new LocationDetails();

    public LocationDetailsBuilder() {
        locationDetails.setLocality(1);
        locationDetails.setZone(null);
        locationDetails.setRevenueWard(null);
        locationDetails.setBlock(null);
        locationDetails.setStreet(null);
        locationDetails.setElectionWard(2);
        locationDetails.setDoorNo(null);
        locationDetails.setPinCode(null);
    }

    public LocationDetailsBuilder withZone(String zone) {
        locationDetails.setZone(zone);
        return this;
    }

    public LocationDetailsBuilder withStreet(String street) {
        locationDetails.setStreet(street);
        return this;
    }

    public LocationDetailsBuilder withRevenueWard(String revenueWard) {
        locationDetails.setRevenueWard(revenueWard);
        return this;
    }

    public LocationDetailsBuilder withPinCode(String pinCode) {
        locationDetails.setPinCode(pinCode);
        return this;
    }

    public LocationDetailsBuilder withLocality(int locality) {
        locationDetails.setLocality(locality);
        return this;
    }

    public LocationDetailsBuilder withBlock(String block) {
        locationDetails.setBlock(block);
        return this;
    }

    public LocationDetailsBuilder withDoorNo(String doorNo) {
        locationDetails.setDoorNo(doorNo);
        return this;
    }

    public LocationDetailsBuilder withElectionWard(int electionWard) {
        locationDetails.setElectionWard(electionWard);
        return this;
    }

    public LocationDetails build() {
        return locationDetails;
    }
}

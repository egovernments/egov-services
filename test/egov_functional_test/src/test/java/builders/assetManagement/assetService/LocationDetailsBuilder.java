package builders.assetManagement.assetService;

import entities.assetManagement.assetService.LocationDetails;

public final class LocationDetailsBuilder {

    LocationDetails locationDetails = new LocationDetails();

    public LocationDetailsBuilder withLocality(String locality) {
        locationDetails.setLocality(locality);
        return this;
    }

    public LocationDetailsBuilder withRevenueWard(String revenueWard) {
        locationDetails.setRevenueWard(revenueWard);
        return this;
    }

    public LocationDetailsBuilder withBlockNumber(String blockNumber) {
        locationDetails.setBlockNumber(blockNumber);
        return this;
    }

    public LocationDetailsBuilder withStreet(String street) {
        locationDetails.setStreet(street);
        return this;
    }

    public LocationDetailsBuilder withElectionWardNumber(String electionWardNumber) {
        locationDetails.setElectionWardNumber(electionWardNumber);
        return this;
    }

    public LocationDetailsBuilder withDoorNo(String doorNo) {
        locationDetails.setDoorNo(doorNo);
        return this;
    }

    public LocationDetailsBuilder withZoneNumber(String zoneNumber) {
        locationDetails.setZoneNumber(zoneNumber);
        return this;
    }

    public LocationDetailsBuilder withPinCode(String pinCode) {
        locationDetails.setPinCode(pinCode);
        return this;
    }

    public LocationDetails build() {
        return locationDetails;
    }
}

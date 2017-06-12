package builders.ptis;

import entities.ptis.PropertyAddressDetails;

public class AddressDetailsBuilder {

    PropertyAddressDetails addressDetails = new PropertyAddressDetails();

    public AddressDetailsBuilder withLocality(String locality) {
        addressDetails.setLocality(locality);
        return this;
    }

    public AddressDetailsBuilder withZoneNumber(String zoneNumber) {
        addressDetails.setZoneNumber(zoneNumber);
        return this;
    }

    public AddressDetailsBuilder withElectionWard(String electionWard) {
        addressDetails.setElectionWard(electionWard);
        return this;
    }

    public AddressDetailsBuilder withDoorNumber(String doorNumber) {
        addressDetails.setDoorNumber(doorNumber);
        return this;
    }

    public AddressDetailsBuilder withPincode(String pincode) {
        addressDetails.setPincode(pincode);
        return this;
    }

    public PropertyAddressDetails build() {
        return addressDetails;
    }
}

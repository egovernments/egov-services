package builders.advertisementTax;

import entities.advertisementTax.LocalityDetails;

public class LocalityDetailsBuilder {

    LocalityDetails localityDetails = new LocalityDetails();

    public LocalityDetailsBuilder() {
    }

    public LocalityDetailsBuilder withLocality(String locality) {
        localityDetails.setLocality(locality);
        return this;
    }

    public LocalityDetailsBuilder withLocalityAddress(String localityAddress) {
        localityDetails.setLocalityAddress(localityAddress);
        return this;
    }

    public LocalityDetailsBuilder withElectionWard(String electionWard) {
        localityDetails.setElectionWard(electionWard);
        return this;
    }

    public LocalityDetails build() {
        return localityDetails;
    }
}


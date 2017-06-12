package builders.tradeLicense;

import entities.tradeLicense.SearchTradeDetails;

public class SearchTradeDetailsBuilder {

    SearchTradeDetails searchTradeDetails = new SearchTradeDetails();

    public SearchTradeDetailsBuilder withApplicationNumber(String applicationNumber) {
        searchTradeDetails.setApplicationNumber(applicationNumber);
        return this;
    }

    public SearchTradeDetailsBuilder withLicenseNumber(String licenseNumber) {
        searchTradeDetails.setLicenseNumber(licenseNumber);
        return this;
    }

    public SearchTradeDetails build() {
        return searchTradeDetails;
    }
}

package builders.ptis;

import entities.ptis.SearchDetails;

public class SearchDetailsBuilder {

    SearchDetails searchDetails = new SearchDetails();

    public SearchDetailsBuilder withAssessmentNumber(String searchValue) {
        searchDetails.setSearchValue1(searchValue);
        return this;
    }

    public SearchDetailsBuilder withMobileNumber(String searchValue) {
        searchDetails.setSearchValue1(searchValue);
        return this;
    }

    public SearchDetailsBuilder withDoorNumber(String searchValue) {
        searchDetails.setSearchValue1(searchValue);
        return this;
    }

    public SearchDetailsBuilder withZoneNumber(String zoneNumber) {
        searchDetails.setSearchValue1(zoneNumber);
        return this;
    }

    public SearchDetailsBuilder withWardNumber(String wardNumber) {
        searchDetails.setSearchValue2(wardNumber);
        return this;
    }

    public SearchDetailsBuilder withHouseNo(String houseNo) {
        searchDetails.setSearchValue3(houseNo);
        return this;
    }

    public SearchDetailsBuilder withOwnerName(String ownerName) {
        searchDetails.setSearchValue4(ownerName);
        return this;
    }

    public SearchDetailsBuilder withLocation(String location) {
        searchDetails.setSearchValue3(location);
        return this;
    }

    public SearchDetailsBuilder withFrom(String from) {
        searchDetails.setSearchValue3(from);
        return this;
    }

    public SearchDetailsBuilder withTo(String to) {
        searchDetails.setSearchValue4(to);
        return this;
    }

    public SearchDetails build() {
        return searchDetails;
    }

}

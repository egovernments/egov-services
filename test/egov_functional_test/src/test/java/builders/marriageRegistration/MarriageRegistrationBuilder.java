package builders.marriageRegistration;

import entities.marriageRegistration.MarriageRegistrationInformation;

public class MarriageRegistrationBuilder {
    MarriageRegistrationInformation marriageRegistrationInformation = new MarriageRegistrationInformation();

    public MarriageRegistrationBuilder withRegistrationUnit(String registrationUnit) {
        marriageRegistrationInformation.setRegistrationUnit(registrationUnit);
        return this;
    }

    public MarriageRegistrationBuilder withStreet(String street) {
        marriageRegistrationInformation.setStreet(street);
        return this;
    }

    public MarriageRegistrationBuilder withLocality(String locality) {
        marriageRegistrationInformation.setLocality(locality);
        return this;
    }

    public MarriageRegistrationBuilder withCity(String city) {
        marriageRegistrationInformation.setCity(city);
        return this;
    }

    public MarriageRegistrationBuilder withDateOfMarraige(String dateOfMarraige) {
        marriageRegistrationInformation.setDateOfMarriage(dateOfMarraige);
        return this;
    }

    public MarriageRegistrationBuilder withVenueOfMarriage(String venueOfMarriage) {
        marriageRegistrationInformation.setVenueOfMarriage(venueOfMarriage);
        return this;
    }

    public MarriageRegistrationBuilder withPlaceOfMarriage(String placeOfMarriage) {
        marriageRegistrationInformation.setPlaceOfMarriage(placeOfMarriage);
        return this;
    }

    public MarriageRegistrationBuilder withFullName(String fullName) {
        marriageRegistrationInformation.setFullName(fullName);
        return this;
    }

    public MarriageRegistrationBuilder withFathersMothersName(String fathersMothersName) {
        marriageRegistrationInformation.setFathersMothersName(fathersMothersName);
        return this;
    }

    public MarriageRegistrationBuilder withReligion(String religion) {
        marriageRegistrationInformation.setReligion(religion);
        return this;
    }

    public MarriageRegistrationBuilder withStatusAtTheTimeMarriage(String statusAtTheTimeMarriage) {
        marriageRegistrationInformation.setStatusAtTheTimeMarriage(statusAtTheTimeMarriage);
        return this;
    }

    public MarriageRegistrationBuilder withResidenceAddress(String residenceAddress) {
        marriageRegistrationInformation.setResidenceAddress(residenceAddress);
        return this;
    }

    public MarriageRegistrationBuilder withOfficeAddress(String officeAddress) {
        marriageRegistrationInformation.setOfficeAddress(officeAddress);
        return this;
    }

    public MarriageRegistrationBuilder withPhoneNo(String phoneNo) {
        marriageRegistrationInformation.setPhoneNo(phoneNo);
        return this;
    }

    public MarriageRegistrationBuilder withOccupation(String occupation) {
        marriageRegistrationInformation.setOccupation(occupation);
        return this;
    }

    public MarriageRegistrationBuilder withEducationQualification(String educationQualification) {
        marriageRegistrationInformation.setEducationQualification(educationQualification);
        return this;
    }

    public MarriageRegistrationBuilder withNationality(String nationality) {
        marriageRegistrationInformation.setNationality(nationality);
        return this;
    }

    public MarriageRegistrationInformation build() {
        return marriageRegistrationInformation;
    }
}

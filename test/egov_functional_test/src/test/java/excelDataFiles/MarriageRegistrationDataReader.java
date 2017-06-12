package excelDataFiles;

import builders.marriageRegistration.MarriageRegistrationBuilder;
import entities.marriageRegistration.MarriageRegistrationInformation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class MarriageRegistrationDataReader extends ExcelReader {

    Sheet applicantsInformationSheet;
    Sheet bridegroomInformationSheet;

    public MarriageRegistrationDataReader(String testData) {
        super(testData);
        applicantsInformationSheet = workbook.getSheet("applicantsInformation");
        bridegroomInformationSheet = workbook.getSheet("bridegroomInformation");
    }

    public MarriageRegistrationInformation getApplicantsInformation(String applicantsInformation) {
        Row dataRow = readDataRow(applicantsInformationSheet, applicantsInformation);
        String RegistrationUnit = convertNumericToString(applicantsInformationSheet, dataRow, "RegistrationUnit");
        String Street = convertNumericToString(applicantsInformationSheet, dataRow, "Street");
        String Locality = convertNumericToString(applicantsInformationSheet, dataRow, "Locality");
        String City = convertNumericToString(applicantsInformationSheet, dataRow, "City");
        String VenueOfMarriage = convertNumericToString(applicantsInformationSheet, dataRow, "VenueOfMarriage");
        String PlaceOfMarriage = convertNumericToString(applicantsInformationSheet, dataRow, "PlaceOfMarriage");

        return new MarriageRegistrationBuilder()
                .withRegistrationUnit(RegistrationUnit)
                .withStreet(Street).withLocality(Locality)
                .withCity(City).withVenueOfMarriage(VenueOfMarriage).withPlaceOfMarriage(PlaceOfMarriage)
                .build();
    }

    public MarriageRegistrationInformation getBrideGroomInformation(String brideGroomInformation) {
        Row dataRow = readDataRow(bridegroomInformationSheet, brideGroomInformation);

        String FullName = convertNumericToString(bridegroomInformationSheet, dataRow, "FullName");
        String FathersMothersName = convertNumericToString(bridegroomInformationSheet, dataRow, "FathersMothersName");
        String Religion = convertNumericToString(bridegroomInformationSheet, dataRow, "Religion");
        String StatusAtTheTimeMarriage = convertNumericToString(bridegroomInformationSheet, dataRow, "StatusAtTheTimeMarriage");
        String Street = convertNumericToString(bridegroomInformationSheet, dataRow, "Street");
        String Locality = convertNumericToString(bridegroomInformationSheet, dataRow, "Locality");
        String City = convertNumericToString(bridegroomInformationSheet, dataRow, "City");
        String ResidenceAddress = convertNumericToString(bridegroomInformationSheet, dataRow, "ResidenceAddress");
        String OfficeAddress = convertNumericToString(bridegroomInformationSheet, dataRow, "OfficeAddress");
        String PhoneNo = convertNumericToString(bridegroomInformationSheet, dataRow, "PhoneNo");
        String Occupation = convertNumericToString(bridegroomInformationSheet, dataRow, "Occupation");
        String EducationQualification = convertNumericToString(bridegroomInformationSheet, dataRow, "EducationQualification");
        String Nationality = convertNumericToString(bridegroomInformationSheet, dataRow, "Nationality");

        Row dataRow2 = readDataRow(bridegroomInformationSheet, brideGroomInformation);

        String fullName = convertNumericToString(bridegroomInformationSheet, dataRow2, "FullName");
        String fathersMothersName = convertNumericToString(bridegroomInformationSheet, dataRow2, "FathersMothersName");
        String religion = convertNumericToString(bridegroomInformationSheet, dataRow2, "Religion");

        return new MarriageRegistrationBuilder()
                .withFullName(FullName).withFathersMothersName(FathersMothersName).withReligion(Religion)
                .withStatusAtTheTimeMarriage(StatusAtTheTimeMarriage).withResidenceAddress(ResidenceAddress)
                .withStreet(Street).withLocality(Locality).withCity(City)
                .withOfficeAddress(OfficeAddress).withPhoneNo(PhoneNo).withOccupation(Occupation)
                .withEducationQualification(EducationQualification).withNationality(Nationality)
                .withFullName(fullName).withFathersMothersName(fathersMothersName).withReligion(religion)
                .build();
    }
}

package excelDataFiles;

import builders.grievances.CreateComplaintDetailsBuilder;
import entities.grievances.CreateComplaintDetails;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class GrievanceDataReader extends ExcelReader {

    Sheet grievancesContactDetailsSheet;
    Sheet grievanceDetailsSheet;

    public GrievanceDataReader(String testData) {
        super(testData);
        grievancesContactDetailsSheet = workbook.getSheet("contactInfo");
        grievanceDetailsSheet = workbook.getSheet("grievanceDetails");
    }

    public CreateComplaintDetails getCitizenContactDetails(String contactInfo) {
        Row dataRow = readDataRow(grievancesContactDetailsSheet, contactInfo);

        String citizenName = getCellData(grievancesContactDetailsSheet, dataRow, "citizenName").getStringCellValue();
        String citizenMobNo = convertNumericToString(grievancesContactDetailsSheet, dataRow, "mobNo");

        String emailId = getCellData(grievancesContactDetailsSheet, dataRow, "emailId").getStringCellValue();

        return new CreateComplaintDetailsBuilder()
                .withCitizenName(citizenName)
                .withCitizenMobNo(citizenMobNo)
                .withEmailId(emailId)
                .build();
    }

    public CreateComplaintDetails getGrievanceDetails(String grievanceDetails) {
        Row dataRow = readDataRow(grievanceDetailsSheet, grievanceDetails);

        String grievanceCategory = getCellData(grievanceDetailsSheet, dataRow, "grievanceCategory").getStringCellValue();
        String grievanceType = getCellData(grievanceDetailsSheet, dataRow, "grievanceType").getStringCellValue();
        String grievanceDetailsText = getCellData(grievanceDetailsSheet, dataRow, "grievanceDetails").getStringCellValue();
        String grievanceLocation = getCellData(grievanceDetailsSheet, dataRow, "grievanceLocation").getStringCellValue();
        String locationLandmark = getCellData(grievanceDetailsSheet, dataRow, "locationLandmark").getStringCellValue();

        return new CreateComplaintDetailsBuilder()
                .withGrievanceCategory(grievanceCategory)
                .withGrievanceType(grievanceType)
                .withGrievanceDetails(grievanceDetailsText)
                .withGrievanceLocation(grievanceLocation)
                .withLocationLandmark(locationLandmark)
                .build();
    }
}

package excelDataFiles;

import builders.lcms.CreateLegalCaseBuilder;
import entities.lcms.CreateLegalCase;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class LCMSDataReader extends ExcelReader {

    Sheet createLegalCaseDataSheet;

    public LCMSDataReader(String testData) {
        super(testData);
        createLegalCaseDataSheet = workbook.getSheet("createLegalCaseData");
    }

    public CreateLegalCase getCreateLegalCaseDetails(String legalCaseData) {

        Row dataRow = readDataRow(createLegalCaseDataSheet, legalCaseData);

        String typeOfCourt = getCellData(createLegalCaseDataSheet, dataRow, "typeOfCourt").getStringCellValue();
        String petitionType = getCellData(createLegalCaseDataSheet, dataRow, "petitionType").getStringCellValue();
        String courtName = getCellData(createLegalCaseDataSheet, dataRow, "courtName").getStringCellValue();
        String caseCategory = getCellData(createLegalCaseDataSheet, dataRow, "caseCategory").getStringCellValue();
        String petitionerName = getCellData(createLegalCaseDataSheet, dataRow, "petitionerName").getStringCellValue();
        String respondentName = getCellData(createLegalCaseDataSheet, dataRow, "respondentName").getStringCellValue();

        return new CreateLegalCaseBuilder()
                .withTypeOfCourt(typeOfCourt)
                .withPetitionType(petitionType)
                .withCourtName(courtName)
                .withPetitionerName(petitionerName)
                .withCaseCategory(caseCategory)
                .withRespondentName(respondentName)
                .build();
    }
}

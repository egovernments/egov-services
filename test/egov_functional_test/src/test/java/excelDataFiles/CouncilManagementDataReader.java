package excelDataFiles;

import builders.councilManagement.PreambleDetailsBuilder;
import entities.councilManagement.CreatePreambleDetails;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CouncilManagementDataReader extends ExcelReader {

    Sheet createPreambleDetailsSheet;
    Sheet createAgendaSheet;
    Sheet createMeetingSheet;
    Sheet createCouncilMOMSheet;

    public CouncilManagementDataReader(String testData) {
        super(testData);
        createPreambleDetailsSheet = workbook.getSheet("createPreamble");
        createAgendaSheet = workbook.getSheet("createAgenda");
        createMeetingSheet = workbook.getSheet("createMeeting");
        createCouncilMOMSheet = workbook.getSheet("createCouncilMOM");
    }

    public CreatePreambleDetails getCreatePreambleDetails(String createPreambleData) {
        Row dataRow = readDataRow(createPreambleDetailsSheet, createPreambleData);

        String preambleDepartment = getCellData(createPreambleDetailsSheet, dataRow, "department").getStringCellValue();
        String amount = convertNumericToString(createPreambleDetailsSheet, dataRow, "amount");

        String gistOfPreamble = getCellData(createPreambleDetailsSheet, dataRow, "gistOfPreamble").getStringCellValue();

        return new PreambleDetailsBuilder()
                .withPreambleDepartment(preambleDepartment)
                .withSanctionAmount(amount)
                .withGistOfPreamble(gistOfPreamble)
                .build();
    }

    public CreatePreambleDetails getCreateAgendaDetails(String createAgendaData) {
        Row dataRow = readDataRow(createAgendaSheet, createAgendaData);

        String committeeType = getCellData(createAgendaSheet, dataRow, "committeeType").getStringCellValue();

        return new PreambleDetailsBuilder()
                .withCommitteeType(committeeType)
                .build();
    }

    public CreatePreambleDetails getCreateMeetingDetails(String createMeetingDetails) {
        Row dataRow = readDataRow(createMeetingSheet, createMeetingDetails);
        String meetingDate = convertNumericToString(createMeetingSheet, dataRow, "meetingDate");
        String meetingTime = convertNumericToString(createMeetingSheet, dataRow, "meetingTime");

        String meetingPlace = getCellData(createMeetingSheet, dataRow, "meetingPlace").getStringCellValue();

        return new PreambleDetailsBuilder()
                .withCouncilMeetingDate(meetingDate)
                .withCouncilMeetingTime(meetingTime)
                .withCouncilMeetingPlace(meetingPlace)
                .build();
    }

    public CreatePreambleDetails getCouncilMOMDetails(String councilMOMData) {
        Row dataRow = readDataRow(createCouncilMOMSheet, councilMOMData);

        String resolutionComment = getCellData(createCouncilMOMSheet, dataRow, "resolutionComments").getStringCellValue();
        String actionTaken = getCellData(createCouncilMOMSheet, dataRow, "actionTaken").getStringCellValue();

        return new PreambleDetailsBuilder()
                .withCouncilMOMResolution(resolutionComment)
                .withCouncilMOMAction(actionTaken)
                .build();
    }
}

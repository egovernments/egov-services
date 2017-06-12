package excelDataFiles;

import builders.assetManagement.assetService.HeaderDetailsBuilder;
import builders.assetManagement.assetService.LocationDetailsBuilder;
import entities.assetManagement.assetService.HeaderDetails;
import entities.assetManagement.assetService.LocationDetails;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class AssetServiceDataReader extends ExcelReader {

    Sheet headerDetailsSheet;
    Sheet locationDetailsSheet;

    public AssetServiceDataReader(String testData) {
        super(testData);
        headerDetailsSheet = workbook.getSheet("headerDetails");
        locationDetailsSheet = workbook.getSheet("locationDetails");
    }


    public HeaderDetails getHeaderDetails(String headerDetails) {

        Row dataRow = readDataRow(headerDetailsSheet, headerDetails);

        String department = getCellData(headerDetailsSheet, dataRow, "department").getStringCellValue();
        String assetCategory = getCellData(headerDetailsSheet, dataRow, "assetCategory").getStringCellValue();
//        String dateOfCreation = getCellData(headerDetailsSheet, dataRow, "dateOfCreation").getStringCellValue();
//        String description = getCellData(headerDetailsSheet, dataRow, "description").getStringCellValue();
//        String assetName = getCellData(headerDetailsSheet, dataRow, "assetName").getStringCellValue();
        String modeOfAcquisition = getCellData(headerDetailsSheet, dataRow, "modeOfAcquisition").getStringCellValue();

        return new HeaderDetailsBuilder()
                .withDepartment(department)
//                .withAssetName(assetName)
                .withAssetCategory(assetCategory)
//                .withDateOfCreation(dateOfCreation)
//                .withDescription(description)
                .withModeOfAcquisition(modeOfAcquisition)
                .build();
    }

    public LocationDetails getLocationDetails(String locationDetails) {
        Row dataRow = readDataRow(locationDetailsSheet, locationDetails);

        String locality = getCellData(locationDetailsSheet, dataRow, "location").getStringCellValue();
        String revenueWard = getCellData(locationDetailsSheet, dataRow, "revenueWard").getStringCellValue();
        String blockNumber = convertNumericToString(locationDetailsSheet, dataRow, "blockNumber");
        String electionWardNo = getCellData(locationDetailsSheet, dataRow, "electionWardNo").getStringCellValue();

        return new LocationDetailsBuilder()
                .withLocality(locality)
                .withRevenueWard(revenueWard)
                .withBlockNumber(blockNumber)
                .withElectionWardNumber(electionWardNo)
                .build();
    }
}

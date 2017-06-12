package excelDataFiles;

import builders.tradeLicense.*;
import entities.tradeLicense.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class TradeLicenseDataReader extends ExcelReader {

    Sheet tradeOwnerDetailsSheet;
    Sheet tradeLocationDetailsSheet;
    Sheet tradeDetailsSheet;
    Sheet licenseClosureSheet;
    Sheet searchTradeDetailsSheet;

    public TradeLicenseDataReader(String testData) {
        super(testData);
        tradeOwnerDetailsSheet = workbook.getSheet("tradeOwnerDetails");
        tradeLocationDetailsSheet = workbook.getSheet("tradeLocationDetails");
        tradeDetailsSheet = workbook.getSheet("tradeDetails");
        licenseClosureSheet = workbook.getSheet("licenseClosure");
        searchTradeDetailsSheet = workbook.getSheet("searchTradeDeatils");
    }

    public TradeOwnerDetails getTradeOwnerDetails(String tradeOwnerDetailsDataId) {
        Row dataRow = readDataRow(tradeOwnerDetailsSheet, tradeOwnerDetailsDataId);

        String aadhaarNumber = convertNumericToString(tradeOwnerDetailsSheet, dataRow, "aadhaarNumber");
        String mobileNumber = convertNumericToString(tradeOwnerDetailsSheet, dataRow, "mobileNumber");
        String tradeOwnerName = getCellData(tradeOwnerDetailsSheet, dataRow, "tradeOwnerName").getStringCellValue();
        String fatherSpouseName = getCellData(tradeOwnerDetailsSheet, dataRow, "fatherSpouseName").getStringCellValue();
        String emailId = getCellData(tradeOwnerDetailsSheet, dataRow, "emailId").getStringCellValue();
        String tradeOwnerAddress = getCellData(tradeOwnerDetailsSheet, dataRow, "tradeOwnerAddress").getStringCellValue();


        return new TradeOwnerDetailsBuilder()
                .withAadhaarNumber(aadhaarNumber)
                .withMobileNumber(mobileNumber)
                .withTradeOwnerName(tradeOwnerName)
                .withFatherSpouseName(fatherSpouseName)
                .withEmailId(emailId)
                .withTradeOwnerAddress(tradeOwnerAddress)
                .build();

    }

    public TradeLocationDetails getTradeLocationDetails(String tradeLocationDetailsDataId) {
        Row dataRow = readDataRow(tradeLocationDetailsSheet, tradeLocationDetailsDataId);

        String propertyAssessmentDetails = getCellData(tradeLocationDetailsSheet, dataRow, "propertyAssessmentDetails").getStringCellValue();
        String ownershipType = getCellData(tradeLocationDetailsSheet, dataRow, "ownershipType").getStringCellValue();
        String locality = getCellData(tradeLocationDetailsSheet, dataRow, "locality").getStringCellValue();
        String ward = getCellData(tradeLocationDetailsSheet, dataRow, "ward").getStringCellValue();

        return new TradeLocationDetailsBuilder()
                .withpropertyAssessmentNumber(propertyAssessmentDetails)
                .withownershipType(ownershipType)
                .withLocality(locality)
                .withWard(ward)
                .build();
    }

    public TradeDetails getTradeDetails(String tradeDetailsData) {
        Row dataRow = readDataRow(tradeDetailsSheet, tradeDetailsData);

        String tradeTitle = getCellData(tradeDetailsSheet, dataRow, "tradeTitle").getStringCellValue();
        String tradeType = getCellData(tradeDetailsSheet, dataRow, "tradeType").getStringCellValue();
        String tradeCategory = getCellData(tradeDetailsSheet, dataRow, "tradeCategory").getStringCellValue();
        String tradeSubCategory = getCellData(tradeDetailsSheet, dataRow, "tradeSubCategory").getStringCellValue();
        String tradeAreaWeightOfPremises = convertNumericToString(tradeDetailsSheet, dataRow, "tradeAreaWeightOfPremises");
        String remarks = getCellData(tradeDetailsSheet, dataRow, "remarks").getStringCellValue();
        String tradeCommencementDate = convertNumericToString(tradeDetailsSheet, dataRow, "TradeCommencementDate");

        return new TradeDetailsBuilder()
                .withtradeTitle(tradeTitle)
                .withtradeType(tradeType)
                .withtradeCategory(tradeCategory)
                .withtradeSubCategory(tradeSubCategory)
                .withtradeAreaWeightOfPremises(tradeAreaWeightOfPremises)
                .withremarks(remarks)
                .withtradeCommencementDate(tradeCommencementDate)
                .build();
    }

    public LicenseClosureDetails getDetailsForClosure(String closureData) {
        Row dataRow = readDataRow(licenseClosureSheet, closureData);

        String status = getCellData(licenseClosureSheet, dataRow, "status").getStringCellValue();
        String tradeCategory = getCellData(licenseClosureSheet, dataRow, "tradeCategory").getStringCellValue();

        return new LicenseClosureDetailsBuilder()
                .withStatusDetails(status)
                .withTradeCategory(tradeCategory)
                .build();
    }

    public SearchTradeDetails getTradeSearchDetails(String searchId) {
        Row dataRow = readDataRow(searchTradeDetailsSheet, searchId);
        SearchTradeDetails searchTradeDetails = new SearchTradeDetails();

        switch (searchId) {
            case "searchWithApplicationNumber":
                String applicationNumber = getCellData(searchTradeDetailsSheet, dataRow, "searchValue").getStringCellValue();

                searchTradeDetails = new SearchTradeDetailsBuilder()
                        .withApplicationNumber(applicationNumber)
                        .build();
                break;

            case "searchWithLicenseNumber":

                String licenseNumber = getCellData(searchTradeDetailsSheet, dataRow, "searchValue").getStringCellValue();

                searchTradeDetails = new SearchTradeDetailsBuilder()
                        .withLicenseNumber(licenseNumber)
                        .build();
                break;
        }
        return searchTradeDetails;
    }
}

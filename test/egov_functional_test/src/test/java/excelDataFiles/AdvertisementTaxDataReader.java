package excelDataFiles;

import builders.advertisementTax.AdvertisementDetailsBuilder;
import builders.advertisementTax.LocalityDetailsBuilder;
import builders.advertisementTax.PermissionDetailsBuilder;
import builders.advertisementTax.StructureDetailsBuilder;
import entities.advertisementTax.AdvertisementDetails;
import entities.advertisementTax.LocalityDetails;
import entities.advertisementTax.PermissionDetails;
import entities.advertisementTax.StructureDetails;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class AdvertisementTaxDataReader extends ExcelReader {

    Sheet advertisementDetailsSheet;
    Sheet permissionDetailsSheet;
    Sheet localityDetailsSheet;
    Sheet structureDetailsSheet;

    public AdvertisementTaxDataReader(String testData) {
        super(testData);
        advertisementDetailsSheet = workbook.getSheet("advertisementDetails");
        permissionDetailsSheet = workbook.getSheet("permissionDetails");
        localityDetailsSheet = workbook.getSheet("localityDetails");
        structureDetailsSheet = workbook.getSheet("structureDetails");
    }

    public AdvertisementDetails getAdvertisementDetails(String advertisementDetailsDataId) {
        Row dataRow = readDataRow(advertisementDetailsSheet, advertisementDetailsDataId);

        String category = getCellData(advertisementDetailsSheet, dataRow, "category").getStringCellValue();
        String subCategory = getCellData(advertisementDetailsSheet, dataRow, "subCategory").getStringCellValue();
        String classType = getCellData(advertisementDetailsSheet, dataRow, "class").getStringCellValue();
        String revenueInspector = getCellData(advertisementDetailsSheet, dataRow, "revenueInspector").getStringCellValue();
        String propertyType = getCellData(advertisementDetailsSheet, dataRow, "propertyType").getStringCellValue();

        return new AdvertisementDetailsBuilder()
                .withCategory(category)
                .withSubCategory(subCategory)
                .withClassType(classType)
                .withRevenueInspector(revenueInspector)
                .withPropertyType(propertyType)
                .build();
    }

    public PermissionDetails getPermissionDetails(String permissionDetailsDataId) {
        Row dataRow = readDataRow(permissionDetailsSheet, permissionDetailsDataId);

        String adParticular = getCellData(permissionDetailsSheet, dataRow, "adParticular").getStringCellValue();
        String owner = getCellData(permissionDetailsSheet, dataRow, "ownerDetails").getStringCellValue();
        String duration = getCellData(permissionDetailsSheet, dataRow, "advertisementDuration").getStringCellValue();

        return new PermissionDetailsBuilder()
                .withAdParticular(adParticular)
                .withOwner(owner)
                .withAdvertisementDuration(duration)
                .build();
    }

    public LocalityDetails getLocalityDetails(String localityDetailsDataId) {
        Row dataRow = readDataRow(localityDetailsSheet, localityDetailsDataId);

        String locality = getCellData(localityDetailsSheet, dataRow, "locality").getStringCellValue();
        String localityAddress = getCellData(localityDetailsSheet, dataRow, "localityAddress").getStringCellValue();
        String ward = getCellData(localityDetailsSheet, dataRow, "electionWard").getStringCellValue();

        return new LocalityDetailsBuilder()
                .withLocality(locality)
                .withLocalityAddress(localityAddress)
                .withElectionWard(ward)
                .build();
    }

    public StructureDetails getStructureDetails(String structureDetailsDataId) {
        Row dataRow = readDataRow(structureDetailsSheet, structureDetailsDataId);

        String measurement = convertNumericToString(structureDetailsSheet, dataRow, "measurement");
        String measurementType = getCellData(structureDetailsSheet, dataRow, "measurementType").getStringCellValue();
        String taxAmount = convertNumericToString(structureDetailsSheet, dataRow, "taxAmount");

        return new StructureDetailsBuilder()
                .withMeasurement(measurement)
                .withMeasurementType(measurementType)
                .withTaxAmount(taxAmount)
                .build();
    }
}

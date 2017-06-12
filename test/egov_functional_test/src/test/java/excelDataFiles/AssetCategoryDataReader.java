package excelDataFiles;

import builders.assetManagement.AssetCategoryDetailsBuilder;
import builders.assetManagement.CustomFieldsDetailsBuilder;
import entities.assetManagement.AssetCategoryDetails;
import entities.assetManagement.CustomFieldsDetails;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class AssetCategoryDataReader extends ExcelReader {

    Sheet assetCategoryDetailsSheet;
    Sheet customFieldsDetailsSheet;

    public AssetCategoryDataReader(String testData) {
        super(testData);
        assetCategoryDetailsSheet = workbook.getSheet("AssetCategoryDetails");
        customFieldsDetailsSheet = workbook.getSheet("CustomFieldsDetails");
    }

    public AssetCategoryDetails getAssetCategoryDetails(String assetCategoryDetailsDataId) {
        Row dataRow = readDataRow(assetCategoryDetailsSheet, assetCategoryDetailsDataId);

        String name = getCellData(assetCategoryDetailsSheet, dataRow, "name").getStringCellValue();
        String categoryType = getCellData(assetCategoryDetailsSheet, dataRow, "assetCategoryType").getStringCellValue();
        String parentCategory = getCellData(assetCategoryDetailsSheet, dataRow, "parentCategory").getStringCellValue();
        String deprecationMethod = getCellData(assetCategoryDetailsSheet, dataRow, "deprecationMethod").getStringCellValue();
        String assetAccountCode = getCellData(assetCategoryDetailsSheet, dataRow, "assetAccountCode").getStringCellValue();
        String accumulatedDepreciationCode = getCellData(assetCategoryDetailsSheet, dataRow, "accumulatedDepreciationCode").getStringCellValue();
        String revaluationReserveAccountCode = getCellData(assetCategoryDetailsSheet, dataRow, "revaluationReserveAccountCode").getStringCellValue();
        String depreciationExpenceAccount = getCellData(assetCategoryDetailsSheet, dataRow, "depreciationExpenceAccount").getStringCellValue();
        String UOM = getCellData(assetCategoryDetailsSheet, dataRow, "UOM").getStringCellValue();

        return new AssetCategoryDetailsBuilder()
                .withName(name)
                .withAssetCategoryType(categoryType)
                .withParentCategory(parentCategory)
                .withDepreciationMethod(deprecationMethod)
                .withAssetAccountCode(assetAccountCode)
                .withAccumulatedDepreciationCode(accumulatedDepreciationCode)
                .withRevaluationReserveAccountCode(revaluationReserveAccountCode)
                .withDepreciationExpenceAccount(depreciationExpenceAccount)
                .withUOM(UOM)
                .build();
    }

    public CustomFieldsDetails getCustomFieldsDetails(String customDetailsDataId) {
        Row dataRow = readDataRow(customFieldsDetailsSheet, customDetailsDataId);

        String name = getCellData(customFieldsDetailsSheet, dataRow, "name").getStringCellValue();
        String dataType = getCellData(customFieldsDetailsSheet, dataRow, "dataType").getStringCellValue();
        String regExFormat = getCellData(customFieldsDetailsSheet, dataRow, "regExFormat").getStringCellValue();
        String value = getCellData(customFieldsDetailsSheet, dataRow, "value").getStringCellValue();
        String localText = getCellData(customFieldsDetailsSheet, dataRow, "localText").getStringCellValue();
        Boolean isActive = getCellData(customFieldsDetailsSheet, dataRow, "isActive").getBooleanCellValue();
        Boolean mandatory = getCellData(customFieldsDetailsSheet, dataRow, "mandatory").getBooleanCellValue();

        return new CustomFieldsDetailsBuilder()
                .withName(name)
                .withDataType(dataType)
//                .withRegExFormat(regExFormat)
//                .withValue(value)
//                .withLocalText(localText)
                .withIsActive(isActive)
                .withMandatory(mandatory)
                .build();
    }
}

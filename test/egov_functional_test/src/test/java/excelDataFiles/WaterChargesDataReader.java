package excelDataFiles;

import builders.wcms.ApplicantInfoBuilder;
import builders.wcms.ConnectionInfoBuilder;
import builders.wcms.EnclosedDocumentBuilder;
import builders.wcms.FieldInspectionDetailsBuilder;
import entities.wcms.ApplicantInfo;
import entities.wcms.ConnectionInfo;
import entities.wcms.EnclosedDocument;
import entities.wcms.FieldInspectionDetails;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class WaterChargesDataReader extends ExcelReader {

    Sheet connectionDetailsSheet;
    Sheet enclosedDocumentsSheet;
    Sheet applicantParticularsSheet;
    Sheet fieldInspectionDetailsForWaterConnectionSheet;

    public WaterChargesDataReader(String testData) {
        super(testData);
        connectionDetailsSheet = workbook.getSheet("connectionDetails");
        enclosedDocumentsSheet = workbook.getSheet("enclosedDocuments");
        applicantParticularsSheet = workbook.getSheet("applicantParticulars");
        fieldInspectionDetailsForWaterConnectionSheet = workbook.getSheet("fieldInseptionDetailsForWaterConnection");
    }

    public ConnectionInfo getConnectionInfo(String connectionDetails) {
        Row dataRow = readDataRow(connectionDetailsSheet, connectionDetails);

        String waterSourceType = convertNumericToString(connectionDetailsSheet, dataRow, "waterSourceType");
        String connectionType = getCellData(connectionDetailsSheet, dataRow, "connectionType").getStringCellValue();
        String propertyType = getCellData(connectionDetailsSheet, dataRow, "propertyType").getStringCellValue();
        String category = getCellData(connectionDetailsSheet, dataRow, "category").getStringCellValue();
        String usageType = getCellData(connectionDetailsSheet, dataRow, "usageType").getStringCellValue();
        String hscPipeSize = convertNumericToString(connectionDetailsSheet, dataRow, "hscPipeSize");
        String sumpCapacity = convertNumericToString(connectionDetailsSheet, dataRow, "sumpCapacity");
        String noOfPersons = convertNumericToString(connectionDetailsSheet, dataRow, "noOfPersons");
        String connectionReason = convertNumericToString(connectionDetailsSheet, dataRow, "reasonForAdditionalConn");
        String waterSupplyType = convertNumericToString(connectionDetailsSheet, dataRow, "waterSupplyType");
        String apartmentName = convertNumericToString(connectionDetailsSheet, dataRow, "apartmentName");

        return new ConnectionInfoBuilder()
                .withWaterSourceType(waterSourceType)
                .withConnectionType(connectionType)
                .withPropertyType(propertyType)
                .withCategory(category)
                .withUsageType(usageType)
                .withHSCPipeSize(hscPipeSize)
                .withSumpCapacity(sumpCapacity)
                .withNoOfPersons(noOfPersons)
                .withReasonForAdditionalConnection(connectionReason)
                .withWaterSupplyType(waterSupplyType)
                .withApartmentName(apartmentName)
                .build();
    }

    public EnclosedDocument getDocumentInfo(String enclosedDocumentDetails) {

        Row dataRow = readDataRow(enclosedDocumentsSheet, enclosedDocumentDetails);

        String documentNo1 = convertNumericToString(enclosedDocumentsSheet, dataRow, "documentNo1");
        String documentNo2 = convertNumericToString(enclosedDocumentsSheet, dataRow, "documentNo2");
        String documentNo3 = convertNumericToString(enclosedDocumentsSheet, dataRow, "documentNo3");

//        String documentDate1 = convertNumericToString(enclosedDocumentsSheet , dataRow , "documentDate1");
//        String documentDate2 = convertNumericToString(enclosedDocumentsSheet , dataRow , "documentDate2");
//        String documentDate3 = convertNumericToString(enclosedDocumentsSheet , dataRow , "documentDate3");

        return new EnclosedDocumentBuilder()
                .withDocumentNo1(documentNo1)
                .withDocumentNo2(documentNo2)
                .withDocumentNo3(documentNo3)
//                .withDocumentDate1(documentDate1)
//                .withDocumentDate2(documentDate2)
//                .withDocumentDate3(documentDate3)
                .build();
    }

    public synchronized ApplicantInfo getApplicantInfo(String applicantDetailsDataId) {

        Row dataRow = readDataRow(applicantParticularsSheet, applicantDetailsDataId);

        String assessmentNumber = convertNumericToString(applicantParticularsSheet, dataRow, "assessmentNumber");
        String hscNumber = convertNumericToString(applicantParticularsSheet, dataRow, "hscNumber");
        String connectionDate = convertNumericToString(applicantParticularsSheet, dataRow, "connectionDate");

        return new ApplicantInfoBuilder()
                .withPTAssessmentNumber(assessmentNumber)
                .withHSCNumber(hscNumber)
                .withConnectionDate(connectionDate).build();
    }

    public FieldInspectionDetails getFieldInspectionInfo(String inspectionInfo) {

        Row dataRow = readDataRow(fieldInspectionDetailsForWaterConnectionSheet, inspectionInfo);

        String material = getCellData(fieldInspectionDetailsForWaterConnectionSheet, dataRow, "material").getStringCellValue();
        String quantity = convertNumericToString(fieldInspectionDetailsForWaterConnectionSheet, dataRow, "quantity");
        String measurement = convertNumericToString(fieldInspectionDetailsForWaterConnectionSheet, dataRow, "unitOfMeasurement");
        String rate = convertNumericToString(fieldInspectionDetailsForWaterConnectionSheet, dataRow, "rate");
        String existingPipeline = convertNumericToString(fieldInspectionDetailsForWaterConnectionSheet, dataRow, "existingDistributionPipeline");
        String pipelineDistance = convertNumericToString(fieldInspectionDetailsForWaterConnectionSheet, dataRow, "pipelineToHomeDistance");
        String estimationCharges = convertNumericToString(fieldInspectionDetailsForWaterConnectionSheet, dataRow, "estimationCharges");


        return new FieldInspectionDetailsBuilder()
                .withMaterial(material)
                .withQuantity(quantity)
                .withUnitOfMeasurement(measurement)
                .withRate(rate)
                .withExistingDistributionPipeLine(existingPipeline)
                .withPipelineToHomeDistance(pipelineDistance)
                .withEstimationCharges(estimationCharges)
                .build();
    }
}

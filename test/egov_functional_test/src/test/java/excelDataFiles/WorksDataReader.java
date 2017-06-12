package excelDataFiles;

import builders.works.*;
import entities.works.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class WorksDataReader extends ExcelReader {

    Sheet estimateHeaderDetailsSheet;
    Sheet financialDetailsSheet;
    Sheet workDetailsSheet;
    Sheet adminSanctionDetailsSheet;
    Sheet technicalSanctionDetailsSheet;

    public WorksDataReader(String testData) {
        super(testData);
        estimateHeaderDetailsSheet = workbook.getSheet("estimateHeaderDetails");
        financialDetailsSheet = workbook.getSheet("financialDetails");
        workDetailsSheet = workbook.getSheet("workDetails");
        adminSanctionDetailsSheet = workbook.getSheet("adminSanctionDetails");
        technicalSanctionDetailsSheet = workbook.getSheet("technicalSanctionDetails");
    }

    public EstimateHeaderDetails getEstimateHeaderDetails(String EstimateDetailsDataId) {
        Row dataRow = readDataRow(estimateHeaderDetailsSheet, EstimateDetailsDataId);
        String Date = convertNumericToString(estimateHeaderDetailsSheet, dataRow, "date");
        String RequirementNumber = getCellData(estimateHeaderDetailsSheet, dataRow, "requirementNumber").getStringCellValue();
        String ElectionWard = convertNumericToString(estimateHeaderDetailsSheet, dataRow, "electionWard");
        String location = convertNumericToString(estimateHeaderDetailsSheet, dataRow, "location");
        String WorkCategory = getCellData(estimateHeaderDetailsSheet, dataRow, "workCategory").getStringCellValue();
        String Beneficiary = convertNumericToString(estimateHeaderDetailsSheet, dataRow, "beneficiary");
        String NatureOfWork = getCellData(estimateHeaderDetailsSheet, dataRow, "natureOfWork").getStringCellValue();
        String TypeOfWork = getCellData(estimateHeaderDetailsSheet, dataRow, "typeOfWork").getStringCellValue();
        String SubTypeOfWork = getCellData(estimateHeaderDetailsSheet, dataRow, "subTypeOfWork").getStringCellValue();
        String ModeOfEntrustment = getCellData(estimateHeaderDetailsSheet, dataRow, "recommendedModeOfEntrustment").getStringCellValue();

        return new EstimateHeaderDetailsBuilder()
                .withDate(Date)
                .withRequirementNumber(RequirementNumber)
                .withElectionWard(ElectionWard)
                .withLocation(location)
                .withWorkCategory(WorkCategory)
                .withBeneficiary(Beneficiary)
                .withNatureOfWork(NatureOfWork)
                .withTypeOfWork(TypeOfWork)
                .withSubTypeOfWork(SubTypeOfWork)
                .withModeOfEntrustment(ModeOfEntrustment)
                .build();
    }

    public FinancialDetails getFinancialDetails(String financialDetailsDataId) {

        Row dataRow = readDataRow(financialDetailsSheet, financialDetailsDataId);

        String fund = getCellData(financialDetailsSheet, dataRow, "fund").getStringCellValue();
        String function = getCellData(financialDetailsSheet, dataRow, "function").getStringCellValue();
        String budgetHead = getCellData(financialDetailsSheet, dataRow, "budgetHead").getStringCellValue();

        return new FinancialDetailsBuilder()
                .withFund(fund)
                .withFunction(function)
                .withBudgetHead(budgetHead)
                .build();
    }

    public WorkDetails getWorkDetails(String workDetailsDataId) {

        Row dataRow = readDataRow(workDetailsSheet, workDetailsDataId);

        boolean worksCreated = getCellData(workDetailsSheet, dataRow, "worksOrderCreated").getBooleanCellValue();
        boolean billCreated = getCellData(workDetailsSheet, dataRow, "isBillCreated").getBooleanCellValue();
        String nameOfWork = getCellData(workDetailsSheet, dataRow, "nameOfWork").getStringCellValue();
        String abstractEstimateNumber = getCellData(workDetailsSheet, dataRow, "abstractEstimateNumber").getStringCellValue();
        String estimateAmount = convertNumericToString(workDetailsSheet, dataRow, "estimatedAmount");
        String WIN = getCellData(workDetailsSheet, dataRow, "workIdentificationNumber").getStringCellValue();
        String actualEstimateAmount = convertNumericToString(workDetailsSheet, dataRow, "actualEstimateAmount");
        String grossAmountBilled = convertNumericToString(workDetailsSheet, dataRow, "grossAmountBilled");

        return new WorkDetailsBuilder()
                .withWorksOrderCreated(worksCreated)
                .withBillsCreated(billCreated)
                .withNameOfWork(nameOfWork)
                .withAbstarctEstimateNumber(abstractEstimateNumber)
                .withEstimatedAmount(estimateAmount)
                .withWorkIdentificationNumber(WIN)
                .withActualEstimateAmount(actualEstimateAmount)
                .withGrossAmountBilled(grossAmountBilled)
                .build();
    }

    public AdminSanctionDetails getAdminSanctionDetails(String adminSanctionDetailsDataId) {

        Row dataRow = readDataRow(adminSanctionDetailsSheet, adminSanctionDetailsDataId);

        String AdminstrativeSanctionNumber = getCellData(adminSanctionDetailsSheet, dataRow, "administrativeSanctionNumber").getStringCellValue();

        String AdminstrativeSanctionAuthority = getCellData(adminSanctionDetailsSheet, dataRow, "administrativeSanctionAuthority").getStringCellValue();


        return new AdminSanctionDetailsBuilder()
                .withAdministrationSanctionNumber(AdminstrativeSanctionNumber)
                .withAdminSanctionAuthority(AdminstrativeSanctionAuthority)
                .build();
    }

    public TechnicalSanctionDetails getTechnicalSanctionDetails(String technicalSanctionDetailsDataId) {

        Row dataRow = readDataRow(technicalSanctionDetailsSheet, technicalSanctionDetailsDataId);

        String TechnicalSanctionNumber = getCellData(technicalSanctionDetailsSheet, dataRow, "technicalSanctionNumber").getStringCellValue();

        String TechnicalSanctionAuthority = getCellData(technicalSanctionDetailsSheet, dataRow, "technicalSanctionAuthority").getStringCellValue();

        return new TechnicalSanctionDetailsBuilder()
                .withTechnicalSanctionNumber(TechnicalSanctionNumber)
                .withTechnicalSanctionAuthority(TechnicalSanctionAuthority)
                .build();
    }
}

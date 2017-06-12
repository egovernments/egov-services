package excelDataFiles;

import builders.leaseAndAgreement.LandAgreementDetailsBuilder;
import builders.leaseAndAgreement.LandAllotteeDetailsBuilder;
import entities.leaseAndAgreement.LandAgreementDetails;
import entities.leaseAndAgreement.LandAllotteeDetails;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class LeaseAndAgreementDataReader extends ExcelReader {

    Sheet allotteeDetailsSheet;
    Sheet agreementDetailsSheet;

    public LeaseAndAgreementDataReader(String testData) {
        super(testData);
        allotteeDetailsSheet = workbook.getSheet("landAllotteeDetails");
        agreementDetailsSheet = workbook.getSheet("landAgreementDetails");
    }

    public LandAllotteeDetails getAllotteeDetails(String dataId) {

        Row dataRow = readDataRow(allotteeDetailsSheet, dataId);
        String nameOfTheAllottee = getCellData(allotteeDetailsSheet, dataRow, "nameOfTheAllottee").getStringCellValue();
        String emailId = getCellData(allotteeDetailsSheet, dataRow, "emailId").getStringCellValue();

        return new LandAllotteeDetailsBuilder()
                .withName(nameOfTheAllottee)
                .withEmail(emailId)
                .build();

    }

    public LandAgreementDetails getAgreementDetails(String dataId) {

        Row dataRow = readDataRow(agreementDetailsSheet, dataId);

        String tenderAuctionDate = convertNumericToString(agreementDetailsSheet, dataRow, "tenderAuctionDate");
        String natureOfTheAllottment = getCellData(agreementDetailsSheet, dataRow, "natureOfTheAllottment").getStringCellValue();
        String CouncilStandingCommitteeResolutionDate = convertNumericToString(agreementDetailsSheet, dataRow, "CouncilStandingCommitteeResolutionDate");
        String landRent = convertNumericToString(agreementDetailsSheet, dataRow, "landRent");
        String paymentCycle = getCellData(agreementDetailsSheet, dataRow, "paymentCycle").getStringCellValue();
        String bankGuaranteeDate = convertNumericToString(agreementDetailsSheet, dataRow, "bankGuaranteeDate");
        String bankGuaranteeAmount = convertNumericToString(agreementDetailsSheet, dataRow, "bankGuaranteeAmount");
        String dateOfCommencement = convertNumericToString(agreementDetailsSheet, dataRow, "dateOfCommencement");
        String solvencyCertificateDate = convertNumericToString(agreementDetailsSheet, dataRow, "solvencyCertificateDate");
        String timePeriod = convertNumericToString(agreementDetailsSheet, dataRow, "timePeriod");
        String methodByIncreaseInRentIsCalculatedDuringRenewal = getCellData(agreementDetailsSheet, dataRow, "methodByIncreaseInRentIsCalculatedDuringRenewal").getStringCellValue();

        return new LandAgreementDetailsBuilder()
                .withTenderDate(tenderAuctionDate)
                .withNatureOfAllotment(natureOfTheAllottment)
                .withCouncilDate(CouncilStandingCommitteeResolutionDate)
                .withLandRent(landRent)
                .withBankGuaranteeAmount(bankGuaranteeAmount)
                .withPaymentCycle(paymentCycle)
                .withBankGuaranteeDate(bankGuaranteeDate)
                .withCommencementDate(dateOfCommencement)
                .withSolvencyCertificateDate(solvencyCertificateDate)
                .withTimePeriod(timePeriod)
                .withRentIncrementMethod(methodByIncreaseInRentIsCalculatedDuringRenewal)
                .build();

    }
}

package excelDataFiles;

import builders.collections.ChallanHeaderDetailsBuilder;
import builders.collections.PaymentMethodBuilder;
import entities.collections.ChallanHeaderDetails;
import entities.collections.PaymentMethod;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class CollectionsDataReader extends ExcelReader {

    Sheet paymentMethodSheet;
    Sheet challanHeaderDetailsSheet;

    public CollectionsDataReader(String testData) {
        super(testData);
        paymentMethodSheet = workbook.getSheet("paymentMethod");
        challanHeaderDetailsSheet = workbook.getSheet("challanHeaderDetails");
    }

    public PaymentMethod getPaymentMethodDetails(String paymentMethod) {
        Row dataRow = readDataRow(paymentMethodSheet, paymentMethod);

        String chequeNumber = convertNumericToString(paymentMethodSheet, dataRow, "dd/chequeNum");
        String bankName = convertNumericToString(paymentMethodSheet, dataRow, "bankName");
        String accountNum = convertNumericToString(paymentMethodSheet, dataRow, "accountNum");

        return new PaymentMethodBuilder()
                .withChequeNumber(chequeNumber)
                .withBankName(bankName)
                .withAccountNumber(accountNum)
                .build();
    }

    public ChallanHeaderDetails getChallanHeader(String challanheaderid) {

        Row dataRow = readDataRow(challanHeaderDetailsSheet, challanheaderid);
        String payeeName = getCellData(challanHeaderDetailsSheet, dataRow, "payeeName").getStringCellValue();
        String payeeAddress = getCellData(challanHeaderDetailsSheet, dataRow, "payeeAddress").getStringCellValue();
        String narration = getCellData(challanHeaderDetailsSheet, dataRow, "narration").getStringCellValue();
        String serviceCategory = getCellData(challanHeaderDetailsSheet, dataRow, "serviceCategory").getStringCellValue();
        String sericeType = getCellData(challanHeaderDetailsSheet, dataRow, "serviceType").getStringCellValue();
        String amount = convertNumericToString(challanHeaderDetailsSheet, dataRow, "amount");

        return new ChallanHeaderDetailsBuilder()
                .withPayeeName(payeeName)
                .withPayeeAddress(payeeAddress)
                .withNarration(narration)
                .withServiceCategory(serviceCategory)
                .withServiceType(sericeType)
                .withAmount(amount)
                .build();
    }
}

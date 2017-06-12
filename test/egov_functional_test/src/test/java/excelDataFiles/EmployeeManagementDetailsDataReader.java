package excelDataFiles;

import builders.employeeManagement.createAttendance.AllEmployeeCodeBuilder;
import builders.employeeManagement.createEmployee.AssignmentDetailsBuilder;
import builders.employeeManagement.createEmployee.EmployeeDetailsBuilder;
import builders.employeeManagement.createEmployee.JurisdictionDetailsBuilder;
import entities.employeeManagement.createAttendance.AllEmployeeCode;
import entities.employeeManagement.createEmployee.AssignmentDetails;
import entities.employeeManagement.createEmployee.EmployeeDetails;
import entities.employeeManagement.createEmployee.JurisdictionDetails;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class EmployeeManagementDetailsDataReader extends ExcelReader {

    Sheet assignmentDetailsSheet;
    Sheet employeeDetailsSheet;
    Sheet jurisdictionDetailsSheet;
    Sheet allEmployeeCodesSheet;

    public EmployeeManagementDetailsDataReader(String testData) {
        super(testData);
        assignmentDetailsSheet = workbook.getSheet("assignmentDetails");
        employeeDetailsSheet = workbook.getSheet("employeeDetails");
        jurisdictionDetailsSheet = workbook.getSheet("jurisdictionList");
        allEmployeeCodesSheet = workbook.getSheet("allEmployeeCodes");
    }

    public AssignmentDetails getAssignmentDetails(String dataName) {
        Row dataRow = readDataRow(assignmentDetailsSheet, dataName);

        String isPrimary = getCellData(assignmentDetailsSheet, dataRow, "IsPrimary").getStringCellValue();
        String mainDepartment = getCellData(assignmentDetailsSheet, dataRow, "MainDepartment").getStringCellValue();
        String designation = getCellData(assignmentDetailsSheet, dataRow, "Designation").getStringCellValue();

        return new AssignmentDetailsBuilder()
                .withIsPrimary(isPrimary)
                .withDepartment(mainDepartment)
                .withDesignation(designation)
                .build();
    }

    public EmployeeDetails getEmployeeDetails(String dataName) {
        Row dataRow = readDataRow(employeeDetailsSheet, dataName);

        String employeeType = getCellData(employeeDetailsSheet, dataRow, "EmployeeType").getStringCellValue();
        String status = getCellData(employeeDetailsSheet, dataRow, "Status").getStringCellValue();
        String dateOfBirth = getCellData(employeeDetailsSheet, dataRow, "DateOfBirth").getStringCellValue();
        String gender = getCellData(employeeDetailsSheet, dataRow, "Gender").getStringCellValue();
        String martialStatus = getCellData(employeeDetailsSheet, dataRow, "MaritalStatus").getStringCellValue();
//        String userName = getCellData(employeeDetailsSheet, dataRow, "UserName").getStringCellValue();
        String isUserActive = getCellData(employeeDetailsSheet, dataRow, "IsUserActive").getStringCellValue();
        String mobileNumber = getCellData(employeeDetailsSheet, dataRow, "Mobile").getStringCellValue();
        String permanentAddress = getCellData(employeeDetailsSheet, dataRow, "PermanentAddress").getStringCellValue();
        String permanentCity = getCellData(employeeDetailsSheet, dataRow, "City").getStringCellValue();
        String permanentPincode = getCellData(employeeDetailsSheet, dataRow, "PinCode").getStringCellValue();
        String dataOfJoining = getCellData(employeeDetailsSheet, dataRow, "DateOfAppointment").getStringCellValue();

        return new EmployeeDetailsBuilder()
                .withEmployeeType(employeeType)
                .withStatus(status)
                .withDateOfBirth(dateOfBirth)
                .withGender(gender)
                .withMaritalStatus(martialStatus)
//                .withUserName(userName)
                .withIsUserActive(isUserActive)
                .withMobileNumber(mobileNumber)
                .withPermanentAddress(permanentAddress)
                .withPermanentCity(permanentCity)
                .withPermanentPincode(permanentPincode)
                .withDateOfAppointment(dataOfJoining)
                .build();
    }

    public JurisdictionDetails getJurisdictionDetails(String dataId) {
        Row dataRow = readDataRow(jurisdictionDetailsSheet, dataId);

        String JurisdictionType = getCellData(jurisdictionDetailsSheet, dataRow, "jurisdictionType").getStringCellValue();
        String JurisdictionList = getCellData(jurisdictionDetailsSheet, dataRow, "jurisdictionList").getStringCellValue();

        return new JurisdictionDetailsBuilder()
                .withJurisdictionType(JurisdictionType)
                .withJurisdictionList(JurisdictionList)
                .build();
    }

    public AllEmployeeCode getEmployeeCodeDetails(String dataId){
        Row dataRow = readDataRow(allEmployeeCodesSheet, dataId);

        String employeeCode = convertNumericToString(allEmployeeCodesSheet, dataRow, "employeeCode");
        return new AllEmployeeCodeBuilder()
                .withEmployeeCode(employeeCode)
                .build();
    }
}

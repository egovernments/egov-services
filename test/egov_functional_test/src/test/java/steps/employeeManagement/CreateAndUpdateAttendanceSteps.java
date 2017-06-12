package steps.employeeManagement;

import cucumber.api.java8.En;
import entities.employeeManagement.createAttendance.AllEmployeeCode;
import excelDataFiles.EmployeeManagementDetailsDataReader;
import pages.employeeManagement.createAndUpdateAttendance.CreateAndUpdateAttendancePage;
import steps.BaseSteps;

public class CreateAndUpdateAttendanceSteps extends BaseSteps implements En {

    public CreateAndUpdateAttendanceSteps() {

        And("^user will enter the details of employee code as (\\w+)$", (String employeeId) -> {
            AllEmployeeCode allEmployeeCode = new EmployeeManagementDetailsDataReader(eisTestDataFileName).getEmployeeCodeDetails(employeeId);
            pageStore.get(CreateAndUpdateAttendancePage.class).enterEmployeeDetailsToCreateAttendance(allEmployeeCode);
        });

        And("^user will enter the attendance details for an employee with status as (\\w+)$", (String status) -> {
            pageStore.get(CreateAndUpdateAttendancePage.class).enterAttendanceDetails(status);
        });

    }
}

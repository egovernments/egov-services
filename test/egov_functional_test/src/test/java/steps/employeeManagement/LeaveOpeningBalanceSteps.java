package steps.employeeManagement;

import cucumber.api.java8.En;
import pages.employeeManagement.leaveManagement.LeaveOpeningBalancePage;
import steps.BaseSteps;

public class LeaveOpeningBalanceSteps extends BaseSteps implements En {

    public LeaveOpeningBalanceSteps() {

        And("^user will search the employee to create the leave opening balance$", () -> {
            pageStore.get(LeaveOpeningBalancePage.class).searchEmployeeToCreateLeaveOpeningBalance(scenarioContext.getApplicationNumber());
        });

        And("^user will enter the no of days to an employee$", () -> {
            pageStore.get(LeaveOpeningBalancePage.class).enterDetailsOfLeaveOpeningBalance("10");
        });
    }
}

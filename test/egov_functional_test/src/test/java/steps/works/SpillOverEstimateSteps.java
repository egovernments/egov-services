package steps.works;

import cucumber.api.java8.En;
import entities.ApprovalDetails;
import entities.works.*;
import excelDataFiles.ExcelReader;
import excelDataFiles.WorksDataReader;
import pages.ApprovalDetailsPage;
import pages.works.SpillOverEstimatePage;
import steps.BaseSteps;

public class SpillOverEstimateSteps extends BaseSteps implements En {

    public SpillOverEstimateSteps() {

        And("^he enters estimate header details as (\\w+)$", (String estimateHeaderDetailsDataId) -> {

            EstimateHeaderDetails estimateHeaderDetails = new WorksDataReader(lineEstimateTestDataFileName).getEstimateHeaderDetails(estimateHeaderDetailsDataId);

            pageStore.get(SpillOverEstimatePage.class).enterEstimateHeaderDetails(estimateHeaderDetails);
        });
        And("^he enters financial details as (\\w+)$", (String financialDetailsDataId) -> {
            FinancialDetails financialDetails = new WorksDataReader(lineEstimateTestDataFileName).getFinancialDetails(financialDetailsDataId);

            pageStore.get(SpillOverEstimatePage.class).enterFinancialDetails(financialDetails);
        });
        And("^he enters work details as (\\w+)$", (String workDetailsDataId) -> {
            WorkDetails workDetails = new WorksDataReader(lineEstimateTestDataFileName).getWorkDetails(workDetailsDataId);

            pageStore.get(SpillOverEstimatePage.class).enterWorkDetails(workDetails);
        });
        And("^he enters administration sanction details as (\\w+)$", (String adminSanctionDetailsDataId) -> {
            AdminSanctionDetails adminSanctionDetails = new WorksDataReader(lineEstimateTestDataFileName).getAdminSanctionDetails(adminSanctionDetailsDataId);

            pageStore.get(SpillOverEstimatePage.class).enterAdminSanctionDetails(adminSanctionDetails);
        });
        And("^he enters technical sanction details as (\\w+)$", (String technicalSanctionDetailsDataId) -> {
            TechnicalSanctionDetails technicalSanctionDetails = new WorksDataReader(lineEstimateTestDataFileName).getTechnicalSanctionDetails(technicalSanctionDetailsDataId);

            pageStore.get(SpillOverEstimatePage.class).enterTechnicalSanctionDetails(technicalSanctionDetails);
        });
        And("^he saves the file and closes the acknowledgement$", () -> {
            pageStore.get(SpillOverEstimatePage.class).saveAndClose();
            String actualMsg = pageStore.get(SpillOverEstimatePage.class).successMessage();
            scenarioContext.setActualMessage(actualMsg);
            pageStore.get(SpillOverEstimatePage.class).close();
        });

        And("^he enters approver details as (\\w+)$", (String approverDetailsDataId) -> {
            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);
        });

        And("^he enters work details as for (\\w+)$", (String workDetailsDataId) -> {
            WorkDetails workDetails = new WorksDataReader(lineEstimateTestDataFileName).getWorkDetails(workDetailsDataId);

            pageStore.get(SpillOverEstimatePage.class).enterWorkDetailsforestimate(workDetails);
        });
        And("^he forwards to DEE and closes the acknowledgement$", () -> {
            String number = pageStore.get(SpillOverEstimatePage.class).forwardToDEE();
            scenarioContext.setApplicationNumber(number);

            String Message = pageStore.get(SpillOverEstimatePage.class).successMessage();
            scenarioContext.setActualMessage(Message);

            pageStore.get(SpillOverEstimatePage.class).close();
        });
        And("^he enters the AdminSanctionNumber$", () -> {
            pageStore.get(SpillOverEstimatePage.class).adminSanctionNumber();
        });
        And("^he submit the application to superIntendent$", () -> {
            pageStore.get(SpillOverEstimatePage.class).submit();

            String actualMsg = pageStore.get(SpillOverEstimatePage.class).successMessage();
            scenarioContext.setActualMessage(actualMsg);

            pageStore.get(SpillOverEstimatePage.class).close();
        });
        And("^he submit the application to commissioner$", () -> {
            pageStore.get(SpillOverEstimatePage.class).submit();

            String actualMsg = pageStore.get(SpillOverEstimatePage.class).successMessage();
            scenarioContext.setActualMessage(actualMsg);

            pageStore.get(SpillOverEstimatePage.class).close();
        });
        And("^he submit the application to assis_Engineer$", () -> {
            pageStore.get(SpillOverEstimatePage.class).submit();

            String actualMsg = pageStore.get(SpillOverEstimatePage.class).successMessage();
            scenarioContext.setActualMessage(actualMsg);

            pageStore.get(SpillOverEstimatePage.class).close();
        });
        And("^he enters the details for approve$", () -> {
            pageStore.get(SpillOverEstimatePage.class).detailsForApprove();
        });
        And("^he approves the application$", () -> {
            pageStore.get(SpillOverEstimatePage.class).approve();

            String actualMsg = pageStore.get(SpillOverEstimatePage.class).successMessage();
            scenarioContext.setActualMessage(actualMsg);

            pageStore.get(SpillOverEstimatePage.class).close();
        });
    }
}



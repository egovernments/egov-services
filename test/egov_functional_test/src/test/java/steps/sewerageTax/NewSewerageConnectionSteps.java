package steps.sewerageTax;

import cucumber.api.java8.En;
import entities.ApprovalDetails;
import entities.sewerageTax.ConnectionDetails;
import excelDataFiles.ExcelReader;
import excelDataFiles.SewerageTaxDataReader;
import pages.ApprovalDetailsPage;
import pages.sewerageTax.NewSewerageConnectionPage;
import steps.BaseSteps;

public class NewSewerageConnectionSteps extends BaseSteps implements En {
    public NewSewerageConnectionSteps() {
        And("^he create new sewerage connection for above assessment number (\\w+)$", (String sewerageTaxDataId) -> {
            ConnectionDetails connectionDetails = new SewerageTaxDataReader(sewerageTaxTestDataFileName).getConnectionDetails(sewerageTaxDataId);
            pageStore.get(NewSewerageConnectionPage.class).createNewConnection(scenarioContext.getAssessmentNumber(), connectionDetails);

            String approverDetailsDataId = "assis_Engineer_1";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);

        });
        And("^he forward to assistant engineer and closes the acknowledgement$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).forward();

            scenarioContext.setApplicationNumber(pageStore.get(NewSewerageConnectionPage.class).getApplicatioNumber());

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessage());

            pageStore.get(NewSewerageConnectionPage.class).close();

        });
        And("^he search for above application number to collect$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).searchForApplicationNumberToCollect(scenarioContext.getApplicationNumber());
        });
        And("^he collect the charges and closes the acknowledgement$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).collectCharges();

            pageStore.get(NewSewerageConnectionPage.class).closeMultipleWindows("/stms/collectfee/search");
        });
        And("^he approve the above sewerage application$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).approveTheApplication();
        });
        And("^he forward to DEE and close the acknowledgement$", () -> {
            String approverDetailsDataId = "deputyExecutiveEngineer";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);

            pageStore.get(NewSewerageConnectionPage.class).forward();

            scenarioContext.setApplicationNumber(pageStore.get(NewSewerageConnectionPage.class).getApplicatioNumber());

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessage());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he closes the sewerage acknowledgement$", () -> {
            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessage());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he generates estimation notice for above sewerage application$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).generateEstimationNotice();
        });
        And("^he forward to executive engineer and closes the acknowledgement$", () -> {
            String approverDetailsDataId = "Executive_engineer";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);

            pageStore.get(NewSewerageConnectionPage.class).forward();

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessage());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he generate workOrder for above sewerage connection$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).generateWorkOrder(scenarioContext.getApplicationNumber());

            String approverDetailsDataId = "assis_Engineer_1";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);
        });
        And("^he execute connection and closes the acknowledgement$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).executeConnection();

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessage1());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });

        And("^he search for above sewerage connection$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).searchForAboveSewerageConnection(scenarioContext.getApplicationNumber(), "modifyConnection");
        });
        And("^he increses the number of closets (\\w+)$", (String sewerageTaxDataId) -> {
            ConnectionDetails connectionDetails = new SewerageTaxDataReader(sewerageTaxTestDataFileName).getConnectionDetails(sewerageTaxDataId);
            pageStore.get(NewSewerageConnectionPage.class).increseTheNumberOfClosets(connectionDetails);

            String approverDetailsDataId = "assis_Engineer_1";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);

        });
        And("^he forward to assistant engineer for change in closets and closes the acknowledgement$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).forward();

            scenarioContext.setApplicationNumber(pageStore.get(NewSewerageConnectionPage.class).getApplicatioNumberForChangeSewerage());

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessageForChangeSewerage());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he forward to DEE for change and close the acknowledgement$", () -> {
            String approverDetailsDataId = "deputyExecutiveEngineer";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);

            pageStore.get(NewSewerageConnectionPage.class).forward();

            scenarioContext.setApplicationNumber(pageStore.get(NewSewerageConnectionPage.class).getApplicatioNumberForChangeSewerage());

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessageForChangeSewerage());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he closes the sewerageChange acknowledgement$", () -> {
            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessageForChangeSewerage());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he forward to executive engineer for change in closets and closes the acknowledgement$", () -> {
            String approverDetailsDataId = "Executive_engineer";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);

            pageStore.get(NewSewerageConnectionPage.class).forward();

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessageForChangeSewerage());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he execute connection for change and closes the acknowledgement$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).executeConnection();

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessage1ForChangeSewerage());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he search for above sewerage application for closure$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).searchForAboveSewerageConnection(scenarioContext.getApplicationNumber(), "closeConnection");
        });
        And("^he put remarks and forward the application$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).remarks();

            String approverDetailsDataId = "assis_Engineer_1";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);

        });
        And("^he forwards to DEE for closure and close the acknowledgement$", () -> {
            String approverDetailsDataId = "deputyExecutiveEngineer";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);

            pageStore.get(NewSewerageConnectionPage.class).forward();

            scenarioContext.setApplicationNumber(pageStore.get(NewSewerageConnectionPage.class).getApplicatioNumberForClosure());

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessageForClosure());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he forwards for closure and closes the acknowledgement$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).forward();

            scenarioContext.setApplicationNumber(pageStore.get(NewSewerageConnectionPage.class).getApplicatioNumberForClosure());

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessageForClosure());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he forwards to executive engineer for closure and close the acknowledgement$", () -> {
            String approverDetailsDataId = "Executive_engineer";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);

            pageStore.get(NewSewerageConnectionPage.class).forward();

            scenarioContext.setApplicationNumber(pageStore.get(NewSewerageConnectionPage.class).getApplicatioNumberForClosure());

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessageForClosure());

            pageStore.get(NewSewerageConnectionPage.class).close();


        });
        And("^he closes the seweargeClosure acknowledgement$", () -> {
            scenarioContext.setApplicationNumber(pageStore.get(NewSewerageConnectionPage.class).getApplicatioNumberForClosure());

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessageForClosure());

            pageStore.get(NewSewerageConnectionPage.class).close();

        });
        And("^he generates closure notice$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).generateClosureNotice();
        });

        And("^he enter details for legacy sewerage connection$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).enterDetailsForLegacySewerageConnection(scenarioContext.getAssessmentNumber());
        });
        And("^he submit the application of legacy sewerage connection and closes the acknowledgement$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).submit();

            scenarioContext.setApplicationNumber(pageStore.get(NewSewerageConnectionPage.class).getApplicationNumberForLegacyCreation());

            scenarioContext.setActualMessage(pageStore.get(NewSewerageConnectionPage.class).getSuccessMessage1());

            pageStore.get(NewSewerageConnectionPage.class).close();
        });
        And("^he search application and generate demand bill$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).searchAndGenerateDemandBill(scenarioContext.getApplicationNumber());
        });
    }
}

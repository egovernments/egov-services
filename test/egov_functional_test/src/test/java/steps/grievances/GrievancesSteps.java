package steps.grievances;

import cucumber.api.java8.En;
import entities.grievances.CreateComplaintDetails;
import excelDataFiles.GrievanceDataReader;
import org.junit.Assert;
import pages.Grievances.GrievancesPage;
import steps.BaseSteps;

public class GrievancesSteps extends BaseSteps implements En {
    public GrievancesSteps() {
        When("^he choose to register a complaint$", () -> {
            pageStore.get(GrievancesPage.class).openCreateGrievancePage();
        });

        And("^he choose to enter contact information as (\\w+)$", (String contactInfo) -> {
            CreateComplaintDetails createComplaintDetails = new GrievanceDataReader(grievanceTestDataFileName).getCitizenContactDetails(contactInfo);
            pageStore.get(GrievancesPage.class).enterCitizenContactDetails(createComplaintDetails);
        });

        And("^he choose to enter grievance details as (\\w+)$", (String grievanceDetails) -> {
            CreateComplaintDetails createComplaintDetails = new GrievanceDataReader(grievanceTestDataFileName).getGrievanceDetails(grievanceDetails);
            scenarioContext.setActualMessage(pageStore.get(GrievancesPage.class).enterGrievanceDetails(createComplaintDetails, scenarioContext.getUser()));
        });

        When("^he choose to register complaint with his login$", () -> {
            pageStore.get(GrievancesPage.class).getRegisterComplaintPage();

        });

        And("^he copies CRN and closes the acknowledgement$", () -> {
            String CRN = pageStore.get(GrievancesPage.class).getCRN();
            scenarioContext.setApplicationNumber(CRN);
            Assert.assertNotNull(scenarioContext.getApplicationNumber());
        });

        And("^he mark status as (\\w+)$", (String status) -> {
            scenarioContext.setActualMessage(pageStore.get(GrievancesPage.class).officialMarkStatus(status));
        });

        And("^he marks the staus as processing$", () -> {
            pageStore.get(GrievancesPage.class).getProcessingStatus();
        });

        And("^he search complaint in his Inbox$", () -> {
            pageStore.get(GrievancesPage.class).searchInCitizenInbox(scenarioContext.getApplicationNumber());
        });

        And("^he (.*) the complaint$", (String complaintStatus) -> {
            pageStore.get(GrievancesPage.class).withdrawComplaint(complaintStatus);
        });
    }
}

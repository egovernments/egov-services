package steps.buildingPlanning;

import cucumber.api.PendingException;
import cucumber.api.java8.En;
import entities.ApprovalDetails;
import excelDataFiles.ExcelReader;
import pages.ApprovalDetailsPage;
import pages.buildingPlanning.CreateBuildingApplicationPage;
import steps.BaseSteps;

public class CreateBuildingApplicationSteps extends BaseSteps implements En {

    public CreateBuildingApplicationSteps() {

        And("^user will enter the Building Application Details$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).enterApplicationDetails();
        });

        And("^user will enter the Applicant Details$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).enterApplicantDetails();
        });

        And("^user will enter the Site Details$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).enterSiteDetails();
        });

        And("^user will enter the Services and Amenities Measurement Details$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).enterServicesAndAmenitiesMeasurementDetails();
        });

        And("^user will enter the Building Details$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).enterBuildingDetails();
        });

        And("^user will click on submit the application$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).submitApplication();
        });

        And("^user will collect the fee from the applicant$", () -> {
            String applicationNumber = pageStore.get(CreateBuildingApplicationPage.class).collectFeeFromApplicant();
            scenarioContext.setApplicationNumber(applicationNumber);
        });

        And("^user will close the acknowledgement form$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).closeAcknowledgementForm();
        });

        And("^user will enter the approval details as (\\w+) and forwards it$", (String officer) -> {
            // Enter Approval Details
            ApprovalDetails approvalDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(officer);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approvalDetails);

            // Click on Forward
            pageStore.get(ApprovalDetailsPage.class).forward();

            // Close the Forwarded Acknowledgement Form
            pageStore.get(CreateBuildingApplicationPage.class).closeForwardAcknowledgementForm();
        });

        And("^user will provide the appointment for the verification of documents$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).provideAppointment();
        });

        And("^user takes action on Document Scrutiny$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).performDocumentScrutiny();
        });

        And("^user takes action on Capturing the Field Inspection Details$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).captureFieldInspectionDetails();
        });

        And("^user will calculate the fees based on the building measurements$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).calculateFees();
        });

        And("^secretary will approve the application with approval details as (\\w+) and forwards it$", (String officer) -> {
            // Enter Approval Details
            ApprovalDetails approvalDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(officer);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approvalDetails);

            // Click on Forward
            pageStore.get(CreateBuildingApplicationPage.class).approve();

            // Close the Forwarded Acknowledgement Form
            pageStore.get(CreateBuildingApplicationPage.class).closeForwardAcknowledgementForm();
        });

        And("^user will search the application based on application number$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).searchApplication(scenarioContext.getApplicationNumber());

            pageStore.get(CreateBuildingApplicationPage.class).collectFeeFromApplicant();
        });

        And("^Secretary will apply the Digital Signature and forwards to (\\w+)$", (String officer) -> {
            // Enter Approval Details
            ApprovalDetails approvalDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(officer);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approvalDetails);

            // Click on Forward
            pageStore.get(CreateBuildingApplicationPage.class).digitalSignature();

            // Close the Forwarded Acknowledgement Form
            pageStore.get(CreateBuildingApplicationPage.class).closeForwardAcknowledgementForm();
        });

        And("^user will click on Generate Permit Order$", () -> {
            pageStore.get(CreateBuildingApplicationPage.class).generatePermitOrder();
        });
    }
}

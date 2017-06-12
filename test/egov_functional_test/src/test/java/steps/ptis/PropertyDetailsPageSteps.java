package steps.ptis;

import cucumber.api.PendingException;
import cucumber.api.java8.En;
import entities.ApprovalDetails;
import entities.ptis.*;
import excelDataFiles.ExcelReader;
import excelDataFiles.PTISDataReader;
import pages.ApprovalDetailsPage;
import pages.ptis.PropertyAcknowledgementPage;
import pages.ptis.PropertyDetailsPage;
import pages.ptis.TransferDetailsPage;
import steps.BaseSteps;

public class PropertyDetailsPageSteps extends BaseSteps implements En {

    public PropertyDetailsPageSteps() {

        And("^he enters property header details as (\\w+)$", (String propertyDetailsDataId) -> {
            PropertyHeaderDetails propertyHeaderDetails = new PTISDataReader(ptisTestDataFileName).getPropertyHeaderDetails(propertyDetailsDataId);
            pageStore.get(PropertyDetailsPage.class).enterPropertyHeader(propertyHeaderDetails);
        });
        And("^he enters owner details for the first owner as (\\w+)$", (String ownerDetailsDataId) -> {
            OwnerDetails ownerDetails = new PTISDataReader(ptisTestDataFileName).getOwnerDetails(ownerDetailsDataId);
            pageStore.get(PropertyDetailsPage.class).enterOwnerDetails(ownerDetails);
        });
        And("^he enters property address details as (\\w+)$", (String addressDetailsDataId) -> {
            PropertyAddressDetails addressDetails = new PTISDataReader(ptisTestDataFileName).getPropertyAddressDetails(addressDetailsDataId);
            pageStore.get(PropertyDetailsPage.class).enterPropertyAddressDetails(addressDetails);
        });
        And("^he enters assessment details as (\\w+)$", (String assessmentDetailsDataId) -> {
            AssessmentDetails assessmentDetails = new PTISDataReader(ptisTestDataFileName).getAssessmentDetails(assessmentDetailsDataId);
            pageStore.get(PropertyDetailsPage.class).enterAssessmentDetails(assessmentDetails);
        });
        And("^he enters amenities as (\\w+)$", (String amenitiesDataId) -> {
            Amenities amenities = new PTISDataReader(ptisTestDataFileName).getAmenties(amenitiesDataId);
            pageStore.get(PropertyDetailsPage.class).selectAmenities(amenities);
        });
        And("^he enters construction type details as (\\w+)$", (String constructionTypeDetailsDataId) -> {
            ConstructionTypeDetails constructionTypeDetails = new PTISDataReader(ptisTestDataFileName).getConstructionTypeDetails(constructionTypeDetailsDataId);
            pageStore.get(PropertyDetailsPage.class).enterConstructionTypeDetails(constructionTypeDetails);
        });
        And("^he enters floor details as (\\w+)$", (String floorDetailsDataId) -> {
            FloorDetails floorDetails = new PTISDataReader(ptisTestDataFileName).getFloorDetails(floorDetailsDataId);
            pageStore.get(PropertyDetailsPage.class).enterFloorDetails(floorDetails);
        });
        And("^he enters approval details as (\\w+)$", (String approvalDetailsDataId) -> {
            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approvalDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApproverDetails(approverDetails);
        });
        And("^he forwards for approval to (.*)$", (String approvalDetailsDataId) -> {
            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approvalDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApproverDetails(approverDetails);
            pageStore.get(PropertyDetailsPage.class).forward();
        });
        And("^he approved the property with remarks \"([^\"]*)\"$", (String remarks) -> {
            pageStore.get(PropertyDetailsPage.class).approve();
        });
        And("^he does a digital signature$", () -> {
            pageStore.get(PropertyDetailsPage.class).digitallySign();
            String acknowledgementMessage = pageStore.get(PropertyAcknowledgementPage.class).getSignatureNotification();
            scenarioContext.setActualMessage(acknowledgementMessage);
        });
        And("^he generates a notice$", () -> {
            pageStore.get(PropertyDetailsPage.class).generateNotice();
        });
        And("^the property tax bill be created$", () -> {
            pageStore.get(PropertyDetailsPage.class).create();
        });
        And("^he check total number of records found$", () -> {
            pageStore.get(PropertyDetailsPage.class).checkNoOfRecords();
        });
        And("^finally user will submit the application$", () -> {
            pageStore.get(PropertyDetailsPage.class).chooseToSubmit();
        });
//        And("^chooses to act upon the above application$", () -> {
//           pageStore.get(DashboardPage.class).openApplicationNew(scenarioContext.getAssessmentNumber());
//        });
        And("^he approved the property with remarks \"([^\"]*)\" for transfer of ownership$", (String arg0) -> {
            String assessmentNo = pageStore.get(PropertyDetailsPage.class).approveForCreation();
            scenarioContext.setAssessmentNumber(assessmentNo);
        });
        And("^he search property with (\\w+)$", (String searchType) -> {
            SearchDetails searchDetails = new PTISDataReader(ptisTestDataFileName).getSearchDetails(searchType);
            pageStore.get(PropertyDetailsPage.class).searchProperty(searchDetails, searchType);
        });
        And("^he approved the property with remarks addition \"([^\"]*)\"$", (String arg0) -> {
            pageStore.get(PropertyDetailsPage.class).approveaddition();
        });
        And("^he enters document type details as (\\w+)$", (String documentSelect) -> {
            DocumentTypeValue documentValue = new PTISDataReader(ptisTestDataFileName).getDocumentValue(documentSelect);
            pageStore.get(PropertyDetailsPage.class).selectDocumentType(documentValue);
        });
        And("^he enters the Vacancy Remission Details$", () -> {
            pageStore.get(PropertyDetailsPage.class).enterVacancyRemissionDetails();
        });
        And("^he forward application to the junior assistant and closes acknowledgement$", () -> {
            pageStore.get(PropertyDetailsPage.class).forward();
            pageStore.get(TransferDetailsPage.class).closesAcknowledgement();
        });
        And("^he search for the Amalgamated Properties$", () -> {
            pageStore.get(PropertyDetailsPage.class).searchAmalgamatedProperties();
        });
        And("^he enters bifurcation assessment details as (\\w+)$", (String bifurcationDetailsDataId) -> {
            AssessmentDetails bifurcationDetails = new PTISDataReader(ptisTestDataFileName).getbifurcationDetails(bifurcationDetailsDataId);
            System.out.println(scenarioContext.getAssessmentNumber());
            scenarioContext.setApplicationNumber(scenarioContext.getAssessmentNumber());
            pageStore.get(PropertyDetailsPage.class).enterBifurcationDetails(bifurcationDetails,scenarioContext.getAssessmentNumber());

        });
        And("^he enters parent bifurcated assessment number$", () -> {
            pageStore.get(PropertyDetailsPage.class).enterBifurcationAssessmentNo(scenarioContext.getApplicationNumber());
        });
        And("^he click on floors Details entered$", () -> {
            pageStore.get(PropertyDetailsPage.class).clickOnFloorDetailsCheckBox();
        });
        And("^he enters the floor details checkbox$", () -> {
            pageStore.get(PropertyDetailsPage.class).enterExtentOfSiteValue();

            pageStore.get(PropertyDetailsPage.class).clickOnFloorDetailsCheckBox();
        });
        And("^he checks the validations for all textBoxes$", () -> {
           pageStore.get(PropertyDetailsPage.class).checkValidationForOwner();
           pageStore.get(PropertyDetailsPage.class).checkValidationForAssessmentDetails();
           pageStore.get(PropertyDetailsPage.class).checkValidationForFloorDetails();
           pageStore.get(PropertyDetailsPage.class).clickOnFloorDetailsCheckBox();
           pageStore.get(PropertyDetailsPage.class).checkValidationForLocationDetails();
        });
        And("^he check the errorMessage of door number$", () -> {
           pageStore.get(PropertyDetailsPage.class).checkDoorNumber();
           pageStore.get(PropertyDetailsPage.class).clickOnFloorDetailsCheckBox();
        });


    }
}
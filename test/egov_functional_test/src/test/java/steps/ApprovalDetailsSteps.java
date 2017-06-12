package steps;

import cucumber.api.java8.En;
import entities.ApprovalDetails;
import excelDataFiles.ExcelReader;
import pages.ApprovalDetailsPage;

public class ApprovalDetailsSteps extends BaseSteps implements En {

    public ApprovalDetailsSteps() {

        And("^he forwards for approver (.*)$", (String approvalDetailsDataId) -> {
            ApprovalDetails approvalDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approvalDetailsDataId);
            if (approvalDetailsDataId.equals("sanitaryInspector1") || approvalDetailsDataId.equals("LightingSuperintendent") || approvalDetailsDataId.equals("commissioner1")) {
                pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approvalDetails);
                if (approvalDetailsDataId.equals("commissioner1")) {
                    pageStore.get(ApprovalDetailsPage.class).forward();
                } else {
                    pageStore.get(ApprovalDetailsPage.class).createGrievance();
                }
            } else if (approvalDetailsDataId.equals("sanitaryInspector") || approvalDetailsDataId.equals("commissioner") || approvalDetailsDataId.equals("commissioner2")) {
                pageStore.get(ApprovalDetailsPage.class).enterApproverDetails(approvalDetails);
                pageStore.get(ApprovalDetailsPage.class).forward();
            }
        });

    }
}

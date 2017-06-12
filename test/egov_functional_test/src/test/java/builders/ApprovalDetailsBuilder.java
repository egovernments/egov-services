package builders;

import entities.ApprovalDetails;

public class ApprovalDetailsBuilder {
    ApprovalDetails approvalDetails = new ApprovalDetails();

    public ApprovalDetailsBuilder() {
    }

    public ApprovalDetailsBuilder withApproverDepartment(String approverDepartment) {
        approvalDetails.setApproverDepartment(approverDepartment);
        return this;
    }

    public ApprovalDetailsBuilder withApproverDesignation(String approverDesignation) {
        approvalDetails.setApproverDesignation(approverDesignation);
        return this;
    }

    public ApprovalDetailsBuilder withApprover(String approver) {
        approvalDetails.setApprover(approver);
        return this;
    }

    public ApprovalDetailsBuilder withApproverRemarks(String approverRemarks) {
        approvalDetails.setApproverRemarks(approverRemarks);
        return this;
    }

    public ApprovalDetails build() {
        return approvalDetails;
    }
}

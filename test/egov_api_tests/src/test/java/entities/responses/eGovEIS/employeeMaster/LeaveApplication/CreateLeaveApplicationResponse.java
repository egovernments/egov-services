package entities.responses.eGovEIS.employeeMaster.LeaveApplication;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateLeaveApplicationResponse {
    private entities.responses.eGovEIS.employeeMaster.LeaveApplication.ResponseInfo ResponseInfo;

    @JsonProperty("LeaveApplication")
    private entities.responses.eGovEIS.employeeMaster.LeaveApplication.LeaveApplication[] LeaveApplication;

    public entities.responses.eGovEIS.employeeMaster.LeaveApplication.ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(entities.responses.eGovEIS.employeeMaster.LeaveApplication.ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public entities.responses.eGovEIS.employeeMaster.LeaveApplication.LeaveApplication[] getLeaveApplication() {
        return this.LeaveApplication;
    }

    public void setLeaveApplication(entities.responses.eGovEIS.employeeMaster.LeaveApplication.LeaveApplication[] LeaveApplication) {
        this.LeaveApplication = LeaveApplication;
    }
}

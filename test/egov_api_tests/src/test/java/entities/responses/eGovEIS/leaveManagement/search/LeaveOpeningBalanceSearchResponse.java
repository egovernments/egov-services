package entities.responses.eGovEIS.leaveManagement.search;

import org.codehaus.jackson.annotate.JsonProperty;

public class LeaveOpeningBalanceSearchResponse {

    private ResponseInfo ResponseInfo;
    @JsonProperty("LeaveOpeningBalance")
    private LeaveOpeningBalance[] LeaveOpeningBalance;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public LeaveOpeningBalance[] getLeaveOpeningBalance() {
        return this.LeaveOpeningBalance;
    }

    public void setLeaveOpeningBalance(LeaveOpeningBalance[] LeaveOpeningBalance) {
        this.LeaveOpeningBalance = LeaveOpeningBalance;
    }
}

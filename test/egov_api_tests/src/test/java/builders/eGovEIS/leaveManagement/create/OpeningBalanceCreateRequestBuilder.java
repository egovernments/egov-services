package builders.eGovEIS.leaveManagement.create;

import entities.requests.eGovEIS.leaveManagement.create.RequestInfo;
import entities.requests.eGovEIS.leaveManagement.create.LeaveOpeningBalance;
import entities.requests.eGovEIS.leaveManagement.create.OpeningBalanceCreateRequest;

public final class OpeningBalanceCreateRequestBuilder {

    OpeningBalanceCreateRequest openingBalanceCreateRequest = new OpeningBalanceCreateRequest();
    RequestInfo requestInfo = new RequestInfo();
    LeaveOpeningBalance[] leaveOpeningBalance = new LeaveOpeningBalance[1];

    public OpeningBalanceCreateRequestBuilder(LeaveOpeningBalance leaveOpeningBalance1) {
        openingBalanceCreateRequest.setRequestInfo(requestInfo);
        leaveOpeningBalance[0] = leaveOpeningBalance1;
        openingBalanceCreateRequest.setLeaveOpeningBalance(leaveOpeningBalance);
    }

    public OpeningBalanceCreateRequestBuilder withLeaveOpeningBalance(LeaveOpeningBalance[] LeaveOpeningBalance) {
        openingBalanceCreateRequest.setLeaveOpeningBalance(LeaveOpeningBalance);
        return this;
    }

    public OpeningBalanceCreateRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        openingBalanceCreateRequest.setRequestInfo(RequestInfo);
        return this;
    }

    public OpeningBalanceCreateRequest build() {
        return openingBalanceCreateRequest;
    }
}

package builders.eGovEIS.employeeMaster.leaveType.create;

import entities.requests.eGovEIS.employeeMaster.RequestInfo;
import entities.requests.eGovEIS.employeeMaster.leaveType.create.LeaveType;
import entities.requests.eGovEIS.employeeMaster.leaveType.create.LeaveTypeCreateRequest;
import tests.BaseAPITest;

public class LeaveTypeCreateRequestBuilder extends BaseAPITest {

    LeaveTypeCreateRequest leaveTypeCreateRequest = new LeaveTypeCreateRequest();
    RequestInfo requestInfo = new RequestInfo();

    LeaveType leaveType1 = new LeaveTypeBuilder()
            .withName("Leave Type" + get3DigitRandomInt())
            .withDescription("Leave Type")
            .withHalfdayAllowed(true)
            .withPayEligible(true)
            .withAccumulative(false)
            .withEncashable(false)
            .withActive(true)
            .withCreatedBy("")
            .withCreatedDate("")
            .withLastModifiedBy("")
            .withLastModifiedDate("")
            .withTenantId("ap.kurnool")
            .build();

    LeaveType[] leaveTypes = new LeaveType[1];

    public LeaveTypeCreateRequestBuilder() {
        leaveTypeCreateRequest.setRequestInfo(requestInfo);
        leaveTypes[0] = leaveType1;
        leaveTypeCreateRequest.setLeaveType(leaveTypes);
    }

    public LeaveTypeCreateRequestBuilder withLeaveType(LeaveType[] LeaveType) {
        leaveTypeCreateRequest.setLeaveType(LeaveType);
        return this;
    }

    public LeaveTypeCreateRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        leaveTypeCreateRequest.setRequestInfo(RequestInfo);
        return this;
    }

    public LeaveTypeCreateRequest build() {
        return leaveTypeCreateRequest;
    }
}

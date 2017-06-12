package builders.eGovEIS.hrMaster.position.create;

import builders.eGovEIS.hrMaster.designation.RequestInfoBuilder;
import entities.requests.eGovEIS.hrMaster.RequestInfo;
import entities.requests.eGovEIS.hrMaster.position.create.HRMasterPositionCreateRequest;
import entities.requests.eGovEIS.hrMaster.position.create.Position;

public class HRMasterPositionCreateRequestBuilder {

    HRMasterPositionCreateRequest hRMasterPositionCreateRequest = new HRMasterPositionCreateRequest();

    RequestInfo requestInfo = new RequestInfoBuilder().build();
    Position position = new PositionBuilder().build();

    Position[] positions = new Position[1];

    public HRMasterPositionCreateRequestBuilder() {
        positions[0] = position;
        hRMasterPositionCreateRequest.setRequestInfo(requestInfo);
        hRMasterPositionCreateRequest.setPosition(positions);
    }

    public HRMasterPositionCreateRequestBuilder withPosition(Position[] Position) {
        hRMasterPositionCreateRequest.setPosition(Position);
        return this;
    }

    public HRMasterPositionCreateRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        hRMasterPositionCreateRequest.setRequestInfo(RequestInfo);
        return this;
    }

    public HRMasterPositionCreateRequest build() {
        return hRMasterPositionCreateRequest;
    }
}

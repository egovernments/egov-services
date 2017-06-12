package entities.requests.eGovEIS.hrMaster.position.create;

import entities.requests.eGovEIS.hrMaster.RequestInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class HRMasterPositionCreateRequest {
    @JsonProperty("Position")
    private Position[] Position;

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public Position[] getPosition() {
        return this.Position;
    }

    public void setPosition(Position[] Position) {
        this.Position = Position;
    }

    public RequestInfo getRequestInfo() {
        return this.RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}

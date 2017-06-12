package entities.responses.eGovEIS.hrMaster.position.create;

import org.codehaus.jackson.annotate.JsonProperty;

public class HRMasterPositionResponse {

    @JsonProperty("ResponseInfo")
    private ResponseInfo ResponseInfo;

    @JsonProperty("Position")
    private Position[] Position;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public Position[] getPosition() {
        return this.Position;
    }

    public void setPosition(Position[] Position) {
        this.Position = Position;
    }
}

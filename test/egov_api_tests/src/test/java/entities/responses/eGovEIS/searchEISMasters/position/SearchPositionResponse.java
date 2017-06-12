package entities.responses.eGovEIS.searchEISMasters.position;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchPositionResponse {
    private entities.responses.eGovEIS.searchEISMasters.position.ResponseInfo ResponseInfo;
    @JsonProperty("Position")
    private entities.responses.eGovEIS.searchEISMasters.position.Position[] Position;

    public entities.responses.eGovEIS.searchEISMasters.position.ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(entities.responses.eGovEIS.searchEISMasters.position.ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public entities.responses.eGovEIS.searchEISMasters.position.Position[] getPosition() {
        return this.Position;
    }

    public void setPosition(entities.responses.eGovEIS.searchEISMasters.position.Position[] Position) {
        this.Position = Position;
    }
}

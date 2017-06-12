package entities.responses.commonMaster.religion;

import entities.responses.commonMaster.ResponseInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class ReligionResponse {
    private ResponseInfo ResponseInfo;
    @JsonProperty("Religion")
    private Religion[] Religion;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public Religion[] getReligion() {
        return this.Religion;
    }

    public void setReligion(Religion[] Religion) {
        this.Religion = Religion;
    }
}

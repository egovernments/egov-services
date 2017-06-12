package entities.responses.commonMaster.community;

import entities.responses.commonMaster.ResponseInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class CommunityResponse {

    private ResponseInfo ResponseInfo;
    @JsonProperty("Community")
    private Community[] Community;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public Community[] getCommunity() {
        return this.Community;
    }

    public void setCommunity(Community[] Community) {
        this.Community = Community;
    }
}

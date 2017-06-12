package entities.requests.assetManagement.assetService;

import entities.requests.assetManagement.RequestInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class CreateAssetServiceRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    @JsonProperty("Asset")
    private Asset Asset;

    public Asset getAsset() {
        return Asset;
    }

    public void setAsset(Asset Asset) {
        this.Asset = Asset;
    }

    public RequestInfo getRequestInfo() {
        return RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }


}

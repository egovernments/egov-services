package entities.responses.assetManagement.assetService;

import org.codehaus.jackson.annotate.JsonProperty;

public class AssetServiceResponse {

    private ResponseInfo ResponseInfo;

    @JsonProperty("Assets")
    private Assets[] Assets;

    public entities.responses.assetManagement.assetService.ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(entities.responses.assetManagement.assetService.ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public entities.responses.assetManagement.assetService.Assets[] getAssets() {
        return this.Assets;
    }

    public void setAssets(entities.responses.assetManagement.assetService.Assets[] Assets) {
        this.Assets = Assets;
    }
}

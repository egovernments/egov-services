package entities.responses.eGovEIS.searchEISMasters.hrConfigurations;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchHRConfigurationsResponse {
    private ResponseInfo ResponseInfo;

    @JsonProperty("HRConfiguration")
    private HRConfiguration HRConfiguration;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public HRConfiguration getHRConfiguration() {
        return this.HRConfiguration;
    }

    public void setHRConfiguration(HRConfiguration HRConfiguration) {
        this.HRConfiguration = HRConfiguration;
    }
}

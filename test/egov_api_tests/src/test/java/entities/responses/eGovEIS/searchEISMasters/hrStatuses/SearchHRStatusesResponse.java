package entities.responses.eGovEIS.searchEISMasters.hrStatuses;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchHRStatusesResponse {
    private entities.responses.eGovEIS.searchEISMasters.hrStatuses.ResponseInfo ResponseInfo;

    @JsonProperty("HRStatus")
    private entities.responses.eGovEIS.searchEISMasters.hrStatuses.HRStatus[] HRStatus;

    public entities.responses.eGovEIS.searchEISMasters.hrStatuses.ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(entities.responses.eGovEIS.searchEISMasters.hrStatuses.ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public entities.responses.eGovEIS.searchEISMasters.hrStatuses.HRStatus[] getHRStatus() {
        return this.HRStatus;
    }

    public void setHRStatus(entities.responses.eGovEIS.searchEISMasters.hrStatuses.HRStatus[] HRStatus) {
        this.HRStatus = HRStatus;
    }
}

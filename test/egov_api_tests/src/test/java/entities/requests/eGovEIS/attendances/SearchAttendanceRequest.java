package entities.requests.eGovEIS.attendances;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchAttendanceRequest {

    @JsonProperty("RequestInfo")
    private entities.requests.eGovEIS.RequestInfo requestInfo;

    public entities.requests.eGovEIS.RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(entities.requests.eGovEIS.RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
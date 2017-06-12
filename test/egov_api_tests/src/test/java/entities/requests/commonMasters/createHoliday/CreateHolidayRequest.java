package entities.requests.commonMasters.createHoliday;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateHolidayRequest {

    @JsonProperty("Holiday")
    private Holiday Holiday;

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public Holiday getHoliday() {
        return this.Holiday;
    }

    public void setHoliday(Holiday Holiday) {
        this.Holiday = Holiday;
    }

    public RequestInfo getRequestInfo() {
        return this.RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}

package entities.responses.commonMaster.holiday;

import entities.responses.commonMaster.ResponseInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class HolidayResponse {
    private ResponseInfo ResponseInfo;
    @JsonProperty("Holiday")
    private Holiday[] Holiday;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public Holiday[] getHoliday() {
        return this.Holiday;
    }

    public void setHoliday(Holiday[] Holiday) {
        this.Holiday = Holiday;
    }
}

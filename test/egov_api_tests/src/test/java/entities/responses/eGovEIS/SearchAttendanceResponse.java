package entities.responses.eGovEIS;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchAttendanceResponse {
    private ResponseInfo ResponseInfo;

    @JsonProperty("Attendance")
    private Attendance[] Attendance;

    public ResponseInfo getResponseInfo() {
        return ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public Attendance[] getAttendance() {
        return Attendance;
    }

    public void setAttendance(Attendance[] Attendance) {
        this.Attendance = Attendance;
    }
}

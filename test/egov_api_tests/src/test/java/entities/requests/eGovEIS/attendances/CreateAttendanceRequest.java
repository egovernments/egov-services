package entities.requests.eGovEIS.attendances;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateAttendanceRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    @JsonProperty("Attendance")
    private Attendance[] Attendance;

    public RequestInfo getRequestInfo() {
        return RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }

    public Attendance[] getAttendance() {
        return Attendance;
    }

    public void setAttendance(Attendance[] Attendance) {
        this.Attendance = Attendance;
    }

}
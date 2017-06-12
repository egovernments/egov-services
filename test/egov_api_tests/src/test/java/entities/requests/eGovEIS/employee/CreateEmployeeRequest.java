package entities.requests.eGovEIS.employee;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateEmployeeRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    @JsonProperty("Employee")
    private Employee Employee;

    public RequestInfo getRequestInfo() {
        return RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }

    public Employee getEmployee() {
        return Employee;
    }

    public void setEmployee(Employee Employee) {
        this.Employee = Employee;
    }
}

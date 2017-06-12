package entities.responses.eGovEIS.createEmployee;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateEmployeeResponse {
    private ResponseInfo ResponseInfo;

    @JsonProperty("Employee")
    private Employee Employee;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public Employee getEmployee() {
        return this.Employee;
    }

    public void setEmployee(Employee Employee) {
        this.Employee = Employee;
    }
}

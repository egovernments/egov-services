package entities.responses.eGovEIS.searchEISMasters.employeeType;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchEmployeeTypeResponse {

    private ResponseInfo ResponseInfo;
    @JsonProperty("EmployeeType")
    private EmployeeType[] EmployeeType;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public EmployeeType[] getEmployeeType() {
        return this.EmployeeType;
    }

    public void setEmployeeType(EmployeeType[] EmployeeType) {
        this.EmployeeType = EmployeeType;
    }
}

package entities.responses.commonMaster.department;

import entities.responses.commonMaster.ResponseInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class DepartmentResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo ResponseInfo;
    @JsonProperty("Department")
    private Department[] Department;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public Department[] getDepartment() {
        return this.Department;
    }

    public void setDepartment(Department[] Department) {
        this.Department = Department;
    }
}

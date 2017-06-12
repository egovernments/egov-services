package entities.requests.eGovEIS.hrMaster.designation.create;

import entities.requests.eGovEIS.hrMaster.RequestInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class HRMasterDesignationCreateRequest {

    @JsonProperty("Designation")
    private Designation Designation;

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public Designation getDesignation() {
        return this.Designation;
    }

    public void setDesignation(Designation Designation) {
        this.Designation = Designation;
    }

    public RequestInfo getRequestInfo() {
        return this.RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}

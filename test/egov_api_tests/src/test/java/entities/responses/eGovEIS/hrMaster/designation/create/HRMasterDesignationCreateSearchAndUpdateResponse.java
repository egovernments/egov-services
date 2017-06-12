package entities.responses.eGovEIS.hrMaster.designation.create;

import entities.responses.eGovEIS.hrMaster.designation.ResponseInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class HRMasterDesignationCreateSearchAndUpdateResponse {

    private ResponseInfo ResponseInfo;

    @JsonProperty("Designation")
    private Designation[] Designation;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public Designation[] getDesignation() {
        return this.Designation;
    }

    public void setDesignation(Designation[] Designation) {
        this.Designation = Designation;
    }
}

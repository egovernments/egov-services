package entities.responses.eGovEIS.searchEISMasters.designationType;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchDesignationResponse {

    private ResponseInfo ResponseInfo;
    @JsonProperty("Designation")
    private Designation[] Designation;

    public entities.responses.eGovEIS.searchEISMasters.designationType.ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(entities.responses.eGovEIS.searchEISMasters.designationType.ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public entities.responses.eGovEIS.searchEISMasters.designationType.Designation[] getDesignation() {
        return this.Designation;
    }

    public void setDesignation(entities.responses.eGovEIS.searchEISMasters.designationType.Designation[] Designation) {
        this.Designation = Designation;
    }
}

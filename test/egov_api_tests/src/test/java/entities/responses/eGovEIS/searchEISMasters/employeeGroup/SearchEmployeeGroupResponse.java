package entities.responses.eGovEIS.searchEISMasters.employeeGroup;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchEmployeeGroupResponse {
    private entities.responses.eGovEIS.searchEISMasters.employeeGroup.ResponseInfo ResponseInfo;
    @JsonProperty("Group")
    private entities.responses.eGovEIS.searchEISMasters.employeeGroup.Group[] Group;

    public entities.responses.eGovEIS.searchEISMasters.employeeGroup.ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(entities.responses.eGovEIS.searchEISMasters.employeeGroup.ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public entities.responses.eGovEIS.searchEISMasters.employeeGroup.Group[] getGroup() {
        return this.Group;
    }

    public void setGroup(entities.responses.eGovEIS.searchEISMasters.employeeGroup.Group[] Group) {
        this.Group = Group;
    }
}

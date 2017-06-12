package entities.responses.eGovEIS.searchEISMasters.grade;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchGradeResponse {
    private entities.responses.eGovEIS.searchEISMasters.grade.ResponseInfo ResponseInfo;
    @JsonProperty("Grade")
    private entities.responses.eGovEIS.searchEISMasters.grade.Grade[] Grade;

    public entities.responses.eGovEIS.searchEISMasters.grade.ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(entities.responses.eGovEIS.searchEISMasters.grade.ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public entities.responses.eGovEIS.searchEISMasters.grade.Grade[] getGrade() {
        return this.Grade;
    }

    public void setGrade(entities.responses.eGovEIS.searchEISMasters.grade.Grade[] Grade) {
        this.Grade = Grade;
    }
}

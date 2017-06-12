package entities.responses.eGovEIS.searchEISMasters.recruitmentModes;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchRecruitmentModesResponse {

    private ResponseInfo ResponseInfo;

    @JsonProperty("RecruitmentMode")
    private RecruitmentMode[] RecruitmentMode;

    public entities.responses.eGovEIS.searchEISMasters.recruitmentModes.ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(entities.responses.eGovEIS.searchEISMasters.recruitmentModes.ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public entities.responses.eGovEIS.searchEISMasters.recruitmentModes.RecruitmentMode[] getRecruitmentMode() {
        return this.RecruitmentMode;
    }

    public void setRecruitmentMode(entities.responses.eGovEIS.searchEISMasters.recruitmentModes.RecruitmentMode[] RecruitmentMode) {
        this.RecruitmentMode = RecruitmentMode;
    }
}

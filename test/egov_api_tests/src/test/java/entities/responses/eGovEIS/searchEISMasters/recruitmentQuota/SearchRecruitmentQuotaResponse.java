package entities.responses.eGovEIS.searchEISMasters.recruitmentQuota;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchRecruitmentQuotaResponse {
    private entities.responses.eGovEIS.searchEISMasters.recruitmentQuota.ResponseInfo ResponseInfo;
    @JsonProperty("RecruitmentQuota")
    private entities.responses.eGovEIS.searchEISMasters.recruitmentQuota.RecruitmentQuota[] RecruitmentQuota;

    public entities.responses.eGovEIS.searchEISMasters.recruitmentQuota.ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(entities.responses.eGovEIS.searchEISMasters.recruitmentQuota.ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public entities.responses.eGovEIS.searchEISMasters.recruitmentQuota.RecruitmentQuota[] getRecruitmentQuota() {
        return this.RecruitmentQuota;
    }

    public void setRecruitmentQuota(entities.responses.eGovEIS.searchEISMasters.recruitmentQuota.RecruitmentQuota[] RecruitmentQuota) {
        this.RecruitmentQuota = RecruitmentQuota;
    }
}

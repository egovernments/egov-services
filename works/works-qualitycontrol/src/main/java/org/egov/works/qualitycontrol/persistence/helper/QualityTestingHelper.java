package org.egov.works.qualitycontrol.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.qualitycontrol.web.contract.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualityTestingHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("letterOfAcceptanceEstimate")
    private String letterOfAcceptanceEstimate = null;

    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    public QualityTesting toDomain() {
        QualityTesting qualityTesting = new QualityTesting();
        qualityTesting.setId(this.id);
        qualityTesting.setTenantId(this.tenantId);
        LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = new LetterOfAcceptanceEstimate();
        letterOfAcceptanceEstimate.setLetterOfAcceptance(this.letterOfAcceptanceEstimate);
        qualityTesting.setLetterOfAcceptanceEstimate(letterOfAcceptanceEstimate);
        WorksStatus worksStatus = new WorksStatus();
        worksStatus.setCode(this.status);
        qualityTesting.setStatus(worksStatus);
        qualityTesting.setRemarks(this.remarks);
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(this.createdBy);
        auditDetails.setCreatedTime(this.createdTime);
        qualityTesting.setAuditDetails(auditDetails);
        return qualityTesting;
    }
}

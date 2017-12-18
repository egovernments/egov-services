package org.egov.works.qualitycontrol.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.qualitycontrol.web.contract.LetterOfAcceptanceEstimate;
import org.egov.works.qualitycontrol.web.contract.QualityTesting;
import org.egov.works.qualitycontrol.web.contract.QualityTestingDetail;

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

    public QualityTesting toDomain() {
        QualityTesting qualityTesting = new QualityTesting();
        qualityTesting.setId(this.id);
        qualityTesting.setTenantId(this.tenantId);
        LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = new LetterOfAcceptanceEstimate();
        letterOfAcceptanceEstimate.setLetterOfAcceptance(this.letterOfAcceptanceEstimate);
        qualityTesting.setLetterOfAcceptanceEstimate(letterOfAcceptanceEstimate);
        qualityTesting.setStatus(this.status);
        qualityTesting.setRemarks(this.remarks);
        return qualityTesting;
    }
}

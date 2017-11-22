package org.egov.works.workorder.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.egov.works.workorder.web.contract.AssetsForLOA;
import org.egov.works.workorder.web.contract.LOAActivity;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceEstimate;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LetterOfAcceptanceEstimateHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("letterOfAcceptance")
    private String letterOfAcceptance = null;

    @JsonProperty("detailedEstimate")
    private String detailedEstimate = null;

    @JsonProperty("workCompletionDate")
    private Long workCompletionDate = null;

    @JsonProperty("estimateLOAAmount")
    private BigDecimal estimateLOAAmount = null;

    public LetterOfAcceptanceEstimate toDomain() {
        LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = new LetterOfAcceptanceEstimate();
        letterOfAcceptanceEstimate.setId(this.id);
        letterOfAcceptanceEstimate.setTenantId(this.tenantId);
        letterOfAcceptanceEstimate.setLetterOfAcceptance(this.letterOfAcceptance);
        letterOfAcceptanceEstimate.setDetailedEstimate(this.detailedEstimate);
        letterOfAcceptanceEstimate.setWorkCompletionDate(this.workCompletionDate);
        letterOfAcceptanceEstimate.setEstimateLOAAmount(this.estimateLOAAmount);

        return  letterOfAcceptanceEstimate;
    }
}

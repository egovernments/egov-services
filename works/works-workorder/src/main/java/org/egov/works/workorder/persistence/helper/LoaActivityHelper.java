package org.egov.works.workorder.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.workorder.web.contract.AuditDetails;
import org.egov.works.workorder.web.contract.EstimateActivity;
import org.egov.works.workorder.web.contract.LOAActivity;
import org.egov.works.workorder.web.contract.LOAMeasurementSheet;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoaActivityHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("letterOfAcceptanceEstimate")
    private String letterOfAcceptanceEstimate = null;

    @JsonProperty("estimateActivity")
    private String estimateActivity = null;

    @JsonProperty("parent")
    private String parent = null;

    @JsonProperty("approvedRate")
    private BigDecimal approvedRate = null;

    @JsonProperty("approvedQuantity")
    private BigDecimal approvedQuantity = null;

    @JsonProperty("approvedAmount")
    private BigDecimal approvedAmount = null;

    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("loaMeasurements")
    private List<LOAMeasurementSheet> loaMeasurements = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public LOAActivity toDomain() {
        LOAActivity loaActivity = new LOAActivity();
        loaActivity.setId(this.id);
        loaActivity.setTenantId(this.tenantId);
        loaActivity.setLetterOfAcceptanceEstimate(this.letterOfAcceptanceEstimate);
        EstimateActivity estimateActivity = new EstimateActivity();
        estimateActivity.setId(this.estimateActivity);
       // loaActivity.setParent(this.parent);
        loaActivity.setApprovedAmount(this.approvedAmount);
        loaActivity.setApprovedRate(this.approvedRate);
        loaActivity.setApprovedQuantity(this.approvedQuantity);
        loaActivity.setRemarks(this.remarks);
        return loaActivity;
    }
}

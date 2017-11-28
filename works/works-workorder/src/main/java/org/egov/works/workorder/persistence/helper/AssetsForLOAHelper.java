package org.egov.works.workorder.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.workorder.web.contract.Asset;
import org.egov.works.workorder.web.contract.AssetsForLOA;
import org.egov.works.workorder.web.contract.AuditDetails;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetsForLOAHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("asset")
    private String asset = null;

    @JsonProperty("landAsset")
    private String landAsset = null;

    @JsonProperty("letterOfAcceptanceEstimate")
    private String letterOfAcceptanceEstimate = null;

    public AssetsForLOA toDomain() {
        AssetsForLOA assetsForLOA = new AssetsForLOA();
        assetsForLOA.setId(this.id);
        assetsForLOA.setTenantId(this.tenantId);
        assetsForLOA.setAsset(new Asset());
        assetsForLOA.getAsset().setCode(this.asset);
        assetsForLOA.setLandAsset(this.landAsset);
        assetsForLOA.setLetterOfAcceptanceEstimate(this.letterOfAcceptanceEstimate);
        return assetsForLOA;
    }
}

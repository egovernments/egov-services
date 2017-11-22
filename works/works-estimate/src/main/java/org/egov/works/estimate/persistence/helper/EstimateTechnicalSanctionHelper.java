package org.egov.works.estimate.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.EstimateOverhead;
import org.egov.works.estimate.web.contract.EstimateTechnicalSanction;
import org.egov.works.estimate.web.contract.User;

/**
 * An Object that holds the basic data of Technical Sanction for Detailed Estimate
 */
@ApiModel(description = "An Object that holds the basic data of Technical Sanction for Detailed Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T07:36:50.343Z")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EstimateTechnicalSanctionHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("technicalSanctionNumber")
    private String technicalSanctionNumber = null;

    @JsonProperty("detailedEstimate")
    private String detailedEstimate = null;

    @JsonProperty("technicalSanctionDate")
    private Long technicalSanctionDate = null;

    @JsonProperty("technicalSanctionBy")
    private String technicalSanctionBy = null;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public EstimateTechnicalSanction toDomain() {

        final EstimateTechnicalSanction estimateTechnicalSanction = new EstimateTechnicalSanction();
        estimateTechnicalSanction.setAuditDetails(new AuditDetails());
        estimateTechnicalSanction.getAuditDetails().setCreatedBy(this.createdBy);
        estimateTechnicalSanction.getAuditDetails().setCreatedTime(this.createdTime);
        estimateTechnicalSanction.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
        estimateTechnicalSanction.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
        estimateTechnicalSanction.setId(this.id);
        estimateTechnicalSanction.setTenantId(this.tenantId);
        estimateTechnicalSanction.setDetailedEstimate(this.detailedEstimate);
        User user = new User();
        user.setUserName(technicalSanctionBy);
        estimateTechnicalSanction.setTechnicalSanctionBy(user);
        estimateTechnicalSanction.setTechnicalSanctionDate(this.technicalSanctionDate);
        estimateTechnicalSanction.setTechnicalSanctionNumber(this.technicalSanctionNumber);
        return estimateTechnicalSanction;

    }
}

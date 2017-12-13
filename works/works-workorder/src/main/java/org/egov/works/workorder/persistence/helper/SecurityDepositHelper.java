package org.egov.works.workorder.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.workorder.web.contract.SecurityDeposit;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityDepositHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("letterOfAcceptance")
    private String letterOfAcceptance = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("collectionMode")
    private String collectionMode = null;

    @JsonProperty("percentage")
    private Double percentage = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    public SecurityDeposit toDomain() {
        SecurityDeposit securityDeposit = new SecurityDeposit();
        securityDeposit.setTenantId(this.tenantId);
        securityDeposit.setAmount(this.amount);
        securityDeposit.setId(this.id);
        securityDeposit.letterOfAcceptance(this.letterOfAcceptance);
        securityDeposit.collectionMode(this.collectionMode);
        securityDeposit.percentage(this.percentage);
        return securityDeposit;
    }
}

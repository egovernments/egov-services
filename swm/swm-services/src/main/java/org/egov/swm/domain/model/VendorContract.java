package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendorContract {

    @NotNull
    @Length(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("vendor")
    private Vendor vendor = null;

    @Size(min = 6, max = 128, message = "Value of contractNo shall be between 6 and 128")
    @JsonProperty("contractNo")
    private String contractNo = null;

    @NotNull
    @JsonProperty("contractDate")
    private Long contractDate = null;

    @NotNull
    @JsonProperty("contractPeriodFrom")
    private Long contractPeriodFrom = null;

    @NotNull
    @JsonProperty("contractPeriodTo")
    private Long contractPeriodTo = null;

    @NotNull
    @JsonProperty("securityDeposit")
    private Double securityDeposit = null;

    @NotNull
    @JsonProperty("paymentAmount")
    private Double paymentAmount = null;

    @NotNull
    @JsonProperty("paymentTerms")
    private PaymentTerms paymentTerms = null;

    @Length(min = 0, max = 500, message = "Value of remarks shall be between 0 and 500")
    @JsonProperty("remarks")
    private String remarks = null;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;
    
    @NotNull
    @Size(min = 1)
    @JsonProperty("servicesOffered")
    private List<SwmProcess> servicesOffered = null;

}

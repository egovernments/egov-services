package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class PaymentDetails {

    @NotNull
    @Size(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId;

    @Size(min = 1, max = 256, message = "Value of code shall be between 1 and 256")
    @JsonProperty("code")
    private String code;

    @NotNull
    @JsonProperty("vendorPaymentDetails")
    private VendorPaymentDetails vendorPaymentDetails;

    @NotNull
    @Size(min = 1, max = 256, message = "Value of voucherNumber shall be between 1 and 256")
    @JsonProperty("voucherNumber")
    private String voucherNumber;

    @NotNull
    @JsonProperty("voucherDate")
    private Long voucherDate;

    @NotNull
    @Size(min = 1, max = 256, message = "Value of instrumentType shall be between 1 and 256")
    @JsonProperty("instrumentType")
    private String instrumentType;

    @NotNull
    @Size(min = 1, max = 256, message = "Value of instrumentNumber shall be between 1 and 256")
    @JsonProperty("instrumentNumber")
    private String instrumentNumber;

    @NotNull
    @JsonProperty("instrumentDate")
    private Long instrumentDate;

    @NotNull
    @JsonProperty("amount")
    private Double amount;

    @NotNull
    @Size(min = 1, max = 256, message = "Value of bankName shall be between 1 and 256")
    @JsonProperty("bankName")
    private String bankName;

    @Size(min = 0, max = 256, message = "Value of branchName shall be between 1 and 256")
    @JsonProperty("branchName")
    private String branchName;

    @JsonProperty("pendingAmount")
    private Double pendingAmount;

    @JsonProperty("documents")
    private List<Document> documents;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;
}

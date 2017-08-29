package org.egov.asset.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.egov.asset.model.enums.TypeOfChangeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Hold the asset Revaluation information.
 */

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Revaluation {

    @JsonProperty("tenantId")
    @NotNull
    private String tenantId;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("assetId")
    private Long assetId = null;

    @JsonProperty("currentCapitalizedValue")
    private BigDecimal currentCapitalizedValue = null;

    @JsonProperty("typeOfChange")
    private TypeOfChangeEnum typeOfChange = null;

    @JsonProperty("revaluationAmount")
    private BigDecimal revaluationAmount = null;

    @JsonProperty("valueAfterRevaluation")
    private BigDecimal valueAfterRevaluation = null;

    @JsonProperty("revaluationDate")
    private Long revaluationDate = null;

    @JsonProperty("reevaluatedBy")
    private String reevaluatedBy = null;

    @JsonProperty("reasonForRevaluation")
    private String reasonForRevaluation = null;

    @JsonProperty("fixedAssetsWrittenOffAccount")
    private Long fixedAssetsWrittenOffAccount = null;

    @JsonProperty("function")
    private Long function = null;

    @JsonProperty("fund")
    private Long fund = null;

    @JsonProperty("scheme")
    private Long scheme = null;

    @JsonProperty("subScheme")
    private Long subScheme = null;

    @JsonProperty("comments")
    private String comments = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails;

    @JsonProperty("voucherReference")
    private String voucherReference;

}

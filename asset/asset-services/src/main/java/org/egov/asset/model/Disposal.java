package org.egov.asset.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.egov.asset.model.enums.TransactionType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Asset sale /Disposal.
 */

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Disposal {

	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("assetId")
	private Long assetId = null;

	@JsonProperty("buyerName")
	private String buyerName = null;

	@JsonProperty("buyerAddress")
	private String buyerAddress = null;

	@JsonProperty("disposalReason")
	private String disposalReason = null;

	@JsonProperty("disposalDate")
	private Long disposalDate = null;

	@JsonProperty("panCardNumber")
	private String panCardNumber = null;

	@JsonProperty("aadharCardNumber")
	private String aadharCardNumber = null;

	@JsonProperty("assetCurrentValue")
	private BigDecimal assetCurrentValue = null;

	@JsonProperty("saleValue")
	private BigDecimal saleValue = null;

	@JsonProperty("transactionType")
	private TransactionType transactionType = null;

	@JsonProperty("assetSaleAccount")
	private Long assetSaleAccount = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
	
	@JsonProperty("profitLossVoucherReference")
	private String profitLossVoucherReference;
}

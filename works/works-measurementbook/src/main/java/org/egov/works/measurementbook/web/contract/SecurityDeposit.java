package org.egov.works.measurementbook.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Object that holds Security deposit collection Details for the given LOA.
 */
@ApiModel(description = "An Object that holds Security deposit collection Details for the given LOA.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-24T06:27:19.470Z")

public class SecurityDeposit {
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

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public SecurityDeposit id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Security Deposit.
	 * 
	 * @return id
	 **/
	@ApiModelProperty(required = true, value = "Unique Identifier of the Security Deposit.")
	@NotNull

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SecurityDeposit letterOfAcceptance(String letterOfAcceptance) {
		this.letterOfAcceptance = letterOfAcceptance;
		return this;
	}

	/**
	 * reference of 'LetterOfAcceptance'. Pimary key is reference here.
	 * 
	 * @return letterOfAcceptance
	 **/
	@ApiModelProperty(value = "reference of 'LetterOfAcceptance'. Pimary key is reference here.")

	public String getLetterOfAcceptance() {
		return letterOfAcceptance;
	}

	public void setLetterOfAcceptance(String letterOfAcceptance) {
		this.letterOfAcceptance = letterOfAcceptance;
	}

	public SecurityDeposit tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Security Deposit
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Security Deposit")
	@NotNull

	@Size(min = 2, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public SecurityDeposit collectionMode(String collectionMode) {
		this.collectionMode = collectionMode;
		return this;
	}

	/**
	 * Enum value from 'CollectionMode'Enum value from 'CollectionMode'
	 * 
	 * @return collectionMode
	 **/
	@ApiModelProperty(required = true, value = "Enum value from 'CollectionMode'Enum value from 'CollectionMode'")
	@NotNull

	@Size(min = 1, max = 50)
	public String getCollectionMode() {
		return collectionMode;
	}

	public void setCollectionMode(String collectionMode) {
		this.collectionMode = collectionMode;
	}

	public SecurityDeposit percentage(Double percentage) {
		this.percentage = percentage;
		return this;
	}

	/**
	 * Percentage for the given mode
	 * 
	 * @return percentage
	 **/
	@ApiModelProperty(required = true, value = "Percentage for the given mode")
	@NotNull

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public SecurityDeposit amount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * Amount of Security Deposit should be calculated as per percentage
	 * 
	 * @return amount
	 **/
	@ApiModelProperty(required = true, value = "Amount of Security Deposit should be calculated as per percentage")
	@NotNull

	@Valid

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public SecurityDeposit auditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
		return this;
	}

	/**
	 * Get auditDetails
	 * 
	 * @return auditDetails
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public AuditDetails getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SecurityDeposit securityDeposit = (SecurityDeposit) o;
		return Objects.equals(this.id, securityDeposit.id)
				&& Objects.equals(this.letterOfAcceptance, securityDeposit.letterOfAcceptance)
				&& Objects.equals(this.tenantId, securityDeposit.tenantId)
				&& Objects.equals(this.collectionMode, securityDeposit.collectionMode)
				&& Objects.equals(this.percentage, securityDeposit.percentage)
				&& Objects.equals(this.amount, securityDeposit.amount)
				&& Objects.equals(this.auditDetails, securityDeposit.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, letterOfAcceptance, tenantId, collectionMode, percentage, amount, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class SecurityDeposit {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    letterOfAcceptance: ").append(toIndentedString(letterOfAcceptance)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    collectionMode: ").append(toIndentedString(collectionMode)).append("\n");
		sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
		sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
		sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

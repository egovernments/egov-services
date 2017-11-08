package org.egov.works.commons.web.contract;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds SOR Rate Details for the given Schedule Of Rate. No
 * Overlapping of rates expected for the same period.
 */
@ApiModel(description = "An Object that holds SOR Rate Details for the given Schedule Of Rate. No Overlapping of rates expected for the same period.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-08T13:36:58.528Z")

public class SORRate {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("scheduleOfRate")
	private ScheduleOfRate scheduleOfRate = null;

	@JsonProperty("fromDate")
	private Long fromDate = null;

	@JsonProperty("toDate")
	private Long toDate = null;

	@JsonProperty("rate")
	private BigDecimal rate = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public SORRate id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Rate Detail
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Rate Detail")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SORRate tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Rate Detail
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Rate Detail")
	@NotNull

	@Size(min = 2, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public SORRate scheduleOfRate(ScheduleOfRate scheduleOfRate) {
		this.scheduleOfRate = scheduleOfRate;
		return this;
	}

	/**
	 * Get scheduleOfRate
	 * 
	 * @return scheduleOfRate
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid

	public ScheduleOfRate getScheduleOfRate() {
		return scheduleOfRate;
	}

	public void setScheduleOfRate(ScheduleOfRate scheduleOfRate) {
		this.scheduleOfRate = scheduleOfRate;
	}

	public SORRate fromDate(Long fromDate) {
		this.fromDate = fromDate;
		return this;
	}

	/**
	 * Epoch time of From Date for the SOR Rate Detail.
	 * 
	 * @return fromDate
	 **/
	@ApiModelProperty(required = true, value = "Epoch time of From Date for the SOR Rate Detail.")
	@NotNull

	public Long getFromDate() {
		return fromDate;
	}

	public void setFromDate(Long fromDate) {
		this.fromDate = fromDate;
	}

	public SORRate toDate(Long toDate) {
		this.toDate = toDate;
		return this;
	}

	/**
	 * Epoch time of To date for the SOR Rate Details.
	 * 
	 * @return toDate
	 **/
	@ApiModelProperty(value = "Epoch time of To date for the SOR Rate Details.")

	public Long getToDate() {
		return toDate;
	}

	public void setToDate(Long toDate) {
		this.toDate = toDate;
	}

	public SORRate rate(BigDecimal rate) {
		this.rate = rate;
		return this;
	}

	/**
	 * SOR Rate which is applicable for given date range
	 * 
	 * @return rate
	 **/
	@ApiModelProperty(required = true, value = "SOR Rate which is applicable for given date range")
	@NotNull

	@Valid

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public SORRate auditDetails(AuditDetails auditDetails) {
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
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SORRate soRRate = (SORRate) o;
		return Objects.equals(this.id, soRRate.id) && Objects.equals(this.tenantId, soRRate.tenantId)
				&& Objects.equals(this.scheduleOfRate, soRRate.scheduleOfRate)
				&& Objects.equals(this.fromDate, soRRate.fromDate) && Objects.equals(this.toDate, soRRate.toDate)
				&& Objects.equals(this.rate, soRRate.rate) && Objects.equals(this.auditDetails, soRRate.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, scheduleOfRate, fromDate, toDate, rate, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class SORRate {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    scheduleOfRate: ").append(toIndentedString(scheduleOfRate)).append("\n");
		sb.append("    fromDate: ").append(toIndentedString(fromDate)).append("\n");
		sb.append("    toDate: ").append(toIndentedString(toDate)).append("\n");
		sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
		sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

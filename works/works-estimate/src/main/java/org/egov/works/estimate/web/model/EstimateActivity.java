package org.egov.works.estimate.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.works.commons.domain.enums.RevisionType;
import org.egov.works.commons.domain.model.AuditDetails;
import org.egov.works.commons.domain.model.NonSOR;
import org.egov.works.commons.domain.model.ScheduleOfRate;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object holds the basic data for a Estimate Activity
 */
@ApiModel(description = "An Object holds the basic data for a Estimate Activity")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-24T10:20:21.690Z")

public class EstimateActivity {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("schedule")
	private ScheduleOfRate schedule = null;

	@JsonProperty("nonSor")
	private NonSOR nonSor = null;

	@JsonProperty("uom")
	private String uom = null;

	@JsonProperty("unitRate")
	private Integer unitRate = null;

	@JsonProperty("estimateRate")
	private Integer estimateRate = null;

	@JsonProperty("quantity")
	private Integer quantity = null;

	@JsonProperty("serviceTaxPerc")
	private Integer serviceTaxPerc = null;

	@JsonProperty("revisionType")
	private RevisionType revisionType = null;

	@JsonProperty("parent")
	private EstimateActivity parent = null;

	@JsonProperty("detailedEstimate")
	private DetailedEstimate detailedEstimate = null;

	@JsonProperty("measurementSheetList")
	private List<MeasurementSheet> measurementSheetList = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public EstimateActivity id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Estimate Activity
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Estimate Activity")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EstimateActivity tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Estimate Activity
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Estimate Activity")
	@NotNull

	@Size(min = 4, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public EstimateActivity schedule(ScheduleOfRate schedule) {
		this.schedule = schedule;
		return this;
	}

	/**
	 * Get schedule
	 * 
	 * @return schedule
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public ScheduleOfRate getSchedule() {
		return schedule;
	}

	public void setSchedule(ScheduleOfRate schedule) {
		this.schedule = schedule;
	}

	public EstimateActivity nonSor(NonSOR nonSor) {
		this.nonSor = nonSor;
		return this;
	}

	/**
	 * Get nonSor
	 * 
	 * @return nonSor
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public NonSOR getNonSor() {
		return nonSor;
	}

	public void setNonSor(NonSOR nonSor) {
		this.nonSor = nonSor;
	}

	public EstimateActivity uom(String uom) {
		this.uom = uom;
		return this;
	}

	/**
	 * UOM for the Estimate Activity
	 * 
	 * @return uom
	 **/
	@ApiModelProperty(required = true, value = "UOM for the Estimate Activity")
	@NotNull

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public EstimateActivity unitRate(Integer unitRate) {
		this.unitRate = unitRate;
		return this;
	}

	/**
	 * Unit Rate of the Estimate Activity
	 * 
	 * @return unitRate
	 **/
	@ApiModelProperty(required = true, value = "Unit Rate of the Estimate Activity")
	@NotNull

	public Integer getUnitRate() {
		return unitRate;
	}

	public void setUnitRate(Integer unitRate) {
		this.unitRate = unitRate;
	}

	public EstimateActivity estimateRate(Integer estimateRate) {
		this.estimateRate = estimateRate;
		return this;
	}

	/**
	 * Estimate Rate of the Estimate Activity
	 * 
	 * @return estimateRate
	 **/
	@ApiModelProperty(required = true, value = "Estimate Rate of the Estimate Activity")
	@NotNull

	public Integer getEstimateRate() {
		return estimateRate;
	}

	public void setEstimateRate(Integer estimateRate) {
		this.estimateRate = estimateRate;
	}

	public EstimateActivity quantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	/**
	 * Quantity of the Estimate Activity
	 * 
	 * @return quantity
	 **/
	@ApiModelProperty(required = true, value = "Quantity of the Estimate Activity")
	@NotNull

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public EstimateActivity serviceTaxPerc(Integer serviceTaxPerc) {
		this.serviceTaxPerc = serviceTaxPerc;
		return this;
	}

	/**
	 * Service Tax Percentage of the Estimate Activity
	 * 
	 * @return serviceTaxPerc
	 **/
	@ApiModelProperty(value = "Service Tax Percentage of the Estimate Activity")

	public Integer getServiceTaxPerc() {
		return serviceTaxPerc;
	}

	public void setServiceTaxPerc(Integer serviceTaxPerc) {
		this.serviceTaxPerc = serviceTaxPerc;
	}

	public EstimateActivity revisionType(RevisionType revisionType) {
		this.revisionType = revisionType;
		return this;
	}

	/**
	 * Get revisionType
	 * 
	 * @return revisionType
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public RevisionType getRevisionType() {
		return revisionType;
	}

	public void setRevisionType(RevisionType revisionType) {
		this.revisionType = revisionType;
	}

	public EstimateActivity parent(EstimateActivity parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * Get parent
	 * 
	 * @return parent
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public EstimateActivity getParent() {
		return parent;
	}

	public void setParent(EstimateActivity parent) {
		this.parent = parent;
	}

	public EstimateActivity detailedEstimate(DetailedEstimate detailedEstimate) {
		this.detailedEstimate = detailedEstimate;
		return this;
	}

	/**
	 * Get detailedEstimate
	 * 
	 * @return detailedEstimate
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid

	public DetailedEstimate getDetailedEstimate() {
		return detailedEstimate;
	}

	public void setDetailedEstimate(DetailedEstimate detailedEstimate) {
		this.detailedEstimate = detailedEstimate;
	}

	public EstimateActivity measurementSheetList(List<MeasurementSheet> measurementSheetList) {
		this.measurementSheetList = measurementSheetList;
		return this;
	}

	public EstimateActivity addMeasurementSheetListItem(MeasurementSheet measurementSheetListItem) {
		if (this.measurementSheetList == null) {
			this.measurementSheetList = new ArrayList<MeasurementSheet>();
		}
		this.measurementSheetList.add(measurementSheetListItem);
		return this;
	}

	/**
	 * Measurement sheet list for the Estimate Activity
	 * 
	 * @return measurementSheetList
	 **/
	@ApiModelProperty(value = "Measurement sheet list for the Estimate Activity")

	@Valid

	public List<MeasurementSheet> getMeasurementSheetList() {
		return measurementSheetList;
	}

	public void setMeasurementSheetList(List<MeasurementSheet> measurementSheetList) {
		this.measurementSheetList = measurementSheetList;
	}

	public EstimateActivity auditDetails(AuditDetails auditDetails) {
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
		EstimateActivity estimateActivity = (EstimateActivity) o;
		return Objects.equals(this.id, estimateActivity.id) && Objects.equals(this.tenantId, estimateActivity.tenantId)
				&& Objects.equals(this.schedule, estimateActivity.schedule)
				&& Objects.equals(this.nonSor, estimateActivity.nonSor)
				&& Objects.equals(this.uom, estimateActivity.uom)
				&& Objects.equals(this.unitRate, estimateActivity.unitRate)
				&& Objects.equals(this.estimateRate, estimateActivity.estimateRate)
				&& Objects.equals(this.quantity, estimateActivity.quantity)
				&& Objects.equals(this.serviceTaxPerc, estimateActivity.serviceTaxPerc)
				&& Objects.equals(this.revisionType, estimateActivity.revisionType)
				&& Objects.equals(this.parent, estimateActivity.parent)
				&& Objects.equals(this.detailedEstimate, estimateActivity.detailedEstimate)
				&& Objects.equals(this.measurementSheetList, estimateActivity.measurementSheetList)
				&& Objects.equals(this.auditDetails, estimateActivity.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, schedule, nonSor, uom, unitRate, estimateRate, quantity, serviceTaxPerc,
				revisionType, parent, detailedEstimate, measurementSheetList, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EstimateActivity {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    schedule: ").append(toIndentedString(schedule)).append("\n");
		sb.append("    nonSor: ").append(toIndentedString(nonSor)).append("\n");
		sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
		sb.append("    unitRate: ").append(toIndentedString(unitRate)).append("\n");
		sb.append("    estimateRate: ").append(toIndentedString(estimateRate)).append("\n");
		sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
		sb.append("    serviceTaxPerc: ").append(toIndentedString(serviceTaxPerc)).append("\n");
		sb.append("    revisionType: ").append(toIndentedString(revisionType)).append("\n");
		sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
		sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
		sb.append("    measurementSheetList: ").append(toIndentedString(measurementSheetList)).append("\n");
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

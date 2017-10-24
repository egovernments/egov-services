package org.egov.works.estimate.web.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.egov.works.commons.domain.model.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object holds the basic data for a Measurement Sheet
 */
@ApiModel(description = "An Object holds the basic data for a Measurement Sheet")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-24T10:20:21.690Z")

public class MeasurementSheet {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("slNo")
	private Integer slNo = null;

	@JsonProperty("identifier")
	private String identifier = null;

	@JsonProperty("remarks")
	private String remarks = null;

	@JsonProperty("no")
	private Integer no = null;

	@JsonProperty("length")
	private Integer length = null;

	@JsonProperty("width")
	private Integer width = null;

	@JsonProperty("depthOrHeight")
	private Integer depthOrHeight = null;

	@JsonProperty("quantity")
	private Integer quantity = null;

	@JsonProperty("estimateActivity")
	private EstimateActivity estimateActivity = null;

	@JsonProperty("parent")
	private MeasurementSheet parent = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public MeasurementSheet id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Measurement Sheet
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Measurement Sheet")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MeasurementSheet tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * tenant id of the Measurement Sheet
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "tenant id of the Measurement Sheet")
	@NotNull

	@Size(min = 4, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public MeasurementSheet slNo(Integer slNo) {
		this.slNo = slNo;
		return this;
	}

	/**
	 * sl No of the Measurement sheet
	 * 
	 * @return slNo
	 **/
	@ApiModelProperty(value = "sl No of the Measurement sheet")

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}

	public MeasurementSheet identifier(String identifier) {
		this.identifier = identifier;
		return this;
	}

	/**
	 * identifier of the Measurement sheet
	 * 
	 * @return identifier
	 **/
	@ApiModelProperty(value = "identifier of the Measurement sheet")

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public MeasurementSheet remarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	/**
	 * remarks of the Measurement sheet
	 * 
	 * @return remarks
	 **/
	@ApiModelProperty(value = "remarks of the Measurement sheet")

	@Pattern(regexp = "[0-9.]")
	@Size(max = 1024)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public MeasurementSheet no(Integer no) {
		this.no = no;
		return this;
	}

	/**
	 * no of the Measurement sheet
	 * 
	 * @return no
	 **/
	@ApiModelProperty(value = "no of the Measurement sheet")

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public MeasurementSheet length(Integer length) {
		this.length = length;
		return this;
	}

	/**
	 * length of the Measurement sheet
	 * 
	 * @return length
	 **/
	@ApiModelProperty(value = "length of the Measurement sheet")

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public MeasurementSheet width(Integer width) {
		this.width = width;
		return this;
	}

	/**
	 * width of the Measurement sheet
	 * 
	 * @return width
	 **/
	@ApiModelProperty(value = "width of the Measurement sheet")

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public MeasurementSheet depthOrHeight(Integer depthOrHeight) {
		this.depthOrHeight = depthOrHeight;
		return this;
	}

	/**
	 * depth Or Height of the Measurement sheet
	 * 
	 * @return depthOrHeight
	 **/
	@ApiModelProperty(value = "depth Or Height of the Measurement sheet")

	public Integer getDepthOrHeight() {
		return depthOrHeight;
	}

	public void setDepthOrHeight(Integer depthOrHeight) {
		this.depthOrHeight = depthOrHeight;
	}

	public MeasurementSheet quantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	/**
	 * quantity of the Measurement sheet
	 * 
	 * @return quantity
	 **/
	@ApiModelProperty(required = true, value = "quantity of the Measurement sheet")
	@NotNull

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public MeasurementSheet estimateActivity(EstimateActivity estimateActivity) {
		this.estimateActivity = estimateActivity;
		return this;
	}

	/**
	 * Get estimateActivity
	 * 
	 * @return estimateActivity
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid

	public EstimateActivity getEstimateActivity() {
		return estimateActivity;
	}

	public void setEstimateActivity(EstimateActivity estimateActivity) {
		this.estimateActivity = estimateActivity;
	}

	public MeasurementSheet parent(MeasurementSheet parent) {
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

	public MeasurementSheet getParent() {
		return parent;
	}

	public void setParent(MeasurementSheet parent) {
		this.parent = parent;
	}

	public MeasurementSheet auditDetails(AuditDetails auditDetails) {
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
		MeasurementSheet measurementSheet = (MeasurementSheet) o;
		return Objects.equals(this.id, measurementSheet.id) && Objects.equals(this.tenantId, measurementSheet.tenantId)
				&& Objects.equals(this.slNo, measurementSheet.slNo)
				&& Objects.equals(this.identifier, measurementSheet.identifier)
				&& Objects.equals(this.remarks, measurementSheet.remarks)
				&& Objects.equals(this.no, measurementSheet.no) && Objects.equals(this.length, measurementSheet.length)
				&& Objects.equals(this.width, measurementSheet.width)
				&& Objects.equals(this.depthOrHeight, measurementSheet.depthOrHeight)
				&& Objects.equals(this.quantity, measurementSheet.quantity)
				&& Objects.equals(this.estimateActivity, measurementSheet.estimateActivity)
				&& Objects.equals(this.parent, measurementSheet.parent)
				&& Objects.equals(this.auditDetails, measurementSheet.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, slNo, identifier, remarks, no, length, width, depthOrHeight, quantity,
				estimateActivity, parent, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class MeasurementSheet {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    slNo: ").append(toIndentedString(slNo)).append("\n");
		sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
		sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
		sb.append("    no: ").append(toIndentedString(no)).append("\n");
		sb.append("    length: ").append(toIndentedString(length)).append("\n");
		sb.append("    width: ").append(toIndentedString(width)).append("\n");
		sb.append("    depthOrHeight: ").append(toIndentedString(depthOrHeight)).append("\n");
		sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
		sb.append("    estimateActivity: ").append(toIndentedString(estimateActivity)).append("\n");
		sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
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

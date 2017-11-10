package org.egov.works.services.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object holds the basic data for a Offline Status
 */
@ApiModel(description = "An Object holds the basic data for a Offline Status")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-10T05:48:31.479Z")

public class OfflineStatus {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("objectType")
	private String objectType = null;

	@JsonProperty("status")
	private String status = null;

	@JsonProperty("statusDate")
	private Long statusDate = null;

	@JsonProperty("objectNumber")
	private String objectNumber = null;

	@JsonProperty("remarks")
	private String remarks = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public OfflineStatus id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Offline Status
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Offline Status")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OfflineStatus tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * tenant id of the Offline Status
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "tenant id of the Offline Status")
	@NotNull

	@Size(min = 2, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public OfflineStatus objectType(String objectType) {
		this.objectType = objectType;
		return this;
	}

	/**
	 * Object type for the offline status
	 * 
	 * @return objectType
	 **/
	@ApiModelProperty(required = true, value = "Object type for the offline status")
	@NotNull

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public OfflineStatus status(String status) {
		this.status = status;
		return this;
	}

	/**
	 * Offline status for the object type.
	 * 
	 * @return status
	 **/
	@ApiModelProperty(required = true, value = "Offline status for the object type.")
	@NotNull

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public OfflineStatus statusDate(Long statusDate) {
		this.statusDate = statusDate;
		return this;
	}

	/**
	 * Epoch time of Offline Status Date
	 * 
	 * @return statusDate
	 **/
	@ApiModelProperty(required = true, value = "Epoch time of Offline Status Date")
	@NotNull

	public Long getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Long statusDate) {
		this.statusDate = statusDate;
	}

	public OfflineStatus objectNumber(String objectNumber) {
		this.objectNumber = objectNumber;
		return this;
	}

	/**
	 * Unique Identifier for the specified obect type.
	 * 
	 * @return objectNumber
	 **/
	@ApiModelProperty(required = true, value = "Unique Identifier for the specified obect type.")
	@NotNull

	public String getObjectNumber() {
		return objectNumber;
	}

	public void setObjectNumber(String objectNumber) {
		this.objectNumber = objectNumber;
	}

	public OfflineStatus remarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	/**
	 * Remarks for the object type.
	 * 
	 * @return remarks
	 **/
	@ApiModelProperty(value = "Remarks for the object type.")

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public OfflineStatus auditDetails(AuditDetails auditDetails) {
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
		OfflineStatus offlineStatus = (OfflineStatus) o;
		return Objects.equals(this.id, offlineStatus.id) && Objects.equals(this.tenantId, offlineStatus.tenantId)
				&& Objects.equals(this.objectType, offlineStatus.objectType)
				&& Objects.equals(this.status, offlineStatus.status)
				&& Objects.equals(this.statusDate, offlineStatus.statusDate)
				&& Objects.equals(this.objectNumber, offlineStatus.objectNumber)
				&& Objects.equals(this.remarks, offlineStatus.remarks)
				&& Objects.equals(this.auditDetails, offlineStatus.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, objectType, status, statusDate, objectNumber, remarks, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class OfflineStatus {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    objectType: ").append(toIndentedString(objectType)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    statusDate: ").append(toIndentedString(statusDate)).append("\n");
		sb.append("    objectNumber: ").append(toIndentedString(objectNumber)).append("\n");
		sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
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

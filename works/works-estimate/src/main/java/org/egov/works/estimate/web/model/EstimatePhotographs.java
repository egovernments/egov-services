package org.egov.works.estimate.web.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.egov.works.commons.domain.enums.WorkProgress;
import org.egov.works.commons.domain.model.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that hold Photographs attached to Estimate
 */
@ApiModel(description = "An Object that hold Photographs attached to Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-24T10:20:21.690Z")

public class EstimatePhotographs {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("abstractEstimateDetails")
	private AbstractEstimateDetails abstractEstimateDetails = null;

	@JsonProperty("latitude")
	private Integer latitude = null;

	@JsonProperty("longitude")
	private Integer longitude = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("dateOfCapture")
	private Long dateOfCapture = null;

	@JsonProperty("fileStoreMapper")
	private String fileStoreMapper = null;

	@JsonProperty("workProgress")
	private WorkProgress workProgress = null;

	@JsonProperty("detailedEstimate")
	private DetailedEstimate detailedEstimate = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public EstimatePhotographs id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the EstimatePhotographs
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the EstimatePhotographs")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EstimatePhotographs tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Abstract Estimate Details
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Abstract Estimate Details")
	@NotNull

	@Size(min = 4, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public EstimatePhotographs abstractEstimateDetails(AbstractEstimateDetails abstractEstimateDetails) {
		this.abstractEstimateDetails = abstractEstimateDetails;
		return this;
	}

	/**
	 * Get abstractEstimateDetails
	 * 
	 * @return abstractEstimateDetails
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public AbstractEstimateDetails getAbstractEstimateDetails() {
		return abstractEstimateDetails;
	}

	public void setAbstractEstimateDetails(AbstractEstimateDetails abstractEstimateDetails) {
		this.abstractEstimateDetails = abstractEstimateDetails;
	}

	public EstimatePhotographs latitude(Integer latitude) {
		this.latitude = latitude;
		return this;
	}

	/**
	 * Latitude of the photograph taken
	 * 
	 * @return latitude
	 **/
	@ApiModelProperty(value = "Latitude of the photograph taken")

	public Integer getLatitude() {
		return latitude;
	}

	public void setLatitude(Integer latitude) {
		this.latitude = latitude;
	}

	public EstimatePhotographs longitude(Integer longitude) {
		this.longitude = longitude;
		return this;
	}

	/**
	 * Longitude of the photograph taken
	 * 
	 * @return longitude
	 **/
	@ApiModelProperty(value = "Longitude of the photograph taken")

	public Integer getLongitude() {
		return longitude;
	}

	public void setLongitude(Integer longitude) {
		this.longitude = longitude;
	}

	public EstimatePhotographs description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Description of the photograph
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "Description of the photograph")

	@Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]")
	@Size(max = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EstimatePhotographs dateOfCapture(Long dateOfCapture) {
		this.dateOfCapture = dateOfCapture;
		return this;
	}

	/**
	 * Epoch time of when photo has captured
	 * 
	 * @return dateOfCapture
	 **/
	@ApiModelProperty(value = "Epoch time of when photo has captured")

	public Long getDateOfCapture() {
		return dateOfCapture;
	}

	public void setDateOfCapture(Long dateOfCapture) {
		this.dateOfCapture = dateOfCapture;
	}

	public EstimatePhotographs fileStoreMapper(String fileStoreMapper) {
		this.fileStoreMapper = fileStoreMapper;
		return this;
	}

	/**
	 * Reference of the file store
	 * 
	 * @return fileStoreMapper
	 **/
	@ApiModelProperty(required = true, value = "Reference of the file store")
	@NotNull

	public String getFileStoreMapper() {
		return fileStoreMapper;
	}

	public void setFileStoreMapper(String fileStoreMapper) {
		this.fileStoreMapper = fileStoreMapper;
	}

	public EstimatePhotographs workProgress(WorkProgress workProgress) {
		this.workProgress = workProgress;
		return this;
	}

	/**
	 * Get workProgress
	 * 
	 * @return workProgress
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid

	public WorkProgress getWorkProgress() {
		return workProgress;
	}

	public void setWorkProgress(WorkProgress workProgress) {
		this.workProgress = workProgress;
	}

	public EstimatePhotographs detailedEstimate(DetailedEstimate detailedEstimate) {
		this.detailedEstimate = detailedEstimate;
		return this;
	}

	/**
	 * Get detailedEstimate
	 * 
	 * @return detailedEstimate
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public DetailedEstimate getDetailedEstimate() {
		return detailedEstimate;
	}

	public void setDetailedEstimate(DetailedEstimate detailedEstimate) {
		this.detailedEstimate = detailedEstimate;
	}

	public EstimatePhotographs auditDetails(AuditDetails auditDetails) {
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
		EstimatePhotographs estimatePhotographs = (EstimatePhotographs) o;
		return Objects.equals(this.id, estimatePhotographs.id)
				&& Objects.equals(this.tenantId, estimatePhotographs.tenantId)
				&& Objects.equals(this.abstractEstimateDetails, estimatePhotographs.abstractEstimateDetails)
				&& Objects.equals(this.latitude, estimatePhotographs.latitude)
				&& Objects.equals(this.longitude, estimatePhotographs.longitude)
				&& Objects.equals(this.description, estimatePhotographs.description)
				&& Objects.equals(this.dateOfCapture, estimatePhotographs.dateOfCapture)
				&& Objects.equals(this.fileStoreMapper, estimatePhotographs.fileStoreMapper)
				&& Objects.equals(this.workProgress, estimatePhotographs.workProgress)
				&& Objects.equals(this.detailedEstimate, estimatePhotographs.detailedEstimate)
				&& Objects.equals(this.auditDetails, estimatePhotographs.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, abstractEstimateDetails, latitude, longitude, description, dateOfCapture,
				fileStoreMapper, workProgress, detailedEstimate, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EstimatePhotographs {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    abstractEstimateDetails: ").append(toIndentedString(abstractEstimateDetails)).append("\n");
		sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
		sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    dateOfCapture: ").append(toIndentedString(dateOfCapture)).append("\n");
		sb.append("    fileStoreMapper: ").append(toIndentedString(fileStoreMapper)).append("\n");
		sb.append("    workProgress: ").append(toIndentedString(workProgress)).append("\n");
		sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
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

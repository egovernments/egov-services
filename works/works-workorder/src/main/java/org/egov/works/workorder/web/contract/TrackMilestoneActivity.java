package org.egov.works.workorder.web.contract;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds Activity for the given Track Milestone. At least one
 * data should be given to create an Track Milestone
 */
@ApiModel(description = "An Object that holds Activity for the given Track  Milestone. At least one data should be given to create an Track Milestone")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-21T10:42:18.195Z")

public class TrackMilestoneActivity {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("trackMilestone")
	private String trackMilestone = null;

	@JsonProperty("milestoneActivity")
	private MilestoneActivity milestoneActivity = null;

	@JsonProperty("remarks")
	private String remarks = null;

	@JsonProperty("percentage")
	private BigDecimal percentage = null;

	@JsonProperty("completionDate")
	private Long completionDate = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public TrackMilestoneActivity id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Track Milestone Activity
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Track Milestone Activity")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TrackMilestoneActivity tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Track Milestone Activity
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Track Milestone Activity")
	@NotNull

	@Size(min = 2, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public TrackMilestoneActivity trackMilestone(String trackMilestone) {
		this.trackMilestone = trackMilestone;
		return this;
	}

	/**
	 * Track Milestone reference
	 * 
	 * @return trackMilestone
	 **/
	@ApiModelProperty(required = true, value = "Track Milestone reference")
	@NotNull

	public String getTrackMilestone() {
		return trackMilestone;
	}

	public void setTrackMilestone(String trackMilestone) {
		this.trackMilestone = trackMilestone;
	}

	public TrackMilestoneActivity milestoneActivity(MilestoneActivity milestoneActivity) {
		this.milestoneActivity = milestoneActivity;
		return this;
	}

	/**
	 * Milestone Activity reference
	 * 
	 * @return milestoneActivity
	 **/
	@ApiModelProperty(required = true, value = "Milestone Activity reference")
	@NotNull

	@Valid

	public MilestoneActivity getMilestoneActivity() {
		return milestoneActivity;
	}

	public void setMilestoneActivity(MilestoneActivity milestoneActivity) {
		this.milestoneActivity = milestoneActivity;
	}

	public TrackMilestoneActivity remarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	/**
	 * Remarks for the Track milestone activity
	 * 
	 * @return remarks
	 **/
	@ApiModelProperty(value = "Remarks for the Track milestone activity")

	@Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
	@Size(min = 4, max = 1024)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public TrackMilestoneActivity percentage(BigDecimal percentage) {
		this.percentage = percentage;
		return this;
	}

	/**
	 * Percentage for the Completed Percentage in Track Milestone Activity.
	 * 
	 * @return percentage
	 **/
	@ApiModelProperty(required = true, value = "Percentage for the Completed Percentage in Track Milestone Activity.")
	@NotNull

	@Valid

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public TrackMilestoneActivity completionDate(Long completionDate) {
		this.completionDate = completionDate;
		return this;
	}

	/**
	 * Epoch time of the Completion Date for the milestone.
	 * 
	 * @return completionDate
	 **/
	@ApiModelProperty(value = "Epoch time of the Completion Date for the milestone.")

	public Long getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Long completionDate) {
		this.completionDate = completionDate;
	}

	public TrackMilestoneActivity auditDetails(AuditDetails auditDetails) {
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
		TrackMilestoneActivity trackMilestoneActivity = (TrackMilestoneActivity) o;
		return Objects.equals(this.id, trackMilestoneActivity.id)
				&& Objects.equals(this.tenantId, trackMilestoneActivity.tenantId)
				&& Objects.equals(this.trackMilestone, trackMilestoneActivity.trackMilestone)
				&& Objects.equals(this.milestoneActivity, trackMilestoneActivity.milestoneActivity)
				&& Objects.equals(this.remarks, trackMilestoneActivity.remarks)
				&& Objects.equals(this.percentage, trackMilestoneActivity.percentage)
				&& Objects.equals(this.completionDate, trackMilestoneActivity.completionDate)
				&& Objects.equals(this.auditDetails, trackMilestoneActivity.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, trackMilestone, milestoneActivity, remarks, percentage, completionDate,
				auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class TrackMilestoneActivity {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    trackMilestone: ").append(toIndentedString(trackMilestone)).append("\n");
		sb.append("    milestoneActivity: ").append(toIndentedString(milestoneActivity)).append("\n");
		sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
		sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
		sb.append("    completionDate: ").append(toIndentedString(completionDate)).append("\n");
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

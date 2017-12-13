package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds the basic data for a Track Milestone
 */
@ApiModel(description = "An Object that holds the basic data for a Track Milestone")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class TrackMilestone {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("milestone")
    private Milestone milestone = null;

    @JsonProperty("status")
    private TrackMilestoneStatus status = null;

    @JsonProperty("totalPercentage")
    private BigDecimal totalPercentage = null;

    @JsonProperty("projectCompleted")
    private Boolean projectCompleted = null;

    @JsonProperty("trackMilestoneActivities")
    private List<TrackMilestoneActivity> trackMilestoneActivities = new ArrayList<TrackMilestoneActivity>();

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public TrackMilestone id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Track Milestone
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Track Milestone")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TrackMilestone tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the  Track Milestone
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the  Track Milestone")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public TrackMilestone milestone(Milestone milestone) {
        this.milestone = milestone;
        return this;
    }

    /**
     * Reference of Milestone
     *
     * @return milestone
     **/
    @ApiModelProperty(required = true, value = "Reference of Milestone")
    @NotNull

    @Valid

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public TrackMilestone status(TrackMilestoneStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Track Milestone status
     *
     * @return status
     **/
    @ApiModelProperty(required = true, value = "Track Milestone status")
    @NotNull

    @Valid

    public TrackMilestoneStatus getStatus() {
        return status;
    }

    public void setStatus(TrackMilestoneStatus status) {
        this.status = status;
    }

    public TrackMilestone totalPercentage(BigDecimal totalPercentage) {
        this.totalPercentage = totalPercentage;
        return this;
    }

    /**
     * Unit Rate of Track Milestone
     *
     * @return totalPercentage
     **/
    @ApiModelProperty(value = "Unit Rate of Track Milestone")

    @Valid

    public BigDecimal getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(BigDecimal totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    public TrackMilestone projectCompleted(Boolean projectCompleted) {
        this.projectCompleted = projectCompleted;
        return this;
    }

    /**
     * True if project completed.
     *
     * @return projectCompleted
     **/
    @ApiModelProperty(value = "True if project completed.")


    public Boolean getProjectCompleted() {
        return projectCompleted;
    }

    public void setProjectCompleted(Boolean projectCompleted) {
        this.projectCompleted = projectCompleted;
    }

    public TrackMilestone trackMilestoneActivities(List<TrackMilestoneActivity> trackMilestoneActivities) {
        this.trackMilestoneActivities = trackMilestoneActivities;
        return this;
    }

    public TrackMilestone addTrackMilestoneActivitiesItem(TrackMilestoneActivity trackMilestoneActivitiesItem) {
        this.trackMilestoneActivities.add(trackMilestoneActivitiesItem);
        return this;
    }

    /**
     * Array of Track Milestone Activity
     *
     * @return trackMilestoneActivities
     **/
    @ApiModelProperty(required = true, value = "Array of Track Milestone Activity")
    @NotNull

    @Valid
    @Size(min = 1)
    public List<TrackMilestoneActivity> getTrackMilestoneActivities() {
        return trackMilestoneActivities;
    }

    public void setTrackMilestoneActivities(List<TrackMilestoneActivity> trackMilestoneActivities) {
        this.trackMilestoneActivities = trackMilestoneActivities;
    }

    public TrackMilestone auditDetails(AuditDetails auditDetails) {
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
        TrackMilestone trackMilestone = (TrackMilestone) o;
        return Objects.equals(this.id, trackMilestone.id) &&
                Objects.equals(this.tenantId, trackMilestone.tenantId) &&
                Objects.equals(this.milestone, trackMilestone.milestone) &&
                Objects.equals(this.status, trackMilestone.status) &&
                Objects.equals(this.totalPercentage, trackMilestone.totalPercentage) &&
                Objects.equals(this.projectCompleted, trackMilestone.projectCompleted) &&
                Objects.equals(this.trackMilestoneActivities, trackMilestone.trackMilestoneActivities) &&
                Objects.equals(this.auditDetails, trackMilestone.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, milestone, status, totalPercentage, projectCompleted, trackMilestoneActivities, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TrackMilestone {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    milestone: ").append(toIndentedString(milestone)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    totalPercentage: ").append(toIndentedString(totalPercentage)).append("\n");
        sb.append("    projectCompleted: ").append(toIndentedString(projectCompleted)).append("\n");
        sb.append("    trackMilestoneActivities: ").append(toIndentedString(trackMilestoneActivities)).append("\n");
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


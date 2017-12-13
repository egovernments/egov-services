package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object taht holds the basic data for a Milestone
 */
@ApiModel(description = "An Object taht holds the basic data for a Milestone")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class Milestone {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("letterOfAcceptanceEstimate")
    private LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = null;

    @JsonProperty("status")
    private MilestoneStatus status = null;

    @JsonProperty("milestoneActivities")
    private List<MilestoneActivity> milestoneActivities = new ArrayList<MilestoneActivity>();

    @JsonProperty("trackMilestones")
    private List<TrackMilestone> trackMilestones = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationRemarks")
    private String cancellationRemarks = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public Milestone id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Milestone
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Milestone")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Milestone tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Milestone
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Milestone")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Milestone letterOfAcceptanceEstimate(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
        this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
        return this;
    }

    /**
     * Reference of Letter of Acceptance
     *
     * @return letterOfAcceptanceEstimate
     **/
    @ApiModelProperty(required = true, value = "Reference of Letter of Acceptance")
    @NotNull

    @Valid

    public LetterOfAcceptanceEstimate getLetterOfAcceptanceEstimate() {
        return letterOfAcceptanceEstimate;
    }

    public void setLetterOfAcceptanceEstimate(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
        this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
    }

    public Milestone status(MilestoneStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     *
     * @return status
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid

    public MilestoneStatus getStatus() {
        return status;
    }

    public void setStatus(MilestoneStatus status) {
        this.status = status;
    }

    public Milestone milestoneActivities(List<MilestoneActivity> milestoneActivities) {
        this.milestoneActivities = milestoneActivities;
        return this;
    }

    public Milestone addMilestoneActivitiesItem(MilestoneActivity milestoneActivitiesItem) {
        this.milestoneActivities.add(milestoneActivitiesItem);
        return this;
    }

    /**
     * Array of Milestone Activity
     *
     * @return milestoneActivities
     **/
    @ApiModelProperty(required = true, value = "Array of Milestone Activity")
    @NotNull

    @Valid
    @Size(min = 1)
    public List<MilestoneActivity> getMilestoneActivities() {
        return milestoneActivities;
    }

    public void setMilestoneActivities(List<MilestoneActivity> milestoneActivities) {
        this.milestoneActivities = milestoneActivities;
    }

    public Milestone trackMilestones(List<TrackMilestone> trackMilestones) {
        this.trackMilestones = trackMilestones;
        return this;
    }

    public Milestone addTrackMilestonesItem(TrackMilestone trackMilestonesItem) {
        if (this.trackMilestones == null) {
            this.trackMilestones = new ArrayList<TrackMilestone>();
        }
        this.trackMilestones.add(trackMilestonesItem);
        return this;
    }

    /**
     * Array of Track Milestone
     *
     * @return trackMilestones
     **/
    @ApiModelProperty(value = "Array of Track Milestone")

    @Valid
    @Size(min = 1)
    public List<TrackMilestone> getTrackMilestones() {
        return trackMilestones;
    }

    public void setTrackMilestones(List<TrackMilestone> trackMilestones) {
        this.trackMilestones = trackMilestones;
    }

    public Milestone cancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
        return this;
    }

    /**
     * Reason for cancellation of the Milestone, Required only while cancelling Milestone
     *
     * @return cancellationReason
     **/
    @ApiModelProperty(value = "Reason for cancellation of the Milestone, Required only while cancelling Milestone")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(max = 100)
    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Milestone cancellationRemarks(String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
        return this;
    }

    /**
     * Remarks for cancellation of the Milestone, Required only while cancelling Milestone
     *
     * @return cancellationRemarks
     **/
    @ApiModelProperty(value = "Remarks for cancellation of the Milestone, Required only while cancelling Milestone")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(max = 512)
    public String getCancellationRemarks() {
        return cancellationRemarks;
    }

    public void setCancellationRemarks(String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
    }

    public Milestone auditDetails(AuditDetails auditDetails) {
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
        Milestone milestone = (Milestone) o;
        return Objects.equals(this.id, milestone.id) &&
                Objects.equals(this.tenantId, milestone.tenantId) &&
                Objects.equals(this.letterOfAcceptanceEstimate, milestone.letterOfAcceptanceEstimate) &&
                Objects.equals(this.status, milestone.status) &&
                Objects.equals(this.milestoneActivities, milestone.milestoneActivities) &&
                Objects.equals(this.trackMilestones, milestone.trackMilestones) &&
                Objects.equals(this.cancellationReason, milestone.cancellationReason) &&
                Objects.equals(this.cancellationRemarks, milestone.cancellationRemarks) &&
                Objects.equals(this.auditDetails, milestone.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, letterOfAcceptanceEstimate, status, milestoneActivities, trackMilestones, cancellationReason, cancellationRemarks, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Milestone {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    letterOfAcceptanceEstimate: ").append(toIndentedString(letterOfAcceptanceEstimate)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    milestoneActivities: ").append(toIndentedString(milestoneActivities)).append("\n");
        sb.append("    trackMilestones: ").append(toIndentedString(trackMilestones)).append("\n");
        sb.append("    cancellationReason: ").append(toIndentedString(cancellationReason)).append("\n");
        sb.append("    cancellationRemarks: ").append(toIndentedString(cancellationRemarks)).append("\n");
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


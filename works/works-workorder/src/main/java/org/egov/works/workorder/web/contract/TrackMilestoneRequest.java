package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Track Milestone items are used in case of search results, also multiple  Track Milestone item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Track Milestone items are used in case of search results, also multiple  Track Milestone item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class TrackMilestoneRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("trackMilestones")
    private List<TrackMilestone> trackMilestones = null;

    public TrackMilestoneRequest requestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    /**
     * Get requestInfo
     *
     * @return requestInfo
     **/
    @ApiModelProperty(value = "")

    @Valid

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public TrackMilestoneRequest trackMilestones(List<TrackMilestone> trackMilestones) {
        this.trackMilestones = trackMilestones;
        return this;
    }

    public TrackMilestoneRequest addTrackMilestonesItem(TrackMilestone trackMilestonesItem) {
        if (this.trackMilestones == null) {
            this.trackMilestones = new ArrayList<TrackMilestone>();
        }
        this.trackMilestones.add(trackMilestonesItem);
        return this;
    }

    /**
     * Used for create and update only
     *
     * @return trackMilestones
     **/
    @ApiModelProperty(value = "Used for create and update only")

    @Valid

    public List<TrackMilestone> getTrackMilestones() {
        return trackMilestones;
    }

    public void setTrackMilestones(List<TrackMilestone> trackMilestones) {
        this.trackMilestones = trackMilestones;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TrackMilestoneRequest trackMilestoneRequest = (TrackMilestoneRequest) o;
        return Objects.equals(this.requestInfo, trackMilestoneRequest.requestInfo) &&
                Objects.equals(this.trackMilestones, trackMilestoneRequest.trackMilestones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestInfo, trackMilestones);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TrackMilestoneRequest {\n");

        sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
        sb.append("    trackMilestones: ").append(toIndentedString(trackMilestones)).append("\n");
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


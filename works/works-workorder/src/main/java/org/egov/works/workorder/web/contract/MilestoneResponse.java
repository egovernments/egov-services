package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contract class to send response. Array of Letter Of Acceptance items are used in case of search results, also multiple  Milestone item is used for create and update
 */
@ApiModel(description = "Contract class to send response. Array of Letter Of Acceptance items are used in case of search results, also multiple  Milestone item is used for create and update")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class MilestoneResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo = null;

    @JsonProperty("milestones")
    private List<Milestone> milestones = null;

    public MilestoneResponse responseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
        return this;
    }

    /**
     * Get responseInfo
     *
     * @return responseInfo
     **/
    @ApiModelProperty(value = "")

    @Valid

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public MilestoneResponse milestones(List<Milestone> milestones) {
        this.milestones = milestones;
        return this;
    }

    public MilestoneResponse addMilestonesItem(Milestone milestonesItem) {
        if (this.milestones == null) {
            this.milestones = new ArrayList<Milestone>();
        }
        this.milestones.add(milestonesItem);
        return this;
    }

    /**
     * Used for search result and create only
     *
     * @return milestones
     **/
    @ApiModelProperty(value = "Used for search result and create only")

    @Valid

    public List<Milestone> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<Milestone> milestones) {
        this.milestones = milestones;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MilestoneResponse milestoneResponse = (MilestoneResponse) o;
        return Objects.equals(this.responseInfo, milestoneResponse.responseInfo) &&
                Objects.equals(this.milestones, milestoneResponse.milestones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseInfo, milestones);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MilestoneResponse {\n");

        sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
        sb.append("    milestones: ").append(toIndentedString(milestones)).append("\n");
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


package org.egov.pgr.contracts.workflow;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.egov.pgr.contracts.grievance.RequestInfo;
import org.egov.pgr.entity.Complaint;

import java.util.Date;

@Getter
public class CreatWorkflowRequest {

    private static final String COMPLAINT = "Complaint";
    private static final String CREATE = "create";

    private RequestInfo requestInfo;

    @JsonProperty("object_id")
    private String objectId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("lastupdatedSince")
    private Date lastUpdatedSince;

    @JsonProperty("status")
    private String status;

    @JsonProperty("action")
    private String action;

    @JsonProperty("businessKey")
    private Long complaintId;

    @JsonProperty("assignee")
    private Long assignee;

    @JsonProperty("group")
    private String group;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("state_id")
    private String stateId;

    @JsonProperty("state_details")
    private String stateDetails;

    public CreatWorkflowRequest fromDomain(Complaint complaint) {
        this.complaintId = complaint.getId();
        this.assignee = complaint.getAssignee();
        //TODO - What happens in anonymous complaint
        this.userId = complaint.getComplainant().getUserDetail();
        this.type = COMPLAINT;
        this.action = CREATE;
        return this;
    }


}

package org.egov.workflow.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
public class ProcessInstance {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("object_id")
    @Setter
    private String id = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("description")
    private String description = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",timezone = "IST")
    @JsonProperty("created_Date")
    private Date createdDate = null;

    @JsonProperty("last_updated")
    private Date lastupdatedSince = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("action")
    private String action = null;

    @JsonProperty("business_key")
    private String businessKey = null;

    @JsonProperty("assignee")
    private Long assignee = null;

    @JsonProperty("group")
    private String group = null;

    @JsonProperty("sender_name")
    private String senderName = null;

    @JsonProperty("state_id")
    private Long stateId;

    @JsonProperty("state_details")
    private String stateDetails;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastupdatedSince() {
        return lastupdatedSince;
    }

    public String getStatus() {
        return status;
    }

    public String getAction() {
        return action;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public Long getAssignee() {
        return assignee;
    }

    public String getGroup() {
        return group;
    }

    public String getSenderName() {
        return senderName;
    }

    public Long getStateId() {
        return stateId;
    }

    public String getStateDetails() {
        return stateDetails;
    }

}

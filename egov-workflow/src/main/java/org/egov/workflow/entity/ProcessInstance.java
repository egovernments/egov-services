package org.egov.workflow.entity;

import java.util.Date;

import org.egov.workflow.model.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessInstance {
    
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo = null;
    
    @JsonProperty("object_id")
    private String id = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("created_Date")
    private Date createdDate = null;

    @JsonProperty("lastupdated_Since")
    private Date lastupdatedSince = null;

    @JsonProperty("status")
    private String status = null;
    
    @JsonProperty("action")
    private String action=null;
   
    @JsonProperty("business_Key")
    private String businessKey = null;

    @JsonProperty("assignee")
    private String assignee = null;

    @JsonProperty("group")
    private String group = null;
    
    @JsonProperty("user_id")
    private Long userId = null;
    
    @JsonProperty("state_id")
    private Long stateId;
    
    @JsonProperty("state_details")
    private String stateDetails;

   //private Map<String,Attribute> attributes = new HashMap<String,Attribute>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastupdatedSince() {
        return lastupdatedSince;
    }

    public void setLastupdatedSince(Date lastupdatedSince) {
        this.lastupdatedSince = lastupdatedSince;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAsignee(String assignee) {
        this.assignee = assignee;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
    

 /*   public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }*/

    public ProcessInstance id(String id) {
      this.id = id;
      return this;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStateDetails() {
        return stateDetails;
    }

    public void setStateDetails(String stateDetails) {
        this.stateDetails = stateDetails;
    }
    
}

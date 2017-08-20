package org.egov.workflow.persistence.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;
import org.egov.workflow.web.contract.Attribute;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Task {
    
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

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
    
    private String owner = null;

    @JsonProperty("assignee")
    private String assignee = null;

    private String module = null;

    private String state = null;

    @JsonProperty("status")
    private String status = null;

    private String url = null;

    @JsonProperty("business_key")
    private String businessKey = null;

    private String action = null;

    @JsonProperty("sender_name")
    private String sender;

    // private WorkflowEntity entity;

    @JsonProperty("comments")
    private String comments;

    private String extraInfo;

    private String details;

    //private Position ownerPosition;

    private String natureOfTask;
    
    @JsonProperty("values")
    private Map<String, Attribute> attributes = new HashMap<String, Attribute>();

    private String tenantId;

    //To be used to fetch single value attributes
    public String getValueForKey(String key){
        if(Objects.nonNull(attributes.get(key)))
            return attributes.get(key).getValues().get(0).getName();

        return "";
    }

    @JsonIgnore
    public String getComplaintTypeCode(){
        return getValueForKey("complaintTypeCode");
    }

    @JsonIgnore
    public Long getPreviousAssignee(){
        return Long.valueOf(getValueForKey("systemPreviousAssignee"));
    }

}

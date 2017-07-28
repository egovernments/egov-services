package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.common.contract.request.RequestInfo;

import java.util.*;

@Getter
@AllArgsConstructor
@Builder
public class ProcessInstance {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("objectId")
    @Setter
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("description")
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",timezone = "IST")
    @JsonProperty("createdDate")
    private Date createdDate;

    @JsonProperty("lastupdated")
    private Date lastupdatedSince;

    @JsonProperty("status")
    private String status;

    @JsonProperty("action")
    private String action;

    @JsonProperty("businessKey")
    private String businessKey;

    @Setter
    @JsonProperty("assignee")
    private Long assignee;

    @JsonProperty("group")
    private String group;

    @JsonProperty("senderName")
    private String senderName;
    
    @JsonProperty("values")
    private Map<String, Attribute> values;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonIgnore
    public String getComments(){
        return getValueForKey("systemApprovalComments");
    }

    @JsonIgnore
    public boolean isGrievanceOfficer(){
        return getValueForKey("userRole").equals("Grievance Officer");
    }

    public void setStateId(Long stateId){
        Value value = new Value("stateId", String.valueOf(stateId));
        List<Value> attributeValues = Collections.singletonList(value);
        Attribute attribute = new Attribute(true, "stateId", "String", true, "This is the id of state",attributeValues,"tenantId");
        values.put("systemStateId",attribute);
    }

    //To be used to fetch single value attributes
    public String getValueForKey(String key){
        if(Objects.nonNull(values.get(key)))
            return values.get(key).getValues().get(0).getName();

        return "";
    }
}
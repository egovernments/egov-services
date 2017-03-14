package org.egov.workflow.web.contract;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
  
    private String id = null;

   
    private String type = null;

   
    private String description = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",timezone = "IST")
  
    private Date createdDate = null;

     
    private Date lastupdatedSince = null;
    
    private String owner = null;

    
    private String assignee = null;

    private String module = null;

    private String state = null;

  
    private String status = null;

    private String url = null;

  
    private String businessKey = null;

    private String action = null;

   
    private String sender;

    
    private String comments;

    private String extraInfo;

    private String details;

    //private Position ownerPosition;

    private String natureOfTask;
    
    private WorkflowEntity entity;

     
    private Map<String, Attribute> attributes = new HashMap<String, Attribute>();
    

    //To be used to fetch single value attributes
    public String getValueForKey(String key){
        if(Objects.nonNull(attributes.get(key)))
            return attributes.get(key).getValues().get(0).getName();

        return "";
    }

}

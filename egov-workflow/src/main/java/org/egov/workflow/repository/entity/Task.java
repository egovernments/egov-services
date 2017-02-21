package org.egov.workflow.repository.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class Task {

    @Setter
    private String id = null;

    @Setter
    private String type = null;

    @Setter
    private String description = null;

    @Setter
    private Date createdDate = null;

    @Setter
    private Date lastupdatedSince = null;

    @Setter
    private String owner = null;

    @Setter
    private String assignee = null;

    @Setter
    private String module = null;

    @Setter
    private String state = null;

    @Setter
    private String status = null;

    @Setter
    private String url = null;

    @Setter
    private String businessKey = null;

    @Setter
    private String action = null;

    @Setter
    private String sender;

    // private WorkflowEntity entity;

    @Setter
    private String comments;

    @Setter
    private String extraInfo;

    @Setter
    private String details;

    //private Position ownerPosition;

    @Setter
    private String natureOfTask;
    
    @Setter
    private Map<String, Attribute> attributes = new HashMap<String, Attribute>();

}

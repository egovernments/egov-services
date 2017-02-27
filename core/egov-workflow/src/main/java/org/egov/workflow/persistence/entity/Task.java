package org.egov.workflow.persistence.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.workflow.web.contract.Attribute;

@Builder
@Getter
@Setter
public class Task {

    private String id = null;

    private String type = null;

    private String description = null;

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

    // private WorkflowEntity entity;

    private String comments;

    private String extraInfo;

    private String details;

    //private Position ownerPosition;

    private String natureOfTask;

    private Map<String, Attribute> attributes = new HashMap<String, Attribute>();

}
